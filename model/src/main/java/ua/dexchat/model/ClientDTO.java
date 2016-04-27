package ua.dexchat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 27.04.16.
 */
public class ClientDTO {

    private String login;
    private String name;

    private List<ClientDTO> friends = new ArrayList<>();

    public ClientDTO(String login, String name){
        this.login = login;
        this.name = name;
    }

    public ClientDTO(Client client){
        this.login = client.getLogin();
        this.name = client.getName();
        initFriends(client.getMyFriends());
    }

    public static ClientDTO getClientDTO(Client client){
        return new ClientDTO(client.getLogin(), client.getName());
    }

    private void initFriends(List<Client> myFriends) {
        for(Client client : myFriends){
            friends.add(ClientDTO.getClientDTO(client));
        }
    }

}
