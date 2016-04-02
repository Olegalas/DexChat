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
}
