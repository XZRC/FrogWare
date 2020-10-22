package fail.mercury.client.client.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.InventoryUtil;
import fail.mercury.client.client.events.UpdateEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

/**
 * @author auto on 2/15/2020
 */
@ModuleManifest(label = "CaptchaSolver", category = Category.MISC, aliases = {"Auto Captcha"}, fakelabel = "Captcha Solver")
public class CaptchaSolver extends Module {

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (mc.getCurrentServerData() == null)
            return;
        if (mc.player.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.player.openContainer;
            IInventory inv = chest.getLowerChestInventory();
            String serverName = mc.getCurrentServerData().serverIP;
            if (inv.hasCustomName()) {
                String chestName = inv.getDisplayName().getFormattedText();
                    if (StringUtils.containsIgnoreCase(serverName, "endcrystal.me")) {
                        for (int i = 9; i < 45; ++i) {
                            if (chest.getSlot(i).getHasStack()) {
                                ItemStack is = chest.getSlot(i).getStack();
                                if (is.getItem() == Items.DIAMOND_SWORD && is.getDisplayName().contains("§a")) {
                                    mc.playerController.windowClick(chest.windowId, InventoryUtil.getItemSlot(chest, is.getItem()), 0, ClickType.PICKUP, mc.player);
                                    mc.playerController.updateController();
                                }
                            }
                        }
                    }
                    if (StringUtils.containsIgnoreCase(serverName, "mc.salc1.com") && chestName.contains("Click ")) {
                            String strippedName = ChatFormatting.stripFormatting(inv.getName()
                                .replace("Click on the ", ""));
                        if (strippedName.equalsIgnoreCase("Jack_o'_Lantern"))
                            strippedName = "Jack_o'Lantern";
                        strippedName = strippedName.replace("spade", "shovel")
                                .replace("enchantment", "enchanting")
                                .replace("o'_lantern", "o'lantern");
                        if (InventoryUtil.getItemCount(chest, Item.getByNameOrId(strippedName.toLowerCase())) > 0) {
                            mc.playerController.windowClick(chest.windowId, InventoryUtil.getItemSlot(chest, Item.getByNameOrId(strippedName.toLowerCase())), 0, ClickType.PICKUP, mc.player);
                            mc.playerController.updateController();
                        }
                    }
                }
            }
        }
    }
