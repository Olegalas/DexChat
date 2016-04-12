package ua.dexchat.server.utils;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import ua.dexchat.model.Login;
import ua.dexchat.model.WebSocketMessage;

import java.lang.reflect.Type;

/**
 * Created by dexter on 05.04.16.
 */
public class JsonUtils {


    public static Login parseLoginJson(String jsonLogin) {
        Gson gson = new Gson();
        return gson.fromJson(jsonLogin, Login.class);
    }

    public static<T> T parseString(String json, Class<T> cls){
        Gson gson = new Gson();
        Object object =  gson.fromJson(json, cls);
        return Primitives.wrap(cls).cast(object);
    }

    public static String transformObjectInJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static WebSocketMessage parseWebSocketMessage(String jsonMessage){
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, WebSocketMessage.class);
    }
}
