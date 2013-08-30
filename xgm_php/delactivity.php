<?php
	function delItem($act, $table) {	
		$id = $act[0];
		$title = $act[1];
		$query = "DELETE FROM $table WHERE activity = '$id'"; 
		$result = mysql_query($query);
		if (!$result) {
			failedlog("{deleteAct} ID<$id> $title", "The activity deletion fails"); 
			echo "<script> alert('删除活动：$id 失败') </script>";
		} 	else 
    		succeedlog("{deleteAct} ID<$id> $title","The activity deletion is successful");
    	return $result;
	}
?>