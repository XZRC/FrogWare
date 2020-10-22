package fail.mercury.client.api.loader;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class MercuryAccessTransformer extends AccessTransformer {

    public MercuryAccessTransformer() throws IOException {
        super("mercury_at.cfg");
    }

}
