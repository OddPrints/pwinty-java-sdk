package uk.co.mattburns.pwinty.v2_6.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import uk.co.mattburns.pwinty.v2_6.Photo.Type;

public class TypeDeserializer implements JsonDeserializer<Type> {
    @Override
    public Type deserialize(
            JsonElement json, java.lang.reflect.Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
        return Type.valueOf("_" + json.getAsJsonPrimitive().getAsString().replaceAll("-", "_"));
    }
}
