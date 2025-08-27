package com.jky.znys.team_kpi.disuse;

import com.jky.znys.team_kpi.entity.KPITotalMoneyDay;
import com.jky.znys.team_kpi.entity.KPITotalMoneyMonth;
import com.jky.znys.team_kpi.entity.KPITotalMoneyWeek;
import com.jky.znys.team_kpi.service.MoneyMakerService;
import com.jky.znys.team_kpi.utils.NetGPData;
import com.jky.znys.team_kpi.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MoneyMaker {
    @Autowired
    private MoneyMakerService moneyMakerService = (MoneyMakerService) SpringContextUtils.getBean(MoneyMakerService.class);

    public String[] makeWeekResult() {
        String preDate = "";
        try {
            preDate = NetGPData.plusDay(-1, NetGPData.getNowDate());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        String[] result = new String[6];
        int indexValue = 0;
        KPITotalMoneyWeek tmw_item = moneyMakerService.getTotalMoneyWeek(preDate);
        // 1周总收入
        ArrayList<KPITotalMoneyDay> tmd_list = moneyMakerService.getTotalMoneyDay(tmw_item.getStart(), tmw_item.getEnd());
        BigDecimal total = new BigDecimal(0.00);
        // 数据总和为double类型 保留小数点2位
        for (int i = 0; i < tmd_list.size(); i++) {
            total = total.add(new BigDecimal(Double.valueOf(tmd_list.get(i).getMoney())));
        }

        // 2均值/天   3周完成比例
        BigDecimal averageValue = new BigDecimal(0.00);
        String donePercent = "";
        // 完成百分比 基数/毛利 1-3月份可用  毛利/基数 4-12月份可用
        // 完成率＝（2－实际完成数/目标完成数）*100%
        if (NetGPData.getWeekOfDate(new Date()).equals("星期一")) {
            averageValue = new BigDecimal(total.doubleValue() / 7).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            // 考虑要不要变为可显示为具体数值
            averageValue = new BigDecimal(total.doubleValue() / tmd_list.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        donePercent = handleMath(averageValue.intValue(),Integer.parseInt(tmw_item.getRadix()));
        // 填充数据
        tmw_item.setTotal(total.doubleValue() + "");
        tmw_item.setAverage(averageValue.doubleValue() + "");
        tmw_item.setPercent(donePercent + "%");
        moneyMakerService.updateTotalMoneyWeek(tmw_item);

        // 编辑周数据结果
        result[0] = "第" + tmw_item.getId() + "周";
        result[1] = tmw_item.getStart() + "～" + tmw_item.getEnd();
        result[2] = total.doubleValue() + "";
        result[3] = averageValue.doubleValue() + "";
        result[4] = tmw_item.getRadix();
        result[5] = donePercent + "%";

        return result;
    }

    private String handleMath (int aver, int radix){
        String result="";
        int aver_temp=aver;
        int radix_temp=radix;
        if(aver>0&&radix>0){
            double f1 = new BigDecimal((float)aver_temp/radix_temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result = str2double(f1*100+"");
        }

        if(aver<0&&radix<0){
            radix_temp = -1*radix;
            aver_temp=-1*aver;
            double f1 = new BigDecimal((float)aver_temp/radix_temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result = str2double((2-f1)*100+"");
        }
        if(aver<0&&radix>0){
            aver_temp=0-aver;
            double f1 = new BigDecimal((float)aver_temp/radix_temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result = str2double((1+f1)*-100+"");
        }
        if(aver>0&&radix<0){
            radix_temp = 0-radix;
            double f1 = new BigDecimal((float)aver_temp/radix_temp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result = str2double((2+f1)*100+"");
        }
        if(aver==0||radix==0){
            result="0.00";
        }


        return result;
    }

    public String[] makeMonthResult() {
        // 当月份为1时，cal.get(Calendar.MONTH)=0
        String[] result = new String[5];
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int month_Num = month;
        int indexValue = 0;
        BigDecimal total = new BigDecimal(0.00);
        BigDecimal averageValue = new BigDecimal(0.00);
        String nowDate = NetGPData.getNowDate();
        String donePercent = "";
        KPITotalMoneyMonth tmm_item = new KPITotalMoneyMonth();
        ArrayList<KPITotalMoneyDay> tmd_list = new ArrayList<KPITotalMoneyDay>();

        //每月总数据
        if (nowDate.split("-")[2].equals("01")) {
            // 当为年初1月时，临时起作用
            if (month == 0) {
                tmm_item = moneyMakerService.getTotalMoneyMonthById(12);
                tmd_list = moneyMakerService.getTotalMoneyDay("2022-12-01", "2022-12-31");
            } else {
                tmm_item = moneyMakerService.getTotalMoneyMonthById(month);
                tmd_list = moneyMakerService.getTotalMoneyDay(NetGPData.getFirstDayOfMonth(month), NetGPData.getLastDayOfMonth(month));
            }
            month_Num = month - 1;
        } else {
            tmm_item = moneyMakerService.getTotalMoneyMonthById(month + 1);
            tmd_list = moneyMakerService.getTotalMoneyDay(NetGPData.getFirstDayOfMonth(month + 1), NetGPData.getLastDayOfMonth(month + 1));
        }
        // 数据总和为double类型 保留小数点2位
        for (int i = 0; i < tmd_list.size(); i++) {
            total = total.add(new BigDecimal(Double.valueOf(tmd_list.get(i).getMoney())));
            double temp_val = Math.abs(Double.valueOf(tmd_list.get(i).getMoney()));
            if (temp_val > 0) {
                indexValue++;
            }
        }

        // 2均值/天   3周完成比例
        // 完成百分比 基数/毛利 1-3月份可用  毛利/基数 4-12月份可用
        averageValue = new BigDecimal(total.doubleValue() / indexValue).setScale(2, BigDecimal.ROUND_HALF_UP);
//        donePercent = NetGPData.getPercent(Integer.parseInt(tmm_item.getRadix()),averageValue.intValue());
        donePercent = handleMath(total.intValue(),Integer.parseInt(tmm_item.getRadix()));
        // 填充数据
        tmm_item.setTotal(str2double(total.doubleValue() + ""));
        tmm_item.setAverage(averageValue.doubleValue() + "");
        tmm_item.setPercent(donePercent + "%");
        moneyMakerService.updateTotalMoneyMonth(tmm_item);

        // 编辑月数据结果
        result[0] = month_Num + 1 + "月";
        result[1] = tmm_item.getRadix();
        result[2] = str2double(total.doubleValue() + "");
        result[3] = averageValue.doubleValue() + "";
        result[4] = donePercent + "%";

        return result;
    }

    private String str2double(String str){
        String result = "";
        Double d= Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        result = df.format(d);
        return result;
    }

    public String[] makeYearResult() throws ParseException {
        String[] result = new String[4];
        String nowDate = NetGPData.getNowDate();
        int indexValue = 0;
        int indexValue_less = 0;
        // 每月总数据
        ArrayList<KPITotalMoneyMonth> tmm_list = moneyMakerService.getTotalMoneyMonth();
        BigDecimal total = new BigDecimal(0.00);
        BigDecimal total_less = new BigDecimal(0.00);
        // 数据总和为double类型 保留小数点2位 实际完成收入数据
        for (int i = 0; i < tmm_list.size(); i++) {
            total = total.add(new BigDecimal(Double.valueOf(tmm_list.get(i).getTotal())));
//            if (Double.valueOf(tmm_list.get(i).getTotal()) > 0) {
//                indexValue++;
//            }
            if (Math.abs(Double.valueOf(tmm_list.get(i).getTotal())) > 0) {
                indexValue++;
            }
        }

        // 整月完成收入数据
        if (nowDate.split("-")[2].equals("01")) {
            total_less = total;
            indexValue_less = indexValue;
        } else {
            indexValue_less = indexValue - 1;
            for (int i = 0; i < indexValue_less; i++) {
                total_less = total_less.add(new BigDecimal(Double.valueOf(tmm_list.get(i).getTotal())));
            }
        }
        int should_total = 0;
        // 应完成数据
        if (indexValue_less <= 0) {
            indexValue_less = 1;
        }
        for (int i = 0; i < indexValue_less; i++) {
//            should_total += (Integer.parseInt(tmm_list.get(i).getRadix()) * NetGPData.getMonthDays(2022, i + 1));
            should_total += Integer.parseInt(tmm_list.get(i).getRadix());
        }

        // 当前完成百分比
//        String nowPercent = NetGPData.getPercent(total_less.intValue(), should_total);
        String nowPercent ="0";
        if(total_less.intValue()!=0){
            nowPercent = handleMath(total_less.intValue(),should_total);
        }
        // 年度完成百分比
//        String yearPercent=NetGPData.getPercent(total_less.intValue(),17500000);

        // 编辑年度数据
        DecimalFormat df = new DecimalFormat("0.00");
//        //总目标
//        result[0]="17500000";
//        //当月实际完成目标
//        result[1]=df.format(total.doubleValue())+"";
//        //完整月应完成目标
//        result[2]=should_total+"";
//        //完整月完成目标
//        result[3]=df.format(total_less.doubleValue())+"";
//        //完整月完成百分比
//        result[4]=nowPercent+"%";
//        //年度完成百分比
//        result[5]=yearPercent+"%";

        //总目标
        result[0] = "500600";
        //当前实际完成
        result[1] = df.format(total.doubleValue()) + "";
        //完整月完成比例
        result[2] = nowPercent + "%";
        //当前完成比例
//        result[3] = handleMath(total.intValue(), NetGPData.daysBetween("2022-01-01", NetGPData.getNowDate()) * 548) + "%";
        result[3] = handleMath(total.intValue(), 200000) + "%";
        return result;
    }
}
