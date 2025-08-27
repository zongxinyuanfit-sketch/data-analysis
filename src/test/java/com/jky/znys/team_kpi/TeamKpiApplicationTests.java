package com.jky.znys.team_kpi;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class TeamKpiApplicationTests {


    @Test
    void contextLoads() {
        System.out.println(getDataBycookie("UM_distinctid=1707ecb1fce6f5-047fd41cc1d9b-39617b0f-13c680-1707ecb1fcf21a; cna=d+zUFifnIkECAbcvDOKO7Rq8; um_lang=zh; dplus_cross_id=1718a9ac481bf2-0621275f231dfc-396f7506-13c680-1718a9ac482bcc; dplus_finger_print=3997343162; uc_session_id=bbd0d8f2-04db-4594-b615-a8efe231d265; umplus_uc_loginid=liuxingzhi%40120.net; umplus_uc_token=1WpV8vnCgL5qiuKmfjGwBPA_60865f396b8f4e50b71a10da47453e2e; cn_1273967994_dplus=1%5B%7B%22acookie%22%3A%22d%2BzUFifnIkECAbcvDOKO7Rq8%22%7D%2Cnull%2Cnull%2Cnull%2Cnull%2C%22%24direct%22%2C%221707ecb1fce6f5-047fd41cc1d9b-39617b0f-13c680-1707ecb1fcf21a%22%2C%221582672993%22%2C%22https%3A%2F%2Fwww.umeng.com%2Fanalytics%3Fspm%3Da211g2.181323.0.0.3cb275efkiDKYe%22%2C%22www.umeng.com%22%5D; Hm_lvt_289016bc8d714b0144dc729f1f2ddc0d=1599877901,1600128337,1600134574,1600415355; Hm_lpvt_289016bc8d714b0144dc729f1f2ddc0d=1600415355; cn_1258498910_dplus=1%5B%7B%22acookie%22%3A%22d%2BzUFifnIkECAbcvDOKO7Rq8%22%2C%22userid%22%3A%22liuxingzhi%40120.net%22%7D%2C0%2C1600415359%2C0%2C1600415359%2C%22%24direct%22%2C%221707ecb1fce6f5-047fd41cc1d9b-39617b0f-13c680-1707ecb1fcf21a%22%2C%221582671182%22%2C%22%24direct%22%2C%22%24direct%22%5D; cn_1278512428_dplus=1%5B%7B%22UserID%22%3A%22liuxingzhi%40120.net%22%7D%2C0%2C1600134467%2C0%2C1600134467%2Cnull%2C%221707ecb1fce6f5-047fd41cc1d9b-39617b0f-13c680-1707ecb1fcf21a%22%2C%221597738165%22%2C%22https%3A%2F%2Fwww.umeng.com%2Fminiprogram%22%2C%22www.umeng.com%22%5D; edtoken=cnzz_5f6466874e574; PHPSESSID=c5j9j2c5pcqc54fli0577v4lf5; isg=BLi4xP8OE39I8n7KFx-cP1YCiWBKIRyrih0L1fIp4vOmDVn3mjVaO3olwQW9XdSD; CNZZDATA33222=cnzz_eid%3D148687156-1600413265-https%253A%252F%252Fmp.umeng.com%252F%26ntime%3D1600413265; CNZZDATA1276392090=65057239-1600413073-https%253A%252F%252Fmp.umeng.com%252F%7C1600413073; CNZZDATA30086426=cnzz_eid%3D447068574-1600414977-https%253A%252F%252Fmp.umeng.com%252F%26ntime%3D1600414977; frontvar=siteListSortId%3D0; cn_1276392090_dplus=1%5B%7B%7D%2C0%2C1600415402%2C0%2C1600415402%2Cnull%2C%221707ecb1fce6f5-047fd41cc1d9b-39617b0f-13c680-1707ecb1fcf21a%22%2C%221600413073%22%2C%22https%3A%2F%2Fmp.umeng.com%2Fapps%3Facm%3Dlb-zebra-602107-8788706.1003.4.8404989%26scm%3D1003.4.lb-zebra-602107-8788706.OTHER_15944166414481_8404989%22%2C%22mp.umeng.com%22%5D; cn_1259864772_dplus=1%5B%7B%22UserID%22%3A%22liuxingzhi%40120.net%22%2C%22acookie%22%3A%22d%2BzUFifnIkECAbcvDOKO7Rq8%22%2C%22%E6%98%AF%E5%90%A6%E7%99%BB%E5%BD%95%22%3Atrue%2C%22Uapp_appkey%22%3A%225d10234a4ca3576ac30008ae%22%2C%22Uapp_platform%22%3A%22android%22%7D%2C0%2C1600153697%2C0%2C1600153697%2C%22%24direct%22%2C%221707ecb1fce6f5-047fd41cc1d9b-39617b0f-13c680-1707ecb1fcf21a%22%2C%221582672993%22%2C%22https%3A%2F%2Fwww.umeng.com%2Fanalytics%3Fspm%3Da211g2.181323.0.0.3cb275efkiDKYe%22%2C%22www.umeng.com%22%5D; CNZZDATA30069868=cnzz_eid%3D2071538713-1600413906-https%253A%252F%252Fmp.umeng.com%252F%26ntime%3D1600414586; _cnzz_CV30069868=%E6%98%AF%E5%90%A6%E7%99%BB%E5%BD%95%7C%E6%AD%A3%E5%B8%B8%E7%99%BB%E5%BD%95%7C1600444203104%26%E7%94%A8%E6%88%B7%E7%B1%BB%E5%9E%8B%7C%E7%AB%99%E9%95%BF%E7%94%A8%E6%88%B7%7C1600415404105; CNZZDATA30001831=cnzz_eid%3D711387016-1600412489-https%253A%252F%252Fmp.umeng.com%252F%26ntime%3D1600412771",
                "https://web.umeng.com/main.php?c=site&a=show&ajax=module=todaydata+%2520_query=_siteid=30040060%7Cmodule=yesterdaydata_siteid=30040060_query1=&_=1600415403849"));
    }

    public static String getDataBycookie(String cookie, String url) {
        String ret = "";
// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Get请求

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("cookie",cookie);



        // 响应模型

        CloseableHttpResponse response = null;

        try {

            // 由客户端执行(发送)Get请求


            response = httpClient.execute(httpGet);


            // 从响应模型中获取响应实体

            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());

            if (responseEntity != null) {

                System.out.println("响应内容长度为:" + responseEntity.getContentLength());

                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));

            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                // 释放资源

                if (httpClient != null) {

                    httpClient.close();

                }

                if (response != null) {

                    response.close();

                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        return ret;
    }



}
