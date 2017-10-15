package simulaHospitalRandom;

public class SimulaHospital {

    public static void main(String[] args) throws InterruptedException  {

        
        HospitalStarter hosp1 = new HospitalStarter(args[0]);
        hosp1.start();        

    }

}
