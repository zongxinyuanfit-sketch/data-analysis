package com.jky.znys.team_kpi.disuse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jky.znys.team_kpi.utils.NetGPData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.HashMap;
import java.util.Map;
public class ALDingXCX {
    private String url_token = "http://api.data.aldwx.com/login";
    private String url_data = "http://api.data.aldwx.com/api";
    private String ald_key_kswy = "c31f32a3d843a0c17887aff02e471351";
    private String ald_key_wlzs = "a57adbbf23b0edd01a93497c21af5c27";
    private String phone = "18600562779";
    private String password = "Fx120ask";

    /**
     * 获取阿拉丁通用token
     *
     * @param url
     * @param phone
     * @param password
     * @return
     */
    private String getUrl_token(String url, String phone, String password) {
        String token = "";
        try {
            Map<String, Object> bodyParams = new HashMap<String, Object>();
            bodyParams.put("phone", phone);
            bodyParams.put("password", password);
            String temp_result = getHttpResult(url, bodyParams);
            JSONObject parse_job = JSONObject.fromObject(temp_result);
            if (parse_job.getInt("code") == 200) {
                token = parse_job.getJSONObject("data").getString("token");
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        return token;
    }

    /**
     * 获取阿拉丁统计数据
     *
     * @param url
     * @param ald_key
     * @return
     */
    private int[] getUrl_data(String url, String ald_key) {
        int[] total_num = new int[2];
        try {
            Map<String, Object> bodyParams = new HashMap<String, Object>();
            String token = getUrl_token(url_token, phone, password);
            bodyParams.put("app_key", ald_key);
            bodyParams.put("modular", "scene_trend_list");
            bodyParams.put("token", token);
            bodyParams.put("date", NetGPData.plusDay(-1, NetGPData.getNowDate()) + "~" + NetGPData.plusDay(-1, NetGPData.getNowDate()));
            String temp_result = getHttpResult(url, bodyParams);
            JSONObject parse_job = JSONObject.fromObject(temp_result);
            if (parse_job.getInt("code") == 200) {
                JSONArray parse_job_arr = parse_job.getJSONArray("data");
                for (int i = 0; i < parse_job_arr.size(); i++) {
                    String temp_str_num1 = parse_job_arr.getJSONObject(i).getString("new_comer_count");
                    String temp_str_num2 = parse_job_arr.getJSONObject(i).getString("visitor_count");
                    if (temp_str_num1.contains(",")) {
                        temp_str_num1 = temp_str_num1.replace(",", "");
                    }
                    if (temp_str_num2.contains(",")) {
                        temp_str_num2 = temp_str_num2.replace(",", "");
                    }
                    total_num[0] += Integer.parseInt(temp_str_num1);
                    total_num[1] += Integer.parseInt(temp_str_num2);

                }
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return total_num;
    }

    public int[] getTotalNum() {
        int[] total_num = new int[2];
        total_num[0] = getUrl_data(url_data, ald_key_kswy)[0] + getUrl_data(url_data, ald_key_wlzs)[0];
        total_num[1] = getUrl_data(url_data, ald_key_kswy)[1] + getUrl_data(url_data, ald_key_wlzs)[1];
        return total_num;
    }

    private String getHttpResult(String url, Map<String, Object> bodyParams) {
        String result = "";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("accept", "application/json;charset=UTF-8");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        //post 请求
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpResponse<JsonNode> httpResponse = Unirest.post(url)
                    .headers(headers)
                    .body(mapper.writeValueAsString(bodyParams))
                    .asJson();
            result = httpResponse.getBody().toString();
        } catch (Exception e) {
            System.out.println("error");
        }
        return result;
    }

    public static void main(String[] args) {
//        System.out.println(getTotalnum());
    }
}
