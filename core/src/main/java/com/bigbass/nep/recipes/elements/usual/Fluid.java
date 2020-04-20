package com.bigbass.nep.recipes.elements.usual;

import com.bigbass.nep.recipes.elements.AElement;
import com.bigbass.nep.recipes.elements.UndefinedElement;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Map;

public class Fluid extends AElement {
    private String name, hrName;

    public static String type = "fluid";
    static {
        AElement.registerElementType(type, UndefinedElement.class);
    }
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String HRName() {
        return this.hrName;
    }

    @Override
    public String type() {
        return Fluid.type;
    }

    @Override
    public String eid() {
        return Fluid.type + "@" + this.name;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("t", Fluid.type);
        builder.add("n", this.name);
        builder.add("h", this.hrName);
        return builder.build();
    }

    public Fluid(JsonObject json) {
        this.name = json.getString("uN");
        this.hrName = json.getString("lN");
    }

    public Fluid(String name, String hrName) {
        this.hrName = hrName;
        this.name = name;
    }
}
