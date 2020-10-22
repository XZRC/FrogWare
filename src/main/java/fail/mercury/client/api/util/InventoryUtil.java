package fail.mercury.client.api.util;


import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author auto on 2/3/2020
 */
public class InventoryUtil implements Util {

    public static int getItemCount(Container container, Item item) {
        int itemCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (container.getSlot(i).getHasStack()) {
                final ItemStack is = container.getSlot(i).getStack();
                if (is.getItem() == item) {
                        itemCount += is.getCount();
                }
            }
        }
        return itemCount;
    }

    public static int getItemSlot(Container container, Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            if (container.getSlot(i).getHasStack()) {
                ItemStack is = container.getSlot(i).getStack();
                if (is.getItem() == item)
                    slot = i;
            }
        }
        return slot;
    }

    public static int getItemSlotInHotbar(Item item) {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
                ItemStack is = mc.player.inventory.getStackInSlot(i);
                if (is.getItem() == item) {
                    slot = i;
                    break;
                }
        }
        return slot;
    }

    public static void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, hotbarNum, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }

}
