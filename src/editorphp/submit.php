<?php
 function getImage($url,$filename='',$pictype){
    if($url==''){return false;}
    if($filename==''){
        $ext=strrchr($url,'.');
        if($ext!='.gif' && $ext!='.jpg'){return false;}
        $filename=time().$ext;
    }
    //文件保存路径 
    if($pictype == 0){
  		$ch=curl_init();
  		$timeout=5;
  		curl_setopt($ch,CURLOPT_URL,$url);
  		curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
  		curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,$timeout);
  		$img=curl_exec($ch);
  		curl_close($ch);
    }else{
     	ob_start(); 
     	readfile($url);
     	$img=ob_get_contents(); 
     	ob_end_clean(); 
    }
    $size=strlen($img);
    //文件大小 
    $fp2=@fopen($filename,'a');
    fwrite($fp2,$img);
    fclose($fp2);
    return $img;
}

include("saveParameter.php");
Function getID($table) {
	$sql = "SELECT * FROM $table";
	$result = mysql_query($sql);
	return mysql_num_rows($result);
}

Function submit($con, $actTable,$subTable,$modTable,$imgPath) {
	include("saveParameter.php");
	schema($con, $online);
	$id = getID($subTable) + 1;
	$title = $_POST['title'];
	$url = $_POST['url'];
	$time = $_POST['time'];
	$starttimeStr = $_POST['starttime'];
	$endtimeStr = $_POST['endtime'];
	$city = $_POST['city'];
	$district = $_POST['district'];
	$place = $_POST['place'];
	$type = $_POST['typeslt'];
	$img = $_POST['img'];
	$content = $_POST['content'];
	if ($_POST['edit'] == null) $edit = 1; else
	$edit = $_POST['edit'] + 1;
	$content = str_replace("\"", "\\\"", $content);

	$starttime = strtotime($starttimeStr);
	$endtime = strtotime($endtimeStr);
	
	//提交活动海报
	if (count(parse_url($img)) <= 1) $pictype = 1; else $pictype = 0;
	$imgName = $imgPath.$id.".jpg";
    getImage($img, $imgName, $pictype); 
	
    //提交活动内容修改
    date_default_timezone_set('PRC');
	$nowtime = time();
	$a = "VALUES ($id,'$title',$type,$city,$district,'$place','$imgName',1,2,$nowtime,1,'$content',$starttime,$endtime,0)";
	$insert = "INSERT INTO $subTable $a";
    mysql_query("set names utf8");
    $insert = json_encode($insert);
	mysql_query(json_decode($insert)) or die("<script> alert('提交失败！请重新提交！')</script>");  
	
	$raw_id = $_GET['id'];
	
	$modsql = "INSERT INTO $modTable VALUES ($id,'$raw_id',2)";
	mysql_query($modsql) or die("<script> alert('提交失败！请重新提交！')</script>");  
		
	schema($con, $offline);
	$update = "UPDATE $actTable SET edit = $edit, recent = 'anonymous' WHERE activity ='$raw_id'";
	mysql_query("set names utf8");
	mysql_query($update) or die("<script> alert('提交失败！请重新提交！')</script>");  
		
	echo "<script> alert(\"第 $edit 次修改 $raw_id 提交成功\") </script>";
//	echo "<script> alert(\" $id \") </script>";
}

Function save($con, $actTable, $postTable) {
	schema($con, $offline);
	$id = $_GET['id'];
	$title = $_POST['title'];
	$url = $_POST['url'];
	$time = $_POST['time'];
	$place = $_POST['place'];
	$type = $_POST['type'];
	$img = $_POST['img'];
	$content = $_POST['content'];
	$content = str_replace("\"", "\\\"", $content);
	
		//提交海报修改
	if (count(parse_url($img)) <= 1) $pictype = 1; else $pictype = 0;
    $mysqlPicture = addslashes(getImage($img, '', $pictype));  
    $result = "UPDATE $postTable SET image= '$mysqlPicture' WHERE activity = '$id'";
    echo "<script> alert('$result')</script>";
    mysql_query($result) or die("<script> alert('图片保存失败！')</script>"); 
    
        //提交活动内容修改
	$update = "UPDATE $actTable SET url='$url',title='$title',time='$time',place='$place',
									type='$type',content='$content' WHERE activity='$id'";
    mysql_query("set names utf8");
    $update = json_encode($update);
	mysql_query(json_decode($update)) or die("<script> alert('保存失败！请重新保存！')</script>");
	
	echo "<script> alert(\"修改保存成功\") </script>";  
}
?>