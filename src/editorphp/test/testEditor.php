<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<script language="JavaScript" type="text/javascript">
function addCookie(objName,objValue,objHours){//添加cookie
var str = objName + "=" + escape(objValue);
//if(objHours > 0){//为0时不设定过期时间，浏览器关闭时cookie自动消失
var date = new Date();
var ms = objHours*3600*1000;
date.setTime(date.getTime() + ms);
str += "; expires=" + date.toGMTString();
//}
document.cookie = str;
}

function getCookie(objName){//获取指定名称的cookie的值
var arrStr = document.cookie.split("; ");
for(var i = 0;i < arrStr.length;i ++){
var temp = arrStr[i].split("=");
if(temp[0] == objName) return unescape(temp[1]);
}
}

function add_(obj){
	addCookie(obj.name,obj.value,10000);
}

function get_(_form){
	for(i=0;i < document.forms[_form].length;i++){
         element = document.forms[_form][i];
		 cookieV = getCookie(element.name)
		 if (typeof(cookieV) != 'undefined')
		 {
			element.value = cookieV;
		 }
    }
}

function delCookie(objName) {
	this.addCookie(objName, '', -1);
}

function delAllCookie() {
	var arrStr = document.cookie.split("; ");
	var items=new Array("inputname1","inputname2","inputname3","inputname4");
	for(var i = 0;i < arrStr.length;i ++){
		var temp = arrStr[i].split("=");
		for(var j = 0;j < items.length;j++)
			if (items[j] == temp[0]) {
			alert(temp[0]+temp[1]);
				delCookie(temp[0]);
			}
	}	
}
</script>

</head>
<body onload="get_('myform');">
<form name="myform">
<p>输入框1：<input type="text" name="inputname1" value = "input1" onkeyup="add_(this);" /></p>
<p>输入框2：<input type="text" name="inputname2" value = "input2" onkeyup="add_(this);" /></p>
<p>输入框3：<input type="text" name="inputname3" value = "input3" onkeyup="add_(this);" /></p>
<p>输入框4：<input type="text" name="inputname4" value = "input4" onkeyup="add_(this);" /></p>
<p>清除cookie：<input type="submit" name="clear" onclick="delAllCookie();" /></p>
</form>
</body>
</html>