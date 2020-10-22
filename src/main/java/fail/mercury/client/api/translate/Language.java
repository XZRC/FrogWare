package fail.mercury.client.api.translate;

/**
 * @author auto on 4/3/2020
 */
public enum Language {

    Azerbaijan("az"),
    Albanian("sq"),
    Amharic("am"),
    English("en"),
    Arabic("ar"),
    Armenian("hy"),
    Afrikaans("af"),
    Basque("eu"),
    Bashkir("ba"),
    Belarusian("be"),
    Bengali("bn"),
    Burmese("my"),
    Bulgarian("bg"),
    Bosnian("bs"),
    Welsh("cy"),
    Hungarian("hu"),
    Vietnamese("vi"),
    Haitian("ht"),
    Galician("gl"),
    Dutch("nl"),
    HillMari("mrj"),
    Greek("el"),
    Georgian("ka"),
    Gujarati("gu"),
    Danish("da"),
    Hebrew("he"),
    Yiddish("yi"),
    Indonesian("id"),
    Irish("ga"),
    Italian("it"),
    Icelandic("is"),
    Spanish("es"),
    Kazakh("kk"),
    Kannada("kn"),
    Catalan("ca"),
    Kyrgyz("ky"),
    Chinese("zh"),
    Korean("ko"),
    Xhosa("xh"),
    Khmer("km"),
    Laotian("lo"),
    Latin("la"),
    Latvian("lv"),
    Lithuanian("lt"),
    Luxembourgish("lb"),
    Malagasy("mg"),
    Malay("ms"),
    Malayalam("ml"),
    Maltese("mt"),
    Macedonian("mk"),
    Maori("mi"),
    Marathi("mr"),
    Mari("mhr"),
    Mongolian("mn"),
    German("de"),
    Nepali("ne"),
    Norwegian("no"),
    Russian("ru"),
    Punjabi("pa"),
    Papiamento("pap"),
    Persian("fa"),
    Polish("pl"),
    Portuguese("pt"),
    Romanian("ro"),
    Cebuano("ceb"),
    Serbian("sr"),
    Sinhala("si"),
    Slovakian("sk"),
    Slovenian("sl"),
    Swahili("sw"),
    Sundanese("su"),
    Tajik("tg"),
    Thai("th"),
    Tagalog("tl"),
    Tamil("ta"),
    Tatar("tt"),
    Telugu("te"),
    Turkish("tr"),
    Udmurt("udm"),
    Uzbek("uz"),
    Ukrainian("uk"),
    Urdu("ur"),
    Finnish("fi"),
    French("fr"),
    Hindi("hi"),
    Croatian("hr"),
    Czech("cs"),
    Swedish("sv"),
    Scottish("gd"),
    Estonian("et"),
    Esperanto("eo"),
    Javanese("jv"),
    Japanese("ja");

    private String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static Language getByCode(String code) {
        for (Language language : values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        return null;
    }
}
