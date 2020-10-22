package fail.mercury.client.api.smalltext;

import fail.mercury.client.api.manager.type.HashMapManager;
import org.apache.commons.lang3.StringUtils;

/**
 * @author auto on 2/8/2020
 */
public class SmallTextManager extends HashMapManager<String, String> {
    
    @Override
    public void load() {
        super.load();
        include("a", "\u1D00");
        include("b", "\u0299");
        include("c", "\u1D04");
        include("d", "\u1D05");
        include("e", "\u1D07");
        include("f", "\u0493");
        include("g", "\u0262");
        include("h", "\u029C");
        include("i", "\u026A");
        include("j", "\u1D0A");
        include("k", "\u1D0B");
        include("l", "\u029F");
        include("m", "\u1D0D");
        include("n", "\u0274");
        include("o", "\u1D0F");
        include("p", "\u1D18");
        include("q", "\u01EB");
        include("r", "\u0280");
        include("s", "\uA731");
        include("t", "\u1D1B");
        include("u", "\u1D1C");
        include("v", "\u1D20");
        include("w", "\u1D21");
        include("x", "\u0078");
        include("y", "\u028F");
        include("z", "\u1D22");
        include("|", "\u23D0");
        include("-", "\u2013");
        include("!", "\uFF01");
        include("?", "\uFF1F");
    }

    public String getUnicodeFromString(String text) {
        return getRegistry().get(text.toLowerCase());
    }

    public String convert(String text) {
        String converted = text;
        for(char c : converted.toLowerCase().toCharArray()) {
            String character = String.valueOf(c);
            converted = StringUtils.replaceIgnoreCase(converted, character, getUnicodeFromString(character));
        }
        return converted;
    }

}
