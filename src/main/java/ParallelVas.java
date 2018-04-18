import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ParallelVas {
    public static void main(String[] args) {
        ParallelVas();
//        WS();
    }

    public static void ParallelVas() {
        long s=System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int i = 1; i < 75; i++) {
            String statement = "select VALUE coordinate from twitter.ds_tweet t where t.geo_tag.stateID=" + i + " and t.create_at>datetime(\"2017-08-08T00:00:00.000\") and t.create_at<datetime(\"2017-08-09T00:00:00.000\");";
            Runnable query = new Query(statement, 0.01);
            executorService.execute(query);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        long e=System.nanoTime();
        System.out.println("time(s):"+(e-s)/1000000000);
    }
    public static void WS(){
        WebSocketClient client=new WebSocketClient();
        try {
            client.start();
            URI uri = new URI("ws://localhost:19002/query/service");
            ClientUpgradeRequest request=new ClientUpgradeRequest();
            request.setMethod("POST");
            request.setHeader("Content-Type","application/x-www-form-urlencoded");
            AstSocket socket=new AstSocket();
            //
            client.connect(socket,uri,request);
            socket.awaitClose(1000, TimeUnit.SECONDS);
            //
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            try {
                client.stop();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        }

    }
}
