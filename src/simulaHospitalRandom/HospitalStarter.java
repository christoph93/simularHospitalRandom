package simulaHospitalRandom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HospitalStarter {

    private FileReader fr;
    String fileName;
    static Queue<User> intervals;
    static HashMap<String, HospPop> hospitals;
    static int stopSignal;

    public HospitalStarter(String file) {
        fileName = file;
    }

    static HashMap<String, HospPop> getHospitals() {
        return hospitals;
    }

    /*
    
    Lê os a lista de chegadas global e decide para qual hospital o usuário será enviado
    
     */
    public void start() throws InterruptedException {

        try {
            fr = new FileReader(fileName);

            intervals = new LinkedBlockingDeque();

            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine(); //para pular aprimeira linha do arquivo
            line = br.readLine(); //linha dos hopsitais

            String[] hospCodes = line.split(";"); //coloca os hospCodes em um array            

            String[] lineElements;

            //aramzena os tempos de viagem para cada hospital por linha
            Map<String, Integer> travelTimes;

            //começa leitura do aquivo
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    //converte a linha em array separando os elementos por ;                   
                    lineElements = line.split(";");
                    travelTimes = new HashMap<>();

                    //primeiro elemento: intervalo para o próximo push
                    //segundo elemento: tempo de atendimento (tempo para o próximo pop)
                    //demais elementos: tempos de viagem para cada hospital, na ordem que estão na 2ª linha do arquivo
                    //coloca os tempos de viagem correspondentes a cada hospital no map de tempos (começando pelo 3º elemento
                    for (int i = 2; i < hospCodes.length + 2; i++) {
                        travelTimes.put(hospCodes[i - 2], Integer.parseInt(lineElements[i]));
                    }
                    //coloca todos os usuários com os tempos de viagem, intervalo e espera na lista de intervalos                    
                    intervals.offer(new User(travelTimes, Integer.parseInt(lineElements[0]), Integer.parseInt(lineElements[1])));
                }
            }

            br.close();
            fr.close();

            hospitals = new HashMap<>(hospCodes.length);
            stopSignal = 0;

            //ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(stopSignal)
            ExecutorService executor = Executors.newFixedThreadPool(hospCodes.length + 3);

            Clock c = new Clock();
            executor.execute(c);

            HospPush hospPush = new HospPush();
            System.out.println("*Starting push thread*");
            executor.execute(hospPush);

            //cria uma pop thread para cada hospital
            for (String s : hospCodes) {
                HospPop hPop = new HospPop(s);
                hospitals.put(s, hPop);
                //System.out.println("*Starting pop thread for " + s + "*");
                executor.execute(hPop);
            }

            executor.shutdown();
            while (!executor.isTerminated() && (stopSignal != hospCodes.length)) {
                if (stopSignal >= hospCodes.length) {
                    Clock.stop();
                }
                Thread.sleep(500);
            }
            Clock.stop();
            System.out.println("Shutting down!");

            System.out.println("Final waiting times: ");

            HospTimes ht = new HospTimes(hospitals.keySet());
            Map<String, Integer> times = ht.getTimes();
            times.keySet().forEach((s) -> {
                System.out.println(s + ": " + times.get(s));
            });

        } catch (FileNotFoundException ex) {
            Logger.getLogger(HospitalStarter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HospitalStarter.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public FileReader getFr() {
        return fr;
    }

    static void signalStop() {
        stopSignal++;
    }

    static Queue<User> getIntervals() {
        return intervals;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
