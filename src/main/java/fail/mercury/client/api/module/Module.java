package fail.mercury.client.api.module;

import com.google.gson.JsonObject;
import fail.mercury.client.Mercury;
import fail.mercury.client.api.module.annotations.ModuleManifest;
import fail.mercury.client.api.module.annotations.Replace;
import fail.mercury.client.api.module.category.Category;
import me.kix.lotus.property.AbstractProperty;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private String label;
    private String[] alias;
    private String fakelabel;
    private String suffix;
    private String description;
    private int bind;
    private boolean hidden;
    private boolean enabled;
    private boolean persistent;
    private List<AbstractProperty> properties = new ArrayList<>();
    private Category category;
    public static Minecraft mc = Minecraft.getMinecraft();

    public Module() {
        if (getClass().isAnnotationPresent(Replace.class)) {
            Replace replace = getClass().getAnnotation(Replace.class);
            if (Mercury.INSTANCE.getModuleManager().find(replace.value()) != null) {
                String name = Mercury.INSTANCE.getModuleManager().find(replace.value()).getLabel();
                Mercury.INSTANCE.getModuleManager().exclude(name);

            }
        }
        if (getClass().isAnnotationPresent(ModuleManifest.class)) {
            ModuleManifest moduleManifest = getClass().getAnnotation(ModuleManifest.class);
            this.label = moduleManifest.label();
            this.category = moduleManifest.category();
            this.alias = moduleManifest.aliases();
            this.fakelabel = moduleManifest.fakelabel();
            this.hidden = moduleManifest.hidden();
            this.description = moduleManifest.description();
            this.persistent = moduleManifest.persistent();
        }
    }


    public void init() {
        Mercury.INSTANCE.getPropertyManager().scan(this);
    }

    public AbstractProperty find(String term) {
        for (AbstractProperty property : properties) {
            if (property.getLabel().equalsIgnoreCase(term)) {
                return property;
            }
        }
        return null;
    }

    public Category getCategory() {
        return category;
    }

    public List<AbstractProperty> getProperties() {
        return this.properties;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getLabel() {
        return this.label;
    }

    public String getFakeLabel() {
        return this.fakelabel;
    }

    public void setFakeLabel(String fakelabel) {
        this.fakelabel = fakelabel;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return  this.suffix;
    }

    public String getDescription() {
        return this.description;
    }

    public int getBind() {
        return this.bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            Mercury.INSTANCE.getEventManager().registerListener(this);
            onEnable();
            onToggle();
        } else {
            Mercury.INSTANCE.getEventManager().deregisterListener(this);
            onDisable();
            onToggle();
        }
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public void save(JsonObject destination) {
        if (Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this) != null) {
            Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this).forEach(property -> destination.addProperty(property.getLabel(), property.getValue().toString()));
        }
        destination.addProperty("Bind", getBind());

        if (!this.isPersistent())
            destination.addProperty("Enabled", isEnabled());

        destination.addProperty("Hidden", isHidden());
        destination.addProperty("FakeLabel", getFakeLabel());
    }


    public void load(JsonObject source) {
        source.entrySet().forEach(entry -> {
            if (Mercury.INSTANCE.getPropertyManager().getPropertiesFromObject(this) != null) {
                source.entrySet().forEach(entri -> Mercury.INSTANCE.getPropertyManager().getProperty(this, entri.getKey()).ifPresent(property -> property.setValue(entri.getValue().getAsString())));
            }
            switch (entry.getKey()) {
                case "Enabled":
                    if (!this.isPersistent()) {
                        if (entry.getValue().getAsBoolean()) {
                            setEnabled(entry.getValue().getAsBoolean());
                        }
                    }
                    break;
                case "Hidden":
                    if (entry.getValue().getAsBoolean()) {
                        setHidden(entry.getValue().getAsBoolean());
                    }
                    break;
                case "Bind":
                    setBind(entry.getValue().getAsInt());
                    break;
                case "FakeLabel":
                    setFakeLabel(entry.getValue().getAsString());
                    break;
            }
        });
    }
}
