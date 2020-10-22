package fail.mercury.client.client.commands;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author auto on 3/24/2020
 */
@CommandManifest(label = "MobOwner", aliases = {"owner", "mowner"}, description = "Shows the owner of the targeted entity.")
public class MobOwnerCommand extends Command {

    @Override
    public void onRun(final String[] args) {
        if (mc.objectMouseOver != null) {
            if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                Entity entity = mc.objectMouseOver.entityHit;
                if (entity instanceof EntityTameable) {
                    EntityTameable tamable = (EntityTameable) entity;
                    if (tamable.isTamed()) {
                     String name = Mercury.INSTANCE.getProfileManager().getName(tamable.getOwnerId());
                        ChatUtil.print(String.format("Entity %s is owned by %s.", tamable.getName(), name));
                    } else {
                        ChatUtil.print(String.format("Entity %s is not tamable.", entity.getName()));
                    }
                }
            }
        }
    }

}
