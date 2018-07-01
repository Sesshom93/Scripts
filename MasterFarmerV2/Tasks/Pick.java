package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Pick extends Task {

    public static int ranarrInInventory = 0;
    public static int snapdragonInInventory = 0;
    public static int necklaceInInventory = 0;


    public Pick(ClientContext arg0) {
        super(arg0);
    }
    //Activates if player has more than 3 health, isn't in combat, isn't pickpocketing, has necklace on, and doesn't have full inv, or has at least 1 piece of food
    @Override
    public boolean activate() {
        return checkIfPickable();
    }

    @Override
    public void execute() {
        pickpocket();
    }

    //checks if player has more than 3 health, isn't in combat, isn't pickpocketing, has necklace on, and doesn't have full inv, or has at least 1 piece of food
    public boolean checkIfPickable(){

        if (ctx.combat.health() <= 3){
            System.out.println("You don't have enough health");
            return false;
        } else if (ctx.players.local().animation() == Constants.STUNNED){
            System.out.println("You stunned");
            Condition.sleep(2000);
         return false;
        } else if (hasFood(Heal.foodIds)){
            System.out.println("You have no more food");
            return false;
        } else if (hasNecklace(Constants.DODGY_NECKLACE)){
            System.out.println("You have no more necklaces");
            return false;
        } else if (ctx.inventory.select().count() == 28){
            return false;
        } else if (ctx.equipment.itemAt(Equipment.Slot.NECK).id() != Constants.DODGY_NECKLACE){
            System.out.println("You don't have a necklace on");
            return equipNeck();
        } else {
            return true;
        }
    }

    public void pickpocket(){
        Npc farmer = ctx.npcs.select().id(Constants.MASTER_FARMER).nearest().poll();

        //if bank is far away and farmer is visible
        if (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) > 3){
            if (farmer.inViewport()){
                farmer.hover();
                farmer.interact("Pickpocket");
                ranarrInInventory = ctx.inventory.select().id(Constants.RANARR_SEED).count();
                snapdragonInInventory = ctx.inventory.select().id(Constants.SNAPDRAGON_SEED).count();
                System.out.println("Pickpocketing");
                Condition.sleep(Random.nextInt(1000, 1250));
            //else turn camera to farmer
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

    //checks for no food 1 food
    public boolean hasFood(int foodId[]) {
        if (ctx.inventory.select().id(foodId).count() < 1){
            return true;
        } else {
            return false;
        }
    }

    //checks for no necklaces
    public boolean hasNecklace(int necklace){
        if (ctx.inventory.select().id(necklace).count() == 0){
            return true;
        } else {
            return false;
        }
    }

    //equips necklace
    public boolean equipNeck () {
        Item neck = ctx.inventory.select().id(Constants.DODGY_NECKLACE).poll();
        if (ctx.equipment.itemAt(Equipment.Slot.NECK).id() != Constants.DODGY_NECKLACE && ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() > 0) {
            neck.click();
            necklaceInInventory = ctx.inventory.select().id(Constants.DODGY_NECKLACE).count();
            return false;

        } else {
            return true;
        }
    }



}
