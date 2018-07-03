package Herblore.Tasks;

import Herblore.Constants;
import Herblore.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Clean extends Task {

    int roll;


    public Clean(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(Constants.GRIMY_TOADFLAX).count() > 0;
    }

    @Override
    public void execute() {
        if (Constants.amountOfTimes == 0){
            Constants.amountOfTimes = 2;
            roll = Random.nextInt(1,4);
            System.out.println("[INFO] Using technique " + roll);
        }

        if (Constants.amountOfTimes > 0 && roll == 1){
            Normal();
        }
        if (Constants.amountOfTimes > 0 && roll == 2){
            Reverse();
        }
        if (Constants.amountOfTimes > 0 && roll == 3){
            Shuffle();
        }

    }

    public void Normal(){
        System.out.println("[INFO] Cleaning herbs.");
        for (Item Herb : ctx.inventory.id(Constants.GRIMY_TOADFLAX)){
            Herb.hover();
            Herb.click();
            Condition.sleep(250);
        }
    }

    public void Reverse(){
        System.out.println("[INFO] Cleaning herbs.");
        for (Item Herb : ctx.inventory.id(Constants.GRIMY_TOADFLAX).reverse()){
            Herb.hover();
            Herb.click();
            Condition.sleep(250);
        }
    }

    public void Shuffle(){
        System.out.println("[INFO] Cleaning herbs.");
        for (Item Herb : ctx.inventory.id(Constants.GRIMY_TOADFLAX).shuffle()){
            Herb.hover();
            Herb.click();
            Condition.sleep(250);
        }
    }


}
