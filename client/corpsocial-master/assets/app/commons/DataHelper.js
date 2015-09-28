/**
 * 打印日志
 * 
 * @param str
 * @param e
 */
function print_log(str, e) {
	try {
		window.console = window.console || {};
		console.log || (console.log = opera.postError);
		if (e) {
			console.log("错误名称：" + e.name + "；错误信息：" + e.message);
		}
		console.log(str);
		console.trace();
	} catch (e) {
	}
}
/**
 * 选择联系人后的回调函数
 */
function selectedContactsCallback(jsonArray){
	print_log("进入选择联系人的回调函数！选择人员返回数据为："+jsonArray);
	var jsonArrayObj = JSON.parse(jsonArray);//由字符串转换为jsonArray
	var callBack = DataHelper.selectContactCallBack;
	if(typeof(callBack)=='function'){
		print_log("调用回调函数："+callBack);
		callBack(jsonArrayObj);
	}else{
		print_log("回调函数为空或不是方法！"+callBack);
	}
}
/**
 * 客户端和html调用的接口函数
 */
var DataHelper = {
	/**
	 * 在选择人时将回调函数保存到此变量中，在回调函数selectedContactsCallback中调用该函数
	 */
	selectContactCallBack:null,
	/**
	 * 
	 * @param selected
	 *            已选人Arraty[]
	 * @param limitedDepart限制选择的部门
	 * @param callback回调函数
	 */
	selectContacts : function(selected, limitedDepart, callback) {
		var retData = null;
		try{
			print_log("选择联系人开始，已选人员列表为：" + selected + ",限制部门：" + limitedDepart);
			this.selectContactCallBack = callback;
			try{
				HostApp.selectContacts(selected,limitedDepart,function(josnArray){
                    selectedContactsCallback(josnArray);
                });
			}catch(e){
				HostApp.selectContacts(selected,limitedDepart);//android处理方法
			}
		} catch (e) {
			print_log("选择联联系人出错，错误名称：" + e.name + "；错误信息：" + e.message);
		}
	},
	/**
	 * 获取客户端选中的数据
	 * 
	 * @returns
	 */
	getData : function() {
		var retData = null;
		try{
//			print_log("获取数据开始：");
			retData = HostApp.getData();
			if(typeof(retData)=="string"){retData = JSON.parse(retData);}
//			print_log("获取数据："+JSON.stringify(retData));
			return retData;
		}catch(e){
			print_log("获取数据失败!"+ e.name + "；错误信息：" + e.message)
		}
	},
	/**
	 * 打开附件
	 * 
	 * @param name
	 *            文件名称
	 * @param url
	 *            文件url
	 */
	openAttach : function(name, url,callback) {
//		print_log("打开附件，url:" + url);
		try{
			HostApp.openAttach(name, url,function(){
				callback();
			});
		}catch(e){
			print_log("打开附件出错，错误名称：" + e.name + "；错误信息：" + e.message);
		}
		
	},
	/*
	 * 4.3 代理访问 url 字符型 可变 否 代理访问地址 content 字符型 可变 否 数据内容，格式由模板与url自行约定 返回结果
	 * Callback(rst) { result：0-成功，1-失败 message:其他信息 content:返回内容 }
	 */
	proxyReq : function(url, content, callback) {
		var retmsg =null;
		try{
			print_log("提交数据，url:" + url + ",content:" + content);
			HostApp.proxyReq(url, content, function (msgObj) {
				if(typeof(msgObj)=="string"){msgObj = JSON.parse(msgObj);}
				print_log("代理访问返回的数据为:"+JSON.stringify(msgObj));
				if(msgObj&&msgObj.result==0){
					print_log("代理访问成功");
					callback(msgObj);
				}else{
					print_log("代理访问出错");
					callback(msgObj);//代理访问出错
				}
			});
		}catch(e){
			print_log("代理访问接口出错，错误名称：" + e.name + "；错误信息：" + e.message);
			callback({"result":"-1","message":"访问出错","content":""})
		}
	},
	call:function(number){
		HostApp.call(number);
	},
	/**
	 * 获取当前用户
	 */
	getCurUser:function(){
	   var retData = HostApp.getCurUser();
	   if(typeof(retData)=="string"){retData = JSON.parse(retData);}
	   return retData;
	},
	updateProcessStatus:function(){
		try{
			HostApp.updateProcessStatus();
		}catch(e){
			print_log("更新流程的状态调用异常！",e);
		}
		
	},
	forward:function(url){
		try{
			HostApp.forward(url);
		}catch(e){
			print_log("跳转界面异常！",e);
		}
	},
	back:function(){
		HostApp.back();
	},
	alert:function(str){
		HostApp.alert(str);
	}
}
