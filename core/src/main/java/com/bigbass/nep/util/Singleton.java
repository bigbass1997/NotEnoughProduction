package com.bigbass.nep.util;

import java.util.HashMap;
import java.util.Map;

public class Singleton {

    static Map<Integer, Object> instances = new HashMap<>();

    static public <T> T getInstance(Class<T> cls) {
        Integer hash = cls.hashCode();
        if (instances.get(hash) == null) {
            T instance = null;
            try {
                instance = cls.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println(String.format("Singleton %s %s ", cls.getName(), e));
                System.exit(1);
            }
            instances.put(hash, instance);
        }
        return ((T)instances.get(hash));
    }
}
