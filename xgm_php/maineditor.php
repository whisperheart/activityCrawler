<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>想干嘛后台数据编辑</title>
</head>

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
    width:1500px;    
    border-collapse:collapse;
    }
    
    #tableData tr td{
    line-height:30px;
    border: solid 1px #ccc; 
    text-align:center;
    }
-->
</style>

<body>
<?php
    session_start();
	include('getConfiguration.php');        // include连接数据库代码文件 include your code to connect to DB.
	include("setPages.php");
	include('delActivity.php');
	include('getConstCity.php');
	include('editlog.php');
	
	$itemsPerPage = getDefaultValue('items-per-page');
	
	global $con;
	$con = getConnection();
	setOfflineSchema($con);
	$actTable = getTable('acts');
	$constTable = getTable('const');
	$defaultPoster = getDefaultPoster();
	$logo = getLogo();
	$defaultHost = getActivityDefault('host');
	
	if (isset($_POST['delete'])) {
		$strSeri = $_POST['del'];
		$delActs = array_map('unserialize', $strSeri);
		$count = 0;
		foreach($delActs as $delAct) {
			$result = delItem($delAct,$actTable);
			if ($result) $count++;
		}
		$total = count($delActs);
		$det = $total-$count;
		if ($det==0) echo "<script> alert('删除活动全部成功, 删除了 $count 个活动') </script>";
		else echo "<script> alert('$det 个活动删除失败，删除了 $count 个活动') </script>";
	}
	
    //$_SESSION['id']='123456';
    //删除一个变量
    //unset($_SESSION['id']);
	if(isset($_POST['clear'])) {
		unset($_SESSION);
		$_POST = array();
	};
    //在单击查询时执行；当翻页时不执行下面代码
	
    if(isset($_POST['txtSearch']))
    {
        $field = $_POST['field'];//title,user
        $value = $_POST['txtSearch'];
        $strWhere=" WHERE $field LIKE '%$value%'";
        $_SESSION['field'] = $field;
        $_SESSION['value'] = $value;
        $_SESSION['strWhere'] = $strWhere;
    }
    
    if(isset($_SESSION['strWhere']))
    {
        $strWhere = $_SESSION['strWhere'];
    }    
    else
    {
        $strWhere = "";
    }    
    
    $modcheck = "";
    $unmodcheck = "";
    $allcheck = "checked";
    $moditype = "all";
    
    if (isset($_GET['type'])) 
    	$moditype = $_GET['type'];
    else 
    if (isset($_SESSION['type'])) 
    	$moditype = $_SESSION['type'];

    	if ($strWhere != "") 
    	    $and = " and ";
    	 else 
    	    $and = "WHERE ";
    	if ($moditype == "modified") {
    		$strWhere .= $and."edit > 0";
    		$modcheck = "checked";
    		$allcheck = "";
    		$_SESSION['type'] = "modified";
    	} else 
    	if ($moditype == "unmodified") {
    		$strWhere .= $and."edit = 0";
    		$unmodcheck = "checked";
    		$allcheck = "";
    		$_SESSION['type'] = "unmodified";
    	} else 
    		$_SESSION['type'] = "all";

    $sql = "";
    setPages($sql,$strWhere,$actTable,"mainEditor.php",$itemsPerPage,true);
//	   echo $sql;
    mysql_query("set names utf8");
    $result = mysql_query($sql);
?>
<div id="divSearch">
<form action="mainEditor.php" method="post">
<img alt="查询新闻" src=<?php echo $logo?>>
请选择查询条件：
<!-- 字段 -->
<select id="field" name="field" style="width:100px;height:20px;" >
<option value="activity" selected="selected">活动id</option>
<option value="title">名称</option>
<option value="time">时间</option>
<option value="city">城市</option>
<option value="place">地点</option>
<option value="type">类型</option>
<option value="host">主办方</option>
<option value='recent'>最后修改</option>
</select>
<input type="text" id="txtSearch" name="txtSearch" class="t">
<input type="submit" name="submit" style="width:100px;height:30px;" value="搜索">
<input type="submit" name="clear" style="width:100px;height:30px;" value="刷新">
</form>
</div>
<br>
<form action="mainEditor.php" method="post">
<label>
<input type="submit" name="delete" value="删除活动" onclick="return confirm('确定需要删除这些活动吗？');" 
		style="width:100px;background:#ffd;height:20px;" >
</label>
<label>
<a href='detailEditor.php?id=Create-<?php echo time()?>'> <input type="button" name="add" value="添加活动"
		style="width:100px;background:#ffd;height:20px;">
</a>
</label>
<label for="unmodified">
 <input type="radio" id="unmodified" name="modfilter" <?php echo $unmodcheck?>
 		onclick="location='mainEditor.php?type=unmodified'"/>
 未提交活动
</label>
<label for="modified">
 <input type="radio" id="modified" name="modfilter" <?php echo $modcheck?>
 		onclick="location='mainEditor.php?type=modified'"/>
 已提交活动
</label>
<label for="all">
 <input type="radio" id = "all" name="modfilter" <?php echo $allcheck?>
 		onclick="location='mainEditor.php?type=all'"/>
 全部活动
</label>
<br>  
<br>	
<table id="tableData">
    <tr>
       <td>&nbsp;活动id&nbsp;</td><td>&nbsp;名称&nbsp;</td><td>&nbsp;时间&nbsp;</td><td>&nbsp;城市&nbsp;</td>
       <td>&nbsp;地点&nbsp;</td><td>&nbsp;类型&nbsp;</td><td>&nbsp;主办方&nbsp;</td><td>&nbsp;详情&nbsp;</td><td>&nbsp;提交&nbsp;</td><td>&nbsp;最近&nbsp;</td><td>&nbsp;删除&nbsp;</td>
    </tr>
    <?php
    //在循环中添加显示数据
    	setOnlineSchema($con);
        while($row = mysql_fetch_array($result)) {       	
        	$city = $row['city'];
        	if (getCity($constTable, "name='$city'", 'id')) {    
        		if (($row['host'] == "") || $row['host'] == null) $row['host'] = $defaultHost; 	 
        ?>
        <tr>
          <td>&nbsp;<a href="<?php echo($row['url'])?>"><?php echo($row['activity'])?></a>&nbsp;</td>
          <td>&nbsp;<?php echo($row['title'])?>&nbsp;</td>
		  <td>&nbsp;<?php echo($row['time'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['city'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['place'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['type'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['host'])?>&nbsp;</td>
          <td>&nbsp;<a href="detailEditor.php?id=<?php echo($row['activity']) ?>">详情</a>&nbsp;</td>
          <td>&nbsp;<?php if ($row['edit'] == null) echo 0; else echo($row['edit'])?>&nbsp;</td>
          <td>&nbsp;<?php if ($row['recent'] == null) echo none; else echo($row['recent'])?>&nbsp;</td>
          <td>
              <input type="checkbox" name="del[]" maxlength="255" value = <?php 
              												$arr = array();
              												$arr[0] = $row['activity'];
              												$arr[1] = $row['title'];
              												$seri = serialize($arr);
              												echo "'$seri'"
              											   ?> />
         </td>
        </tr>
        <?php
        }
        }
    ?>
    <tr>
      <td colspan="3">
      共<?php echo($total_pages)?>行||共<?php echo($lastpage)?>页   
      <!-- 翻页的按钮 -->
      <?php echo($pagination)?>
  <script type="text/javascript">
    var webUrl=document.getElementById("webUrl");
    webUrl.selectedIndex=<?php echo($page-1)?>;
    //反添数据
    var field=document.getElementById("field");
    if (field != null) 
    field.value='<?php echo($_SESSION['field'])?>';
    var txtSearch=document.getElementById("txtSearch");
    if (txtSearch != null)
    txtSearch.value='<?php echo($_SESSION['value'])?>';
    
  </script> 
      </td>
    </tr>
    </table>
    </form>
    </body>
</html>