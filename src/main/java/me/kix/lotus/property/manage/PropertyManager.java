package me.kix.lotus.property.manage;


import fail.mercury.client.api.manager.type.HashMapManager;
import me.kix.lotus.property.annotations.Clamp;
import me.kix.lotus.property.IProperty;
import me.kix.lotus.property.annotations.Mode;
import me.kix.lotus.property.annotations.Property;
import me.kix.lotus.property.impl.BooleanProperty;
import me.kix.lotus.property.impl.NumberProperty;
import me.kix.lotus.property.impl.string.StringProperty;
import me.kix.lotus.property.impl.string.impl.ModeStringProperty;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Manages property actions.
 *
 * @author Kix
 * Created in Mar 2019
 */
public class PropertyManager extends HashMapManager<Object, List<IProperty>> {

    /**
     * Scans for all of the properties in the object.
     *
     * @param object The object being scanned for properties.
     */
    public void scan(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            boolean accessibility = field.isAccessible();
            if (field.isAnnotationPresent(Property.class)) {
                field.setAccessible(true);
                Property property = field.getAnnotation(Property.class);
                try {
                    if (field.get(object) instanceof Boolean) {
                        register(object, new BooleanProperty(property.value(), object, field));
                    }

                    if (field.get(object) instanceof String) {
                        if (field.isAnnotationPresent(Mode.class)) {
                            Mode mode = field.getAnnotation(Mode.class);
                            register(object, new ModeStringProperty(property.value(), object, field, mode.value()));
                        } else {
                            register(object, new StringProperty(property.value(), object, field));
                        }
                    }

                    if (field.get(object) instanceof Number && field.isAnnotationPresent(Clamp.class)) {
                        Clamp clamp = field.getAnnotation(Clamp.class);

                        /* We have to do this to determine the number property's type. */
                        if (field.get(object) instanceof Integer) {
                            register(object, new NumberProperty<>(property.value(), object, field, Integer.parseInt(clamp.minimum()), Integer.parseInt(clamp.maximum())));
                        } else if (field.get(object) instanceof Double) {
                            register(object, new NumberProperty<>(property.value(), object, field, Double.parseDouble(clamp.minimum()), Double.parseDouble(clamp.maximum())));
                        } else if (field.get(object) instanceof Float) {
                            register(object, new NumberProperty<>(property.value(), object, field, Float.parseFloat(clamp.minimum()), Float.parseFloat(clamp.maximum())));
                        } else if (field.get(object) instanceof Long) {
                            register(object, new NumberProperty<>(property.value(), object, field, Long.parseLong(clamp.minimum()), Long.parseLong(clamp.maximum())));
                        } else if (field.get(object) instanceof Short) {
                            register(object, new NumberProperty<>(property.value(), object, field, Short.parseShort(clamp.minimum()), Short.parseShort(clamp.maximum())));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(accessibility);
            }
        }
    }

    /**
     * Registers a property to the collection.
     *
     * @param object   The object where the property is located.
     * @param property The property object to be added.
     */
    public void register(Object object, IProperty property) {
        /* Create the list if it doesn't exist. */
        getRegistry().computeIfAbsent(object, collection -> new ArrayList<>());

        /* Add the property. */
        pull(object).add(property);
    }

    /**
     * @param object The object which contains properties.
     * @return The properties found in the object.
     */
    public List<IProperty> getPropertiesFromObject(Object object) {
        if (getRegistry().get(object) != null) {
            return getRegistry().get(object);
        } else {
            return null;
        }
    }

    /**
     * Finds a property based on object and label.
     *
     * @param object The container of the property.
     * @param label  The property's name.
     * @return The instance of the property.
     */
    public Optional<IProperty> getProperty(Object object, String label) {
        return getPropertiesFromObject(object).stream()
                .filter(property -> property.getLabel().equalsIgnoreCase(label))
                .findFirst();
    }
}
