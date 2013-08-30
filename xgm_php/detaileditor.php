<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<link href="test/css/jquery.selectbox.css" type="text/css" rel="stylesheet" />
<title>想干嘛后台数据编辑</title>
</head>
<script language="JavaScript" type="text/javascript">
function refresh() {
	confirm('重置后填写信息将丢失，确定要重置？');
	delAllCookie();
	window.location.reload();
	return true;
}
function addCookie(objName,objValue,objHours){//添加cookie
	var str = objName + "=" + escape(objValue);
//	if(objHours > 0){//为0时不设定过期时间，浏览器关闭时cookie自动消失
		var date = new Date();
		var ms = objHours*3600*1000;
		date.setTime(date.getTime() + ms);
		str += "; expires=" + date.toGMTString();
//	} 
	document.cookie = str;
}

function getCookie(objName){//获取指定名称的cookie的值
	var arrStr = document.cookie.split("; ");
	var cookie;
	for(var i = 0;i < arrStr.length;i ++){
		var temp = arrStr[i].split("=");
		if(temp[0] == objName) {
			 return unescape(temp[1]);
		}
	}
}

function delCookie(objName) {
	this.addCookie(objName, '', -1);
}

function delAllCookie() {
	var arrStr = document.cookie.split("; ");
	var items=new Array("id","title","url","time","starttime","endtime","place","type","img","content");
	for(var i = 0;i < arrStr.length;i ++){
		var temp = arrStr[i].split("=");
		for(var j = 0;j < items.length;j++)
			if (items[j] == temp[0]) 
				delCookie(temp[0]);
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
			element.value = cookieV;
    }
}

function selOption(){
	document.forms["selectform"].submit();
}
</script>
<style type="text/css">
<!--
body,table{font-size:12px;}
/*样式的类，只能用于div*/
div.pagination {
    padding: 3px;
    margin: 3px;
}
/*超级链接的样式*/
div.pagination a {
    /*上 右 下 左*/
    padding: 2px 5px 2px 5px;
    margin: 2px;
    border: 1px solid #AAAADD;
    /*文本的修饰为null,一般用于取消下划线*/
    text-decoration: none; /* no underline */
    /*字体的颜色*/
    color: #000099;
}
/*鼠标悬停时的样式*/
div.pagination a:hover, div.pagination a:active {
    border: 1px solid #000099;
    color: #000;
}
    /*当前页样式*/
div.pagination span.current {
    padding: 2px 5px 2px 5px;
    margin: 2px;
        border: 1px solid #000099;
        
        font-weight: bold;
        background-color: #000099;
        color: #FFF;
    }
    /*翻页按钮不可用时样式*/
    div.pagination span.disabled {
        padding: 2px 5px 2px 5px;
        margin: 2px;
        border: 1px solid #EEE;
    
        color: #DDD;
    }
    
    #tableData{
    border: solid 1px #ccc; 
    width:900px;    
    border-collapse:collapse;
    }
    
    #tableData tr td{
    line-height:30px;
    border: solid 1px #ccc; 
    text-align:center;
    }
-->
</style>

<body onload="get_('myform');">
<?php
	include("textParameter.php");
	include("getConfiguration.php");
	include('submit.php');
	include('upload_img.php');
	include('getConst.php');
	include('getConstCity.php');
	          // include连接数据库代码文件 include your code to connect to DB.	
	include('getContent.php');
	include('editlog.php');
	
	global $con;
	$con = getConnection();
	$actTable = getTable('acts');
	$postTable = getTable('poster');
	$constTable = getTable('const');
	$modTable = getTable('modify');
	$onlineTable = getTable('online');
	$teamTable = getTable('team');
	$imgPath = getImgPath();
	$uploadPath = getUploadPath();
	$defaultPoster = getDefaultPoster();
	
	$filepath = null;	
	if (isset($_POST['save']))
		save($con,$actTable, $postTable, $constTable);
	if (isset($_POST['submit'])) 
		submit($con, $actTable, $onlineTable, $modTable, $teamTable,$imgPath);	
	
	setOfflineSchema($con);
	
	$id = $_GET["id"];
	$row = getContent($actTable, $id);
	if(isset($_POST["psubmit"])) 
		$filepath = upload_img($uploadPath);
	if (!$row) {
		$row['id'] = "";
		$row['title'] = "想干嘛";
		$row['url'] = "http://";
		$row['time'] = "2000-1-1 00:00";
		$row['starttime'] = "2000-1-1 00:00";
		$row['endtime'] = "2000-1-1 00:00";
		$row['city'] = null;
		$row['district'] = 0;
		$row['place'] = "";
		$row['type'] = "";
		$row['typeslt'] = 0;
		$row['host'] = "";
		$row['img'] = "";
		$row['content'] = "";
		$row['edit'] = null;
		$row['recent'] = null;
		
		if ($filepath == null) 
			$filepath = $defaultPoster;
	} 
	$rowcity = $row['city'];
		
	setOnlineSchema($con);
	$cityCode = "<select id='city' name='city' style='width:100px;height:20px;' onchange=\"location='detailEditor.php?id=$id&city='+this.value\"> \n";
	$cityid = null;
	if ($rowcity == null) {
		$cities = getCity($constTable, "", '*');
		foreach($cities as $city){
			$select = "";
			if (isset($_GET['city'])) {
			if ($_GET['city'] == $city['id']) {
				$select = "selected='selected'";
				$cityid = $_GET['city'];
				$cityName = $city['name'];
			}
			} else {
				if ($cityid == null) {
					$select = "selected='selected'";
					$cityid = $city['id'];
					$cityName = $city['name'];			
				}
			}
			$value = array();
			$cname = $city['name'];
			$cid = $city['id'];
//			$value['name'] = $name;
//			$value['id'] = $cityid;
			$code = json_encode($value);
			//$seri = serialize($code);
			$str = "<option value= $cid $select>$cname</option> \n";
			$cityCode = $cityCode.$str; 
		} 
		$cityCode = $cityCode."</select>";
		$row['city'] = $cityName;
	} else {
		$cityid = getCity($constTable, "name='$rowcity'", 'id');
		$cityCode = "<input type='hidden' name='city' size='$textSize' maxlength='100' value='$cityid' />$rowcity";
	}
	$districts = getDistricts($constTable,$cityid);
	$districtCode = "";

	foreach($districts as $district){
		$select = "";
		$a = $district['id'];
		$b = $district['name'];
		if ($row['district'] == $a) $select = " selected=\"selected\"";
		
		$str = "<option value=$a $select>$b</option>\n";
		$districtCode = $districtCode.$str; 
	}
		
	$actTypes = getTypes($constTable);
	$typeCode = "";
	foreach($actTypes as $actType){
		$select = "";
		if ($row['typeslt'] == $actType['id']) $select = " selected=\"selected\"";
		$a = $actType['id'];
		$b = $actType['name'];
		$typeCode = $typeCode."<option value=$a $select>$b</option>\n";
	}

	if ($row['edit'] == null) 
		$edit = 0; 
	else 
		$edit = $row['edit'];	
		
	if ($filepath != null) {
		$imgsrcCode = $filepath;
		$imgpathCode = $filepath;
	}
	else {
		$imgsrcCode = "getImage.php?id=$id";
		$imgpathCode = $row['img'];
	}

?>				
<a>加*项不会提交到线上数据库</a>
<form name="myform" action="" method="post" enctype="multipart/form-data">
<table id="tableData">
    <tr>
       <td>&nbsp;海报&nbsp;</td>
       <td>	<img src= "<?php echo $imgsrcCode ?>"></td>
    </tr>
    <tr>
       <td> id </td>
       <td><a href="<?php echo($row['url'])?>"><?php echo($id)?></a><a><?php echo "(".($edit).")"?></a></td>
    </tr>
    <tr>
       <td>&nbsp;名称&nbsp;</td>
       <td><input type="text" name="title" size=<?php echo $textSize?>  maxlength="100" value = "<?php echo($row['title'])?>" onkeyup="add_(this);" /></td>
    </tr>
    <tr>
       <td>&nbsp;*链接&nbsp;</td>
       <td><input type="text" name="url" size=<?php echo $textSize?>  maxlength="100" value="<?php echo($row['url'])?>" onkeyup="add_(this);"/></td>
    </tr>
    <tr>
       <td>&nbsp;时间&nbsp;</td>
       <td><input type="text" name="time" size=<?php echo $textSize?>  maxlength="100" value="<?php echo($row['time'])?>" onkeyup="add_(this);" /></td>
    </tr>
    <tr>
       <td>&nbsp;开始时间&nbsp;</td>
       <td><input type="text" name="starttime" size=<?php echo $textSize?>  maxlength="100" value="<?php echo($row['starttime'])?>" onkeyup="add_(this);" /></td>
    </tr>
    <tr>
       <td>&nbsp;结束时间&nbsp;</td>
       <td><input type="text" name="endtime" size=<?php echo $textSize?>  maxlength="100" value="<?php echo($row['endtime'])?>" onkeyup="add_(this);" /></td>
    </tr>
    <tr>
       <td>&nbsp;城市&nbsp;</td>
       <td><?php echo $cityCode ?></td>
    </tr>
    <tr>
       <td>
	   		<input type="hidden" name="cityName" value="<?php echo($row['city'])?>" />
	   </td>
    </tr>
    <tr>
       <td>&nbsp;区域&nbsp;</td>
       <td>
       		<select id="district" name="district" style="width:100px;height:20px;">
       			<?php echo $districtCode ?>
       		</select>
       </td>
    </tr>
    <tr>
       <td>&nbsp;地点&nbsp;</td>
       <td><input type="text" name="place" size=<?php echo $textSize?> maxlength="100" value="<?php echo($row['place'])?>" onkeyup="add_(this);" /></td>
    </tr>
    <tr>
       <td>&nbsp;*类型&nbsp;</td>
       <td><input type="text" name="type" size=<?php echo $textSize?>  maxlength="100" value="<?php echo($row['type'])?>" onkeyup="add_(this);" /></td>
    </tr>
     <tr>
       <td>&nbsp;类别&nbsp;</td>
       <td>
       		<select id="typeslt" name="typeslt" style="width:100px;height:20px;" >
			<?php echo $typeCode ?>	
			</select>
       </td>
    </tr>
    <tr>
       <td>&nbsp;主办方&nbsp;</td>
       <td><input type="text" name="host" size=<?php echo $textSize?>  maxlength="100" value="<?php echo($row['host'])?>" onkeyup="add_(this);" /></td>
    </tr>   
    <tr>
       <td>&nbsp;*海报地址&nbsp;</td>
       <td>	<input type="text" name="img" size=<?php echo $textSize?> maxlength="100" value="<?php echo($imgpathCode)?>" onkeyup="add_(this);"/></td>
    </tr>
    <tr>
       <td>&nbsp;详情&nbsp; </td>
       <td><textarea name="content" cols=<?php echo $textareaCol?> rows=<?php echo $textareaRow?> onkeyup="add_(this);"> <?php echo $row['content']?> </textarea></td>
    </tr>    
    <tr>
       <td><input type="hidden" name="edit" value="<?php echo $edit?>" /></td>
    </tr>
</table>  
   <br> 
   <label>
   	<input type="button" name="reset" style="width:150px;background:#ffa;height:30px;" 
   		  value="重置修改" onclick="refresh()">
   	</label>
   	<label>
    <input type="submit" name="save" style="width:150px;background:#ffa;height:30px;" value="保存修改" 
   		  onclick="return confirm('确定要保存吗？')"/>
	</label>
	<label>
   	<input type="submit" name="submit" style="width:150px;background:#ffa;height:30px;" value="提交修改" 
   		  onclick="return confirm('确定要提交吗？')"/>
   	</label>
   <br>
   <br>
   	<label for="file">上传图片文件：</label> 
	<input type="file" name="file" style="width:150px;height:30px;" value="选择图片">
	<input type="submit" name="psubmit" style="width:100px;background:#ffd;height:20px;" value="上传图片">
	<input type="reset" name="preset" style="width:100px;background:#ffd;height:20px;" value="重置图片"/>
</form>

<a onclick="delAllCookie()" href="mainEditor.php"><?php echo("<<返回首页")?></a>
</body>
</html>