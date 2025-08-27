//借宝地一用，存个临时数据。
var toastData;

var arrInCome = ["总收入"];
var arrFree = ["金额","免费咨询数","免费咨询订单数"];
var arrPay = ["金额","订单数"];
var arrOther = ["金额","药品订单数","续方订单数","体检订单数","付费课程订单数","杏林中医订单数"];
var arrJSZX = ["金额","订单数"];
var arrAPPWX = ["总日活","总新增","新增占比"];
var arrAPP = ["Android日活","IOS日活","Android新增","IOS新增"];
var arrWx = ["快问日活","问医生日活","快问健康日活","快问新增","问医生新增"];
var arrUV = ["友盟UV总数据","医生推荐订单","医生奖励数","处方数","诊室订单数","HZBD"];
var arrMoneyWeek = ["周","日期","周总收入","均值/天","基数/天","周完成比例"];
var arrMoneyMonth = ["月份","基数/月","总收入","均值/天","月完成比例"];
var arrMoneyYear = ["年度总收入目标","当前实际完成","完整月完成比例","当前完成比例"];
var arrZFB=["支付宝收入","支付宝日活","支付宝新增"]

function initExcel(){
	var table_income = document.getElementById("toast-income");
	var table_free = document.getElementById("toast-free");
	var table_pay = document.getElementById("toast-pay");
	var table_other = document.getElementById("toast-other");
	var table_jszx = document.getElementById("toast-jszx");
	var table_appwx = document.getElementById("toast-appwx");
    var table_app = document.getElementById("toast-app");
    var table_wx = document.getElementById("toast-wx");
	var table_uv = document.getElementById("toast-uv");
	var table_week = document.getElementById("toast-week");
	var table_month = document.getElementById("toast-month");
	var table_year = document.getElementById("toast-year");
	var table_zfb = document.getElementById("toast-zfb");

	var new_uv_list_data=[]
	var filter_list = [0,2,4,7,9,10,11,13,15,18,20,21,22,24,26,29,31,32]
	for(var i=0;i<filter_list.length;i++){
		var new_i = filter_list[i]
		new_uv_list_data.push(toastData[8][new_i])
	}

	makeTable(table_income,toastData[16],arrInCome);
	makeTable(table_free,toastData[1],arrFree);
	makeTable(table_pay,toastData[2],arrPay);
	makeTable(table_other,toastData[3],arrOther);
	makeTable(table_jszx,toastData[4],arrJSZX);
	makeTable(table_appwx,makeArrPercent(toastData[7]),arrAPPWX);
    makeTable(table_app,toastData[5],arrAPP);
    makeTable(table_wx,toastData[6],arrWx);
	makeTable(table_uv,new_uv_list_data,arrUV);
	makeTableSingle(table_week,toastData[9],arrMoneyWeek);
	makeTableSingle(table_month,toastData[10],arrMoneyMonth);
	makeTableSingle(table_year,toastData[11],arrMoneyYear);
	makeTable(table_zfb,toastData[12],arrZFB);
}

function makeArrPercent(listarr){
	var arr = new Array();
	arr[0]=listarr[0];
	arr[1]=listarr[1];
	arr[2]=Math.round(listarr[1] / listarr[0] * 10000) / 100.00 + "%";
	arr[3]=listarr[2];
	arr[4]=listarr[3];
	arr[5]=Math.round(listarr[3] / listarr[2] * 10000) / 100.00 + "%";
	arr[6]=listarr[4];
	arr[7]=listarr[5];
	arr[8]=Math.round(listarr[5] / listarr[4] * 10000) / 100.00 + "%";

	
	return arr;
}
function makeTableSingle(tablename,listarr,titlelist){
	var tr_title = document.createElement("tr");
	var th_cate = document.createElement("th");
	th_cate.setAttribute("style","text-align: center; vertical-align: middle;");
	th_cate.innerText = "日期";

	var th_date_s = document.createElement("th");
	th_date_s.setAttribute("style","text-align: center; vertical-align: middle;");
	th_date_s.innerText = addDate(getNowFormatDate(),-1);

	tr_title.appendChild(th_cate);
	tr_title.appendChild(th_date_s);
	tablename.appendChild(tr_title);

	var tbody = document.createElement("tbody");
	for(var i=0;i<titlelist.length;i++){
		var tr_data = document.createElement("tr");
		var th_title = document.createElement("th");
		th_title.setAttribute("style","text-align: center; vertical-align: middle;");
		th_title.innerText = titlelist[i];

		tr_data.appendChild(th_title);

		var th_data = document.createElement("th");
		th_data.setAttribute("style","text-align: center; vertical-align: middle;");
		th_data.innerText = listarr[i];
		tr_data.appendChild(th_data);


		tbody.appendChild(tr_data);
	}

	tablename.appendChild(tbody);
}

function makeTable(tablename,listarr,titlelist){
	
	var tr_title = document.createElement("tr");
	var th_cate = document.createElement("th");
	th_cate.setAttribute("style","text-align: center; vertical-align: middle;");
	th_cate.innerText = "日期";
	
	var th_date_o = document.createElement("th");
	th_date_o.setAttribute("style","text-align: center; vertical-align: middle;");
	th_date_o.innerText = addDate(getNowFormatDate(),-3);
	
	var th_date_t = document.createElement("th");
	th_date_t.setAttribute("style","text-align: center; vertical-align: middle;");
	th_date_t.innerText = addDate(getNowFormatDate(),-2);
	
	var th_date_s = document.createElement("th");
	th_date_s.setAttribute("style","text-align: center; vertical-align: middle;");
	th_date_s.innerText = addDate(getNowFormatDate(),-1);
	
	tr_title.appendChild(th_cate);
	tr_title.appendChild(th_date_s);
	tr_title.appendChild(th_date_t);
	tr_title.appendChild(th_date_o);
	tablename.appendChild(tr_title);
	
	var tbody = document.createElement("tbody");
	for(var i=0;i<titlelist.length;i++){
		var tr_data = document.createElement("tr");
		var th_title = document.createElement("th");
		th_title.setAttribute("style","text-align: center; vertical-align: middle;");
		th_title.innerText = titlelist[i];
		
		tr_data.appendChild(th_title);
		for(var j=0;j<3;j++){
			var k=i;
			k=k+titlelist.length*j;
			var th_data = document.createElement("th");
			th_data.setAttribute("style","text-align: center; vertical-align: middle;");
			th_data.innerText = listarr[k];
			tr_data.appendChild(th_data);

		}
		
		tbody.appendChild(tr_data);
	}

	tablename.appendChild(tbody);
	
}
function getToast(){
	var date= addDate(getNowFormatDate(),-1);
	$.ajax({
		type : "POST",
		contentType : "application/json; charset=utf-8",
		url : "GetReport",
		data : JSON.stringify({"date":date}),
		dataType : 'json',
		success : function(result) {
			// 作为数据展示的传回数据
			toastData = result;
			initExcel();
		}
	});
}
/**
 * //日期加减法 date参数为计算开始的日期，days为需要加的天数 //格式:addDate('2017-1-11',20)
 * 
 * @param date
 * @param days
 * @returns
 */
function addDate(date, days) {
    var d = new Date(date);
    d.setDate(d.getDate() + days);
    var month = d.getMonth() + 1;
    var day = d.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var val = d.getFullYear() + "-" + month + "-" + day;
    return val;
}

/**
 * 获取星期
 * 
 * @returns
 */
function getday(dateStr){
	if (typeof(dateStr) != "undefined"){ 
	var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];    
    var myDate = new Date(Date.parse(dateStr.replace(/-/g, "/")));  
    return weekDay[myDate.getDay()];   
	}
}
/**
 * 获取当前时间，格式YYYY-MM-DD
 * 
 * @returns
 */
function getNowFormatDate() {
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}
