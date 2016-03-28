import javax.persistence.*;
import java.util.List;

/**
 * Created by dexter on 28.03.16.
 */

@Entity
@Table(name="clients")
public class Client {

    @Id
    private String login;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String pass;

    @ManyToMany
    private List<Client> friends;

    public Client() {
    }

    public Client(String login, String name, String pass, List<Client> clients) {
        this.login = login;
        this.name = name;
        this.pass = pass;
        this.friends = clients;
    }

    public Client(Login client){
        login = client.name;
        pass = client.pass;
    }

    public void setFriends(List<Client> friends) {
        this.friends = friends;
    }

    public List<Client> getFriends() {
        return friends;
    }

    public void addFriend(Client client){
        friends.add(client);
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (login != null ? !login.equals(client.login) : client.login != null) return false;
        if (name != null ? !name.equals(client.name) : client.name != null) return false;
        return pass != null ? pass.equals(client.pass) : client.pass == null;

    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }


}
