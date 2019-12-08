package com.example.minitest.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class network {
    private final static String backend_ip = "http://192.168.100.3:3000/";//192.168.43.234
   //private final static String backend_ip = "http://192.168.43.234:3000/";//192.168.43.234


    public static String getBackend_ip(){
        return backend_ip;
    }

    //POST/DELETE http request
    public static String makePostRequest(String stringUrl, String type,String body) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        String line;
        StringBuffer jsonString = new StringBuffer();

        uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        uc.setRequestMethod(type);
        uc.setDoInput(true);
        uc.setInstanceFollowRedirects(false);
        uc.connect();

        OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
        writer.write(body);
        writer.close();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        uc.disconnect();
        return jsonString.toString();
    }

    //GET http request
    public static String request(String uri) throws IOException {
        StringBuilder sb = new StringBuilder();

        URL url = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            // temporary string to hold each line read from the reader.
            String inputLine;
            while ((inputLine = bin.readLine()) != null) {
                sb.append(inputLine);
            }
        } finally {
            // regardless of success or failure, we will disconnect from the URLConnection.
            urlConnection.disconnect();
        }
        return sb.toString();
    }

}
