package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import MasterFarmer.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Walk extends Task {

    private final Walker walker = new Walker(ctx);

    public Walk(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return checkInventory(Heal.foodIds) || checkIfAttacked();
    }

    @Override
    public void execute() {
        runToBank(Heal.foodIds);

    }

    public void runToBank(int foodIds[]){

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 1){
            if (Constants.PATH_TO_BANK[6].distanceTo(ctx.players.local()) > 8){
                //Walks to bank if no food, no necklaces, or is being attacked.
                if (ctx.inventory.select().id(foodIds).count() == 0 || ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() == 0 || checkIfAttacked()){
                    System.out.println("Walking to bank");
                    walker.walkPath(Constants.PATH_TO_BANK);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return  Constants.PATH_TO_BANK[6].distanceTo(ctx.players.local()) < 2;
                        }
                    },500,10);
                }
            }
            //walks to farmer if inventory has food and necklaces
            if (ctx.inventory.select().id(foodIds).count() > 1 && ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() > 1){
                if (Constants.PATH_TO_FARMER[5].distanceTo(ctx.players.local()) > 8){
                    System.out.println("Walking to bank");
                    walker.walkPath(Constants.PATH_TO_FARMER);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return  Constants.PATH_TO_FARMER[5].distanceTo(ctx.players.local()) < 3 ;
                        }
                    },2000,1);
                }
            }
        }


    }
    //Check if player is being attacked
    public boolean checkIfAttacked(){
        Npc guard = ctx.npcs.select().id(Constants.GUARD).nearest().poll();
        if (guard.interacting().equals(ctx.players.local())) {
            return true;
        } else {
            return false;
        }
    }
    //Check if player has no food, and no seeds or no necklaces and no seeds.
    //Checks if player has food and necklaces
    public boolean checkInventory(int foodId[]) {
        if (ctx.inventory.select().id(foodId).count() == 0 && ctx.inventory.select().id(Drop.seedIds).count() < 1 || ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() == 0 && ctx.inventory.select().id(Drop.seedIds).count() < 1){
            return true;
        } else if (ctx.inventory.select().id(foodId).count() > 0 && ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() > 0){
            return true;
        } else {
            return false;
        }
    }


}
