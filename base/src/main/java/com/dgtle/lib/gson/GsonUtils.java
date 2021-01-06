package com.dgtle.lib.gson;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: GsonUtils
 * 创 建 人: qczhang
 * // * 创建日期: 2016/a11/17 0017 14:57
 * // * 描    述: ${desc} TODO
 */
public class GsonUtils {

    private static final Gson sGson = new GsonBuilder().disableHtmlEscaping().create();

    static {
        Class<Gson> aClass = Gson.class;
        try {
            //添加自定义的解析类型,防止后台班扑街胡乱返回各种不同类型导致解析错误,老是要改来改去
            //防止本来是string类型的,但是数据是数组[]这样格式的,直接返回null
            //防止出现 Expected a string but was BEGIN_ARRAY... 异常
            //防止出现 Expected a name but was BEGIN_ARRAY at... 异常
            //上面的这些异常出现在Gson->fromJson()->TypeAdapter->JsonReader->beginArray()或nextString()或beginObject()等方法中
            //这些方法在各种TypeAdapter中调用
            //只要改写这些类型的TypeAdapter中的方法,捕获异常就可以了
            //这些TypeAdapter在TypeAdapterFactory中创建
            //TypeAdapterFactory保存在Gson中的factories属性中
            //该属性属于隐藏属性,只能使用反射方法
            //1.先获取Gson中的属性
            Field field = aClass.getDeclaredField("factories");
            //2.设置为可见
            field.setAccessible(true);
            //3.获取实例sGson中的属性参数
            List list = (List) field.get(sGson);

            //获取到的list其实是 java.util.Collections.UnmodifiableRandomAccessList类型的
            //在使用Arrays.asList()后调用add，remove这些method时出现java.lang.UnsupportedOperationException异常。这是由于Arrays.asList() 返回java.util.Arrays$ArrayList， 而不是ArrayList。Arrays$ArrayList和ArrayList都是继承AbstractList，remove，add等method在AbstractList中是默认throw UnsupportedOperationException而且不作任何操作。ArrayList override这些method来对list进行操作，但是Arrays$ArrayList没有override remove()，add()等，所以throw UnsupportedOperationException。
            //4.所以要重新把该List转换为ArrayList
            List temp = new ArrayList(list);
            //TypeAdapterFactory newFactory = newFactory(int.class, Integer.class, INTEGER);
            List<Object> objects = new ArrayList<>();
            for (Object o : temp) {
                if (o == TypeAdapters.INTEGER_FACTORY) {
                    objects.add(o);
                } else if (o == TypeAdapters.STRING_FACTORY) {
                    objects.add(o);
                } else {
                    if (o.getClass() == ReflectiveTypeAdapterFactory.class) {
                        objects.add(o);
                    } else if (o.getClass() == CollectionTypeAdapterFactory.class) {
                        objects.add(o);
                    }
                }
            }
            temp.removeAll(objects);

            //5.添加StringTypeAdapter,解决解析string类型时遇到的错误
            temp.add(0, TypeAdapters.newFactory(String.class, new StringTypeAdapter()));

            //解决数字转换类型异常
            temp.add(0, TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter()));

            ConstructorConstructor constructorConstructor = getClassField(aClass, sGson, "constructorConstructor");

            CollectionTypeAdapterFactory2 typeAdapterFactory2 = new CollectionTypeAdapterFactory2(
                    constructorConstructor);
            //添加解析数组时的兼容性错误
            temp.add(typeAdapterFactory2);

            Excluder excluder = getClassField(aClass, sGson, "excluder");
            FieldNamingStrategy fieldNamingStrategy = getClassField(aClass, sGson, "fieldNamingStrategy");

            ReflectiveTypeAdapterFactory2 adapterFactory2 = new ReflectiveTypeAdapterFactory2(constructorConstructor,
                    fieldNamingStrategy, excluder, new JsonAdapterAnnotationTypeAdapterFactory2(constructorConstructor));

            //添加自定义ObjectTypeAdapter解决解析Expected BEGIN_OBJECT but was BEGIN_ARRAY
            temp.add(adapterFactory2);

            //把该list重新设置到实例sGson的factories属性中
            field.set(sGson, temp);

            //LogUtils.e("noah","反射--->插入factories成功!");
        } catch (Exception e) {
            e.printStackTrace();
            //LogUtils.e("noah","反射--->插入factories失败!" + e.getMessage());
        }
    }

    public static void init() {
    }

    private static <T> T getClassField(Class clazz, Object object, String name)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(object);
    }

    /**
     * 把json解析成T类型
     *
     * @param json 哟啊解析的json
     * @return 返回结果
     */
    public static <T> T get(String json, Class<T> clazz) {
        try {
            return sGson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T get(String json, Type typeOfT) {
        try {
            return sGson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T get(String json) {
        Type type = new TypeToken<T>() {
        }.getType();
        return sGson.fromJson(json, type);
    }

    public static <T> T get(String json, T t) {
        try {
            return sGson.<T>fromJson(json, t.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String toJson(T t) {
        String json = sGson.toJson(t);
        return json;
    }

    /**
     * 解析没有数据头的纯数组
     * 如下:
     * [
     * {
     * "name": "zhangsan",
     * "age": "10",
     * "phone": "11111",
     * "email": "11111@11.com"
     * },
     * {
     * "name": "lisi",
     * "age": "20",
     * "phone": "22222",
     * "email": "22222@22.com"
     * },
     * ...
     * ]
     */
    public static <T> ArrayList<T> parseNoHeaderJsonArray(String json, Class<T> clazz) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        Gson gson = new Gson();
        ArrayList<T> list = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T userBean = gson.fromJson(user, clazz);
            list.add(userBean);
        }
        return list;
    }

    /**
     * 解析有数据头的纯数组
     * 如下
     * {
     * "muser": [
     * {
     * "name": "zhangsan",
     * "age": "10",
     * "phone": "11111",
     * "email": "11111@11.com"
     * },
     * {
     * "name": "lisi",
     * "age": "20",
     * "phone": "22222",
     * "email": "22222@22.com"
     * },
     * ...
     * ]
     * }
     * <p>
     * muser为headerName
     */
    public static <T> ArrayList<T> parseHaveHeaderJsonArray(String json, String headerName, Class<T> clazz) {
        //先转JsonObject
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        //再转JsonArray 加上数据头
        JsonArray jsonArray = jsonObject.getAsJsonArray(headerName);

        Gson gson = new Gson();
        ArrayList<T> list = new ArrayList<>();

        //循环遍历
        for (JsonElement user : jsonArray) {
            //通过反射 得到UserBean.class
            T userBean = gson.fromJson(user, new TypeToken<T>() {
            }.getType());
            list.add(userBean);
        }
        return list;
    }

    public static Gson getGson() {
        return sGson;
    }
}

