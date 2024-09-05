import java.io.File;
import java.sql.*;
import java.util.TreeMap;

public class test {
    public static String DBURL = null;
    public static String DBUserName = null;
    public static String DBPassword = null;

    public static void main(String[] args) {
        // 调用方法并打印结果
        long nextHourAsInt = ToolKits.getNextHourAsInt() ;
        System.out.println("下一个小时的时间（以整数形式）: " + nextHourAsInt);
    }
}
