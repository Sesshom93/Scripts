package MasterFarmer.Tasks;

import MasterFarmer.Constants;
import MasterFarmer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Drop extends Task {

    public Drop(ClientContext arg0) {
        super(arg0);
    }

    //activates if inventory is inventory is full and not in combat
    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 && !ctx.players.local().inCombat();
    }

    @Override
    public void execute() {

        drop();

    }

    public void drop() {
        //drops all specified items
        for (Item useless : ctx.inventory.id(Constants.POTATO_SEED, Constants.ONION_SEED ,Constants.CABBAGE_SEED ,Constants.TOMATO_SEED ,Constants.SWEETCORN_SEED, Constants.STRAWBERRY_SEED, Constants.WATERMELON_SEED, Constants.BARLEY_SEED, Constants.HAMMERSTONE_SEED, Constants.ASGARNIAN_SEED, Constants.JUTE_SEED, Constants.YANILLIAN_SEED, Constants.KRANDORIAN_SEED, Constants.WILDBLOOD_SEED, Constants.MARIGOLD_SEED, Constants.NASTURTIUM_SEED, Constants.ROSEMARY_SEED, Constants.WOAD_SEED, Constants.LIMPWURT_SEED, Constants.REDBERRY_SEED, Constants.CADAVABERRY_SEED, Constants.DWELLBERRY_SEED, Constants.JANGERBERRY_SEED, Constants.WHITEBERRY_SEED, Constants.POISON_IVY_SEED, Constants.GUAM_SEED, Constants.MARRENTILL_SEED, Constants.TARROMIN_SEED, Constants.HARRALANDER_SEED, Constants.IRIT_SEED, Constants.AVANTOE_SEED, Constants.KWUARM_SEED, Constants.CADANTINE_SEED, Constants.DWARF_WEED_SEED, Constants.TORSTOL_SEED, Constants.MUSHROOM_SPORE, Constants.BELLADONNA_SEED ,Constants.CACTUS_SEED)){

            System.out.println("Inventory full");
            ctx.input.send("{VK_SHIFT down}");
            useless.hover();
            useless.click();
            Condition.sleep(250);
            ctx.input.send("{VK_SHIFT up}");

        }
    }


}
