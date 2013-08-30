<?php
Function upload_img($uploaddir) {
	$message = "";
	$filepath = "";
	$reason = "";
	if (($uploaddir == "") || ($uploaddir == null)) 
     				$uploaddir = "../upload_file/";
	
	if(is_uploaded_file($_FILES['file']['tmp_name'])){
 	//$upfile="../upload_file/".$_FILES["file"]["name"];
 		$name=time();//定义变量，保存图片名，以防图片的名字相同
 		$name.=strrchr($_FILES["file"]["name"],".");//上传文件的名称
		$num=rand(1,10);
		$type=$_FILES["file"]["type"];
 		$size=$_FILES["file"]["size"];
 		$tmp_name=$_FILES["file"]["tmp_name"];
 		if($_FILES["file"]["error"]>0){
 			$reason =  "wrong file:".$_FILES["file"]["error"];
 			$message = "上传文件有误: \n";		 
 		}else{
 	 		$message = "上传文件名为：".$name."\n"
  				 	."上传文件类型为：".$type."\n"
  				 	."上传文件大小为：".($size/1024)."\n"
  				 	."上传到：".$tmp_name."\n";
  			$reason =  "filename:".$name.";type:".$type.";size:".($size/1024).";temp:".$tmp_name;
  			if(file_exists("$name")){
   				$message .= "已经存在"."\n";
   				$reason .= ";file already exist";
  			}else{  
     			if(!file_exists($uploaddir)){
     				$mkd = mkdir($uploaddir);
     				chmod($uploaddir,0777);
     				if ($mkd) {
     					$message .= "创建文件夹成功"."\n"; 
     					$reason .= ";folder is created";
     				} else {
      					$message .= "创建文件夹失败"."\n";
      					$reason .= ";folder cannot be created";
     				}
     			}
     			
      			$filepath = $uploaddir.$name;
      			$message .= "文件保存在：".$filepath."\n";
      			$reason .= ";file saved in:".$filepath;
   				if(move_uploaded_file($tmp_name,$filepath)){
    				$message = $name."上传成功"."\n\n".$message;
    				$ok = true;
   				}  else {
    				$message .= $name."上传失败"."\n\n".$message;
   				}
  				}
 			}
	} else {
		$message .= "无效的图片文件"."\n";
		$reason .= ";invalid image";
	}
	$output = json_encode($message);
	echo "<script> alert($output) </script>";
	$id = $_GET['id'];
	$title = $_POST['title'];
	if (!$ok) 
    	failedlog("{UploadImage} ID<$id> $title", "The upload of image fails: $reason");
   	else 
    	succeedlog("{UploadImage} ID<$id> $title","The upload of image is successful: $reason");
	return $filepath;
}
?>