<?php
	function getContent($table, $id) {	
		$query = "SELECT * FROM ".$table." WHERE activity = '$id'"; 
    	 mysql_query("set names utf8");
		$result=mysql_query($query);
		if (!$result) {
			failedlog("ID<$id>","Fail to get activity content from table $table");
		echo ("<script> alert('获取活动内容失败！请检查数据库配置和表 $table 设置！')</script>");
		return false;  
		}
		return mysql_fetch_array($result); 
	}
?>