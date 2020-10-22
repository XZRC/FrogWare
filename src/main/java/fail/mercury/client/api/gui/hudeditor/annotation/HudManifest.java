package fail.mercury.client.api.gui.hudeditor.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HudManifest {
    String label();
    float x();
    float y();
    float width();
    float height();
    boolean shown() default true;
    boolean showlabel() default true;
}
