<?php  
	 include('getConfiguration.php');
	 $con = getConnection();
	 setOfflineSchema($con);
	 $postTable = getTable('poster');
	 $id = $_GET['id'];
     mysql_query("set names utf8");
	 $result=mysql_query("SELECT * FROM $postTable where activity = '$id'") 
	 or die("Cant perform query to get poster from database");  
	 $row=mysql_fetch_object($result);  
	 header( "Content-type: image/jpg"); 
	 echo $row->image."<br>";
?> 
