(function() {
	$
			.extend(
					$.fn,
					{
						mask : function(msg, maskDivClass) {
							this.unmask();
							// 参数
							var op = {
								opacity : 0.8,
								z : 10000,
								bgcolor : '#ccc'
							};
							var original = $(document.body);
							var position = {
								top : 0,
								left : 0
							};
							if (this[0] && this[0] !== window.document) {
								original = this;
								position = original.position();
							}
							// 创建一个 Mask 层，追加到对象中
							var maskDiv = $('<div class="maskdivgen">&nbsp;</div>');
							maskDiv.appendTo(original);
							var maskWidth = original.outerWidth();
							if (!maskWidth) {
								maskWidth = original.width();
							}
							var bodyHeight = $(document.body).outerHeight();
							var maskHeight = document.body.scrollHeight;
							if (!maskHeight) {
								maskHeight = original.height();
							}
							maskDiv.css({
								position : 'absolute',
								top : position.top,
								left : position.left,
								'z-index' : op.z,
								width : maskWidth,
								height : maskHeight,
								'background-color' : op.bgcolor,
								opacity : 0
							});
							if (maskDivClass) {
								maskDiv.addClass(maskDivClass);
							}
							if (msg) {
								var msgDiv = $('<div style="position:absolute;border:#6593cf 1px solid; padding:2px;background:#ccca"><div style="line-height:24px;border:#a3bad9 1px solid;background:white;padding:2px 10px 2px 10px">'
										+ msg + '<img src="../../commons/images/loading.gif"/></div></div>');
								msgDiv.appendTo(maskDiv);
								var widthspace = (maskDiv.width() - msgDiv
										.width());
								var heightspace = (bodyHeight - msgDiv
										.height());
								msgDiv.css({
									cursor : 'wait',
									top : (heightspace / 2 - 2),
									left : (widthspace / 2 - 2)
								});
							}
							maskDiv.fadeIn('fast', function() {
								// 淡入淡出效果
								$(this).fadeTo('fast', op.opacity);
							})
							return maskDiv;
						},
						unmask : function() {
							var original = $(document.body);
							if (this[0] && this[0] !== window.document) {
								original = $(this[0]);
							}
							original.find("> div.maskdivgen").remove();
						}
					});
})();

String.prototype.trim = function() {
	return this.replace(/^\s+/, '').replace(/\s+$/, '');
}

String.prototype.endsWith = function(compareValue, ignoreCase) {
	if (ignoreCase) {
		return this.toLowerCase().lastIndexOf(compareValue.toLowerCase()) == this.length
				- compareValue.length;
	}
	return this.lastIndexOf(compareValue) == this.length - compareValue.length;
}
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
    } else {  
        return this.replace(reallyDo, replaceWith);  
	}  
}
var js2java = {
	getIsVisibleDiv : function() {
		return "1";
	},
	showErrorMsg : function(str) {
		DataHelper.alert(str);
	},
	back : function() {
		DataHelper.back();
	}
}
var processData = null; //全局变量
var curUser = null;//全局变量当前登录人信息
function initPageTemplate() {
	if(processData==null){processData=HostApp.getData();}
	if(curUser==null){curUser=HostApp.getCurUser();}
	$.each($("[rendname]"), function() {
		var attrName = $(this).attr("rendname");
		var htmlData = ""
		try {
			htmlData = eval("processData." + attrName);
		} catch (e) {
			print_log("获取数据错误" + "processData." + attrName, e);
		}
		$(this).html(htmlData)
	});
	var bt = baidu.template;
	$.each($("[templateId]"), function() {
		var tmplId = $(this).attr("templateId");
		var html = "";
		try {
			html = bt(tmplId, processData);
		} catch (e) {
			print_log("获取模版数据错误!" + tmplId, e);
		}
		var templatePosition = $(this).attr("templatePosition");
		if (!templatePosition || templatePosition == "innerHTML") {
			$(this).html(html)
		} else if (templatePosition == "after") {
			$(this).after(html)
		} else if (templatePosition == "before") {
			$(this).before(html)
		}
	})
}
