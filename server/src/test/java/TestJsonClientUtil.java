import org.junit.Assert;
import org.junit.Test;
import ua.dexchat.model.Login;
import ua.dexchat.server.json.JsonClientUtil;

/**
 * Created by dexter on 05.04.16.
 */
public class TestJsonClientUtil {

    private static final String expectedJson = "{\"name\":\"name\",\"pass\":\"pass\",\"login\":\"login\"}";
    private static final Login expectedLogin = new Login("name", "pass", "login");

    @Test
    public void testParseLogin(){
        Login actualLogin = JsonClientUtil.parseLoginJson(expectedJson);
        Assert.assertEquals(expectedLogin, actualLogin);
    }

    @Test
    public void testTransformLoginInJson(){
        String jsonLogin = JsonClientUtil.transformLoginInJson(expectedLogin);
        Assert.assertEquals(expectedJson, jsonLogin);
    }
}
