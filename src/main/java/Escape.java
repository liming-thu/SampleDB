import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.lang3.StringEscapeUtils;

public class Escape {
    public static void main(String[] args) throws Exception {
        FileReader fr = new FileReader("../TestDB/id_text_coordinate.csv");
        BufferedReader bf = new BufferedReader(fr);
        String ln = "";
        int i = 0;
        while ((ln = bf.readLine()) != null) {
            ln= ln.substring(0,20)+StringEscapeUtils.escapeJava(ln.substring(20,ln.lastIndexOf("\",\"(")).replace("\"",""))+ln.substring(ln.lastIndexOf("\",\"("));
//            ln=ln.replace("\\\"(","\"(").replace(")\\",")");
            Files.write(Paths.get("escaped_id_text_coordinate.csv"),(ln+"\n").getBytes(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }
    }
}