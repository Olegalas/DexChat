package ua.dexchat.model;

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

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany
    @JoinColumn(name="friend_id", referencedColumnName = "id")
    private List<Client> iFriendTo = new ArrayList<>();

    @ManyToMany(mappedBy="iFriendTo", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Client> myFriends = new ArrayList<>();

    @OneToMany(mappedBy="idOwner", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<History> buffers = new ArrayList<>();

    public Client() {
    }

    public Client(String login, String name, String pass, String email) {
        this.login = login;
        this.name = name;
        this.pass = pass;
        this.email = email;
    }

    public Client(Login client){
        this.login = client.login;
        this.pass = client.pass;
        this.name = client.name;
        this.email = client.email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<History> getBuffers() {
        return buffers;
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

    public List<History> getHistory() {
        return buffers;
    }

    public void setBuffers(List<History> buffers) {
        this.buffers = buffers;
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
