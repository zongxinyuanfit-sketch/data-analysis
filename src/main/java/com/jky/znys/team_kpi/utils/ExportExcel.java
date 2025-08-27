package com.jky.znys.team_kpi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jky.znys.team_kpi.entity.*;
import com.jky.znys.team_kpi.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ExportExcel {
    @Autowired
    private ExcelConfigService excelConfigService = (ExcelConfigService) SpringContextUtils.getBean(ExcelConfigService.class);
    @Autowired
    private ToastContentService toastContentService = (ToastContentService) SpringContextUtils.getBean(ToastContentService.class);
    @Autowired
    private UserService userService = (UserService) SpringContextUtils.getBean(UserService.class);
    @Autowired
    private DescribesService describesService = (DescribesService) SpringContextUtils.getBean(DescribesService.class);
    @Autowired
    private CategoryService categoryService = (CategoryService) SpringContextUtils.getBean(CategoryService.class);
    @Autowired
    private UMengService uMengService = (UMengService) SpringContextUtils.getBean(UMengService.class);
    @Autowired
    private ManualService manualService = (ManualService) SpringContextUtils.getBean(ManualService.class);

    ArrayList<KPIExcelConfig> get_excelconfig_result = excelConfigService.getConfigList();
    ToastDingTalk tool_toast = new ToastDingTalk();
    ArrayList<ArrayList<String>> punishList = new ArrayList<ArrayList<String>>();

    /**
     * 获取数据
     *
     * @param starttime
     * @param endtime
     */
    public String getMultidata(String starttime, String endtime, int mark) {
        ArrayList<String> datelist = new ArrayList<String>();
        datelist = getDateList(starttime, endtime);
        try {
//            if (endtime.equals(NetGPData.getNowDate())) {
//                endtime = NetGPData.plusDay(-1, NetGPData.getNowDate());
//            }
            //统一成获取前一天数据
            endtime = NetGPData.plusDay(-1, endtime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ret = "";
        try {
            //总数据
            ArrayList<KPIToastContent> d_toast = toastContentService.getDataList(starttime, endtime);

            // 考核人员列表
            ArrayList<KPIUser> p_user = userService.getUserList();

            // 考核内容列表
            ArrayList<KPIDescribes> p_describes = describesService.getDescribeList();

            // 考核组别列表
            ArrayList<KPICategory> d_cate = categoryService.getCategoryList();

            // 友盟数据
            ArrayList<KPIUmeng> kpiUmeng = uMengService.getDataList(starttime, endtime);

            // 手填数据
            ArrayList<KPIManual> kpiManuals = manualService.getDataList(starttime, endtime);

            ret = makeExcel(p_user, p_describes, datelist, d_toast, d_cate, kpiUmeng, ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(), kpiManuals);
        } catch (Exception e) {
            e.printStackTrace();
            tool_toast.makeDToast_text("15015962016", "系统报错了，老板快去看看");
            ret = "makeing";
        }
        return ret;
    }

    /**
     * 获取数据
     *
     * @param starttime
     * @param endtime
     */
    public String getYLMultidata(String starttime, String endtime, int mark) {
        ArrayList<String> datelist = new ArrayList<String>();
        datelist = getDateList(starttime, endtime);
        try {
//            if (endtime.equals(NetGPData.getNowDate())) {
//                endtime = NetGPData.plusDay(-1, NetGPData.getNowDate());
//            }
            //统一成获取前一天数据
            endtime = NetGPData.plusDay(-1, endtime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ret = "";
        try {
            // 友盟数据
            ArrayList<KPIUmeng> kpiUmeng = uMengService.getDataList(starttime, endtime);

            // 手填数据
            ArrayList<KPIManual> kpiManuals = manualService.getDataList(starttime, endtime);
            //总数据
            ArrayList<KPIToastContent> d_toast = toastContentService.getDataList(starttime, endtime);

            ret = makeYLExcel(kpiManuals,kpiUmeng,datelist, d_toast,((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        } catch (Exception e) {
            e.printStackTrace();
            tool_toast.makeDToast_text("15015962016", "系统报错了，YL，老板快去看看");
            ret = "makeing";
        }
        return ret;
    }

    private ArrayList<String> getDateList(String starttime, String endtime) {
        ArrayList<String> ret = new ArrayList<String>();
        try {
            int days = NetGPData.daysBetween(starttime, endtime);
            for (int i = 0; i < days; i++) {
                ret.add(NetGPData.plusDay(i, starttime));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 生成Excel表
     */
    private String makeExcel(ArrayList<KPIUser> d_user,
                             ArrayList<KPIDescribes> d_describe,
                             ArrayList<String> d_dates,
                             ArrayList<KPIToastContent> d_toast,
                             ArrayList<KPICategory> d_cate,
                             ArrayList<KPIUmeng> kpiUmeng,
                             HttpServletRequest request,
                             ArrayList<KPIManual> kpiManuals) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle base_cellStyle = wb.createCellStyle();
        HSSFCellStyle bg_cellStyle = wb.createCellStyle();

        bg_cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        bg_cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        bg_cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        bg_cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平
        bg_cellStyle.setBorderBottom(BorderStyle.THIN);
        bg_cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        bg_cellStyle.setBorderLeft(BorderStyle.THIN);
        bg_cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        bg_cellStyle.setBorderRight(BorderStyle.THIN);
        bg_cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        bg_cellStyle.setBorderTop(BorderStyle.THIN);
        bg_cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        base_cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        base_cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平
        base_cellStyle.setBorderBottom(BorderStyle.THIN);
        base_cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        base_cellStyle.setBorderLeft(BorderStyle.THIN);
        base_cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        base_cellStyle.setBorderRight(BorderStyle.THIN);
        base_cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        base_cellStyle.setBorderTop(BorderStyle.THIN);
        base_cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        HSSFSheet sheet_znys = wb.createSheet("数据考核表格");
        HSSFSheet sheet_live = wb.createSheet("运营日活数据");
        HSSFSheet sheet_new = wb.createSheet("运营新增数据");
//        HSSFSheet sheet_punish = wb.createSheet("小组数据记录");
        HSSFSheet sheet_core = wb.createSheet("运营核心数据");

        // 数据考核表格
        makeExcelznys(d_user, d_describe, sheet_znys, base_cellStyle, d_dates, d_toast, d_cate, bg_cellStyle, kpiUmeng, kpiManuals);

        // 运营日活数据
        makeExcelLive(d_toast, sheet_live, base_cellStyle, d_dates, kpiUmeng);

        // 运营新增数据
        makeExcelNew(d_toast, sheet_new, base_cellStyle, d_dates, kpiUmeng, kpiManuals);

        // 小组数据记录
//        makePunish(sheet_punish, base_cellStyle, d_dates,d_toast, kpiUmeng,kpiManuals);

        // 运营核心数据
        makeCore(sheet_core, base_cellStyle, d_dates, d_toast, kpiUmeng, kpiManuals);

        // 输出Excel文件
        FileOutputStream output = null;
        String defaultName = "";
        try {
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");

            defaultName = df.format(day) + "-ZNYS-ZhongLi.xls";
            //获取项目根目录路径
            String defaultDirect = request.getSession().getServletContext().getRealPath("/");
            //拼接存储表格地址
            String path = defaultDirect + "/excel/" + defaultName;
            //创建文件
            File tempFile = new File(defaultDirect + "/excel/");
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            output = new FileOutputStream(path);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (output != null) {
                wb.write(output);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (output != null) {
                output.flush();
                wb.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "https://znyskpi.120.net/excel/" + defaultName;
//        return "http://127.0.0.1:8080/team_kpi/excel/" + defaultName;
    }

    /**
     * 生成Excel表
     */
    private String makeYLExcel(
            ArrayList<KPIManual> d_manual,
            ArrayList<KPIUmeng> d_umeng,
                             ArrayList<String> d_dates,
                             ArrayList<KPIToastContent> d_toast,
                             HttpServletRequest request) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle base_cellStyle = wb.createCellStyle();
        HSSFCellStyle bg_cellStyle = wb.createCellStyle();

        bg_cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        bg_cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        bg_cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        bg_cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平
        bg_cellStyle.setBorderBottom(BorderStyle.THIN);
        bg_cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        bg_cellStyle.setBorderLeft(BorderStyle.THIN);
        bg_cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        bg_cellStyle.setBorderRight(BorderStyle.THIN);
        bg_cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        bg_cellStyle.setBorderTop(BorderStyle.THIN);
        bg_cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        base_cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
        base_cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平
        base_cellStyle.setBorderBottom(BorderStyle.THIN);
        base_cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        base_cellStyle.setBorderLeft(BorderStyle.THIN);
        base_cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        base_cellStyle.setBorderRight(BorderStyle.THIN);
        base_cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        base_cellStyle.setBorderTop(BorderStyle.THIN);
        base_cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        HSSFSheet sheet_znys = wb.createSheet("小组数据导出表格");

        // 数据考核表格
        makeYlDataExcel(d_manual,d_umeng,d_dates, d_toast,sheet_znys,base_cellStyle);


        // 输出Excel文件
        FileOutputStream output = null;
        String defaultName = "";
        try {
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");

            defaultName = df.format(day) + "-ZNYS-YuLian.xls";
            //获取项目根目录路径
            String defaultDirect = request.getSession().getServletContext().getRealPath("/");
            //拼接存储表格地址
            String path = defaultDirect + "/excel/" + defaultName;
            //创建文件
            File tempFile = new File(defaultDirect + "/excel/");
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            output = new FileOutputStream(path);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (output != null) {
                wb.write(output);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (output != null) {
                output.flush();
                wb.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "https://znyskpi.120.net/excel/" + defaultName;
//        return "http://127.0.0.1:8080/team_kpi/excel/" + defaultName;
    }

    private String str2double(String str) {
        String result = "";
        Double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        result = df.format(d);
        return result;
    }

    private ArrayList<String[]> parseZTCdata(ArrayList<KPIToastContent> ztc_datalist, ArrayList<KPIUmeng> d_umeng, ArrayList<KPIManual> d_manual, ArrayList<String> dateList) {
        ArrayList<String[]> arr_list = new ArrayList<String[]>();
        String[] arr_keys_caiwu = {"profit", "order_fg_user_num"};
        String[] arr_keys_team = {"total|total_profit", "app|total_profit", "hd|total_profit", "other|total_profit", "wx|total_profit", "zfb|total_profit", "app|total_income", "hd|total_income", "other|total_income", "wx|total_income", "zfb|total_income"};
        int date_size = dateList.size() > ztc_datalist.size() ? dateList.size() - 1 : dateList.size();
        for (int i = 0; i < date_size; i++) {
            String str_kteam_o = HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/kaohe_team?", "dateline=" + ztc_datalist.get(i).getDate());
            String str_kcaiwu_o = HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/department?", "dateline=" + ztc_datalist.get(i).getDate());

            ArrayList<String> result_team_list = new ArrayList();
            result_team_list.add(str_kteam_o);
            ArrayList<ArrayList<String>> result_team_22Ja = NetGPData.parseSourceData(result_team_list, arr_keys_team);

            ArrayList<String> result_caiwu_list = new ArrayList();
            result_caiwu_list.add(str_kcaiwu_o);
            ArrayList<ArrayList<String>> result_caiwu = NetGPData.parseSourceData(result_caiwu_list, arr_keys_caiwu);
            // 安卓日活
            int a_live_num = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(5).getInt(0);
            // IOS日活
            int i_live_num = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(5).getInt(1);
            // 微信-快速问医生
            int wx_kw_live_num = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(6).getInt(0);
            // 微信-问医生
            int wx_wys_live_num = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(6).getInt(1);
            // 微信-快问健康快讯
            int wx_kwjk_num = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(6).getInt(2);
            // 活跃-kswys
            int xcx_kswys_num = Integer.parseInt(d_umeng.get(i).getLivexcxkswys());
            // 活跃-kswy
            int xcx_kswy_num = Integer.parseInt(d_umeng.get(i).getLivexcxkswy());
            // 活跃-kpy
            int xcx_kpy_num = Integer.parseInt(d_umeng.get(i).getLivexcxkpy());
            // 活跃-支付宝
            int xcx_zfb_num = Integer.parseInt(d_umeng.get(i).getLivexcxzfb());
            // 活跃-快应用
            int kyy_num = Integer.parseInt(d_umeng.get(i).getLivekyy());


            JSONArray temp_array = JSONArray.fromObject(ztc_datalist.get(i).getContent());
            String[] arr = new String[14];

            //总销售额
            arr[0] = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(13).getString(0);
            //总日活
            arr[1] = (a_live_num + i_live_num + wx_kw_live_num + wx_wys_live_num + wx_kwjk_num + xcx_zfb_num + kyy_num + xcx_kswy_num + xcx_kswys_num) + "";
            //微信收入
            arr[2] = result_team_22Ja.get(0).get(9);
            //运营-新增
            //短视频、支付宝、APP、微信
//            int shorts_new=Integer.parseInt(d_manual.get(i).getNewkwjk());
            int zfb_new = Integer.parseInt(d_manual.get(i).getZfbreuser());
            int app_new = Integer.parseInt(d_umeng.get(i).getNewumengakswys()) + Integer.parseInt(d_umeng.get(i).getNewumengikswys()) + Integer.parseInt(d_umeng.get(i).getNewkyy());
            int wx_new = Integer.parseInt(d_manual.get(i).getVideopv());
            arr[3] = (zfb_new + app_new + wx_new) + "";
            //运营-垂类用户销售额
            arr[4] = JSONArray.fromObject(ztc_datalist.get(i).getContent()).getJSONArray(3).getString(0);
            //运营-私域销售额
            arr[5] = d_manual.get(i).getZfbmoney();
            //运营-复购用户数
            arr[6] = Math.round(Double.valueOf(result_caiwu.get(0).get(1)).doubleValue()) + "";
            //运营-总毛利润
            arr[7] = Math.round(Double.valueOf(result_caiwu.get(0).get(0)).doubleValue()) + "";


            arr_list.add(arr);

        }
        return arr_list;
    }
    private HSSFSheet makeYlDataExcel(
            ArrayList<KPIManual> manual_data,
            ArrayList<KPIUmeng> umeng_data,
                                       ArrayList<String> dateList,
                                       ArrayList<KPIToastContent> ztc_data,
                                       HSSFSheet sheet,
                                       HSSFCellStyle cellStyle) {
        int date_size = dateList.size() > ztc_data.size() ? dateList.size() - 1 : dateList.size();
        ArrayList<HSSFRow> ftitle_row = new ArrayList<HSSFRow>();

//        String[] kpi_column_title_under = {"时间", "指定咨询","指定咨询单数", "极速咨询","极速咨询单数", "免费咨询","免费咨询未悬赏数","免费咨询悬赏数", "新收入","新收入单数"};
        String[] kpi_column_title_up = {"时间", "纯收入","总毛利", "总活跃","总新增","指定咨询金额", "指定咨询单数","极速咨询金额","极速咨询单数", "免费咨询金额","免费咨询数","免费咨询单数"};
        String[] kpi_column_title_under = {"时间","新收入", "新收入订单","微信毛利", "微信流水","微信活跃", "微信新增","支付宝毛利","支付宝流水", "支付宝活跃","支付宝新增","APP+快应用毛利","APP+快应用流水","APP+快应用活跃","APP+快应用流水新增"};
        int column_num = kpi_column_title_under.length;
        int row_num = date_size + 1;

        for (int i = 0; i < row_num; i++) {
            // 创建表行与单元格
            ftitle_row.add(sheet.createRow(i));
            for (int j = 0; j < column_num; j++) {
                ftitle_row.get(i).createCell(j).setCellValue(" ");
                ftitle_row.get(i).getCell(j).setCellStyle(cellStyle);
            }
        }
        for (int i = 0; i < kpi_column_title_under.length; i++) {
            ftitle_row.get(0).getCell(i).setCellValue(kpi_column_title_under[i]);
        }

        for (int i = 0; i < date_size; i++) {
            ArrayList<String> result_team_list = new ArrayList();
            String str_kteam_s=HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/kaohe_team?", "dateline=" + dateList.get(i));
            String[] arr_keys_team={"total|total_profit","app|total_profit","hd|total_profit","other|total_profit","wx|total_profit","zfb|total_profit","app|total_income","hd|total_income","other|total_income","wx|total_income","zfb|total_income"};
            result_team_list.add(str_kteam_s);
            ArrayList<ArrayList<String>> result_team_22Ja = NetGPData.parseSourceData(result_team_list, arr_keys_team);

            JSONArray result_data = JSONArray.fromObject(ztc_data.get(i).getContent());

            int wx_live = (Integer.parseInt(manual_data.get(i).getLivewxkswys())
                    +Integer.parseInt(manual_data.get(i).getLivewxwys())
                    +Integer.parseInt(manual_data.get(i).getLivekwjk())
                    +Integer.parseInt(umeng_data.get(i).getLivexcxkswys())
                    +Integer.parseInt(umeng_data.get(i).getLivexcxkswy())
                    +Integer.parseInt(umeng_data.get(i).getLivexcxkpy()));
            int wx_new =result_data.getJSONArray(6).getInt(3)
                    +result_data.getJSONArray(6).getInt(4)
                    +Integer.parseInt(manual_data.get(i).getNewkwjk())
                    +Integer.parseInt(umeng_data.get(i).getNewxcxkswys())
                    +Integer.parseInt(umeng_data.get(i).getNewxcxkswy())
                    +Integer.parseInt(umeng_data.get(i).getNewxcxkpy());

            int zfb_live = Integer.parseInt(manual_data.get(i).getZfblive());
            int zfb_new=Integer.parseInt(manual_data.get(i).getZfbreuser());

            int app_kyy_live=(Integer.parseInt(umeng_data.get(i).getLiveumengakswys())+Integer.parseInt(umeng_data.get(i).getLiveumengikswys())+Integer.parseInt(umeng_data.get(i).getLivekyy()));
            int app_kyy_new=(Integer.parseInt(umeng_data.get(i).getNewumengakswys())+Integer.parseInt(umeng_data.get(i).getNewumengikswys())+Integer.parseInt(umeng_data.get(i).getNewkyy()));


//            // 毛利
//            String pure_income = "0";
//            // 总流水
//            String all_income = (result_data.getJSONArray(0)).getString(0);
//            // 总活跃
//            int all_active = wx_live+zfb_live+app_kyy_live;
//            // 总新增
//            int all_new = wx_new+app_kyy_new+zfb_new;
//            // 指定总金额
//            String special_income = (result_data.getJSONArray(2)).getString(0);
//            // 指定单数
//            String special_num = (result_data.getJSONArray(2)).getString(1);
//
//            // 极速总金额
//            String fast_income =(result_data.getJSONArray(4)).getString(0);
//            // 极速单数
//            String fast_num = (result_data.getJSONArray(4)).getString(1);
//            // 免费总金额
//            String free_income = (result_data.getJSONArray(1)).getString(0);
//            // 免费咨询数
//            String free_ask_num = (result_data.getJSONArray(1)).getString(1);
//            // 免费单数
//            String free_num = (result_data.getJSONArray(1)).getString(2);


            //  新收入
            String new_income = (result_data.getJSONArray(3)).getString(0);
            //  新收入订单
            String new_income_num = (result_data.getJSONArray(3)).getString(5);
            //  微信毛利
            String wx_pure = result_team_22Ja.get(0).get(4);
            //  微信流水
            String wx_income = result_team_22Ja.get(0).get(9);
            //  微信活跃
            int wx_liven = wx_live;
            //  微信新增
            int wx_newn = wx_new;
            //  支付宝毛利
            String zfb_pure = result_team_22Ja.get(0).get(5);
            //  支付宝流水
            String zfb_income = result_team_22Ja.get(0).get(10);
            //  支付宝活跃
            int zfb_liven = zfb_live;
            //  支付宝新增
            int zfb_newn = zfb_new;
            //  APP+快应用毛利
            String app_kyy_pure = result_team_22Ja.get(0).get(1);
            //  APP+快应用流水
            String app_kyy_income = result_team_22Ja.get(0).get(6);
            //  APP+快应用活跃
            int app_kyy_liven = app_kyy_live;
            //  APP+快应用流水增新
            int app_kyy_newn = app_kyy_new;



            ftitle_row.get(i + 1).getCell(0).setCellValue(dateList.get(date_size - i - 1));
            ftitle_row.get(i + 1).getCell(1).setCellValue(new_income);
            ftitle_row.get(i + 1).getCell(2).setCellValue(new_income_num);
            ftitle_row.get(i + 1).getCell(3).setCellValue(wx_pure);
            ftitle_row.get(i + 1).getCell(4).setCellValue(wx_income);
            ftitle_row.get(i + 1).getCell(5).setCellValue(wx_liven);
            ftitle_row.get(i + 1).getCell(6).setCellValue(wx_newn);
            ftitle_row.get(i + 1).getCell(7).setCellValue(zfb_pure);
            ftitle_row.get(i + 1).getCell(8).setCellValue(zfb_income);
            ftitle_row.get(i + 1).getCell(9).setCellValue(zfb_liven);
            ftitle_row.get(i + 1).getCell(10).setCellValue(zfb_newn);
            ftitle_row.get(i + 1).getCell(11).setCellValue(app_kyy_pure);
            ftitle_row.get(i + 1).getCell(12).setCellValue(app_kyy_income);
            ftitle_row.get(i + 1).getCell(13).setCellValue(app_kyy_liven);
            ftitle_row.get(i + 1).getCell(14).setCellValue(app_kyy_newn);
        }

        setColumnwidth(sheet, 15, 3000);
        return sheet;
    }
    private HSSFSheet makeExcelznys(ArrayList<KPIUser> p_user,
                                    ArrayList<KPIDescribes> p_describe,
                                    HSSFSheet sheet,
                                    HSSFCellStyle base_cellStyle,
                                    ArrayList<String> dateList,
                                    ArrayList<KPIToastContent> ztc_datalist,
                                    ArrayList<KPICategory> d_cate,
                                    HSSFCellStyle bg_cellStyle,
                                    ArrayList<KPIUmeng> d_umeng,
                                    ArrayList<KPIManual> d_manual) {
        ArrayList<HSSFRow> ftitle_row = new ArrayList<HSSFRow>();
        ArrayList<String[]> ztcData = this.parseZTCdata(ztc_datalist, d_umeng, d_manual, dateList);
        String[] kpi_base_title = get_excelconfig_result.get(0).getKpibasetitle().split(",");

        int date_size = dateList.size() > ztc_datalist.size() ? dateList.size() - 1 : dateList.size();

        for (int i = 0; i < p_describe.size() * 2 + 1; i++) {
            // 创建表行与单元格
            ftitle_row.add(sheet.createRow(i));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            int column_num = kpi_base_title.length + date_size;
            // 配置奖罚表格
//            if (NetGPData.getWeekOfDate(new Date()).equals("星期一")&&ztcData.size()==7) {
//                column_num = kpi_base_title.length + date_size + 3;
//            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            int column_num = kpi_base_title.length + date_size + 3;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            ////////////////////////////
            for (int j = 0; j < column_num; j++) {
                ftitle_row.get(i).createCell(j).setCellValue("未加载");
                ftitle_row.get(i).getCell(j).setCellStyle(base_cellStyle);
                if ((int) ((float) (i - 2) / 4) == (float) (i - 2) / 4) {
                    ftitle_row.get(i - 1).getCell(j).setCellStyle(bg_cellStyle);
                    ftitle_row.get(i).getCell(j).setCellStyle(bg_cellStyle);
                }
            }
        }


        for (int i = 0; i < kpi_base_title.length; i++) {
            ftitle_row.get(0).getCell(i).setCellValue(kpi_base_title[i]);
        }
        //填充日期 数据
        for (int k = 0; k < date_size; k++) {
            ftitle_row.get(0).getCell(kpi_base_title.length + k).setCellValue(dateList.get(date_size - k - 1));
            for (int i = 0; i < p_describe.size(); i++) {
                for (int j = 0; j < p_user.size(); j++) {
                    if (p_user.get(j).getStatuesid() == 1 && p_user.get(j).getDescribeid() == p_describe.get(i).getId()) {
                        ftitle_row.get(2 * i + 1).getCell(3 + k).setCellValue(p_user.get(j).getRadix());// ztc_datalist
                        ftitle_row.get(2 * i + 2).getCell(3 + k).setCellValue(ztcData.get(k)[i]);
                    }
                }
            }
        }
        //填充考核内容、姓名
        for (int i = 0; i < p_describe.size(); i++) {
            String kpi_names = "";
            for (int k = 0; k < p_user.size(); k++) {
                if (p_user.get(k).getStatuesid() == 1 && p_user.get(k).getDescribeid() == p_describe.get(i).getId()) {
                    kpi_names = kpi_names + p_user.get(k).getName() + " | ";
                    ftitle_row.get(2 * i + 1).getCell(0).setCellValue(p_describe.get(p_user.get(k).getDescribeid() - 1).getUdescribe());
                    ftitle_row.get(2 * i + 1).getCell(1).setCellValue(kpi_names.substring(0, kpi_names.length() - 2));
                    if (i == 4 || i == 5) {
                        ftitle_row.get(2 * i + 1).getCell(2).setCellValue("基数(周)");
                        ftitle_row.get(2 * i + 2).getCell(2).setCellValue("数据");
                    } else {
                        ftitle_row.get(2 * i + 1).getCell(2).setCellValue("基数");
                        ftitle_row.get(2 * i + 2).getCell(2).setCellValue("数据");
                    }

                }

            }
        }
        // 配置奖罚表格
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        if (NetGPData.getWeekOfDate(new Date()).equals("星期一")&&ztcData.size()==7) {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ftitle_row.get(0).getCell(kpi_base_title.length + date_size).setCellValue("平均值");
            ftitle_row.get(0).getCell(kpi_base_title.length + date_size + 1).setCellValue("占比");
            ftitle_row.get(0).getCell(kpi_base_title.length + date_size + 2).setCellValue("奖罚");
            for (int i = 0; i < p_describe.size(); i++) {
                for (int j = 0; j < p_user.size(); j++) {
                    if (p_user.get(j).getDescribeid() == p_describe.get(i).getId()&& p_user.get(j).getStatuesid()!=2) {
                        double result = 0;
                        for (int k = 0; k < date_size; k++) {
                            if (p_describe.get(i).getVtype() == 0) {
                                result += Double.parseDouble(ztcData.get(k)[i]);
                            } else {
                                result += Integer.parseInt(ztcData.get(k)[i]);
                            }

                        }
                        String p_result = "";
                        if (p_describe.get(i).getVtype() == 0) {
                            p_result = Math.round(result / 7) + "";
                        } else {
                            p_result = Double.valueOf(Math.round(result / 7)).intValue() + "";
                        }

                        if (p_describe.get(i).getId() == 5 || p_describe.get(i).getId() == 6) {
                            p_result = Math.round(result) + "";
                        }
                        double p_percent = Math.round(Double.valueOf(NetGPData.getPercent(Double.valueOf(p_result).intValue(), Integer.parseInt(p_user.get(j).getRadix()))));
                        ftitle_row.get(2 * i + 1).getCell(3 + date_size).setCellValue(p_result);
                        ftitle_row.get(2 * i + 1).getCell(4 + date_size).setCellValue(p_percent);
                        ftitle_row.get(2 * i + 1).getCell(5 + date_size).setCellValue(p_percent < 90.0 ? (-200 * p_describe.get(i).getKpimembers() + "") : (p_percent >= 111.0 ? (200 * p_describe.get(i).getKpimembers() + "") : "0"));
                    }
                }
            }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////
        //合并单元格 标题部分
        for (int i = 0; i < p_describe.size(); i++) {
            // 合并单元格
            for (int j = 0; j < (kpi_base_title.length - 1); j++) {
                // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
                sheet.addMergedRegion(new CellRangeAddress(2 * i + 1, 2 * i + 2, j, j));
            }
        }
        // 配置奖罚表格
        //合并单元格 奖罚部分
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        if (NetGPData.getWeekOfDate(new Date()).equals("星期一")&&ztcData.size()==7) {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            for (int i = 0; i < p_describe.size(); i++) {
                // 合并单元格
                for (int j = kpi_base_title.length + date_size; j < kpi_base_title.length + date_size + 3; j++) {
                    // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
                    sheet.addMergedRegion(new CellRangeAddress(2 * i + 1, 2 * i + 2, j, j));
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////
        //设置列宽
        setColumnwidth(sheet, 3, 6000);
        return sheet;
    }

    private HSSFSheet makeExcelLive(ArrayList<KPIToastContent> d_toast, HSSFSheet sheet,
                                    HSSFCellStyle cellStyle, ArrayList<String> dateList, ArrayList<KPIUmeng> kpiUmeng) {
        int date_size = dateList.size() > d_toast.size() ? dateList.size() - 1 : dateList.size();
        ArrayList<HSSFRow> ftitle_row = new ArrayList<HSSFRow>();
        String[] kpi_live_title = get_excelconfig_result.get(0).getKpilivetitle().split(",");
        for (int i = 0; i < date_size + 1; i++) {
            // 创建表行与单元格
            ftitle_row.add(sheet.createRow(i));
            for (int j = 0; j < kpi_live_title.length; j++) {
                ftitle_row.get(i).createCell(j).setCellValue("未加载");
                ftitle_row.get(i).getCell(j).setCellStyle(cellStyle);
            }
        }
        for (int i = 0; i < kpi_live_title.length; i++) {
            ftitle_row.get(0).getCell(i).setCellValue(kpi_live_title[i]);
        }
        // 微信活跃数据 ztc_datalist kswys_uv wys_uv
        for (int i = 0; i < date_size; i++) {
            // 日期
            ftitle_row.get(i + 1).getCell(0).setCellValue(dateList.get(date_size - i - 1));
            // 安卓日活
            int a_live_num =Integer.parseInt(kpiUmeng.get(i).getLiveumengakswys());
            ftitle_row.get(i + 1).getCell(1).setCellValue(a_live_num);
            // IOS日活
            int i_live_num = Integer.parseInt(kpiUmeng.get(i).getLiveumengikswys());;
            ftitle_row.get(i + 1).getCell(2).setCellValue(i_live_num);
            // 微信-快速问医生
            int wx_kw_live_num = JSONArray.fromObject(d_toast.get(i).getContent()).getJSONArray(6).getInt(0);
            ftitle_row.get(i + 1).getCell(3).setCellValue(wx_kw_live_num);
            // 微信-问医生
            int wx_wys_live_num = JSONArray.fromObject(d_toast.get(i).getContent()).getJSONArray(6).getInt(1);
            ftitle_row.get(i + 1).getCell(4).setCellValue(wx_wys_live_num);
            // 微信-快问健康快讯
            int wx_kwjk_num = JSONArray.fromObject(d_toast.get(i).getContent()).getJSONArray(6).getInt(2);
            ftitle_row.get(i + 1).getCell(5).setCellValue(wx_kwjk_num);
            // 活跃-kswys
            int xcx_kswys_num = Integer.parseInt(kpiUmeng.get(i).getLivexcxkswys());
            ftitle_row.get(i + 1).getCell(6).setCellValue(xcx_kswys_num);
            // 活跃-kswy
            int xcx_kswy_num = Integer.parseInt(kpiUmeng.get(i).getLivexcxkswy());
            ftitle_row.get(i + 1).getCell(7).setCellValue(xcx_kswy_num);
            // 活跃-kpy
//            int xcx_kpy_num = Integer.parseInt(kpiUmeng.get(i).getLivexcxkpy());
//            ftitle_row.get(i + 1).getCell(8).setCellValue(xcx_kpy_num);
            // 活跃-支付宝
            int xcx_zfb_num = Integer.parseInt(kpiUmeng.get(i).getLivexcxzfb());
            ftitle_row.get(i + 1).getCell(8).setCellValue(xcx_zfb_num);
            // 活跃-快应用
            int kyy_num = Integer.parseInt(kpiUmeng.get(i).getLivekyy());
            ftitle_row.get(i + 1).getCell(9).setCellValue(kyy_num);
            // 总数据
            // 智能问答总数据
            ftitle_row.get(i + 1).getCell(10).setCellValue(a_live_num + i_live_num + wx_kw_live_num + wx_wys_live_num + wx_kwjk_num + xcx_zfb_num + kyy_num + xcx_kswy_num + xcx_kswys_num);
        }

        setColumnwidth(sheet, 11, 3000);
        return sheet;
    }

    private HSSFSheet makeExcelNew(ArrayList<KPIToastContent> d_toast, HSSFSheet sheet,
                                   HSSFCellStyle cellStyle, ArrayList<String> dateList, ArrayList<KPIUmeng> kpiUmeng, ArrayList<KPIManual> kpiManual) {
        int date_size = dateList.size() > d_toast.size() ? dateList.size() - 1 : dateList.size();
        ArrayList<HSSFRow> ftitle_row = new ArrayList<HSSFRow>();
        String[] kpi_live_title = {"日期", "支付宝新增", "App新增", "微信新增", "数据总计"};
        for (int i = 0; i < date_size + 1; i++) {
            // 创建表行与单元格
            ftitle_row.add(sheet.createRow(i));
            for (int j = 0; j < kpi_live_title.length; j++) {
                ftitle_row.get(i).createCell(j).setCellValue("未加载");
                ftitle_row.get(i).getCell(j).setCellStyle(cellStyle);
            }
        }
        for (int i = 0; i < kpi_live_title.length; i++) {
            ftitle_row.get(0).getCell(i).setCellValue(kpi_live_title[i]);
        }
        // 微信活跃数据 ztc_datalist kswys_uv wys_uv
        for (int i = 0; i < date_size; i++) {
            JSONArray temp_array = JSONArray.fromObject(d_toast.get(i).getContent());

            // 日期
            ftitle_row.get(i + 1).getCell(0).setCellValue(dateList.get(date_size - i - 1));
            // 支付宝新增
            int new_zfb = Integer.parseInt(kpiManual.get(i).getZfbreuser());
            ftitle_row.get(i + 1).getCell(1).setCellValue(new_zfb);

            //App新增
            int new_app = Integer.parseInt(kpiUmeng.get(i).getNewumengakswys()) + Integer.parseInt(kpiUmeng.get(i).getNewumengikswys()) + Integer.parseInt(kpiUmeng.get(i).getNewkyy());
            ftitle_row.get(i + 1).getCell(2).setCellValue(new_app);

            //微信新增
            int new_wx = Integer.parseInt(kpiManual.get(i).getVideopv());
            ftitle_row.get(i + 1).getCell(3).setCellValue(new_wx);
            // 总数据
            // 智能问答总数据
            ftitle_row.get(i + 1).getCell(4).setCellValue(new_zfb + new_app + new_wx);
        }

        setColumnwidth(sheet, 12, 3000);
        return sheet;
    }

    private HSSFSheet makePunish(HSSFSheet sheet, HSSFCellStyle cellStyle, ArrayList<String> dateList, ArrayList<KPIToastContent> ztc_datalist, ArrayList<KPIUmeng> d_umeng,
                                 ArrayList<KPIManual> d_manual) {
//        int date_size = dateList.size()>ztc_datalist.size()?dateList.size()-1:dateList.size();
//        ArrayList<HSSFRow> ftitle_row = new ArrayList<HSSFRow>();
//        ArrayList<KPIUser> d_users = new ArrayList<KPIUser>();
//        String[] kpi_column_title = {"成员", "组别", "月均"};
//        String[] kpi_members = {"彭少桐","彭少桐","林玉莲","林玉莲","李嘉登","李嘉登"};
//        String[] kpi_category = {"支付宝总收入","支付宝毛利","微信总收入","微信毛利","App+快应用总收入","App+快应用毛利"};
//        String[] kpi_radix={"8333","2500","13000","4000","15500","4600"};
//        ArrayList<String[]> ztcData = this.parseZTCdata(ztc_datalist,d_umeng,d_manual,dateList);
//        ArrayList<String[]> result = new ArrayList<String[]>();
//        for(int i=0;i<date_size;i++){
//            String[] kpi_result={
//                    ztcData.get(i)[13],
//                    ztcData.get(i)[3],
//                    ztcData.get(i)[6],
//                    ztcData.get(i)[5],
//                    ztcData.get(i)[10],
//                    ztcData.get(i)[9],
//            };
//            result.add(kpi_result);
//        }
//
//
//
//        int column_num = kpi_column_title.length + dateList.size();
//        int row_num = kpi_category.length*2+1;
//
//        for (int i = 0; i < row_num; i++) {
//            // 创建表行与单元格
//            ftitle_row.add(sheet.createRow(i));
//            for (int j = 0; j < column_num; j++) {
//                ftitle_row.get(i).createCell(j).setCellValue(" ");
//                ftitle_row.get(i).getCell(j).setCellStyle(cellStyle);
//            }
//        }
//        for (int i = 0; i < kpi_column_title.length; i++) {
//            ftitle_row.get(0).getCell(i).setCellValue(kpi_column_title[i]);
//        }
//        // 填充日期
//        for (int i = 0; i < date_size; i++) {
//            ftitle_row.get(0).getCell(kpi_column_title.length + i).setCellValue(dateList.get(date_size - i - 1));
//
//        }
//
//        for (int i = 0; i < kpi_category.length; i++) {
//                ftitle_row.get(i*2+1).getCell(0).setCellValue(kpi_members[i]);
//                ftitle_row.get(i*2+1).getCell(1).setCellValue(kpi_category[i]);
//                for(int j=0;j<result.size();j++){
//                    ftitle_row.get(i*2+1).getCell(3+j).setCellValue(kpi_radix[i]);//基数
//                    ftitle_row.get(i*2+2).getCell(3+j).setCellValue(result.get(j)[i]);//数据
//                }
//
//        }
//
//        setColumnwidth(sheet, 4 + dateList.size(), 2000);
        return sheet;
    }

    private HSSFSheet makeCore(HSSFSheet sheet, HSSFCellStyle cellStyle, ArrayList<String> dateList, ArrayList<KPIToastContent> ztc_data, ArrayList<KPIUmeng> d_umeng,
                               ArrayList<KPIManual> d_manual) {
        int date_size = dateList.size() > ztc_data.size() ? dateList.size() - 1 : dateList.size();
        ArrayList<HSSFRow> ftitle_row = new ArrayList<HSSFRow>();
        String[] kpi_column_title = {"时间", "新增数据总计", "日活数据总计", "新增付费用户数", "复购用户数", "客单价"};

        int column_num = kpi_column_title.length;
        int row_num = date_size + 1;

        for (int i = 0; i < row_num; i++) {
            // 创建表行与单元格
            ftitle_row.add(sheet.createRow(i));
            for (int j = 0; j < column_num; j++) {
                ftitle_row.get(i).createCell(j).setCellValue(" ");
                ftitle_row.get(i).getCell(j).setCellStyle(cellStyle);
            }
        }
        for (int i = 0; i < kpi_column_title.length; i++) {
            ftitle_row.get(0).getCell(i).setCellValue(kpi_column_title[i]);
        }

        for (int i = 0; i < date_size; i++) {
            // 支付宝新增
            int new_zfb = Integer.parseInt(d_manual.get(i).getZfbreuser());
            //App新增
            int new_app = Integer.parseInt(d_umeng.get(i).getNewumengakswys()) + Integer.parseInt(d_umeng.get(i).getNewumengikswys()) + Integer.parseInt(d_umeng.get(i).getNewkyy());
            //微信新增
            int new_wx = Integer.parseInt(d_manual.get(i).getVideopv());

            // 安卓日活
            int a_live_num = JSONArray.fromObject(ztc_data.get(i).getContent()).getJSONArray(5).getInt(0);
            // IOS日活
            int i_live_num = JSONArray.fromObject(ztc_data.get(i).getContent()).getJSONArray(5).getInt(1);
            // 微信-快速问医生
            int wx_kw_live_num = JSONArray.fromObject(ztc_data.get(i).getContent()).getJSONArray(6).getInt(0);
            // 微信-问医生
            int wx_wys_live_num = JSONArray.fromObject(ztc_data.get(i).getContent()).getJSONArray(6).getInt(1);
            // 微信-快问健康快讯
            int wx_kwjk_num = JSONArray.fromObject(ztc_data.get(i).getContent()).getJSONArray(6).getInt(2);
            // 活跃-kswys
            int xcx_kswys_num = Integer.parseInt(d_umeng.get(i).getLivexcxkswys());
            // 活跃-kswy
            int xcx_kswy_num = Integer.parseInt(d_umeng.get(i).getLivexcxkswy());
            // 活跃-支付宝
            int xcx_zfb_num = Integer.parseInt(d_umeng.get(i).getLivexcxzfb());
            // 活跃-快应用
            int kyy_num = Integer.parseInt(d_umeng.get(i).getLivekyy());

            String str_kcaiwu = HttpTransfer.DTsendGet("https://k.120.net/cron/statistics/department?", "dateline=" + dateList.get(date_size - i - 1));
            JSONObject o_caiwu = JSONObject.fromObject(str_kcaiwu);
            int new_pay = o_caiwu.getInt("order_user_num") - o_caiwu.getInt("order_fg_user_num");
            int re_pay = o_caiwu.getInt("order_fg_user_num");
            String avg_pau = str2double(String.valueOf(o_caiwu.getDouble("order_amount") / o_caiwu.getInt("order_num")));


            ftitle_row.get(i + 1).getCell(0).setCellValue(dateList.get(date_size - i - 1));
            ftitle_row.get(i + 1).getCell(1).setCellValue(String.valueOf(new_zfb + new_app + new_wx));
            ftitle_row.get(i + 1).getCell(2).setCellValue(String.valueOf(a_live_num + i_live_num + wx_kw_live_num + wx_wys_live_num + wx_kwjk_num + xcx_zfb_num + kyy_num + xcx_kswy_num + xcx_kswys_num));
            ftitle_row.get(i + 1).getCell(3).setCellValue(String.valueOf(new_pay));
            ftitle_row.get(i + 1).getCell(4).setCellValue(String.valueOf(re_pay));
            ftitle_row.get(i + 1).getCell(5).setCellValue(avg_pau);
        }

        setColumnwidth(sheet, 6, 3000);
        return sheet;
    }

    private void setColumnwidth(HSSFSheet sheet, int num, int standard) {
        /* 自动调整宽度 */
        for (int i = 0; i < num; i++) {
            sheet.autoSizeColumn(i);
        }
        /* 实际宽度 */
        int curColWidth = 0;

        /* 默认宽度 */
        ArrayList<Integer> defaultColWidth = new ArrayList<Integer>();
        for (int i = 0; i < num; i++) {
            defaultColWidth.add(standard);
        }
        /* 实际宽度 < 默认宽度的时候、设置为默认宽度 */
        for (int i = 0; i < num; i++) {
            curColWidth = sheet.getColumnWidth(i);
            if (curColWidth < defaultColWidth.get(i)) {
                sheet.setColumnWidth(i, defaultColWidth.get(i));
            }
        }
    }
}
