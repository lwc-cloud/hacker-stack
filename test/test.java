import java.io.File;

public class test {
    public static void main(String[] args) throws Exception {
        for(File file : new File("../db").listFiles()) {
            String name = file.getName().substring(0 , file.getName().lastIndexOf("-"));
            String pwd = file.getName().substring(file.getName().lastIndexOf("-")+1 , file.getName().lastIndexOf("."));
            System.out.println(name +" "+pwd);
            EasyDB.insertDataToDatabase("money","0.0",name,pwd);
        }
    }
}
