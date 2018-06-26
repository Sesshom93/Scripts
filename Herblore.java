package HerbloreState;

import com.sun.org.apache.bcel.internal.generic.CASTORE;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

@Script.Manifest (name = "Herblorev2", description = "Cleans herbs and makes potions.", properties = "author=Javier; topic=999; client =4")

public class Herblore extends PollingScript<ClientContext> {


    private static final int GRIMY_GUAM = 199;
    private static final int CLEAN_GUAM = 249;
    private static final int VIAL_OF_WATER = 227;
    private static final int UNFINISHED_GUAM_POTION = 91;
    private int amountOfHerbs;
    Item Herb = ctx.inventory.select().id(CLEAN_GUAM).reverse().poll();
    Item Vial = ctx.inventory.select().id(VIAL_OF_WATER).poll();

    @Override
    public void start() {




    }

    @Override
    public void poll() {

        final STATE state = state();

        switch (state){

            case MIXING:
                mix();
                break;

            case CLEANING:
                Normal();
                break;

            case BANKINGHERBS:
                withdrawHerbs();
                break;
            case OPENINGBANK:
                openBank();
                break;

            default:
                 break;
        }

    }

    private enum STATE {
        OPENINGBANK,BANKINGHERBS, BANKINGVIALS, CLEANING, MIXING, IDLE
    }

    private STATE state(){
        ctx.inventory.select();

        if (!ctx.bank.opened() && ctx.inventory.select().id(CLEAN_GUAM).count() == 28 || !ctx.bank.opened() && ctx.inventory.select().count() == 0){
            return STATE.OPENINGBANK;
        }

        if (ctx.bank.opened() && ctx.inventory.select().id(CLEAN_GUAM).count() == 28 || ctx.bank.opened() && ctx.inventory.select().count() == 0){
            return STATE.BANKINGHERBS;
        }

        if (ctx.inventory.select().id(GRIMY_GUAM).count() >= 1){
            return STATE.CLEANING;
        }

        if (!ctx.bank.opened() && ctx.inventory.select().id(CLEAN_GUAM).count() == 14 && ctx.inventory.select().id(VIAL_OF_WATER).count() == 14){
            return STATE.MIXING;
        }



        return STATE.IDLE;
    }

    public void openBank(){
        System.out.println("[INFO] Phase 1");
        System.out.println("[INFO] Opening Bank");
        ctx.bank.open();
        amountOfHerbs = ctx.bank.select().id(GRIMY_GUAM).peek().stackSize();
        System.out.println("You have " + amountOfHerbs + " herbs left");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.opened();
            }
        }, 250, 10);
    }

    private void withdrawHerbs(){
        System.out.println("[INFO] Phase 2");
        if (ctx.inventory.select().id(CLEAN_GUAM).count() == 28){
            System.out.println("[INFO] Depositing inventory");
            ctx.bank.depositInventory();
            Condition.sleep(500);
        }

        if (ctx.inventory.select().id(CLEAN_GUAM).count() == 0 && amountOfHerbs > 0){
            ctx.bank.withdraw(GRIMY_GUAM, 28);
            System.out.println("[INFO] Withdrawing Herbs");
            Condition.sleep(500);
            ctx.input.send("{VK_ESCAPE}");
            System.out.println("[INFO] Closing Bank");
            Condition.sleep(500);
        }
        if (amountOfHerbs < 0 && ctx.inventory.count() == 0){
            ctx.bank.withdraw(CLEAN_GUAM, 14);
            System.out.println("[INFO] Withdrawing Herbs");
            Condition.sleep(500);
            ctx.bank.withdraw(VIAL_OF_WATER, 14);
            System.out.println("[INFO] Withdrawing vials");
            Condition.sleep(500);
            ctx.input.send("{VK_ESCAPE}");
            System.out.println("[INFO] Closing Bank");
            Condition.sleep(1000);
        }
    }

    public void Normal(){
        for (Item Herb : ctx.inventory.id(GRIMY_GUAM)){
            Herb.hover();
            Herb.click();
            Condition.sleep(250);
        }
    }

    public void mix(){
        Herb.hover();
        Herb.click();
        System.out.println("[INFO] Clicking herb");
        Condition.sleep(500);
        Vial.hover();
        Vial.click();
        System.out.println("[INFO] Clicking vial");
        Condition.sleep(1000);
        ctx.input.send("{VK_SPACE}");
        System.out.println("[INFO] Pressing space");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().id(UNFINISHED_GUAM_POTION).count() == 14;
            }
        }, 1500, 10);

    }



}
