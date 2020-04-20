package com.bigbass.nep.recipes.elements;

import javax.json.JsonObject;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class AElement {
    static AElement undefinedElement = new AElement() {
        @Override
        public String name() {
            return "Undefined@NEP++";
        }

        @Override
        public String type() {
            return "Undefined@NEP++";
        }
    };

    static Map<String, Class<Object>> registry = new HashMap<>();

    public abstract String name();
    public abstract String type();

    private static String getTypeFromJSON(JsonObject object) {
        return "item";
    }

    public static AElement fromJson(JsonObject object) {
        Class<Object> cls = AElement.registry.get(AElement.getTypeFromJSON(object));
        if (cls == null) {
            System.out.println(String.format("404: Meme not found (can not determine type of Element: \n%s\n)", object));
            return AElement.undefinedElement;
        }
        Class[] types = new Class[1];
        types[0] = JsonObject.class;
        AElement obj = null;
        try {
            obj = (AElement) cls.getConstructor(types).newInstance(object);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(String.format("Element init error: %s %s ", cls.getName(), e));
            System.exit(1);
        }
        return obj;
    }

    public static void registerElementType(String name, Class<Object> type) {
        AElement.registry.put(name, type);
    }
}
