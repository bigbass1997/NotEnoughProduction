package com.bigbass.nep.recipes.elements;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Pile {
    public Integer amount;
    public AElement element;

    public Pile(Integer amount, AElement element) {
        this.amount = amount;
        this.element = element;
    }

    public static Pile fromJson(JsonObject json) {
        return new Pile(
                json.getJsonNumber("a").intValue(),
                AElement.mendeley.get(json.getString("e"))
        );
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("a", this.amount);
        builder.add("e", this.element.eid());
        return builder.build();
    }
}
