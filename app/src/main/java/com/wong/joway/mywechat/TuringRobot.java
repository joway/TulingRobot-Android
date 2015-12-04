package com.wong.joway.mywechat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by JowayWong on 2015/9/24.
 */

public class TuringRobot {

    public static void LogPrint(String e){
        Log.d("ErrorLog",e);
    }

    public static String getResponse(String content) {

        try {

            String APIKEY = "a33208bb10bba61e9a05e7cec9037cc4";
            String INFO = URLEncoder.encode(content, "utf-8");
            String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
            URL getUrl = new URL(getURL);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();

            // 取得输入流，并使用Reader读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            // 断开连接
            connection.disconnect();

            return (sb.substring(sb.indexOf("text") + 7, sb.indexOf("\"}")));
        }
        catch (java.io.UnsupportedEncodingException e) {
            LogPrint(e.toString()+"1");
        }
        catch (java.net.MalformedURLException e){
            LogPrint(e.toString()+"2");
        }
        catch (IOException e) {
//            e.printStackTrace();
        }
        catch (Exception e){
            LogPrint(e.toString() + "3");
        }
        return "ErrorLog";
    }
}