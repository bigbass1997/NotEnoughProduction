package com.bigbass.nep.util;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class UJSON {
    public static String prettyPrint(JsonObject json) {
        StringWriter sw = new StringWriter();

        try {
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);

            JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
            JsonWriter jsonWriter = writerFactory.createWriter(sw);

            jsonWriter.writeObject(json);
            jsonWriter.close();
        } catch (Exception e) {
            System.out.println("Can not pretty print: %s");
        }

        return sw.toString();
    }

    public static String prettyPrint(JsonArray json) {
        StringWriter sw = new StringWriter();

        try {
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);

            JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
            JsonWriter jsonWriter = writerFactory.createWriter(sw);

            jsonWriter.writeArray(json);
            jsonWriter.close();
        } catch (Exception e) {
            System.out.println("Can not pretty print: %s");
        }

        return sw.toString().trim();
    }
}
