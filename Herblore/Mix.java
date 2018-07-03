package Herblore.Tasks;

import Herblore.Constants;
import Herblore.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class Mix extends Task {


    public Mix(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return !ctx.bank.opened() && ctx.inventory.select().id(Constants.CLEAN_TOADFLAX).count() >= 1 && ctx.inventory.select().id(Constants.VIAL_OF_WATER).count() >= 1;
    }

    @Override
    public void execute() {
        makePotion();
    }

    public void makePotion(){
        Item Herb = ctx.inventory.select().id(Constants.CLEAN_TOADFLAX).reverse().poll();
        Item Vial = ctx.inventory.select().id(Constants.VIAL_OF_WATER).poll();
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
                return ctx.inventory.select().id(Constants.CLEAN_TOADFLAX).count() == 0;
            }
        }, 1000, 10);

    }
}
