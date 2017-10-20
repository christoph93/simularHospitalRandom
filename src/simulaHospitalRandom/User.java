/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulaHospitalRandom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author ccalifi
 */
public class User {

    private Map<String, Integer> travelTimes;
    private Map<String, Integer> queueWaitTimes;
    private HashMap<String, Integer> totalTimes;
    private int arrivalDelay;
    private int service;
    private String bestChoice = "no best choice";

    public User(Map<String, Integer> travelTimes, int arrival, int service) {
        this.travelTimes = travelTimes;
        queueWaitTimes = new HashMap<>(travelTimes.size());
        this.arrivalDelay = arrival;
        this.service = service;
    }

    //calcula o melhor tempo fazendo a chamada em tempo real
    public HashMap<String, Integer> calculateTotalTimes() throws IOException {

        HashMap<String, Integer> localTotalTimes = new HashMap<>(travelTimes.keySet().size());

        HospTimes htimes = new HospTimes(travelTimes.keySet());
        queueWaitTimes = htimes.getTimes();

        queueWaitTimes.keySet().stream().forEach((s) -> {
            //calcula o tempo total para cada hospital         
            localTotalTimes.put(s, travelTimes.get(s) + queueWaitTimes.get(s));
        });
        this.totalTimes = localTotalTimes;

//        System.out.println("totaTimes: " + totalTimes);
//        System.out.println("travelTimes: " + travelTimes);
//        System.out.println("queueTimes: " + queueWaitTimes);
        return this.totalTimes;
    }

    //calcula o melhor tempo recebendo um Map (para teste offline ou para calcular em itervalos)
    public HashMap<String, Integer> calculateTotalTimes(Map<String, Integer> waitTimes) {
        HashMap<String, Integer> totalTimes = new HashMap<>(travelTimes.keySet().size());
        queueWaitTimes = waitTimes;

        queueWaitTimes.keySet().stream().forEach((s) -> {
            //calcula o tempo total para cada hospital         
            totalTimes.put(s, travelTimes.get(s) + queueWaitTimes.get(s));
        });
        this.totalTimes = totalTimes;
        return totalTimes;
    }

    public String bestChoice() {

        Random r = new Random();

        Object[] tTimes = totalTimes.keySet().toArray();
        int i = r.nextInt(tTimes.length);

        this.bestChoice = (String) tTimes[i];

        return this.bestChoice;
    }

    @Override
    public String toString() {
        return "Travel times: " + travelTimes
                + " Wait times: " + queueWaitTimes
                + " Total times: " + totalTimes
                + " Best choice: " + bestChoice
                + "\nArrival delay: " + arrivalDelay
                + " Service time: " + service;

    }

    public Map<String, Integer> getTravelTimes() {
        return travelTimes;
    }

    public void setTravelTimes(Map<String, Integer> travelTimes) {
        this.travelTimes = travelTimes;
    }

    public Map<String, Integer> getWaitTimes() {
        return queueWaitTimes;
    }

    public void setWaitTimes(Map<String, Integer> waitTimes) {
        this.queueWaitTimes = waitTimes;
    }

    public int getArrivalDelay() {
        return arrivalDelay;
    }

    public void setNextArrival(int arrival) {
        this.arrivalDelay = arrival;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

}
