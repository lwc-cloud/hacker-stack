import java.io.File;
import java.util.TreeMap;

public class test {
    public static void main(String[] args) throws Exception {
        //System.out.println(JsonObject.getFileString("1.json"));
        String json  =JsonObject.getFileString("1.json");
        long start = System.currentTimeMillis();
        for (int i = 0 ; i < 1000 ;i++) {
            JsonObject jsonObject = new JsonObject(json);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
