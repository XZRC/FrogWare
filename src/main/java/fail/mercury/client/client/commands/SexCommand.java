package fail.mercury.client.client.commands;

import fail.mercury.client.Mercury;
import fail.mercury.client.api.command.Command;
import fail.mercury.client.api.command.annotation.CommandManifest;
import fail.mercury.client.api.util.ChatUtil;
import fail.mercury.client.api.util.MotionUtil;
import fail.mercury.client.api.util.TimerUtil;
import fail.mercury.client.client.events.UpdateEvent;
import net.b0at.api.event.EventHandler;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.Session;

/**
 * @author auto on 2/12/2020
 */
@CommandManifest(label = "Sex", description = "owo")
public class SexCommand extends Command {

    String bottom, top;
    public static EntityOtherPlayerMP bottomEntity, topEntity;
    public TimerUtil shiftTimer = new TimerUtil();
    public TimerUtil endTimer = new TimerUtil();
    //TODO: Make this fuckin work because its USEFUL
    @Override
    public void onRun(String[] args) {
        if (args.length <= 1) {
            ChatUtil.print("Not enough args.");
            return;
        }
        Mercury.INSTANCE.getEventManager().registerListener(this);
        bottom = args[1];
        top = args[2];
        double[] dir = MotionUtil.forward(2);
        String bottomID = Mercury.INSTANCE.getProfileManager().getUUID(bottom).toString();
        String topID = Mercury.INSTANCE.getProfileManager().getUUID(top).toString();
        Session bottomSession = new Session(bottom, bottomID, "69691", "legacy");
        Session topSession = new Session(top, topID, "69692", "legacy");
        bottomEntity = new EntityOtherPlayerMP(mc.world, bottomSession.getProfile());
        topEntity = new EntityOtherPlayerMP(mc.world, topSession.getProfile());
        bottomEntity.posX = mc.player.posX + dir[0];
        bottomEntity.posZ = mc.player.posZ + dir[1] + 1;
        bottomEntity.posY = mc.player.posY;
        bottomEntity.rotationYaw = mc.player.rotationYaw - 90;
        bottomEntity.rotationPitch = 90;
        topEntity.posX = mc.player.posX + dir[0];
        topEntity.posZ = mc.player.posZ + dir[1];
        topEntity.posY = mc.player.posY;
        topEntity.rotationYaw = mc.player.rotationYaw - 90;
        topEntity.rotationPitch = 180;
        mc.world.addEntityToWorld(69691, bottomEntity);
        mc.world.addEntityToWorld(69692, topEntity);
        ChatUtil.print(String.format("%s and %s are now doing da sex", bottom, top));
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (shiftTimer.hasReached(1000))
            bottomEntity.setSneaking(true);
        if (shiftTimer.hasReached(2000)) {
            shiftTimer.reset();
            bottomEntity.setSneaking(false);
        }
        if (endTimer.hasReached(10000)) {
            mc.world.removeEntity(bottomEntity);
            mc.world.removeEntity(topEntity);
            Mercury.INSTANCE.getEventManager().deregisterListener(this);
        }

    }

}
