

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class EasyDB implements Serializable {
    public static void writeObjectToFile(String f,Object object)
            throws Exception {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(f));
        outputStream.writeObject(object);
        outputStream.flush();
        outputStream.close();
    }
    public static Object readObjectFromFile(String f)
        throws Exception {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(f));
        Object object = inputStream.readObject();
        inputStream.close();
        return object;
    }
    public static boolean isUserName(String filename , String user) {
        try {
            String username = filename.substring( 0 , filename.indexOf("-"));
            return username.equals(user);
        }catch (Exception e) {
            return false;
        }
    }
    public static void createDB(String db_name , String password)
    throws Exception{
        if (db_name.contains("//")) {
            throw new Exception("Error Charset.");
        }
        File target = new File("db/"+db_name+"-"+ password +".edb");
        if (target.isFile()) {
            throw new Exception("db exists.");
        }
        else {
            File[] files = new File("db/").listFiles();
            boolean isExists = false;
            for (File f : files) {
                if (f.isFile() && isUserName(f.getName() , db_name)) {
                    isExists = true;
                    break;
                }
            }
            if (isExists) {
                throw new Exception("user was exists.");
            }
            target.createNewFile();
            writeObjectToFile(target.getAbsolutePath() , new TreeMap<String , String>());
        }
    }
    public static boolean existsDB(String db_name , String password)
        throws Exception{
        return new File("db/"+db_name+"-"+password+".edb").isFile();
    }
    public static String GetDataFromDatabase(String db , String password , String data)
        throws Exception {
        TreeMap<String , String> treeMap = (TreeMap<String, String>) readObjectFromFile("db/"+db+"-"+password+".edb");
        return treeMap.get(data);
    }
    public static ArrayList<String> searchDataFromDB(String db , String password,String searcher)
        throws Exception{
        TreeMap<String , String> treeMap = (TreeMap<String, String>) readObjectFromFile("db/"+db+"-"+password+".edb");
        ArrayList<String> arrayList = new ArrayList<>();
        for (String i : treeMap.keySet()) {
            if (i.contains(searcher)) {
                arrayList.add(i);
            }
        }
        return arrayList;
    }
    public static TreeMap<String , String> getTreeMap(String db , String password)
    throws Exception{
        return (TreeMap<String, String>) readObjectFromFile("db/"+db+"-"+password+".edb");
    }
    public static void insertDataToDatabase(String key,String data , String db,String password)
    throws Exception{
        TreeMap<String , String> treeMap = (TreeMap<String, String>) readObjectFromFile("db/"+db+"-"+password+".edb");
        treeMap.put(key , data);
        writeObjectToFile("db/"+db+"-"+password+".edb" , treeMap);
    }
}
