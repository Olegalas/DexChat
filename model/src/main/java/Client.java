import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 28.03.16.
 */

@Entity
@Table(name="clients")
public class Client extends IdGenerate{

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String pass;

    @ManyToMany
    @JoinColumn(name="friend_id", referencedColumnName = "id")
    private List<Client> iFriendTo = new ArrayList<>();

    @ManyToMany(mappedBy="iFriendTo", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Client> myFriends = new ArrayList<>();

    public Client() {
    }

    public Client(String login, String name, String pass) {
        this.login = login;
        this.name = name;
        this.pass = pass;
    }

    public Client(Login client){
        login = client.name;
        pass = client.pass;
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

    public List<Client> getiFriendTo() {
        return iFriendTo;
    }

    public void setiFriendTo(List<Client> iFriendTo) {
        this.iFriendTo = iFriendTo;
    }

    public List<Client> getMyFriends() {
        return myFriends;
    }

    public void setMyFriends(List<Client> myFriends) {
        this.myFriends = myFriends;
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