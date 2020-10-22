package fail.mercury.client.client.capes;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
 @author Crystallinqq on 2/4/20
 */
public class Capes {

    public static final Logger log = LogManager.getLogger("Mercury");
    private static Capes INSTANCE;
    private CapeUser[] capeUser;

    public Capes() {
        INSTANCE = this;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://raw.githubusercontent.com/Crystallinqq/mercurycapes/master/capes.json").openConnection();

            connection.connect();
            this.capeUser = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), CapeUser[].class);
            connection.disconnect();
        } catch (Exception e) {
            Capes.log.error("Failed to load capes");
            e.printStackTrace();
        }

        if (capeUser != null) {
            for(CapeUser user : capeUser) {
                bindTexture(user.url, "capes/baldent/" + formatUUID(user.uuid));
            }
        }
    }

    public static ResourceLocation getCapeResource(AbstractClientPlayer player) {
        for(CapeUser user : INSTANCE.capeUser) {
            if(player.getUniqueID().toString().equalsIgnoreCase(user.uuid)) {
                return new ResourceLocation("capes/baldent/" + formatUUID(user.uuid));
            }
        }

        return null;
    }

    private void bindTexture(String url, String resource) {
        IImageBuffer iib = new IImageBuffer() {
            @Override
            public BufferedImage parseUserSkin(BufferedImage image) {
                return parseCape(image);
            }

            @Override
            public void skinAvailable() {}
        };

        ResourceLocation rl = new ResourceLocation(resource);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.getTexture(rl);
        ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, url, null, iib);
        textureManager.loadTexture(rl, textureCape);

    }

    private BufferedImage parseCape(BufferedImage img)  {
        int imageWidth = 64;
        int imageHeight = 32;

        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();
        while (imageWidth < srcWidth || imageHeight < srcHeight) {
            imageWidth *= 2;
            imageHeight *= 2;
        }
        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return imgNew;
    }

    private static String formatUUID(String uuid) {
        return uuid.replaceAll("-", "");
    }

    private class CapeUser {
        public String uuid;
        public String url;
    }
}
