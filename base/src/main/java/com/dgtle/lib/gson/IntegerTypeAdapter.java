package com.dgtle.lib.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntegerTypeAdapter extends TypeAdapter<Number> {
    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if (in.peek() == JsonToken.STRING) {
            String string = in.nextString();

            try {
                return Integer.valueOf(string);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        if (in.peek() == JsonToken.BOOLEAN) {
            boolean aBoolean = in.nextBoolean();
            try {
                if (aBoolean) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        try {
            return in.nextInt();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
    }
}