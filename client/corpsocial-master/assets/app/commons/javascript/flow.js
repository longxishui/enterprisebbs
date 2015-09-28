
	function initListView() {
		if (!dynApp.config || !dynApp.config.groups) return;
		var groups = dynApp.config.groups;
		for(var j = 0; j < groups.length; j++) {
			var ulContent = '';

			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				ulContent = ulContent + getListViewItem(items[i]) + "\n";
			}

			$("#" + groups[j].id).append(ulContent);
		}
	}

	function getListViewItem(itemConfig) {
		if (dynApp && dynApp.status === "approving")
		{
			//如果是审批，则直接返回只读Item
			return getListViewReadOnlyTextItem(itemConfig);
		}

		if (itemConfig.input === 'text')
		{
			return getListViewTextItem(itemConfig);
		} else if (itemConfig.input === 'textarea')
		{
			return getListViewTextAreaItem(itemConfig);
		} else if (itemConfig.input === 'select')
		{
			return getListViewSelectItem(itemConfig);
		} else if ($.inArray(itemConfig.input, ['date', 'time', 'datetime']) >= 0)
		{
			return getListViewDateItem(itemConfig);
		} else if (itemConfig.input == 'contact') {
		    return getListViewContactItem(itemConfig);
		}
	}

	/**
	* 获取只读的行
	*/
	function getListViewReadOnlyTextItem(itemConfig) {
		var result = '<li class="homeul" id="' + getListViewItemId(itemConfig) + '">' +
				'<table class="layout ' + getNotLastItemClass(itemConfig) + '"><tr>' +
					'<td class="label">' + itemConfig.title + '</td>' +
					'<td class="textvalue"><span id="' + getListViewItemValueId(itemConfig) + '">' + itemConfig.displayValue + '</span></td>' +
					'<td class="space"></td>' +
				'</tr></table>' +
			'</li>';
		return result;
	}

    /**
    * 选择联系人
    */
    function getListViewContactItem(itemConfig) {
		var result = '<li class="homeul" id="' + getListViewItemId(itemConfig) + '">' +
				'<table class="layout ' + getNotLastItemClass(itemConfig) + '"><tr>' +
					'<td class="label">' + itemConfig.title + '</td>' +
					'<td class="textvalue"><span id="' + getListViewItemValueId(itemConfig) + '">' + itemConfig.displayValue + '</span></td>' +
					'<td class="icon icon-contact"></td>' +
					'<td class="space"></td>' +
				'</tr></table>' +
			'</li>';
		return result;
	}

	/**
	* 文本输入
	*/
	function getListViewTextItem(itemConfig) {
		var result = '<li class="homeul" id="' + getListViewItemId(itemConfig) + '">' +
				'<table class="layout ' + getNotLastItemClass(itemConfig) + '"><tr>' +
					'<td class="label">' + itemConfig.title + '</td>' +
					'<td class="textvalue"><span id="' + getListViewItemValueId(itemConfig) + '">' + itemConfig.displayValue + '</span></td>' +
					'<td class="icon icon-input"></td>' +
					'<td class="space"></td>' +
				'</tr></table>' +
			'</li>';
		return result;
	}

	function getListViewTextAreaItem(itemConfig) {
		var result = '<li id="' + getListViewItemId(itemConfig) + '">' +
				'<table class="layout ' + getNotLastItemClass(itemConfig) + '"><tr>' +
					'<td class="label">' + itemConfig.title + '</td>' +
					'<td class="textvalue"><span id="' + getListViewItemValueId(itemConfig) + '">' + itemConfig.displayValue + '</span></td>' +
					'<td class="icon icon-input"></td>' +
					'<td class="space"></td>' +
				'</tr></table>' +
			'</li>';
		return result;
	}

	function getListViewSelectItem(itemConfig) {
		var result = '<li id="' + getListViewItemId(itemConfig) + '">' +
				'<table class="layout ' + getNotLastItemClass(itemConfig) + '"><tr>' +
					'<td class="label">' + itemConfig.title + '</td>' +
					'<td class="textvalue"><span id="' + getListViewItemValueId(itemConfig) + '">' + itemConfig.displayValue + '</span></td>' +
					'<td class="icon icon-select"></td>' +
					'<td class="space"></td>' +
				'</tr></table>' +
				'	<div data-role="popup" id="' + getPopupMenuId(itemConfig) + '" data-theme="d" data-overlay-theme="a">' +
				'		<ul id="'+ getPopupMenuULId(itemConfig) + '" data-role="listview" data-inset="true" style="min-width:210px;" data-theme="d">' +
				'		</ul>' +
				'	</div>' +
			'</li>';
		return result;
	}

	function getListViewDateItem(itemConfig) {
		var result = '<li id="' + getListViewItemId(itemConfig) + '">' +
				'<table class="layout ' + getNotLastItemClass(itemConfig) + '"><tr>' +
					'<td class="label">' + itemConfig.title + '</td>' +
					'<td class="textvalue"><span id="' + getListViewItemValueId(itemConfig) + '">' + itemConfig.displayValue + '</span></td>' +
					'<td class="icon icon-calendar"></td>' +
					'<td class="space"></td>' +
				'</tr></table>' +
			'</li>';
		return result;
	}

	function initItemsDefaultValue() {
		if (!dynApp.config || !dynApp.config.groups) return;
				
		var groups = dynApp.config.groups;
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				if (items[i].defaultValueFun)
				{
					items[i].value = items[i].defaultValueFun();
					items[i].displayValue = items[i].value;
				}

				items[i].isLastItem = i === (items.length - 1);
			}
		}
	}

	function getNotLastItemClass(itemConfig) {
		if (! itemConfig.isLastItem)
		{
			return "notlast";
		}

		return "";
	}

	/*******************************************************************
	* 初始化弹出菜单
	*******************************************************************/
	function initPopupMenus() {
		if (!dynApp.config.groups) return;

		var groups = dynApp.config.groups;
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				if (items[i].selectOptions && items[i].selectOptions.length > 0)
				{
					initPopupMenu(items[i]);
				}
			}
		}
	}

	function initPopupMenu(itemConfig) {
		var li = '<li data-role="divider" data-theme="e">' + itemConfig.title + '</li>';
		var entries = itemConfig.selectOptions;
		for (i = 0; i < entries.length; i++) {
			li += '<li data-icon="false">' + entries[i].text + "</li>";
		}
		var id = getPopupMenuULId(itemConfig)
		$("#" + id).append(li).promise().done(function () {
			//refresh list here
			//$(this).listview("refresh");
			//then add click event using delegation
			$(this).on("click", "li", function () {
				var text = $(this).text();
				if (itemConfig.title === text)
				{
					//选择的是标题，返回。
					return;
				}
				var value = getSelectOptionValue(itemConfig, text);
				$("#" + getPopupMenuId(itemConfig)).popup( "close" );
				setItemValue(itemConfig, value, text);

				if (itemConfig.selectEventHandler)
				{
					itemConfig.selectEventHandler(value);
				}
			});
		});

		$("#" + getListViewItemId(itemConfig)).on("click", function(e){
		        dynApp.currentPage = "popupMenu";
			    dynApp.currentPopupWindowId = "#" + getPopupMenuId(itemConfig);
				$("#" + getPopupMenuId(itemConfig)).popup("open");
			}
		);
	}

	function getSelectOptionValue(itemConfig, text) {
		if (itemConfig.selectOptions && itemConfig.selectOptions.length > 0)
		{
			var entries = itemConfig.selectOptions;
			for(var i = 0; i < entries.length; i++) {
				if (entries[i].text === text)
				{
					return entries[i].value;
				}
			}
		}

		return undefined;
	}


	/*******************************************************************
	* 初始化日期控件
	*******************************************************************/
	function initDateControls() {
		if (!dynApp.config.groups) return;

		var groups = dynApp.config.groups;
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;

			for (var i = 0; i < items.length; i++)
			{
				if ($.inArray(items[i].input, ['date', 'time', 'datetime']) >= 0)
				{
					setDateControl(items[i]);
				}
			}
		}
	}

	function setDateControl(itemConfig) {
		var opt = {
			preset: 'datetime', //日期
			theme: 'android-ics light', //皮肤样式
			display: 'modal', //显示方式
			mode: 'scroller', //日期选择模式
			lang: 'zh',
			dateFormat: 'yy-mm-dd', // 日期格式
			setText: '确定', //确认按钮名称
			cancelText: '取消',//取消按钮名籍我
			dateOrder: 'yymmdd', //面板中日期排列格式
			dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
			endYear:2030, //结束年份,

			onShow : function(html, valueText, inst) {
			    dynApp.currentPage = "mobiScroll";
			    dynApp.currentPopupWindowId = "#" + getListViewItemId(itemConfig);
			},

			onClose : function(valueText, btn, inst) {
				if (btn === 'set')
				{
					setItemValue(itemConfig, valueText, valueText);
				}
			}
		};
		opt.preset = itemConfig.input;

		$("#" + getListViewItemId(itemConfig)).mobiscroll(opt);
		$("#" + getListViewItemId(itemConfig)).mobiscroll('setValue', itemConfig.value, true);
	}

	/**
	* 初始化选择联系人的记录
	*/
    function initContacts() {
		if (!dynApp.config.groups) return;

        var groups = dynApp.config.groups;
        for(var j = 0; j < groups.length; j++) {
            var items = groups[j].items;
            for (var i = 0; i < items.length; i++)
            {
                if (items[i].input === 'contact')
                {
                    initContact(items[i]);
                }
            }
        }
    }

    function initContact(itemConfig) {
        $("#" + getListViewItemId(itemConfig)).on("click",
            function(e){
                HostApp.contactSelect("", itemConfig.value, "",
                    function(jsonArray){
                        var results = JSON.parse(jsonArray);
                        if (results && results.length > 0) {
                            var person = results[0];

                            setItemValue(itemConfig, person.id, person.name);
                        } else {
                            myalert("没有选择下一环节处理人。")
                        }
                    }
                );
            }
        );
    }

	/*******************************************************************
	* 初始化MobiScroll选择控件
	******************************************************************
	function initMobiScrollSelectControls() {
		var groups = dynApp.config.groups;
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;

			for (var i = 0; i < items.length; i++)
			{
				if (items[i].input === 'select2')
				{
					setMobiScrollSelectControl(items[i]);
				}
			}
		}
	}

	function setMobiScrollSelectControl(itemConfig) {
		var opt = {
			preset: 'select', //日期
			theme: 'android-ics light', //皮肤样式
			display: 'modal', //显示方式
			mode: 'scroller', //日期选择模式
			lang: 'zh',
			dateFormat: 'yy-mm-dd', // 日期格式
			setText: '确定', //确认按钮名称
			cancelText: '取消',//取消按钮名籍我
			dateOrder: 'yymmdd', //面板中日期排列格式
			dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
			endYear:2030, //结束年份,
			showInput: false,

			onClose : function(valueText, btn, inst) {
				if (btn === 'set')
				{
					setItemValue(itemConfig, valueText, valueText);
					dynApp.dataModified = true;
				}
			}
		};
		//opt.preset = itemConfig.input;

		$("#" + getListViewItemId(itemConfig)).mobiscroll(opt);
		$("#" + getListViewItemId(itemConfig)).mobiscroll('setValue', itemConfig.value, true);
	}
*/

	/*******************************************************************
	* 初始化input, textarea跳转
	*******************************************************************/
	function initInputControlls() {
		if (!dynApp.config.groups) return;

		var groups = dynApp.config.groups;
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				if (items[i].input === 'text')
				{
					initTextInputControl(items[i]);
				} else if (items[i].input === 'textarea')
				{
					initTextAreaControl(items[i]);
				}
			}
		}
	}

	function initTextInputControl(itemConfig) {
		$("#" + getListViewItemId(itemConfig)).on('click', function(e) {
			//$.mobile.pageContainer.pagecontainer("change", "#textInputPage");
			//$("body").pagecontainer("change", "#textInputPage");
			$("#textInput").val(itemConfig.value);
			$("#textInput").attr("placeholder", "请输入" + itemConfig.title);
			$.mobile.changePage('#textInputPage',{
				  transition: "slide",
				  reverse: false,
				  changeHash: true
				});
			dynApp.currentPage = "textInputPage";
			dynApp.currentItem = itemConfig;

			setInputControlMaxLength("#textInput", itemConfig.maxLength);
			initCharCount("#textInputCharCount", itemConfig.maxLength);
			$('#textInput').trigger('input');

			setTimeout(function(){
				document.getElementById("textInput").focus();
			}, 300);

			changeTitle(itemConfig.title);
		});
	}

	function initTextAreaControl(itemConfig) {
		$("#" + getListViewItemId(itemConfig)).on('click', function(e) {
			$("#textAreaInput").val(itemConfig.value);
			$("#textAreaInput").attr("placeholder", "请输入" + itemConfig.title);
			$.mobile.changePage('#textAreaInputPage',{
				  transition: "slide",
				  reverse: false,
				  changeHash: true
				});
			dynApp.currentPage = "textAreaInputPage";
			dynApp.currentItem = itemConfig;

			setInputControlMaxLength("#textAreaInput", itemConfig.maxLength);
			initCharCount("#textAreaInputCharCount", itemConfig.maxLength);
			$('#textAreaInput').trigger('input');

			setTimeout(function(){
				document.getElementById("textAreaInput").focus();
			}, 300);

			changeTitle(itemConfig.title);
		});
	}

	function setInputControlMaxLength(controlId, maxlength) {
		if (maxlength)
		{
			dynApp.maxInputLength = maxlength;
			$(controlId).attr("maxlength", maxlength);
		} else {
			dynApp.maxInputLength = 0;
			$(controlId).removeAttr("maxlength");
		}
	}

	function initCharCount(countId, maxlength) {
		if (maxlength)
		{
			$(countId).text("" + maxlength);
		} else {
			$(countId).text("");
		}
	}

	/**字数统计
	*/
	$(document).on('input','#textAreaInput',function(e){
		var limit = parseInt($(this).attr('maxlength'));
		if (limit)
		{
			var text = $(this).val();
			var chars = text.length;
			$("#textAreaInputCharCount").text("" + (limit - chars));
		}
	});

	$(document).on('input','#textInput',function(e){
		var limit = parseInt($(this).attr('maxlength'));
		if (limit)
		{
			var text = $(this).val();
			var chars = text.length;
			$("#textInputCharCount").text("" + (limit - chars));
		}
	});

	/*******************************************************************
	* 获取相关的DOM ID
	*******************************************************************/

	/**
	* 获取主列表，每个li的ID
	*/
	function getListViewItemId(itemConfig) {
		return 'mainlvli-' + itemConfig.name;
	}

	/**
	* 获取主列表，每个li的值对应的ID
	*/
	function getListViewItemValueId(itemConfig) {
		return 'mainlvli-value-' + itemConfig.name;
	}

	/**
	* 获取弹出菜单的ID
	*/
	function getPopupMenuId(itemConfig) {
		return 'popupmenu-' + itemConfig.name;
	}

	/**
	* 获取弹出菜单UL的ID
	*/
	function getPopupMenuULId(itemConfig) {
		return 'popupmenu-ul-' + itemConfig.name;
	}

	/*******************************************************************
	* 提交相关处理
	*******************************************************************/
	/**
	* 提交前的自定义校验。
	* 返回：false: 失败，true: 成功
	*/
	function customValid() {

	}

	function changeTitle(title) {
	    HostApp.notifyActivityEvent("createProcess", "changeTitle", title);
	}

	/*******************************************************************
	* 按钮事件处理
	*******************************************************************/
	function okBtnClick() {
		if (dynApp.currentPage === "home")
		{
			//校验并提交

			//校验
			var groups = dynApp.config.groups;
			for(var j = 0; j < groups.length; j++) {
				var items = groups[j].items;
				for (var i = 0; i < items.length; i++)
				{
					if (! isValid(items[i], items[i].value))
					{
						myalert(items[i].title + "不能为空。");
						return;
					}

					dynApp.result[items[i].name] = items[i].value;
				}
			}

			//提交
			if (dynApp.status === "create")
			{
				createFlow();
			} 
		} else if (dynApp.currentPage === "textInputPage")
		{
			//文本输入
			var value = $("#textInput").val();
			if (! isValid(dynApp.currentItem, value))
			{
				myalert(dynApp.currentItem.title + " 不允许为空。");
				return;
			}
			setItemValue(dynApp.currentItem, value, value);
			gotoHome();
		} else if (dynApp.currentPage === "textAreaInputPage") {
			//多文本输入
			var value = $("#textAreaInput").val();
			if (! isValid(dynApp.currentItem, value))
			{
				myalert(dynApp.currentItem.title + " 不允许为空。");
				return;
			}
			setItemValue(dynApp.currentItem, value, value);
			gotoHome();
		}
	}

	function gotoHome() {
		$.mobile.changePage('#home',{
			  transition: "slide",
			  reverse: true,
			  changeHash: false
			});
		dynApp.currentPage = "home";
		changeTitle("创建电子流");
	}

	function backBtnClick() {
	    if (dynApp.currentPage === "mobiScroll") {
            //关闭日历弹窗
            $(dynApp.currentPopupWindowId).mobiscroll('hide');
	    } else if (dynApp.currentPage === "popupMenu") {
            //关闭选择弹窗
            $(dynApp.currentPopupWindowId).popup("close");
	    } else if (dynApp.currentPage === "textInputPage" ||
	                dynApp.currentPage === "textAreaInputPage")
		{
			gotoHome();
		} else {
			HostApp.goBack();

			/*
			if (dynApp.dataModified)
			{
				myalert("有数据修改");
			} else {
				myalert("有数据修改2");
				HostApp.goBack();
			}
			*/
		}
	}

	function selectedContactsCallback(jsonArray) {
        var jsonArrayObj = JSON.parse(jsonArray);//由字符串转换为jsonArray

	}


    function isValid(itemConfig, value) {
		if (itemConfig.required)
		{
			if (value == undefined || value == null || value.length == 0)
			{
				return false;
			}
		}

		return true;
	}

	function myalert(msg) {
		//alert(msg);
		HostApp.toast(msg);
	}

	function isOKBtnVisible() {
		myalert("设置可视");
		HostApp.setResultValue("true");
	}

	function setItemValue(itemConfig, value, text) {
		itemConfig.value = value;
		itemConfig.displayValue = text;
		dynApp.dataModified = true;
		$("#" + getListViewItemValueId(itemConfig)).text(text);
	}


    $('#collection').append('<li>Line Item</li>');


    function initUL() {
		if (!dynApp.config || !dynApp.config.groups) return;

		var groups = dynApp.config.groups;
		var ulContent = "";
		for(var j = 0; j < groups.length; j++) {
			ulContent = ulContent + '<ul id="' + groups[j].id + '" class="homeul" data-role="listview" data-inset="true" data-theme="d"></ul>';
		}
        $("#flowContainer").append(ulContent);
	}

	function initAppSelector() {
	    var appConfig = {
        						name: 'appitem',
        						title: '工单类型',
        						dataType: 'string', //string, int, number, date, time, datetime,
        						value: '',
        						displayValue: '',
        						required: true,
        						input: 'select', //text, textarea, select, date, time, datetime, number
        						icon: '',
        						maxLength: 30,
								isLastItem: true,
        						selectOptions : dynApp.flows,
								selectEventHandler : function(v) {
									initFlow(v);
								}
        					};
        var lihtml = getListViewSelectItem(appConfig);
        $("#appselector").append(lihtml);
        initPopupMenu(appConfig);
	}

	function initFlow(selectedApp) {
		$("#flowContainer").empty();

		dynApp.currentFlow = selectedApp;

		if (dynApp.status === 'approving')
		{
			//审批配置
			dynApp.config = setApprovingFlow(dynApp.app[selectedApp], 'GD20141226-0001', '老王', '隔壁', '2014-12-25', '');
		} else {
			dynApp.config = dynApp.app[selectedApp];
		}
		initItemsDefaultValue();

		initUL();
		initListView();

		if (dynApp && dynApp.status === "create") {
			initPopupMenus();
			initDateControls();
			initInputControlls();
			initContacts();
			$("#flowContainer").trigger('create');
		}

		dynApp.currentPage = "home"; 
	}

	function setApprovingFlow(config, flowId, submitter, dept, submitdate, approveNote) {
		if ( !config.groups) return;

		var groupsJson = JSON.stringify(config.groups);
		var groups = JSON.parse(groupsJson);
		var result = {
			groups :[
				{
					id: 'approve_fixgroup',
					items: []
				},
				{
					id: 'approve_flowgroup',
					items: []
				}
			]
		};
		var resultItems = result.groups[0].items;

		resultItems.push({
						name: '_approve_id',
						title: '工单编号',
						dataType: 'string', //string, int, number, date, time, datetime,
						value: flowId,
						displayValue: flowId,
						required: true,
						input: 'text', //text, textarea, select, date, time, datetime, number
						icon: '',
						maxLength: 30			
		});
		resultItems.push({
						name: '_approve_submitter',
						title: '提交人',
						dataType: 'string', //string, int, number, date, time, datetime,
						value: submitter,
						displayValue: submitter,
						required: true,
						input: 'text', //text, textarea, select, date, time, datetime, number
						icon: '',
						maxLength: 50			
		});
		resultItems.push({
						name: '_approve_dept',
						title: '所属部门',
						dataType: 'string', //string, int, number, date, time, datetime,
						value: dept,
						displayValue: dept,
						required: true,
						input: 'text', //text, textarea, select, date, time, datetime, number
						icon: '',
						maxLength: 200			
		});
		resultItems.push({
						name: '_submit_time',
						title: '提交时间',
						dataType: 'string', //string, int, number, date, time, datetime,
						value: submitdate,
						displayValue: submitdate,
						required: true,
						input: 'text', //text, textarea, select, date, time, datetime, number
						icon: '',
						maxLength: 20			
		});

		resultItems = result.groups[1].items;

		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				if (items[i].visibleForApproving === false) {
					continue;
				}
				resultItems.push(JSON.parse(JSON.stringify(items[i])));
			}
		}

		return result;
	}

	function getFlowResult() {
		if (!dynApp.config || !dynApp.config.groups) return "";
				
		var groups = dynApp.config.groups;
		var result = {};
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				result[items[i].name] = {};
				result[items[i].name].value = items[i].value;
				result[items[i].name].displayValue = items[i].displayValue;
			}
		}

		return JSON.stringify(result);
	}
	
	function getItemResult(itemName) {
		if (!dynApp.config || !dynApp.config.groups) return "";
				
		var groups = dynApp.config.groups;
		var result = {};
		for(var j = 0; j < groups.length; j++) {
			var items = groups[j].items;
			for (var i = 0; i < items.length; i++)
			{
				if (items[i].name === itemName)
				{
					return {value: items[i].value, displayValue: items[i].displayValue} ;
				}
			}
		}

		return {value: "", displayValue: ""};
	}

	/**
	* 新建流程
	*/
	function createFlow() {
		//$("body").mask("");
		$.mobile.loading('show');

		var processName = "";
		for(var i = 0; i < dynApp.flows.length; i++) {
			if (dynApp.flows[i].value === dynApp.currentFlow)
			{
				processName = dynApp.flows[i].text;
				break;
			}
		}

		var title = dynApp.config.getTitle();
		var processValue= getFlowResult();
		var approverId = getItemResult('nextcontact').value;
		
		HostApp.createSimpleProcess(processName,  title, processValue, approverId,
			function (e) {
				myalert(e);
				//$("body").unmask("");
				//$.mobile.loading('hide');

				if (e)
				{
					var result = JSON.parse(e);
					//返回结果
					if (result.result === "0")
					{
						myalert(processName +"创建成功。");
						HostApp.goBack();
					} else {
						myalert(processName + "创建失败: " + result.message);
					}
				}
			}
		);
	}

	/**
	* 流程审批
	actType : 1-同意/下一步，2-驳回
	*/
	function approveFlow(actType, nextApproverId) {
		var approverNote = $("#_approve_note").val();

		HostApp.approveSimpleProcess(dynApp.pid, actType, approverNote, nextApproverId,
			function (e) {
				//返回结果
				if (e.result === "0")
				{
					myalert(processName +"审批完成。");
					HostApp.goBack();
				} else {
					myalert(processName + "审批失败: " + e.message);
				}
			}
		);
	}