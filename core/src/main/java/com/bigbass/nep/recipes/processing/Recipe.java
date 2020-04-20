package com.bigbass.nep.recipes.processing;

import com.bigbass.nep.recipes.elements.Pile;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    static public class IO {
        public boolean input, output;
    }

    public List<Pile> inputs = new ArrayList<>();
    public List<Pile> outputs = new ArrayList<>();
    public Integer duration;

    public static Recipe fromJson(JsonObject json) {
        Recipe instance = new Recipe();
        for (JsonValue val : json.getJsonArray("i")) {
            instance.inputs.add(Pile.fromJson(val.asJsonObject()));
        }
        for (JsonValue val : json.getJsonArray("o")) {
            instance.outputs.add(Pile.fromJson(val.asJsonObject()));
        }
        instance.duration = json.getInt("d", 0);
        return instance;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder inputs = Json.createArrayBuilder();
        for (Pile p : this.inputs) {
            inputs.add(p.toJson());
        }
        JsonArrayBuilder outputs = Json.createArrayBuilder();
        for (Pile p : this.outputs) {
            outputs.add(p.toJson());
        }

        builder.add("i", inputs.build());
        builder.add("o", outputs.build());
        builder.add("d", this.duration);
        return builder.build();
    }

    public boolean containsElement(String name, IO config) {
        return true;  // TODO (rebenkoy);
    }
}
