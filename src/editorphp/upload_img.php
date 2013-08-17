<?php
Function upload_img($uploaddir) {
	$message = "";
	$filepath = "";
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
 			$message = "上传文件有误:".$_FILES["file"]["error"]."\n";
 		}else{
 	 		$message = "上传文件名为：".$name."\n"
  				 	."上传文件类型为：".$type."\n"
  				 	."上传文件大小为：".($size/1024)."\n"
  				 	."上传到：".$tmp_name."\n";
  			if(file_exists("$name")){
   				$message .= "已经存在"."\n";
  			}else{  
     			if(!file_exists($uploaddir)){
     				$mkd = mkdir($uploaddir);
     				chmod($uploaddir,0777);
     				if ($mkd) 
     					$message .= "创建文件夹成功"."\n"; 
     				else
      					$message .= "创建文件夹失败"."\n";
     			}
     			
      			$filepath = $uploaddir.$name;
      			$message .= "文件保存在：".$filepath."\n";
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
	}
	$output = json_encode($message);
	echo "<script> alert($output) </script>";
	return $filepath;
}
?>