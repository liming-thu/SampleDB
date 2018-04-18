import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class Query implements Runnable {
    private String statement;
    private double sampleRatio;
//    private OutputStream outputStream;
//    private InputStream inputStream;

    public Query(String s, double r) {
        statement = s;
        sampleRatio = r;
        //
    }

    @Override
    public void run() {
        VAS vas = new VAS();
        Float[][] pointList = getCoordinates();
        if (pointList!=null&&pointList.length > 0) {
            vas.getSample(pointList, (int) (pointList.length * sampleRatio));
            System.out.println(statement.substring(statement.indexOf("stateID="), statement.indexOf("and t.create_at")));
        }
    }
    public Float[][] wsGetCoordinates(){
        WebSocketClient client=new WebSocketClient();
        try {
            client.start();
            URI uri = new URI("ws://localhost:19002/query/service");
            ClientUpgradeRequest request=new ClientUpgradeRequest();
            AstSocket socket=new AstSocket();
            //
            client.connect(socket,uri,request);
            //
        }catch (Exception ex){

        }
        return null;
    }
    public Float[][] getCoordinates() {
        try {
            String strUrl = "http://localhost:19002/query/service";
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.connect();
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(statement.getBytes());
            outputStream.flush();
            outputStream.close();
            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder text = new StringBuilder();
            String ln = "";
            while ((ln = bufferedReader.readLine()) != null) {
                text.append(ln);
            }
            bufferedReader.close();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Response response = objectMapper.readValue(text.toString(), Response.class);
            return response.getResults();
        } catch (Exception ex) {
//                System.out.println("err:" + ex.getMessage());
            return null;
        }
    }
}
