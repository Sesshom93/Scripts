package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;


import java.util.concurrent.Callable;

public class Bank extends Task {
    
    //For paint purposes
    public static int ranarrBank = 0;
    public static int snapdragonBank = 0;
    public static int foodInBank = 0;
    public static int necklaceBank = 0;
    public static int foodInInventory = 0;

    public Bank(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return noFood(Heal.foodIds) || noNecklace(Constants.DODGY_NECKLACE);
    }

    @Override
    public void execute() {

        bank();
    }

    public void bank(){
        //If bank is visible, player isn't moving, and the bank isn't opened. Open it.
        if (ctx.bank.inViewport() && !ctx.players.local().inMotion() && !ctx.bank.opened()){
            System.out.println("Bank is visible");
            System.out.println("Opening bank");
            ctx.bank.open();
            //wait till opened
            Condition.sleep(Random.nextInt(125, 250));
            System.out.println("Waiting for bank to be opened");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            },250,10);
            //turn camera to it if not in viewport
        } else {
            System.out.println("Turning camera to bank");
            ctx.camera.turnTo(ctx.bank.nearest());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.inViewport();
                }
            },500,10);
        }
        //if opened check inventory
        if (ctx.bank.opened()){
            ranarrBank = ctx.bank.select().id(Constants.RANARR_SEED).peek().stackSize();
            snapdragonBank = ctx.bank.select().id(Constants.SNAPDRAGON_SEED).peek().stackSize();
            foodInBank = ctx.bank.select().id(Heal.foodIds).peek().stackSize();
            necklaceBank = ctx.bank.select().id(Constants.DODGY_NECKLACE).peek().stackSize();
            System.out.println("Bank is Open");
            System.out.println("Checking items");
            //deposit seeds if any are ranarr or snapdragon
            deposit();
            //check if food or necklaces are needed
            checkInventory(Heal.foodIds);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !noFood(Heal.foodIds) && !noNecklace(Constants.DODGY_NECKLACE);
                }
            },250,10);
            //close bank and wait till not opened
            System.out.println("Closing bank");
            ctx.bank.close();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            },250,10);
        }

    }
    
    public void deposit(){
        if (ctx.inventory.select().id(Constants.RANARR_SEED).count() < 1 || ctx.inventory.select().id(Constants.SNAPDRAGON_SEED).count() < 1 );{
            ctx.bank.deposit(Constants.RANARR_SEED, 28);
            Condition.sleep(500);
            ctx.bank.deposit(Constants.SNAPDRAGON_SEED, 28);
            Condition.sleep(500);
        }
    }   
    //check if no food and player is near bank
    public boolean noFood(int foodId[]) {
        if (ctx.inventory.select().id(foodId).count() == 0 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 2){
            return true;
        } else {
            return false;
        }
    }
    
    //check if no necklace and if player is near bank
    public boolean noNecklace(int necklace){
        if (ctx.inventory.select().id(necklace).count() == 0 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 2){
            return true;
        } else {
            return false;
        }
    }
    
    //calculate food or necklace needed
    public void checkInventory(int food[]){
        int amountOfNecklaces = ctx.inventory.select().id(Constants.DODGY_NECKLACE).count();
        int amountOfFood = ctx.inventory.select().id(Heal.foodIds).count();
        int necklacesNeeded = 4 - amountOfNecklaces;
        int foodNeeded = 8 - amountOfFood;

        if (foodNeeded > 0){
            System.out.println("Need food");
            ctx.bank.withdraw(food[0], foodNeeded);
            foodInInventory = ctx.inventory.select().id(Heal.foodIds).count();
            Condition.sleep(Random.nextInt(125, 250));
        }
        if (necklacesNeeded > 0){
            System.out.println("Need necklaces");
            ctx.bank.withdraw(Constants.DODGY_NECKLACE, necklacesNeeded);
            Pick.necklaceInInventory = ctx.inventory.select().id(Constants.DODGY_NECKLACE).count();
            Condition.sleep(Random.nextInt(125, 250));
        }
    }


}
