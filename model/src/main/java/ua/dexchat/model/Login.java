package ua.dexchat.model;

import java.io.Serializable;

/**
 * Created by dexter on 28.03.16.
 */
public class Login implements Serializable{

    public String name;
    public String pass;
    public String login;

    public Login() {
    }

    public Login(String name, String pass, String login) {
        this.name = name;
        this.pass = pass;
        this.login = login;
    }

    @Override
    public String toString() {
        return "Login{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login1 = (Login) o;

        if (name != null ? !name.equals(login1.name) : login1.name != null) return false;
        if (pass != null ? !pass.equals(login1.pass) : login1.pass != null) return false;
        return login != null ? login.equals(login1.login) : login1.login == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }
}
