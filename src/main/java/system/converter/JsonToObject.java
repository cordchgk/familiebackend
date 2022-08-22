package system.converter;

import com.google.gson.Gson;

public final class JsonToObject<T> {


    public static Object getObjectFromJson(
            String json,
            Class c) {

        Gson gson = new Gson();
        Object toReturn = gson.fromJson(
                json,
                c);
        return toReturn;
    }


}
