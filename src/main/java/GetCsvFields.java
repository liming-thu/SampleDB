import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GetCsvFields {
    public static void main(String[] args) throws Exception {
        String query = "use dataverse twitter;" +
//                "set output-record-type \"OutputFields\";" +
                "for $n in dataset ds_tweet return {\"id\":$n.id,\"text\":$n.text,\"coordinate\":$n.coordinate};";
        URL asterix = new URL("http://ipubmed2.ics.uci.edu:19002/query?query=" +
                URLEncoder.encode(query, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) asterix.openConnection();
        conn.setRequestProperty("Accept", "text/csv");
        BufferedReader result = new BufferedReader
                (new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = result.readLine()) != null) {
            Files.write(Paths.get("id_text_coordinate.dat"),(line+"\n").getBytes(), StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        }
        result.close();
    }
}
