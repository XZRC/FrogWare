package fail.mercury.client.client.modules.visual;

import fail.mercury.client.api.module.Module;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.category.Category;

/**
 * @author Crystallinqq 2/25/2020
 */
@ModuleManifest(label = "FullBright", aliases = {"Brightness"}, category = Category.VISUAL)
public class FullBright extends Module {
    private float originalgamma;

    @Override
    public void onEnable() {
        originalgamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1.5999999E7F;
    }
    @Override
    public void onDisable()
    {
        mc.gameSettings.gammaSetting = originalgamma;
    }
}
