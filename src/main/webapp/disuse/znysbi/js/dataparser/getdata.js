function getToast(start,end,b_type) {
    const params = {
        "start":start,
        "end":end,
        "b_type":b_type
    };
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "GetBI",
        data: JSON.stringify(params),
        dataType: 'json',
        success: function (result) {
            if(result['code']==200){
                toastr.success("数据加载中！！");
                let json_result = make40data(result['result']);
                // 作为数据展示的传回数据
                shitu1(json_result[0])
                shitu2(json_result[2],json_result[7])
                shitu3(json_result[1])
                shitu4(json_result[4])
                shitu5(json_result[3])
                shitu6(json_result[5])
                shitu7(json_result[6])
            }else {
                if(result['code']==404){
                    toastr.warning("请缩小数据选择范围，数据不足！！");
                }else {
                    toastr.error("如果看不到数据，请联系宗佳！！");
                }
            }
        }
    });
}

function sum_int(result) {
    let sum_result = new Array();
    // 初始化数组
    if(result.length>0){
        for(let i=0;i<result[0].length;i++){
            sum_result[i] = new Array();
            sum_result[i] =0;
        }
    }
    // 同类别数组 数据求和
    for(let i = 0;i<result.length;i++){
        for(let j =0;j<result[i].length;j++){
            sum_result[j] = numAdd(sum_result[j],result[i][j]);
        }
    }
    // 同类别数组求均值
    for(let i=0;i<sum_result.length;i++){
        sum_result[i] = toDecimal2(numDiv(sum_result[i],result.length));
    }
    return sum_result;
}

function sum_json(result) {
    // 特殊处理 留存json {"new":0,"live":0}
    let sum_result = new Array();
    let temp_result = new Array();
    // 同类别数组 数据求和
    for(let i=0;i<result.length;i++){
        sum_result[i] = new Array();
        for(let j =0;j<result[i].length;j++){
            sum_result[i][j] = new Array();
            let result_item = new Array();
            result_item[0] = result[i][j]['new'];
            result_item[1] = result[i][j]['live'];
            sum_result[i][j] = result_item;
        }
    }
    for(let i=0;i<sum_result.length;i++){
        sum_result[i] = sum_result[i].join().split(',');
    }

    // 初始化数组
    if(sum_result.length>0){
        for(let i=0;i<sum_result[0].length;i++){
            temp_result[i] = new Array();
            temp_result[i] =0;
        }
    }
    // 同类别数组 数据求和
    for(let i = 0;i<sum_result.length;i++){
        for(let j =0;j<sum_result[i].length;j++){
            temp_result[j] = numAdd(temp_result[j],sum_result[i][j]);
        }
    }
    // 同类别数组求均值
    for(let i=0;i<temp_result.length;i++){
        temp_result[i] = toDecimal2(numDiv(temp_result[i],sum_result.length));
    }


    return temp_result;
}

function make40data(result) {
    let json_result = []
    let new_json = new Array(8)
    let ret_result =[]
    // 重新排序数据
    for(let i=0;i<8;i++){
        new_json[i] = new Array();
        for(let j =0;j<result.length;j++){
            let result_item = new Array();
            result_item = result[j]['content'];
            new_json[i][j] = result_item[i];
        }
    }

    ret_result.push(sum_int(new_json[0]));
    ret_result.push(sum_int(new_json[1]));
    ret_result.push(sum_int(new_json[2]));
    ret_result.push(sum_int(new_json[4]));
    ret_result.push(sum_json(new_json[3]));
    ret_result.push(sum_int(new_json[5]));
    ret_result.push(sum_int(new_json[6]));
    ret_result.push(sum_int(new_json[7]));


    return ret_result;
}

function numAdd(num1, num2) {
    let baseNum, baseNum1, baseNum2;
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
    let precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;//精度
    return ((num1 * baseNum + num2 * baseNum) / baseNum).toFixed(precision);
}

// 除法运算，避免数据相除小数点后产生多位数和计算精度损失。
function numDiv(num1, num2) {
    let baseNum1 = 0, baseNum2 = 0;
    let baseNum3, baseNum4;
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    with (Math) {
        baseNum3 = Number(num1.toString().replace(".", ""));
        baseNum4 = Number(num2.toString().replace(".", ""));
        return (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1);
    }
}

function toDecimal2(x) {
    var f = parseFloat(x);
    if (isNaN(f)) {
        return false;
    }
    var f = Math.round(x*100)/100;
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + 2) {
        s += '0';
    }
    return s;
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
