import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jdk.nashorn.internal.parser.JSONParser;
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

    public void run() {
        VAS vas = new VAS();
        long s1=System.nanoTime();
        Float[][] pointList = getCoordinates();
        long e1=System.nanoTime();
        if (pointList!=null&&pointList.length > 0) {
            long s=System.nanoTime();
            vas.getSample(pointList, (int) (pointList.length * sampleRatio));
            long e=System.nanoTime();
            System.out.println("get data:"+(e1-s1)/1000000.0+" vas:"+(e-s)/1000000.0);
//            System.out.println(statement.substring(statement.indexOf("stateID="), statement.indexOf("and t.create_at")));
        }
    }
    //read data from file;
    public void run2(){
        try {
            Path path= Paths.get("/root/TestDB/state/"+statement);
            List<String> ln=Files.readAllLines(path);
            //
            String[] strs=ln.get(0).replace("None","").replace("[","").replace("]","").split("\\,");
            Float[][] pointList=new Float[strs.length/2][2];
            //
            for(int i=0,j=0;i<strs.length;i++){
                if(i%2==0) {
                    pointList[j][0]=Float.parseFloat(strs[i]);
                }else{
                    pointList[j][1]=Float.parseFloat(strs[i]);
                    j++;
                }
            }
            VAS vas=new VAS();
            vas.getSample(pointList, (int) (pointList.length * sampleRatio));
            System.out.println(pointList.length);
        }catch (Exception ex){

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
