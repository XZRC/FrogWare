package fail.mercury.client.api.manager.type;

import fail.mercury.client.api.manager.IManager;

import java.util.ArrayList;
import java.util.List;

public abstract class ListManager<T> implements IManager {

    protected List<T> registry = new ArrayList<>();

    protected List<Class> classRegistry;

    public List<T> getRegistry() {
        return registry;
    }

    public void setRegistry(List<T> registry) {
        this.registry = registry;
    }

    public List<Class> getClassRegistry() {
        return classRegistry;
    }

    public boolean has(T check) {
        return registry.contains(check);
    }

    public boolean has(Class check) {
        return classRegistry.contains(check);
    }

    public void include(T add) {
        if (!has(add))
            registry.add(add);
    }

    public void include(Class add) {
        if (!has(add))
            classRegistry.add(add);
    }

    public void remove(T remove) {
        if (has(remove))
            registry.remove(remove);
    }

    public void remove(Class remove) {
        if (has(remove))
            classRegistry.remove(remove);
    }

    public void register(T... queue) {
        for (T type : queue) {
            include(type);
        }
    }

    public void register(Class... queue) {
        for (Class type : queue) {
            include(type);
        }
    }

    public T pull(Class<? extends T> clazz) {
        return getRegistry().stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);
    }

    public void clear() {
        if (!getRegistry().isEmpty())
            getRegistry().clear();
    }
}
