/* 请求参数构造器 */
/**
 * 传递参数构造器
 * @param isexist
 * @param tablename
 * @param uid
 * @param starttime
 * @param endtime
 * @param usetime
 * @param savejson
 * @returns
 */
function parambuilder(isexist,tablename,uid,starttime,endtime,usetime,savejson){
	// {"isexist":"1","tablename":"user","uid":"10001","starttime":"","endtime":"","usetime":"0","savejson":"",}
	//var ret = "\"isexist\":"+"\""+isexist+"\","+"\"tablename\":"+"\""+tablename+"\","+"\"uid\":"+"\""+uid+"\","+"\"starttime\":"+"\""+starttime+"\","+"\"endtime\":"+"\""+endtime+"\","+"\"usetime\":"+"\""+usetime+"\","+"\"savejson\":"+"\""+savejson+"\"";
	var ret = "isexist="+isexist+"&tablename="+tablename+"&uid="+uid+"&starttime="+starttime+"&endtime="+endtime+"&usetime="+usetime+"&savejson="+savejson;
	return ret;
}

function parambuilderJson(isexist,tablename,uid,starttime,endtime,usetime,savejson){
	var ret ={"isexist":isexist,"tablename":tablename,"uid":uid,"starttime":starttime,"endtime":endtime,"usetime":usetime,"savejson":savejson};
	//var ret = "\"isexist\":"+"\""+isexist+"\","+"\"tablename\":"+"\""+tablename+"\","+"\"uid\":"+"\""+uid+"\","+"\"starttime\":"+"\""+starttime+"\","+"\"endtime\":"+"\""+endtime+"\","+"\"usetime\":"+"\""+usetime+"\","+"\"savejson\":"+"\""+savejson+"\"";
	//var ret = "isexist="+isexist+"&tablename="+tablename+"&uid="+uid+"&starttime="+starttime+"&endtime="+endtime+"&usetime="+usetime+"&savejson="+savejson;
	return ret;
}