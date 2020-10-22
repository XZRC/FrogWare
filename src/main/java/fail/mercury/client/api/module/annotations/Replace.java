package fail.mercury.client.api.module.annotations;

import fail.mercury.client.api.module.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author auto on 2/2/2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Replace {
    Class<? extends Module> value();
}
