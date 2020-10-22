package fail.mercury.client.api.mixin;

import com.mojang.authlib.GameProfile;
import fail.mercury.client.client.capes.LayerCape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {
    /*
     @author Crystallinqq on 2/4/20
     */
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void AbstractClientPlayer(World worldIn, GameProfile playerProfile, CallbackInfo callbackInfo) {
        for (RenderPlayer renderPlayer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            LayerCape cape = new LayerCape(renderPlayer);
            renderPlayer.addLayer(cape);
        }
    }
}
