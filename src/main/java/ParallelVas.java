import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ParallelVas {
    public static void main(String[] args) {
        ParallelVas();
//        WS();
//        ParallelVasFromFile();// should exchange the function names of Query.run and Query.run2
    }

    public static void ParallelVas() {
        long s = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 1; i < 75; i++) {
            String statement = "select VALUE coordinate from twitter.ds_tweet t where t.geo_tag.stateID=" + i + " and t.create_at>datetime(\"2017-08-08T00:00:00.000\") and t.create_at<datetime(\"2017-08-09T00:00:00.000\");";
            Runnable query = new Query(statement, 0.1);
            executorService.execute(query);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        long e = System.nanoTime();
        System.out.println("time(s):" + (e - s) / 1000000000);
    }

    public static void ParallelVasFromFile() {
        long s=System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (File file: (new File("/root/TestDB/state/").listFiles())) {
            Runnable query = new Query(file.getName(), 0.2);
            executorService.execute(query);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        long e=System.nanoTime();
        System.out.println("time(s):"+(e-s)/1000000000.0);
    }

    public static void WS() {
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
