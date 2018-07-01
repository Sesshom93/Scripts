package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;

public class Drop extends Task {

    public static int seedIds[] = {Constants.POTATO_SEED, Constants.ONION_SEED ,Constants.CABBAGE_SEED ,Constants.TOMATO_SEED ,Constants.SWEETCORN_SEED, Constants.STRAWBERRY_SEED, Constants.WATERMELON_SEED, Constants.BARLEY_SEED, Constants.HAMMERSTONE_SEED, Constants.ASGARNIAN_SEED, Constants.JUTE_SEED, Constants.YANILLIAN_SEED, Constants.KRANDORIAN_SEED, Constants.WILDBLOOD_SEED, Constants.MARIGOLD_SEED, Constants.NASTURTIUM_SEED, Constants.ROSEMARY_SEED, Constants.WOAD_SEED, Constants.LIMPWURT_SEED, Constants.REDBERRY_SEED, Constants.CADAVABERRY_SEED, Constants.DWELLBERRY_SEED, Constants.JANGERBERRY_SEED, Constants.WHITEBERRY_SEED, Constants.POISON_IVY_SEED, Constants.GUAM_SEED, Constants.MARRENTILL_SEED, Constants.TARROMIN_SEED, Constants.HARRALANDER_SEED, Constants.IRIT_SEED, Constants.AVANTOE_SEED, Constants.KWUARM_SEED, Constants.CADANTINE_SEED, Constants.DWARF_WEED_SEED, Constants.TORSTOL_SEED, Constants.MUSHROOM_SPORE, Constants.BELLADONNA_SEED ,Constants.CACTUS_SEED};


    public Drop(ClientContext arg0) {
        super(arg0);
    }

    //activates if inventory is inventory is full, not in combat, or food and necklaces are needed.
    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 && !ctx.players.local().inCombat() || noFood(Heal.foodIds) || noNecklace(Constants.DODGY_NECKLACE);
    }

    @Override
    public void execute() {
        drop(seedIds);
    }

    public void drop(int seedsIds[]) {
        //If Shift dropping is enabled
        if (ctx.varpbits.varpbit(1055) == 139264 && ctx.game.tab(Game.Tab.INVENTORY)){
            System.out.println("Shift dropping enabled");
            System.out.println("Inventory full");
            ctx.input.send("{VK_SHIFT down}");
            //Drop items
            for (Item useless : ctx.inventory.id(seedsIds)){
                useless.hover();
                useless.click();
                Condition.sleep(Random.nextInt(125, 250));
            }
            ctx.input.send("{VK_SHIFT up}");
            System.out.println("Shift up");
        }
        else{
            //Enable shift dropping
            toggleShift();
        }
    }

    //enable shift dropping
    public void toggleShift(){
        final Component controls = ctx.widgets.component(261, 1).component(6);
        final Component toggleShift = ctx.widgets.component(261, 65);

        if (!ctx.game.tab().equals(Game.Tab.OPTIONS)){
            ctx.game.tab(Game.Tab.OPTIONS);
            System.out.println("Click");
            Condition.sleep(Random.nextInt(125, 250));
            if (controls.valid()){
                controls.hover();
                Condition.sleep(Random.nextInt(125, 250));
                controls.click();
                Condition.sleep(Random.nextInt(125, 250));
                System.out.println("Click");
                if (toggleShift.valid()){
                    toggleShift.hover();
                    toggleShift.click();
                    Condition.sleep(Random.nextInt(125, 250));
                    System.out.println("Click");
                    ctx.game.tab(Game.Tab.INVENTORY);
                    Condition.sleep(Random.nextInt(125, 250));
                }
            }
        }

    }

    //inventory has no food and has some seeds in it
    public boolean noFood(int foodId[]) {
        if (ctx.inventory.select().id(foodId).count() == 0 && ctx.inventory.select().id(seedIds).count() > 1){
            return true;
        } else {
            return false;
        }
    }

    //inventory has no necklaces and has some seeds in it
    public boolean noNecklace(int necklace){
        if (ctx.inventory.select().id(necklace).count() == 0 && ctx.inventory.select().id(seedIds).count() > 1){
            return true;
        } else {
            return false;
        }
    }




}
