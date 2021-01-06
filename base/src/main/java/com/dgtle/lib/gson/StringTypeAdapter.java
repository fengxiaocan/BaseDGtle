package com.dgtle.lib.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StringTypeAdapter extends TypeAdapter<String> {
    private static final String NULL_STRING = null;

    @Override
    public String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            return NULL_STRING;
        }
        /* coerce booleans to strings for backwards compatibility */
        if (peek == JsonToken.BOOLEAN) {
            return Boolean.toString(in.nextBoolean());
        }
        if (peek == JsonToken.BEGIN_ARRAY) {
            //添加这一句就能防止本来是string类型的,但是数据是数组[]这样格式的,直接返回null
            //防止出现 Expected a string but was BEGIN_ARRAY... 异常
            //防止出现 Expected a name but was BEGIN_ARRAY at... 异常
            //            ArrayList arrayList = new ArrayList();
            in.beginArray();
            while (in.hasNext()) {
                //                arrayList.add(read(in));
                read(in);
            }
            in.endArray();
            //            LogUtils.e("noah", "Expected a string but was BEGIN_ARRAY");
            return NULL_STRING;
        }
        if (peek == JsonToken.BEGIN_OBJECT) {
            //添加这一句就能防止本来是string类型的,但是数据是数组[]这样格式的,直接返回null
            //防止出现 Expected a string but was BEGIN_OBJECT... 异常
            //防止出现 Expected a name but was BEGIN_OBJECT at... 异常
            in.beginObject();
            in.endObject();
            //            LogUtils.e("noah", "Expected string but was BEGIN_OBJECT");
            return NULL_STRING;
        }
        return in.nextString();
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }
}
