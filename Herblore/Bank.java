package Herblore.Tasks;

import Herblore.Constants;
import Herblore.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class Bank extends Task {
    
    private int amountOfHerbs;

    public Bank(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return !ctx.bank.opened() && ctx.inventory.select().id(Constants.GRIMY_TOADFLAX).count() == 0 && ctx.inventory.select().id(Constants.VIAL_OF_WATER).count() == 0
                || !ctx.bank.opened() && ctx.inventory.select().count() == 0
                || !ctx.bank.opened() && ctx.inventory.select().id(Constants.UNFINISHED_TOADFLAX).count() >= 1;
    }

    @Override
    public void execute() {

        openBank();

    }

    public void openBank() {
        final Component bankerChat = ctx.widgets.component(231, 2);
        if (bankerChat.valid() && bankerChat.visible()) {
            System.out.println("[INFO] MISLICK");
            bankerChat.hover();
            Condition.sleep(250);
            bankerChat.click();
        } else {
            Condition.sleep(150);
            System.out.println("[INFO] Phase 1");
            System.out.println("[INFO] Opening Bank");
            ctx.bank.open();
            amountOfHerbs = ctx.bank.select().id(Constants.GRIMY_TOADFLAX).peek().stackSize();
            System.out.println("You have " + amountOfHerbs + " herbs left");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, 250, 10);

            System.out.println("[INFO] Phase 2");

            if (ctx.inventory.select().id(Constants.CLEAN_TOADFLAX).count() >= 1) {
                System.out.println("[INFO] Depositing inventory");
                ctx.bank.depositInventory();
                Constants.amountOfTimes--;
                System.out.println("[INFO] Time left for this technique are " + Constants.amountOfTimes);
                Condition.sleep(500);
            }
            if (ctx.inventory.select().id(Constants.UNFINISHED_TOADFLAX).count() >= 1) {
                System.out.println("[INFO] Depositing inventory");
                ctx.bank.depositInventory();
                Condition.sleep(500);
            }

            if (amountOfHerbs > 0) {
                ctx.bank.withdraw(Constants.GRIMY_TOADFLAX, 28);
                System.out.println("[INFO] Withdrawing Herbs");
                Condition.sleep(500);
                ctx.input.send("{VK_ESCAPE}");
                System.out.println("[INFO] Closing Bank");
                Condition.sleep(500);
            }

            if (amountOfHerbs < 0) {
                System.out.println("[INFO] Withdrawing Herbs");
                ctx.bank.withdraw(Constants.CLEAN_TOADFLAX, 14);
                Condition.sleep(250);
                System.out.println("[INFO] Withdrawing vials");
                ctx.bank.withdraw(Constants.VIAL_OF_WATER, 14);
                Condition.sleep(250);
                ctx.input.send("{VK_ESCAPE}");
                System.out.println("[INFO] Closing Bank");
                Condition.sleep(1000);
            }
        }
    }



}
