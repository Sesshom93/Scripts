package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import MasterFarmer.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Heal extends Task {

    private final Walker walker = new Walker(ctx);

    public Heal(ClientContext arg0) {
        super(arg0);
    }

    //Always active - Bad thing?
    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {
        Npc guard = ctx.npcs.select().id(Constants.GUARD).nearest().poll();

        //if player has no health
        if (ctx.combat.health() < 1){
            System.out.println("You died.");
        }

        //if players health is at or below 3
        if (ctx.combat.health() <= 3){
            System.out.println("Eating");
            eat();
        }
        //if guard is attacking the player and health is at or below 3 eat else run away then run back
        if (guard.interacting().equals(ctx.players.local())){
            if (ctx.combat.health() <= 3){
                eat();
            }
            System.out.println("Running away!");
            walker.walkPath(Constants.PATH_TO_BANK);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inCombat();
                }
            },500,10);
            System.out.println("Out of combat");
            walker.walkPathReverse(Constants.PATH_TO_BANK);

        }



    }

    public void eat(){
        Item cake1 = ctx.inventory.select().id(Constants.CAKE).poll();
        Item cake2 = ctx.inventory.select().id(Constants.CAKE2).poll();
        Item cake3 = ctx.inventory.select().id(Constants.CAKE3).poll();
        Bank.cakeInventory = ctx.inventory.select().id(Constants.CAKE).count();

        //eats cake starting with smallest size
        if (ctx.combat.health() <= 3 && cake3.valid()){
            System.out.println("Eating cake");
            cake3.click();
            Condition.sleep(1000);
        }
        if (ctx.combat.health() <= 3 && cake2.valid()){
            System.out.println("Eating cake");
            cake2.click();
            Condition.sleep(1000);
        }
        if (ctx.combat.health() <= 3 && cake1.valid()){
            System.out.println("Eating cake");
            cake1.click();
            Condition.sleep(1000);
        }
    }




}
