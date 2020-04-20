package com.bigbass.nep.recipes.elements.usual;

import com.bigbass.nep.recipes.elements.AElement;
import com.bigbass.nep.recipes.elements.UndefinedElement;

import javax.json.*;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.HashMap;
import java.util.Map;

public class Item extends AElement {
    private String name, hrName;

    private Map<String, String> NBT;

    public static String type = "item";

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
        return Item.type;
    }

    @Override
    public String eid() {
        return Item.type + "@" + this.name;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("t", Item.type);
        builder.add("n", this.name);
        builder.add("h", this.hrName);
        if (this.NBT.size() > 0) {
            JsonObjectBuilder nbtBuilder = Json.createObjectBuilder();
            for (Map.Entry<String, String> pair : this.NBT.entrySet()) {
                nbtBuilder.add(pair.getKey(), pair.getValue());
            }
            builder.add("nbt", nbtBuilder.build());
        }
        return builder.build();
    }

    public Item(JsonObject json) {
        this.name = json.getString("n");
        this.hrName = json.getString("h");
        this.NBT = new HashMap<>();
        if (json.containsKey("nbt")) {
            for (Map.Entry<String, JsonValue> entry : json.getJsonObject("nbt").entrySet()) {
                this.NBT.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    public Item(String name, String hrName, Map<String, String> nbt) {
        this.NBT = nbt;
        this.hrName = hrName;
        this.name = name;
    }
}
