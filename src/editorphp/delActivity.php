<?php
	function delItem($id, $key, $table) {	
		$query = "DELETE FROM ".$table." WHERE activity = '$id'"; 
		mysql_query($query) or die("Cannot perform Delete");
	}
?>