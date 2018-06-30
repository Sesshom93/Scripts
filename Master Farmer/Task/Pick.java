package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import MasterFarmer.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Pick extends Task {

    private final Walker walker = new Walker(ctx);
    public static int ranarrInventory = 0;
    public static int snapdragonInventory = 0;


    public Pick(ClientContext arg0) {
        super(arg0);
    }
    //Activates if player has more than 3 health, isn't in combat, isn't pickpocketing, has necklace on, and doesn't have full inv, or has at least 1 of any cake.
    @Override
    public boolean activate() {
        return ctx.combat.health() > 3
                && ctx.players.local().animation() != Constants.DAZED
                && !ctx.players.local().inCombat()
                || ctx.combat.health() > 3
                && ctx.players.local().animation() != Constants.PICKPOCKET
                && !ctx.players.local().inCombat()
                || ctx.combat.health() > 3
                && ctx.equipment.itemAt(Equipment.Slot.NECK).id() != Constants.DODGY_NECKLACE
                && !ctx.players.local().inCombat()
                || ctx.combat.health() > 3
                && ctx.inventory.select().count() != 28
                && !ctx.players.local().inCombat()
                || ctx.combat.health() > 3
                && ctx.inventory.select().id(Constants.CAKE).count() > 1
                && !ctx.players.local().inCombat()
                || ctx.combat.health() > 3
                && ctx.inventory.select().id(Constants.CAKE2).count() > 1
                && !ctx.players.local().inCombat()
                || ctx.combat.health() > 3
                && ctx.inventory.select().id(Constants.CAKE3).count() > 1
                && !ctx.players.local().inCombat();
    }

    @Override
    public void execute() {

        //if no neck on equip one
        if (ctx.equipment.itemAt(Equipment.Slot.NECK).id() != Constants.DODGY_NECKLACE) {
            Item neck = ctx.inventory.select().id(Constants.DODGY_NECKLACE).poll();
            neck.click();
            Bank.necklaceInventory = ctx.inventory.select().id(Constants.DODGY_NECKLACE).count();
        }
        pickpocket();

    }

    public void pickpocket(){
        Npc farmer = ctx.npcs.select().id(Constants.MASTER_FARMER).nearest().poll();
        //get count
        ranarrInventory = ctx.inventory.select().id(Constants.RANARR_SEED).count();
        snapdragonInventory = ctx.inventory.select().id(Constants.SNAPDRAGON_SEED).count();

        //if farmer visible pick else turn camera
        if (farmer.inViewport()){
            farmer.hover();
            farmer.interact("Pickpocket");
            System.out.println("Pickpocketing");
            Condition.sleep(Random.nextInt(1000, 1600));

        } else {
            System.out.println("Turning camera to farmer");
            ctx.camera.turnTo(farmer);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return farmer.inViewport();
                }
            },500,10);
        }
    }


}
