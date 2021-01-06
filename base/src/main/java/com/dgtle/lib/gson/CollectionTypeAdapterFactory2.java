package com.dgtle.lib.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

public class CollectionTypeAdapterFactory2 implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public CollectionTypeAdapterFactory2(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!Collection.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

        @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
                TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter,
                constructor);
        return result;
    }

    private static final class Adapter<E> extends TypeAdapter<Collection<E>> {
        private final TypeAdapter<E> elementTypeAdapter;
        private final ObjectConstructor<? extends Collection<E>> constructor;

        public Adapter(Gson context, Type elementType, TypeAdapter<E> elementTypeAdapter,
                       ObjectConstructor<? extends Collection<E>> constructor) {
            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper2<E>(context, elementTypeAdapter, elementType);
            this.constructor = constructor;
        }

        @Override
        public Collection<E> read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            if (in.peek() == JsonToken.STRING) {
                String msg = in.nextString();
                //                LogUtils.e("noah", "Expected BEGIN_ARRAY but was String ->>>" + msg);
                return null;
            }
            if (in.peek() == JsonToken.NUMBER) {
                in.nextInt();
                //                LogUtils.e("noah", "Expected BEGIN_ARRAY but was Number ->>>" + msg);
                return null;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                in.nextBoolean();
                //                LogUtils.e("noah", "Expected BEGIN_ARRAY but was Number ->>>" + msg);
                return null;
            }
            if (in.peek() == JsonToken.BEGIN_OBJECT) {
                //添加这一句就能防止本来是string类型的,但是数据是数组[]这样格式的,直接返回null
                //防止出现 Expected a string but was BEGIN_OBJECT... 异常
                //防止出现 Expected a name but was BEGIN_OBJECT at... 异常
                in.beginObject();
                in.endObject();
                //                LogUtils.e("noah", "Expected BEGIN_ARRAY but was BEGIN_OBJECT");
                return null;
            }
            Collection<E> collection = constructor.construct();
            in.beginArray();
            while (in.hasNext()) {
                E instance = elementTypeAdapter.read(in);
                collection.add(instance);
            }
            in.endArray();
            return collection;
        }

        @Override
        public void write(JsonWriter out, Collection<E> collection) throws IOException {
            if (collection == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            for (E element : collection) {
                elementTypeAdapter.write(out, element);
            }
            out.endArray();
        }
    }
}

