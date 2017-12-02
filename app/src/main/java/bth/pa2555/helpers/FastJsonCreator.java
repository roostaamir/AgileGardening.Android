package bth.pa2555.helpers;


import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class FastJsonCreator {
    public static JsonObject create(HashMap<String, String> keyValue) {
        JsonObject object = new JsonObject();
        for (Map.Entry<String, String> entry : keyValue.entrySet()) {
            object.addProperty(entry.getKey(), entry.getValue());
        }
        return object;
    }
}
