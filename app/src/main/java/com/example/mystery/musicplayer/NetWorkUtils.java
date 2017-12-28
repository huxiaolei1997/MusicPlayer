package com.example.mystery.musicplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mystery on 2017/12/18.
 */

public class NetWorkUtils {
    private static final String API = "http://139.196.140.168/musicService/getSongBySearch";
    public static String getSongBySort(String sort) {
        PrintWriter out = null;
        BufferedReader bufferedReader = null;
        // 初始化返回结果
        String result = "";
        try {
            //sort = "sort=" + sort;
            URL url = new URL(API);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            out = new PrintWriter(outputStream);
            out.print("search=" + sort);
            out.flush();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            httpURLConnection.disconnect();
        } catch (Exception e) {
//           throw new RuntimeException("发送post请求出现异常！");
            e.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
