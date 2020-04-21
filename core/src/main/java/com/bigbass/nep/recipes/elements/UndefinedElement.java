package com.bigbass.nep.recipes.elements;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Type;

public class UndefinedElement extends AElement {
    static String placeholder = "Undefined@NEP++";
    public static String type = placeholder;
    static {
        AElement.registerElementType(placeholder, UndefinedElement.class);
        AElement.mendeley.put(placeholder, new UndefinedElement());
    }
    @Override
    public String name() {
        return placeholder;
    }

    @Override
    public String HRName() {
        return placeholder;
    }

    @Override
    public String type() {
        return placeholder;
    }

    @Override
    public String eid() {
        return placeholder;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("type", placeholder);
        return builder.build();
    }

    public UndefinedElement() {};
}
