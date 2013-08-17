<?php
	Function getCity($table, $city) { 
		$query = "SELECT id FROM $table WHERE cate=\"city\" and name =\"$city\"";
		mysql_query("set names utf8");
		$result = mysql_query($query);
		$row = mysql_fetch_array($result);
		if (!$row) Return false;
		else Return $row['id'];
	} 
?>