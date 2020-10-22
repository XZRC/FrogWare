package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

/**
 * @author auto on 2/15/2020
 */
@ModuleManifest(label = "FastPlace", category = Category.MISC, fakelabel = "Fast Place", description = "Handles right clicks faster.")
public class FastPlace extends Module {

    @Property("EXP")
    public boolean exp = false;

    @Property("CrystalOnly")
    public boolean crystal = false;

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (exp && !(mc.player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle))
            return;
        if (crystal && !(mc.player.inventory.getCurrentItem().getItem() instanceof ItemEndCrystal))
            return;
        mc.rightClickDelayTimer = 0;
    }
}
