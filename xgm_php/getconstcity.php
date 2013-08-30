<?php
	Function getCity($table, $where, $col) { 
		if ($where != "") $where = "WHERE ".$where." and cate='city'";
		else $where = "WHERE cate='city'";
		$query = "SELECT $col FROM $table $where";
		mysql_query("set names utf8");
		$result = mysql_query($query) or die("getCity: Cannot Query!");
		if ($col == '*') {
			$rowArr = array();
			while($row = mysql_fetch_array($result)) 
				array_push($rowArr, $row);
			return $rowArr;			
		} else {
			$row = mysql_fetch_object($result);
			if (!$row) 
				Return false;
			else 
				Return $row->$col;	
		}
	} 
?>