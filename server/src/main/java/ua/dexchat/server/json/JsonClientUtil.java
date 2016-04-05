package ua.dexchat.server.json;

import com.google.gson.Gson;
import ua.dexchat.model.Login;

/**
 * Created by dexter on 05.04.16.
 */
public class JsonClientUtil {


    public static Login parseLoginJson(String jsonLogin) {
        Gson gson = new Gson();
        return gson.fromJson(jsonLogin, Login.class);
    }

    public static String transformLoginInJson(Login login){
        Gson gson = new Gson();
        return gson.toJson(login);
    }
}
