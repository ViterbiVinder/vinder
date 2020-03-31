package CS201.Vinder;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.*;
import java.net.*;

@RestController
public class IndexController {
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello there! Vinder API is up and running!";
    }

    // * Get request for all posts with the given tag [name]
    @RequestMapping(value = "/get/tag", method = RequestMethod.GET)
    public Map<String, Object> tags(@RequestParam(required = false, defaultValue = "all") String name) {
        Map<String, Object> rtn = new LinkedHashMap<>();

        rtn.put("response", "test");
        return rtn;
    }

    // * Get request for [num] tags
    @RequestMapping(value = "/get/tags", method = RequestMethod.GET)
    public Map<String, Object> tags(@RequestParam(required = false, defaultValue = "1") int num) {
        Map<String, Object> rtn = new LinkedHashMap<>();
        if (num <= 0) {
            num = 100;
        }
        String[] res = new String[num];
        try {
            getConnection();
            res = executeTagsStatement(num);
        } catch (URISyntaxException uri) {
            res[0] = "ERROR=500; Failed Query - URI Exception.";
            System.err.print("URI Exception: " + uri.getMessage());
        } catch (SQLException sqle) {
            res[0] = "ERROR=500; Failed Query - SQL Exception.";
            System.err.print("SQL Exception: " + sqle.getMessage());
        } finally {
            rtn.put("response", res);
            return rtn;
        }
    }

    private static void getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        conn = DriverManager.getConnection(dbUrl, username, password);
    }

    private static String[] executeTagsStatement(int num) throws URISyntaxException, SQLException {
        String[] res = new String[num];
        st = conn.createStatement();
        rs = st.executeQuery("SELECT * from public.\"Tags\"");
        int count = 0;
        while (rs.next() && count < num) {
            String tagName = rs.getString("Name");
            res[count] = tagName;
            ++count;
        }
        return res;
    }
}