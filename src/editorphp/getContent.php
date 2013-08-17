<?php
	function getContent($table, $id) {	
		$query = "SELECT * FROM ".$table." WHERE activity = '$id'"; 
    	 mysql_query("set names utf8");
		$result=mysql_query($query) or die("Cant perform Query"); 
		return mysql_fetch_object($result); 
	}
?>