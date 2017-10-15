/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulaHospitalRandom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author chris
 */
public class Post {

    private String url;
    private List<NameValuePair> jsonData;

    public Post(String url, List<NameValuePair> jsonData) {
        this.url = url;
        this.jsonData = jsonData;
    }

    public ArrayList<String> sendRequest() throws IOException {

        ArrayList<String> responseList = new ArrayList<>();

        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new UrlEncodedFormEntity(jsonData));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {

                BufferedReader brResp = null;

               responseList.add(0, response.getStatusLine().toString());     
                
                HttpEntity entity = response.getEntity();
                StringBuilder sb = new StringBuilder();

                String line;

                try {
                    brResp = new BufferedReader(new InputStreamReader(entity.getContent()));
                    while ((line = brResp.readLine()) != null) {                        
                        sb.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (brResp != null) {
                        try {
                            brResp.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //cria um objeto JSON a partir da resposta do request
                if (sb.toString().contains("{")) {
                    JSONObject jsonResp = new JSONObject(sb.toString());                  
                    
                    
                    responseList.add(1, jsonResp.get("hospitalCode").toString());
                    responseList.add(2, jsonResp.get("name").toString());
                    responseList.add(3, jsonResp.get("location").toString());

                    //converte a fila em uma array
                    JSONArray jsonArray = jsonResp.getJSONArray("queue");

                    for (Object s : jsonArray) {
                        responseList.add(4, s.toString());
                    }
                    EntityUtils.consume(entity);
                } else{
                    responseList.add(0, "");
                    responseList.add(1, String.valueOf(jsonData.get(0)));
                    System.out.println("Failed post to " + url);
                }
            } finally {
                response.close();
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Post.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return responseList;

    }

}
