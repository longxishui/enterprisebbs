/*
 * 获取页面模版所需的数据到全局变量中
 */
function getPageData() {
	return processData;
}
/**
 * 获取除了附件的fullData
 * 
 * @returns
 */
var common = {
	getPageFullData : function() {
		return processData.fullData;
	},
	/**
	 * 审批提交默认的提交数据
	 */
	getSubmitDefaultData : function() {
		var data = getPageData();
		var fullData = data.fullData;
		var curLoginName ="";
		if(typeof(curUser.loginName)!="undefined"&&curUser.loginName!=null){
			curLoginName = curUser.loginName;
		}
		var json = {
			"serverId" : fullData.serverId,
			"modelId" : fullData.modelId,
			"processId" : fullData.processId,
			"circuitNode" : fullData.taskId,
			"isTest" : fullData.isTest,
			"username" : curLoginName,
			"action" : "",
			"memo" : "",
			"transmitUser" : "",
			"countersignUserList" : "",
			"otherValue" : "",
			"approveSelected" : "",
		}
		return json;
	},
	/**
	 * 将json对象转换为字符串
	 * 
	 * @param o
	 * @returns {String}
	 */
	jsonToStr : function(o) {
		return JSON.stringify(o);
	},
	getSubmitJsonToStr : function(data) {
		var defaultSubmit = this.getSubmitDefaultData();
		var submit = $.extend(defaultSubmit, data);
		var submitStr = this.jsonToStr(submit);
		print_log("提交的json数据字符串为：" + submitStr);
		return submitStr;
	}
}
$.extend(DataHelper,{
	submitUrl : "http://localhost:7083/process/submit.do",//表单提交的url
	createProcessUrl : 'http://localhost:7083/process/create.do',//创建流程的url
	httpRequest:function(receiverMsg, onError, content) {
		var url = this.submitUrl;
		$("body").mask("");
		$.mobile.loading('show');
		this.proxyReq(url, content, function(msgObj) {
			var rstCode = msgObj.result;
			if(rstCode!='0'){//代理访问失败
				HostApp.toast("提交失败,请稍后重试！");
			}else{
				//代理访问成功，判断审批返回的是否成功
				var rst = JSON.parse(msgObj.content);
				if (rst==null||typeof(rst.result)=='undefined'||rst.result != '0') {//rst=null为代理访问失败
					onError(rst);
					HostApp.toast("提交失败,请稍后重试！");
				} else {
					onReceiverMsg(rst);
					DataHelper.updateProcessStatus();
					window.setTimeout(function(){
						HostApp.goBack();
					},500); 
				}
			}
			$("body").unmask();
			$.mobile.loading('hide');
		})
	}
})