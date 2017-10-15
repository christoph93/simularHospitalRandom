package simulaHospitalRandom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
                String response = g.sendRequest("http://tcc-si.herokuapp.com/api/queue/getMediumTime/" + s);                
                String[] aux = response.replace("\"", "").split(":");

                int t = ((Integer.parseInt(aux[0]) * 3600) + (Integer.parseInt(aux[1]) * 60) + Integer.parseInt(aux[2]));
                times.put(s, t);
            } catch (IOException ex) {

            }

        }
        return times;

    }

}
