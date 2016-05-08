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

    public ClientDTO(String login, String name, List<ClientDTO> friends){
        this.login = login;
        this.name = name;
        this.friends = friends;
    }

    public static ClientDTO getClientDTOWithFriends(Client client){
        return new ClientDTO(client.getLogin(), client.getName(), initFriends(client.getMyFriends()));
    }

    public static ClientDTO getClientDTOWithoutFriends(Client client){
        return new ClientDTO(client.getLogin(), client.getName());
    }

    private static List<ClientDTO> initFriends(List<Client> myFriends) {
        List<ClientDTO> friends = new ArrayList<>();
        for(Client client : myFriends){
            friends.add(ClientDTO.getClientDTOWithoutFriends(client));
        }
        return friends;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClientDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<ClientDTO> friends) {
        this.friends = friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientDTO clientDTO = (ClientDTO) o;

        if (login != null ? !login.equals(clientDTO.login) : clientDTO.login != null) return false;
        return name != null ? name.equals(clientDTO.name) : clientDTO.name == null;

    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", friends=" + friends +
                '}';
    }
}
