package fail.mercury.client.api.loader;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.Name("Mercury")
@IFMLLoadingPlugin.SortingIndex(29384)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MercuryLoader implements IFMLLoadingPlugin {

    public MercuryLoader() {
        MixinBootstrap.init();

        /*String obfuscation = ObfuscationServiceMCP.NOTCH;
        for (String s : (List<String>) Launch.blackboard.get("TweakClasses")) {
            if (s.contains("net.minecraftforge.fml.common.launcher")) {
                obfuscation = ObfuscationServiceMCP.SEARGE;
                break;
            }
        }

        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext(obfuscation);*/

        Mixins.addConfiguration("mixins.mercury.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) { }

    @Override
    public String getAccessTransformerClass() {
        return MercuryAccessTransformer.class.getName();
    }

}
