package fail.mercury.client.client.modules.misc;


import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;
import fail.mercury.client.client.events.PacketEvent;
import me.kix.lotus.property.annotations.Property;
import net.b0at.api.event.EventHandler;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.play.client.*;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
/*
@Author Crystallinqq on 2/29/2020
 */
@ModuleManifest(label = "PacketCancel", fakelabel = "Packet Cancel",category = Category.MISC, description = "Cancels certain packets for various purposes.")
public class PacketCancel extends Module {
    @Property("Animation")
    public boolean animation = false;
    @Property("KeepAlive")
    public boolean keepalive = false;
    @Property("ChatMessage")
    public boolean chatmessage = false;
    @Property("ClickWindow")
    public boolean clickwindow = false;
    @Property("ClientSettings")
    public boolean clientsettings = false;
    @Property("ClientStatus")
    public boolean clientstatus = false;
    @Property("CloseWindow")
    public boolean closewindow = false;
    @Property("ConfirmTeleport")
    public boolean confirmteleport = false;
    @Property("ConfirmTransaction")
    public boolean confirmtransaction = false;
    @Property("CreativeInventoryAction")
    public boolean creativeinventoryaction = false;
    @Property("CustomPayload")
    public boolean custompayload = false;
    @Property("HeldItemChange")
    public boolean helditemchange = false;
    @Property("EnchantItem")
    public boolean enchantitem = false;
    @Property("EntityAction")
    public boolean entityaction = false;
    @Property("PlaceRecipe")
    public boolean placerecipe = false;
    @Property("Input")
    public boolean input = false;
    @Property("Player")
    public boolean player = false;
    @Property("PlayerAbilities")
    public boolean playerabilities = false;
    @Property("PlayerTryUseItem")
    public boolean tryuseitem = false;
    @Property("PlayerTryUseItemOnBlock")
    public boolean tryuseitemonblock = false;
    @Property("PlayerDigging")
    public boolean playerdigging = false;
    @Property("RecipeInfo")
    public boolean recipeinfo = false;
    @Property("ResourcePackStatus")
    public boolean resourcepackstatus = false;
    @Property("SeenAdvancements")
    public boolean seendadvancements = false;
    @Property("Spectate")
    public boolean spectate = false;
    @Property("SteerBoat")
    public boolean steerboat = false;
    @Property("TabComplete")
    public boolean tabcomplete = false;
    @Property("UpdateSign")
    public boolean updatesign = false;
    @Property("VehicleMove")
    public boolean vehiclemove = false;
    @Property("EncryptionResponse")
    public boolean encryptionresponse = false;
    @Property("Ping")
    public boolean ping = false;
    @Property("ServerQuery")
    public boolean serverquery = false;

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (animation) {
            if (e.getPacket() instanceof CPacketAnimation) {
                e.setCancelled(true);
            }
        }
        if (keepalive) {
            if (e.getPacket() instanceof CPacketKeepAlive) {
                e.setCancelled(true);
            }
        }
        if (chatmessage) {
            if (e.getPacket() instanceof CPacketChatMessage) {
                e.setCancelled(true);
            }
        }
        if (clickwindow) {
            if (e.getPacket() instanceof CPacketClickWindow) {
                e.setCancelled(true);
            }
        }
        if (clientsettings) {
            if (e.getPacket() instanceof CPacketClientSettings) {
                e.setCancelled(true);
            }
        }
        if (clientstatus) {
            if (e.getPacket() instanceof CPacketClientStatus) {
                e.setCancelled(true);
            }
        }
        if (closewindow) {
            if (e.getPacket() instanceof CPacketCloseWindow) {
                e.setCancelled(true);
            }
        }
        if (confirmteleport) {
            if (e.getPacket() instanceof CPacketConfirmTeleport) {
                e.setCancelled(true);
            }
        }
        if (confirmtransaction) {
            if (e.getPacket() instanceof CPacketConfirmTransaction) {
                e.setCancelled(true);
            }
        }
        if (creativeinventoryaction) {
            if (e.getPacket() instanceof CPacketCreativeInventoryAction) {
                e.setCancelled(true);
            }
        }
        if (custompayload) {
            if (e.getPacket() instanceof CPacketCustomPayload) {
                e.setCancelled(true);
            }
        }
        if (enchantitem) {
            if (e.getPacket() instanceof CPacketEnchantItem) {
                e.setCancelled(true);
            }
        }
        if (entityaction) {
            if (e.getPacket() instanceof CPacketEntityAction) {
                e.setCancelled(true);
            }
        }
        if (helditemchange) {
            if (e.getPacket() instanceof CPacketHeldItemChange) {
                e.setCancelled(true);
            }
        }
        if (input) {
            if (e.getPacket() instanceof CPacketInput) {
                e.setCancelled(true);
            }
        }
    if (placerecipe) {
        if (e.getPacket() instanceof CPacketPlaceRecipe) {
            e.setCancelled(true);
        }
    }
    if (player) {
        if (e.getPacket() instanceof CPacketPlayer) {
            e.setCancelled(true);
        }
    }
    if (playerabilities) {
        if (e.getPacket() instanceof CPacketPlayerAbilities) {
            e.setCancelled(true);
        }
    }    if (playerdigging) {
            if (e.getPacket() instanceof CPacketPlayerDigging) {
                e.setCancelled(true);
            }
        }    if (tryuseitem) {
            if (e.getPacket() instanceof CPacketPlayerTryUseItem) {
                e.setCancelled(true);
            }
        }
        if (tryuseitemonblock) {
            if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                e.setCancelled(true);
            }
        }    if (recipeinfo) {
            if (e.getPacket() instanceof CPacketRecipeInfo) {
                e.setCancelled(true);
            }
        }
        if (resourcepackstatus) {
            if (e.getPacket() instanceof CPacketResourcePackStatus) {
                e.setCancelled(true);
            }
        }
        if (seendadvancements) {
            if (e.getPacket() instanceof CPacketSeenAdvancements) {
                e.setCancelled(true);
            }
        }
        if (spectate) {
            if (e.getPacket() instanceof CPacketSpectate) {
                e.setCancelled(true);
            }
        }
        if (steerboat) {
            if (e.getPacket() instanceof CPacketSteerBoat) {
                e.setCancelled(true);
            }
        }
        if (tabcomplete) {
            if (e.getPacket() instanceof CPacketTabComplete) {
                e.setCancelled(true);
            }
        }
        if (updatesign) {
            if (e.getPacket() instanceof CPacketUpdateSign) {
                e.setCancelled(true);
            }
        }
        if (vehiclemove) {
            if (e.getPacket() instanceof CPacketVehicleMove) {
                e.setCancelled(true);
            }
        }
        if (encryptionresponse) {
            if (e.getPacket() instanceof CPacketEncryptionResponse) {
                e.setCancelled(true);
            }
        }
        if (ping) {
            if (e.getPacket() instanceof CPacketPing) {
                e.setCancelled(true);
            }
        }
        if (serverquery) {
            if (e.getPacket() instanceof CPacketServerQuery) {
                e.setCancelled(true);
            }
        }

    }
        }
