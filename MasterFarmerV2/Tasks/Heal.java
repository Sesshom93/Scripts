package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import MasterFarmer.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;


import java.util.concurrent.Callable;

public class Heal extends Task {

    private final Walker walker = new Walker(ctx);
    public static int foodIds[] = {Constants.TROUT};

    public Heal(ClientContext arg0) {
        super(arg0);
    }


    @Override
    public boolean activate() {
        return checkForHealth();
    }

    @Override
    public void execute() {

        eat(foodIds);

    }

    //Eats food
    public void eat(int foodIds[]){
        Item foodItem = ctx.inventory.select().id(foodIds).poll();

        if (foodItem.valid()){
            foodItem.interact("Eat");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.combat.health() < 3;
                }
            },250, 10);
            Bank.foodInInventory = ctx.inventory.select().id(Heal.foodIds).count();
        }
    }

    //Check if health is at or below 3
    public boolean checkForHealth(){
        if (ctx.combat.health() <= 3){
            return true;
        } else{
            return false;
        }
    }




}
