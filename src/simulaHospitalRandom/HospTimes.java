package simulaHospitalRandom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ccalifi
 */
public class HospTimes {

    private String[] hospCodes;

    public HospTimes(Set hospCodes) {
        this.hospCodes = (String[]) hospCodes.toArray(new String[hospCodes.size()]);
    }

    public Map<String, Integer> getTimes() throws IOException {

        Get g = new Get();
        Map<String, Integer> times = new HashMap<>();

        for (String s : hospCodes) {
            try {
                String response = g.sendRequest("https://tcc-si.herokuapp.com/api/queue/getMediumTime/" + s);
                int t;
                if (response.contains("Erro")) {
                    t = 0;
                } else {
                    String[] aux = response.replace("\"", "").split(":");
                    t = ((Integer.parseInt(aux[0]) * 3600) + (Integer.parseInt(aux[1]) * 60) + Integer.parseInt(aux[2]));
                }

                times.put(s, t);
            } catch (IOException ex) {

            }

        }
        return times;

    }

}
