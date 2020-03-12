package com.example.simplebottomnav.repository;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class UploadServerUtil {
    /**
     * @param actionUrl 服务器url
     * @param params    参数封装为map类
     * @param file      文件
     * @return
     * @throws IOException
     */
    public static String upLoadFilePost(String actionUrl, Map<String, String> params, File file) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(6 * 1000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        StringBuilder sb1 = new StringBuilder();

        /*
        上传参数
         */
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb1.append(PREFIX).append(BOUNDARY).append(LINEND)
                        .append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND)
                        .append("Content-Type: text/plain; charset=" + CHARSET + LINEND)
                        .append("Content-Transfer-Encoding: 8bit" + LINEND)
                        .append(LINEND).append(entry.getValue()).append(LINEND);
            }
        }

        // 发送文件数据
        if (file != null) {
//            for (Map.Entry<String, File> file : files.entrySet()) {

            sb1.append(PREFIX);
            sb1.append(BOUNDARY);
            sb1.append(LINEND);
            sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + file.getName() + "\"" + LINEND);
            sb1.append("Content-Type: application/octet-stream; charset="
                    + CHARSET + LINEND);
            sb1.append(LINEND);

//            }
        }

        outStream.write(sb1.toString().getBytes());
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        is.close();
        outStream.write(LINEND.getBytes());

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();

        // 得到响应码
        int res = conn.getResponseCode();
        Log.e("TAG", "response code:" + res);
        outStream.close();
        conn.disconnect();
        if (res == 200) {
            String oneLine;
            StringBuffer response = new StringBuffer();
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((oneLine = input.readLine()) != null) {
                response.append(oneLine);
            }
            return response.toString();
        } else
            return "false";
    }

}