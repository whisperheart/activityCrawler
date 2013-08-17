<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>test</title>
<?php echo "test" ?> 
</head>
<style type="text/css">
</style>

<body>
<?php  
	 $host = "127.0.0.1:3306";
	 $username = "root";
	 $password = "";
	 $DOCUMENT_ROOT = $_SERVER['DOCUMENT_ROOT']; 
	 $Picture = "$DOCUMENT_ROOT/2.jpg";
	 $db = "test";
	 if ($Picture != "none") {  
	 	
    	$PSize = filesize($Picture);  
    	$mysqlPicture = addslashes(fread(fopen($Picture, "r"), $PSize));  
    	mysql_connect($host,$username,$password) or die("Unable to connect to SQL server");  
    	mysql_select_db($db) or die("Unable to select database");  
    	$num = 6;
    	mysql_query("replace into newtable values ('$num', '$mysqlPicture')") or die("Cant Perform Query6"); 
 //   	mysql_query("SELECT * FROM newtable") or die("Cant Perform Query");
    	 echo "finish";
	 }else {  
     echo"You did not upload any picture";  
	}   
?> 
</body>
</html>