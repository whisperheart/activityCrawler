<?php
	Function getDistricts($table, $cityid) { 
    	$queryDistricts = "SELECT id,name FROM $table where parentid = $cityid";
    	mysql_query("set names utf8");
    	$result = mysql_query($queryDistricts);
    	if (!$result) {
    		failedlog("ID<cityid> Get Districts","Fail to get districs from $table");
    		die("fails");
    	}
//    	$district = mysql_fetch_array($result) or die("fails");
		$districts = array();
		$item = array();
    	while($row = mysql_fetch_array($result))
        {
        	$item['name'] = $row['name'];
        	$item['id'] = $row['id'];
        	$districts[] = $item;
        }
		Return $districts;
	}  

	Function getTypes($table) {
		$query = "SELECT id,name FROM $table where cate = \"category\"";
	    mysql_query("set names utf8");
		$result = mysql_query($query);
		if (!$result) {
			failedlog("Get Types","Fail to get types from $table");
			die("fails");
		}
    	$types = array(); 
    	$item = array();
    	while($row = mysql_fetch_array($result))
        {
        	
        	$item['name'] = $row['name'];
        	$item['id'] = $row['id'];
    		$types[] = $item;
        }
        
		Return $types;
	}
//	echo print_r(getDistricts("xgm_commlist",1))."<br>";
//	echo print_r(getTypes("xgm_commlist"))."<br>";
?>