package simulaHospitalRandom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HospPop implements Runnable {

    private String hospCode;
    private Queue<Integer> popIntervals;
    private boolean running = true;

    public HospPop(String hospCode) {
        this.hospCode = hospCode;
        popIntervals = new LinkedBlockingDeque<>();        
    }

    public void insertInerval(int interval) {
        popIntervals.offer(interval);
    }
    
    public boolean queueIsEmpty(){
        if(popIntervals.isEmpty()){
            return true;
        } else return false;
    }

    @Override
    public void run() {
        
        System.out.println("*running pop thread for " + hospCode + "*");
        
        while (running) {
            while (popIntervals.isEmpty()) {
                try {
                    Thread.sleep(500);
                    //System.out.println("Checking " + hospCode + " for pops");
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            Post p = null;

            if (!popIntervals.isEmpty()) {                
                //tira o próximo itervalo da fila
                int popInterval = popIntervals.peek();  
                
                
                if(popInterval == -1){
                    running = false;
                    break;
                } //cheogu no final da fila e não serão colocados mais elementos

                try {
                    Thread.sleep(popInterval * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HospPop.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                popIntervals.poll();

                List<NameValuePair> jsonData = new ArrayList<>();
                jsonData.add(new BasicNameValuePair("hospitalCode", hospCode));
                p = new Post("http://tcc-si.herokuapp.com/api/queue/pop", jsonData);

                ArrayList<String> response = new ArrayList<>();                

                try {
                    //envia o post e coloca a resposta no array
                    response = p.sendRequest();

                } catch (IOException ex) {
                    Logger.getLogger(HospPush.class.getName()).log(Level.SEVERE, null, ex);                    
                } finally {
                    if (response.get(0).contains("200")) {
                        System.out.println("<- Pop from " + response.get(1) + " OK");
                    } else {
                        System.out.println("<- Pop from " + hospCode + " FAIL");
                    }
                }

            }
        }
        HospitalStarter.signalStop();
        System.out.println("Finished popping for " + hospCode);

    }
}
