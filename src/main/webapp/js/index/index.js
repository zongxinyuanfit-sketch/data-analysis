/**
 * 上传手动输入的数据
 *
 * @returns
 */
var subStatus='';
function uploadData() {
    let date = document.getElementById("mediadate").value;
    let livewxkswys = document.getElementById("zy-live-wx-kswys").value;
    let livewxwys = document.getElementById("zy-live-wx-wys").value;
    //let livekwjk = document.getElementById("zy-live-kwjk").value;
    let yysymoney = document.getElementById("yy-sy-money").value;
    let zfblive = document.getElementById("yy-zfb-live").value;
    let zfbreuser = document.getElementById("yy-zfb-reuser").value;
    //let shortsnew = document.getElementById("shorts-new").value;
    let fugouwx = document.getElementById("zy-fugou-wx").value;
    let yyvideopv = document.getElementById("yy-video-pv").value;


    if (parseDate(date) == getNowFormatDate()) {
        toastr.warning("请选择今天之前的日期");
    } else {
        let json_manual = {
            "id": 0,
            "mediareadnum": '0',
            "livewxkswys": livewxkswys,
            "livewxwys": livewxwys,
            "livekwjk": '0',
            "applitelive": '0',
            "udate": parseDate(date),
            "videopv": yyvideopv,
            "zfbmoney": yysymoney,
            "zfblive": zfblive,
            "zfbreuser": zfbreuser,
            "newkwjk": '0',
            "fugouwx": fugouwx
        };
        adddata(json_manual);
    }

}

/**
 * 更新数据库数据
 *
 * @param request_str
 * @returns
 */
function adddata(request_str) {
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "AddData",
        data: JSON.stringify(request_str),
        dataType: 'json',
        success: function (result) {
            if(result.code==200){
                document.getElementById('sub_status').innerText = '操作结果：提交成功'
            }else{
                document.getElementById('sub_status').innerText = '操作结果：提交失败'
            }
            //toastr.success(result.message);
        }
    });

}

// 日期格式化
function formartDate(y, m, d, symbol) {
    symbol = symbol || '-';
    m = (m.toString())[1] ? m : '0' + m;
    d = (d.toString())[1] ? d : '0' + d;
    return y + symbol + m + symbol + d
}

/**
 * 下载数据表格
 *
 * @returns
 */
function loadExcel() {
    // if(isReadyDownload()) {
    let stime = document.getElementById("starttime").value.split("-");
    let etime = document.getElementById("endtime").value.split("-");
    let starttime = formartDate(stime[0], stime[1], stime[2], "-");
    let endtime = formartDate(etime[0], etime[1], etime[2], "-");

    if (endtime <= starttime) {
        toastr.warning("开始时间要小于结束时间，请重新选择");
    } else {
        let paramexcel = parambuilderJson(0, "", "", starttime, endtime, 1, "");
        $.ajax({
            type: "POST",
            url: "GetExcel",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(paramexcel),
            async: true,
            success: function (resultdata) {
                if (resultdata == "makeing") {
                    toastr.warning("数据还没有收集完成，请过10分钟后再次尝试");
                } else {
                    try {
                        toastr.success("报表生成中");
                        let a = document.getElementById("downKPI");
                        a.href = resultdata;
                        a.click();
                    } catch (e) {
                    }
                }
            }
        });
    }
}

function addDate(date, days) {
    let d = new Date(date);
    d.setDate(d.getDate() + days);
    let month = d.getMonth() + 1;
    let day = d.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    let val = d.getFullYear() + "-" + month + "-" + day;
    return val;
}

/**
 * 获取当前时间，格式YYYY-MM-DD
 *
 * @returns
 */
function getNowFormatDate() {
    let date = new Date();
    let seperator1 = "-";
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    let currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

//借宝地一用，存个临时数据。
let toastDayData;

function parseDate(date) {
    let arr_date = date.split('-');
    let new_date;
    if (arr_date[1].length == 1) {
        new_date = arr_date[0] + "-" + "0" + arr_date[1];
    } else {
        new_date = arr_date[0] + "-" + arr_date[1];
    }
    if (arr_date[2].length == 1) {
        new_date = new_date + "-" + "0" + arr_date[2];
    } else {
        new_date = new_date + "-" + arr_date[2];
    }
    return new_date;
}

function isReadyDownload() {
    let date = new Date();
    let hour = date.getHours();
    let minute = date.getMinutes();
    if (hour > 9) {
        return true;
    } else if (hour == 9) {
        if (minute > 10) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }

}

function uploadUMData() {
    let date = document.getElementById("umengdate").value;
// 活跃-快问-IOS
    let liveumengikswys = document.getElementById("umeng-live-ios-kswys").value;
// 活跃-快问-ANDROID
    let liveumengakswys = document.getElementById("umeng-live-android-kswys").value;
// 活跃-快问-KYY
    let livekyy = document.getElementById("umeng-live-kyy-kswys").value;
// 新增-快问-IOS
    let newumengikswys = document.getElementById("umeng-new-ios-kswys").value;
// 新增-快问-ANDROID
    let newumengakswys = document.getElementById("umeng-new-android-kswys").value;
// 新增-快问-KYY
    let newkyy = document.getElementById("umeng-new-kyy-kswys").value;
// 活跃-快速问医生-WX
    let livexcxkswys = document.getElementById("umeng-live-wx-kswys").value;
// 活跃-快速问医-WX
    let livexcxkswy = document.getElementById("umeng-live-wx-kswy").value;
// 活跃-杏林普康-WX
    let livexcxkpy = document.getElementById("umeng-live-wx-xlpk").value;
// 活跃-快速问医生-ZFB
    let livexcxzfb = document.getElementById("umeng-live-zfb-kswys").value;
// 新增-快速问医生-WX
    let newxcxkswys = document.getElementById("umeng-new-wx-kswys").value;
// 新增-快速问医-WX
    let newxcxkswy = document.getElementById("umeng-new-wx-kswy").value;
// 新增-杏林普康-WX
    let newxcxkpy = document.getElementById("umeng-new-wx-xlpk").value;
// 新增-快速问医生-ZFB
    let newxcxzfb = document.getElementById("umeng-new-zfb-kswys").value;
// 爬虫-快速问医生-WX
    let pcxcxkswys = document.getElementById("umeng-pchong-wx-kswys").value;
// 爬虫-快速问医-WX
    let pcxcxkswy = document.getElementById("umeng-pchong-wx-kswy").value;
// 爬虫-杏林普康-WX
    let pcxcxkpy = document.getElementById("umeng-pchong-wx-xlpk").value;


    if (parseDate(date) == getNowFormatDate()) {
        toastr.warning("请选择今天之前的日期");
    } else {
        let json_manual = {
            "id": 0,
            "newumengakswys": newumengakswys,
            "newumengikswys": newumengikswys,
            "liveumengakswys": liveumengakswys,
            "liveumengikswys": liveumengikswys,
            "udate": date,
            "livekyy": livekyy,
            "newkyy": newkyy,
            "dcrecommend": 0,
            "tuidanlv": 0,
            "uvwebm": 0,
            "uvwebpc": 0,
            "livexcxkswys": livexcxkswys,
            "livexcxkswy": livexcxkswy,
            "livexcxkpy": livexcxkpy,
            "livexcxzfb": livexcxzfb,
            "newxcxkswys": newxcxkswys,
            "newxcxkswy": newxcxkswy,
            "newxcxkpy": newxcxkpy,
            "newxcxzfb": newxcxzfb,
            "pcxcxkswys": pcxcxkswys,
            "pcxcxkswy": pcxcxkswy,
            "pcxcxkpy": pcxcxkpy,
        };
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: "AddUMData",
            data: JSON.stringify(json_manual),
            dataType: 'json',
            success: function (result) {
                toastr.success(result.message);
            }
        });
    }
}
