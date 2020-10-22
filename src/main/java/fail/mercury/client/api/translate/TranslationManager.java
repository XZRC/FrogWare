package fail.mercury.client.api.translate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author auto on 3/25/2020
 */
public class TranslationManager {
    public final String API_KEY = "trnsl.1.1.20200403T133250Z.c0062863622d7503.ca7fca44b9d2259ba3dadd61ddf7c15a2c9f3876";

    private JsonObject request(String URL) throws IOException {
        java.net.URL url = new URL(URL);
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla");

        InputStream inStream = urlConn.getInputStream();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) urlConn.getContent()));

        inStream.close();
        return root.getAsJsonObject();
    }

    public String translate(String text, Language sourceLang, Language lang) throws IOException {
        return request("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + API_KEY + "&text=" + text.replace(" ", "%20") + "&lang=" + sourceLang.getCode() + "-" + lang.getCode()).get("text").getAsString();
    }

    public Language detectLanguage(String text) throws IOException {
        String response = request("https://translate.yandex.net/api/v1.5/tr.json/detect?key=" + API_KEY + "&text=" + text.replace(" ", "%20")).get("lang").getAsString();
        return Language.getByCode(response);
    }
}
