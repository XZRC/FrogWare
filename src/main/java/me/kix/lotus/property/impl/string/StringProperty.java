package me.kix.lotus.property.impl.string;


import me.kix.lotus.property.AbstractProperty;

import java.lang.reflect.Field;

/**
 * An implementation of {@link me.kix.lotus.property.IProperty} for strings.
 *
 * @author Kix
 * Created in Mar 2019
 */
public class StringProperty extends AbstractProperty<String> {

    public StringProperty(String label, Object parentObject, Field value) {
        super(label, parentObject, value);
    }
}
