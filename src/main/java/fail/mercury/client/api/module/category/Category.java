package fail.mercury.client.api.module.category;

public enum Category {
    COMBAT("Combat", true),
    MOVEMENT("Movement", true),
    PLAYER("Player", true),
    MISC("Misc", true),
    VISUAL("Visual", true);

    String label;

    boolean visible;

    Category(String label, boolean visible) {
        this.label = label;
        this.visible = visible;
    }

    public String getLabel() {
        return this.label;
    }

    public boolean isVisible() {
        return this.visible;
    }

}
