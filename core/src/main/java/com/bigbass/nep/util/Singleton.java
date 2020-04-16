package com.bigbass.nep.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Singleton {

    static Map<Integer, Object> instances = new HashMap<>();

    static public <T> T getInstance(Class<T> cls, Object... args) {
        Integer hash = cls.hashCode();
        if (instances.get(hash) == null) {
            T instance = null;
            try {
                Class[] argsTypes = new Class[args.length];
                for (int n = 0; n < args.length; ++n) {
                    argsTypes[n] = args[n].getClass();
                }
                instance = cls.getDeclaredConstructor(argsTypes).newInstance(args);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                System.out.println(String.format("Singleton %s %s ", cls.getName(), e));
                System.exit(1);
            }
            instances.put(hash, instance);
        }
        return ((T)instances.get(hash));
    }
}
