package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import MasterFarmer.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class Bank extends Task {

    private final Walker walker = new Walker(ctx);
    public static int ranarrBank = 0;
    public static int snapdragonBank = 0;
    public static int cakeBank = 0;
    public static int necklaceBank = 0;
    public static int cakeInventory = 0;
    public static int necklaceInventory = 0;

    public Bank(ClientContext arg0) {
        super(arg0);
    }
    //Activates if i have no cake, am not in combat, out of necklaces, or i am at the bank.
    @Override
    public boolean activate() {
        return ctx.inventory.select().id(Constants.CAKE).count() < 1
                && ctx.inventory.select().id(Constants.CAKE2).count() < 1
                && ctx.inventory.select().id(Constants.CAKE3).count() < 1
                && !ctx.players.local().inCombat()
                || ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() < 1
                && ctx.equipment.itemAt(Equipment.Slot.NECK).id() != Constants.DODGY_NECKLACE
                && !ctx.players.local().inCombat()
                || ctx.inventory.select().count() == 0
                && !ctx.players.local().inCombat()
                && ctx.bank.inViewport();
    }

    @Override
    public void execute() {
        Bank();
    }

    public void Bank(){
        System.out.println("Banking");
        //if my inventory isn't 0 -- conflicted with having 0 items at bank(ran around then banked)
        if (ctx.inventory.select().count() != 0){
            //run if i have no cake, am not in combat, out of necklaces, or i am at the bank. Redundant maybe?
            if (!ctx.players.local().inMotion()
                    || ctx.movement.destination().equals(Tile.NIL)
                    || ctx.movement.destination().distanceTo(ctx.players.local()) < 10
                    || ctx.inventory.select().id(Constants.CAKE).count() < 1
                    && ctx.inventory.select().id(Constants.CAKE2).count() < 1
                    && ctx.inventory.select().id(Constants.CAKE3).count() < 1
                    || ctx.inventory.select().id(Constants.DODGY_NECKLACE).count() < 1){
                System.out.println("Walking to bank");
                walker.walkPath(Constants.PATH_TO_BANK);
                //wait till im near bank and its opened
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return Constants.PATH_TO_BANK[6].distanceTo(ctx.players.local()) <= 1 && ctx.bank.opened();
                    }
                },500,10);
                System.out.println("At bank");


            }
        }
        //Drops items if i at the banker - Tried to figure out how to drop everything before running to bank and couldn't.
        if (ctx.inventory.count() > 1){
            for (Item useless : ctx.inventory.id(Constants.POTATO_SEED, Constants.ONION_SEED ,Constants.CABBAGE_SEED ,Constants.TOMATO_SEED ,Constants.SWEETCORN_SEED, Constants.STRAWBERRY_SEED, Constants.WATERMELON_SEED, Constants.BARLEY_SEED, Constants.HAMMERSTONE_SEED, Constants.ASGARNIAN_SEED, Constants.JUTE_SEED, Constants.YANILLIAN_SEED, Constants.KRANDORIAN_SEED, Constants.WILDBLOOD_SEED, Constants.MARIGOLD_SEED, Constants.NASTURTIUM_SEED, Constants.ROSEMARY_SEED, Constants.WOAD_SEED, Constants.LIMPWURT_SEED, Constants.REDBERRY_SEED, Constants.CADAVABERRY_SEED, Constants.DWELLBERRY_SEED, Constants.JANGERBERRY_SEED, Constants.WHITEBERRY_SEED, Constants.POISON_IVY_SEED, Constants.GUAM_SEED, Constants.MARRENTILL_SEED, Constants.TARROMIN_SEED, Constants.HARRALANDER_SEED, Constants.IRIT_SEED, Constants.AVANTOE_SEED, Constants.KWUARM_SEED, Constants.CADANTINE_SEED, Constants.DWARF_WEED_SEED, Constants.TORSTOL_SEED, Constants.MUSHROOM_SPORE, Constants.BELLADONNA_SEED ,Constants.CACTUS_SEED)){

                System.out.println("Inventory full");
                ctx.input.send("{VK_SHIFT down}");
                useless.hover();
                useless.click();
                Condition.sleep(250);
                ctx.input.send("{VK_SHIFT up}");

            }
        }
        // if i need cake bank
        if (ctx.inventory.select().id(Constants.CAKE).count() < 1
                && ctx.inventory.select().id(Constants.CAKE2).count() < 1
                && ctx.inventory.select().id(Constants.CAKE3).count() < 1 || ctx.inventory.select().count() != 0){
           bankForCake();
        }

    }

    public void bankForCake(){
        //if bank is visible open it
        if (ctx.bank.inViewport()){
            System.out.println("Bank is visible");
            System.out.println("Opening Bank");
            ctx.bank.open();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            },250,10);
            //Get stack sizes
            ranarrBank = ctx.bank.select().id(Constants.RANARR_SEED).peek().stackSize();
            snapdragonBank = ctx.bank.select().id(Constants.SNAPDRAGON_SEED).peek().stackSize();
            cakeBank = ctx.bank.select().id(Constants.CAKE).peek().stackSize();
            necklaceBank = ctx.bank.select().id(Constants.DODGY_NECKLACE).peek().stackSize();
            //withdraws cakes
            System.out.println("Withdrawing cake");
            ctx.bank.withdraw(Constants.CAKE, 8);
            Condition.sleep(500);
            cakeInventory = ctx.inventory.select().id(Constants.CAKE).count();
        }
            //have at least 4 necklaces always
        System.out.println("Need more necklaces");
        int necksNeeded = ctx.inventory.select().id(Constants.DODGY_NECKLACE).count();
        int amount = 4 - necksNeeded;
        if (amount > 0){
            ctx.bank.withdraw(Constants.DODGY_NECKLACE, amount);
        }
        necklaceInventory = ctx.inventory.select().id(Constants.DODGY_NECKLACE).count();
        //close bank
        System.out.println("Closing Bank");
        ctx.bank.close();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.bank.opened();
            }
        },250,10);
        //no neck on? equip
        System.out.println("No neck on, equipping one");
        if (ctx.equipment.itemAt(Equipment.Slot.NECK).id() != Constants.DODGY_NECKLACE){
            Item neck = ctx.inventory.select().id(Constants.DODGY_NECKLACE).poll();
            neck.click();
        }
        System.out.println("Walking back");
        //walks back
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 1){
            walker.walkPathReverse(Constants.PATH_TO_BANK);
        }
    }

  



}
