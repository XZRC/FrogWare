package fail.mercury.client.api.module.annotations;

import fail.mercury.client.api.module.category.Category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleManifest {
    String label();
    String[] aliases() default {};
    Category category();
    String fakelabel() default "";
    boolean hidden() default false;
    String description() default "";
    boolean persistent() default false;
}
