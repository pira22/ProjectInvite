package pira.stellerin.com.projectinvite.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Pira on 24/08/2015.
 */
public class ParseFacebookPicture {

    public static String getPicture(String json){
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject data =  jsonObj.getJSONObject("data");
            return   data.getString("url");
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String ParsePicture(String src) {

        BufferedReader input = null;
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            // replace THE_ADDRESS_YOU_WANT with the url you want to
            // comsume.
            URL url = new URL(src);

            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                response.append(line + "\t");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return "IEO";
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();


    }
}
