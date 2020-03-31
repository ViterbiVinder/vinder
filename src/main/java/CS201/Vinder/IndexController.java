package CS201.Vinder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;

@RestController
public class IndexController {
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;

    @GetMapping("/")
    public String index() {
        return "Hello there! I'm running.";
    }

    @GetMapping("/tags")
    public String index() {
        String res = "ERROR=500; Failed Query.";
        try {
            getConnection();
            res = executeTagsStatement();
        } catch (URISyntaxException uri) {
            System.err.print("URI Exception: " + uri.getMessage());
        } catch (SQLException sqle) {
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

    private static String executeTagsStatement() throws URISyntaxException, SQLException {
        String res = "";
        st = conn.createStatement();
        rs = st.executeQuery("SELECT * from public.\"Tags\"");
        while (rs.next()) {
            String tagName = rs.getString("Name");
            res += tagName + "\n";
        }
        return res;
    }
}