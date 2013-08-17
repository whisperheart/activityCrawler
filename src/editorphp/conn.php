<?php
function connect($dburl, $user, $pwd) {
	global $con;
	$con = mysql_connect($dburl,$user,$pwd);
	if (!$con) {
  		die('Could not connect: ' . mysql_error());
  		return false;
  	}; 	
  	return $con;
}

function schema($con, $schema) {
	if (!$con) {
		die('Could not connect: ' . mysql_error());
	}
	mysql_select_db($schema, $con);
}

//function disconnect($con) {
//	mysql_close($con);
//}
?>