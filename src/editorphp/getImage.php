<?php  
	 include("conn.php");
	 include("saveParameter.php");
	 schema(connect($dburl, $user, $pwd),$offline);
	 $id = $_GET["id"];
     mysql_query("set names utf8");
	 $result=mysql_query("SELECT * FROM $postTable where activity = '$id'") or die("Cant perform Query0");  
	 $row=mysql_fetch_object($result);  
	 header( "Content-type: image/jpg"); 
	 echo $row->image."<br>";
?> 
