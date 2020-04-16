package com.bigbass.nep.gui;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import com.bigbass.nep.util.KeyBinding;
import com.bigbass.nep.util.Singleton;

public class KeyBindingManager extends Singleton {
    private HashMap<String, KeyBinding> bindings;

    public KeyBindingManager() {
        this.bindings = new HashMap<>();
    }

    public void addBinding(String name, KeyBinding binding) {
        this.bindings.put(name, binding);
    }

    public void setKeys(String name, int... keys) {
        this.bindings.get(name).setKeys(keys);
    }

    public void act() {
        try {
            for (Map.Entry<String, KeyBinding> binding : this.bindings.entrySet()) {
                binding.getValue().act();
            }
        } catch (Exception e) {}
    }

    public KeyBinding get(String name) {
        return this.bindings.get(name);
    }

    public Set<Map.Entry<String, KeyBinding>> entrySet() {
        return this.bindings.entrySet();
    }
}