/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulaHospitalRandom;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccalifi
 */
class Clock implements Runnable {

    public int i;
    static boolean aux = true;

    @Override
    public void run() {
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        i = 0;
        while (aux) {
            System.out.println(i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
            }

            i++;
        }                
    }

   static void stop(){
       aux = false;
   } 
   

}
