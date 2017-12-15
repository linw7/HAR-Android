package com.example.lele.protoui;

/**
 * Created by LeLe on 2017/8/21.
 */

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnNet {

    public ConnNet() {
    }

    public HttpURLConnection getConn(String urlpath) {
        String finalurl = "http://192.168.1.66:8080/fashion_server/" + urlpath;
        //创建HttpURLConnection对象
        HttpURLConnection connection = null;

        try {
            URL e = new URL(finalurl);
            //将urlConnection转换为HttpURLConnection对象，以便用到HttpURLConnection更多的API
            connection = (HttpURLConnection) e.openConnection();
            //设置是否向httpUrlConnection输出，因为这是POST请求，参数要放在http正文内，因此需要设为true，默认情况下是false
            connection.setDoInput(true);
            //设置是否从httpUrlConnection读入，默认情况下是true
            connection.setDoOutput(true);
            //POST请求不能使用缓存
            connection.setUseCaches(false);
            //设定传送的内容类型是可序列化的java对象，以防当WEB服务默认的不是这种类型时可能抛出java.io.EOFException
            connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
            connection.setRequestProperty("Charset", "utf-8");
            //设定请求的方法为“POST”，默认是GET
            connection.setRequestMethod("POST");
        } catch (MalformedURLException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return connection;
    }

    public HttpPost gethttPost(String uripath) {
        //创建HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象
        HttpPost httpPost = new HttpPost("http://192.168.1.66:8080/fashion_server/" + uripath);
        System.out.println("http://192.168.1.66:8080/fashion_server/" + uripath);
        return httpPost;
    }
}