package ua.dexchat.server.utils;

import com.google.gson.*;
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

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(messageString);
        JsonObject object = element.getAsJsonObject();

        JsonObject message = object.getAsJsonObject("message");
        JsonArray array = message.getAsJsonArray("friends");
        String name = message.get("name").getAsString();
        String login = message.get("login").getAsString();
        String email = message.get("email").getAsString();

        for(JsonElement tmp : array){
            JsonObject obj = tmp.getAsJsonObject();
            String nameFriend = obj.get("name").getAsString();
            String loginFriend = obj.get("login").getAsString();
            String emailFriend = obj.get("email").getAsString();
            friends.add(new ClientDTO(loginFriend, nameFriend, emailFriend));
        }

        return new ClientDTO(login, name, email,friends);
    }
}
