package com.sqkj.pdfreport.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cryan on 2021/9/16.
 * TODO.
 */
public class RadarPng {
    public static void main(String[] args) {
        String url = "http://localhost:6666";
        // 不必要的空格最好删除，字符串请求过程中会将空格转码成+号
        /* optJson = "{title:{text:'ECharts 示例'},tooltip:{},legend:{data:['销量']},"
                + "xAxis:{data:['衬衫','羊毛衫','雪纺衫','裤子','高跟鞋','袜子']},yAxis:{},"
                + "series:[{name:'销量',type:'bar',data:[5,20,36,10,10,20]}]}";*/
        String optJson = "{title:{text:'您在13项素质能力上的评估层级如下:'},legend:{data:['您的层级','平均层级'],left:'right'},radar:{indicator:[{name:'销售（Sales）',max:6500},{name:'管理（Administration）',max:16000},{name:'信息技术（InformationTechnology）',max:30000},{name:'客服（CustomerSupport）',max:38000},{name:'研发（Development）',max:52000},{name:'市场（Marketing）',max:25000}]},series:[{name:'您的层级vs平均层级',type:'radar',data:[{value:[4200,3000,20000,35000,50000,18000],name:'您的层级'},{value:[5000,14000,28000,26000,42000,21000],name:'平均层级'}]}]}";
        //删掉不必要的换行空格等
        optJson = optJson.replaceAll("\\s+", "").replaceAll("\"", "'");
        System.out.println(optJson);
        Map<String, String> map = new HashMap<>();
        map.put("opt", optJson);
        try {
            String post = post(url, map, "utf-8");
            System.out.println(post);
            JSONObject jsonObject = JSON.parseObject(post);
            String data = jsonObject.getString("data");
            System.out.println(data);
            GenerateImage(data, "d:/test/test.png");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // post请求
    public static String post(String url, Map<String, String> map, String encoding) throws ParseException, IOException {
        String body = "";

        // 创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 装填参数
        List<NameValuePair> nvps = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        // 获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            // 按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        // 释放链接
        response.close();
        return body;
    }


    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}