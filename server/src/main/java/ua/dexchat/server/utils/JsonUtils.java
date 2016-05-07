package ua.dexchat.server.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Primitives;
import ua.dexchat.model.ClientDTO;
import ua.dexchat.model.Login;
import ua.dexchat.model.WebSocketMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public static ClientDTO getClientDTO(String messageString) {

        List<ClientDTO> friends = new ArrayList<>();

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(messageString);
        JsonObject object = element.getAsJsonObject();
        JsonArray array = object.getAsJsonArray("friends");
        String name = object.get("name").getAsString();
        String login = object.get("login").getAsString();

        for(JsonElement tmp : array){
            JsonObject obj = tmp.getAsJsonObject();
            String nameFriend = obj.get("name").getAsString();
            String loginFriend = obj.get("login").getAsString();
            friends.add(new ClientDTO(loginFriend, nameFriend));
        }

        return new ClientDTO(login, name, friends);
    }
}
