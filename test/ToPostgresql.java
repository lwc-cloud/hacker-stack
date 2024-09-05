import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TreeMap;

public class ToPostgresql {
    public static String DBURL = null;
    public static String DBUserName = null;
    public static String DBPassword = null;

    public static Object readObjectFromFile(String path) throws Exception{
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
        Object object = inputStream.readObject();
        inputStream.close();
        return object;
    }

    public static void main(String[] args) throws Exception{
        Class.forName("org.postgresql.Driver");
        File file = new File("./db/");

        JsonObject jsonObject = new JsonObject(JsonObject.getFileString("config.json"));
        DBURL = (String) jsonObject.get("db_url");
        DBUserName = (String) jsonObject.get("db_user");
        DBPassword = (String) jsonObject.get("db_pwd");

        String url = DBURL+"/accounts";
        Connection conn = DriverManager.getConnection(url, DBUserName, DBPassword);
        Statement stmt = conn.createStatement();


        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                continue;
            }
            try {
                TreeMap<String,String> treeMap = (TreeMap<String, String>) readObjectFromFile(f.getAbsolutePath());
                String username = f.getName().substring(0,f.getName().indexOf("-")).trim();
                String password = f.getName().substring(f.getName().indexOf("-")+1 , f.getName().lastIndexOf(".")).trim();

                String sqlQuery = "INSERT INTO accounts (username,password,about,apikey) VALUES('"+username+"','"+password+"','"+treeMap.get("about")+"',NULL);";
                stmt.execute(sqlQuery);
            }
            catch (Exception e) {
                continue;
            }
        }
    }
}
