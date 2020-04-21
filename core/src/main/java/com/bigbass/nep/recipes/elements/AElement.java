package com.bigbass.nep.recipes.elements;

import com.bigbass.nep.recipes.processing.Recipe;

import javax.json.JsonObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AElement {
    static Map<String, Class<?>> registry = new HashMap<>();
    public static Map<String, AElement> mendeley = new HashMap<>();

    public Set<Recipe> asInput = new HashSet<>();
    public Set<Recipe> asOutput = new HashSet<>();

    public abstract String name();
    public abstract String HRName();

    public abstract String type();
    public abstract String eid();

    public abstract JsonObject toJson();

    private static String getTypeFromJSON(JsonObject object) {
        return object.getString("t");
    }

    public static AElement fromJson(JsonObject object) {
        Class<?> cls = AElement.registry.get(AElement.getTypeFromJSON(object));
        if (cls == null) {
            System.out.println(String.format("404: Meme not found (can not determine type of Element: \n%s\n)", object));
            return AElement.mendeley.get(UndefinedElement.placeholder);
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
        if (!mendeley.containsKey(obj.eid())) {
            mendeley.put(obj.eid(), obj);
        }
        return mendeley.get(obj.eid());
    }

    public static void registerElementType(String name, Class<?> type) {
        AElement.registry.put(name, type);
    }
}
