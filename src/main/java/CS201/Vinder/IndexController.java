package CS201.Vinder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.io.*;
import java.net.*;

@RestController
public class IndexController {
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;

    @GetMapping("/", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public String index() {
        return "Hello there! I'm running.";
    }

    @GetMapping("/tags", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public String[] tags(@RequestParam int num) {
        if(!num || num <= 0) {
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
            return res;
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