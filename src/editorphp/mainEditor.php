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
	include('saveParameter.php');
	
	include('conn.php');        // include连接数据库代码文件 include your code to connect to DB.
	include("setPages.php");
	include('delActivity.php');
	include('getConstCity.php');
	
	schema(connect($dburl, $user, $pwd), $offline);
	
	if (isset($_POST['delete'])) {
		$del = $_POST['del'];
		array_walk($del, delItem, $actTable);
	}
    //开始会话
    session_start();
	
    
	
    //$_SESSION['id']='123456';
    //删除一个变量
    //unset($_SESSION['id']);
    //清除所有会话
    //session_destroy();
    //在单击查询时执行；当翻页时不执行下面代码
    if(isset($_POST['txtSearch']))
    {
        $field = $_POST['field'];//title,user
        $value = $_POST['txtSearch'];
        $strWhere=" where $field like '%$value%'";
        $_SESSION['field']=$field;
        $_SESSION['value']=$value;
        $_SESSION['strWhere']=$strWhere;
    }
    
    if(isset($_SESSION['strWhere']))
    {
        $strWhere=$_SESSION['strWhere'];
    }    
    else
    {
        $strWhere="";
    }    
    $sql="";
    setPages($sql,$strWhere,$actTable,"mainEditor.php", 15,true);
//	   echo $sql;
    mysql_query("set names utf8");
    $result=mysql_query($sql);
?>
<div id="divSearch">
<form action="" method="post">
<img alt="查询新闻" src="images/img.gif">
请选择查询条件：
<!-- 字段 -->
<select id="field" name="field" style="width:100px;height:20px;" >
<option value="activity" selected="selected">活动id</option>
<option value="title">名称</option>
<option value="time">时间</option>
<option value="city">城市</option>
<option value="type">类型</option>
</select>
<input type="text" id="txtSearch" name="txtSearch" class="t">
<label>
<input type="submit" name="submit" style="width:100px;height:30px;" value="搜索">
</label>
</form>
</div>
<br>
<form action="" method="post">
<input type="submit" name="delete" value="删除活动" onclick="return confirm('确定需要删除这些活动吗？');" 
		style="width:100px;background:#ffd;height:20px;" >
<a href='detailEditor.php'> <input type="button" name="add" value="添加活动"
		style="width:100px;background:#ffd;height:20px;">
</a>
<br>  
<br>	
<table id="tableData">
    <tr>
       <td>&nbsp;活动id&nbsp;</td><td>&nbsp;名称&nbsp;</td><td>&nbsp;时间&nbsp;</td><td>&nbsp;城市&nbsp;</td>
       <td>&nbsp;地点&nbsp;</td><td>&nbsp;类型&nbsp;</td><td>&nbsp;详情&nbsp;</td><td>&nbsp;提交&nbsp;</td><td>&nbsp;最近&nbsp;</td><td>&nbsp;删除&nbsp;</td>
    </tr>
    <?php
    //在循环中添加显示数据
    	schema($con, $online);
        while($row = mysql_fetch_array($result))
        if (getCity($constTable, $row['city'])) 
        {      	 
        ?>
        <tr>
          <td>&nbsp;<a href="<?php echo($row['url'])?>"><?php echo($row['activity'])?></a>&nbsp;</td>
          <td>&nbsp;<?php echo($row['title'])?>&nbsp;</td>
		  <td>&nbsp;<?php echo($row['time'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['city'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['place'])?>&nbsp;</td>
          <td>&nbsp;<?php echo($row['type'])?>&nbsp;</td>
          <td>&nbsp;<a href="detailEditor.php?id=<?php echo($row['activity']) ?>">详情</a>&nbsp;</td>
          <td>&nbsp;<?php if ($row['edit'] == null) echo 0; else echo($row['edit'])?>&nbsp;</td>
          <td>&nbsp;<?php if ($row['recent'] == null) echo none; else echo($row['recent'])?>&nbsp;</td>
          <td>
              <input type="checkbox" name="del[]" value = <?php echo($row['activity'])?> />
         </td>
        </tr>
        <?php
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
    field.value='<?php echo($_SESSION['field'])?>';
    var txtSearch=document.getElementById("txtSearch");
    txtSearch.value='<?php echo($_SESSION['value'])?>';
  </script> 
      </td>
    </tr>
    </table>
    </form>
    </body>
</html>