<?php
////////上传文件////////////
if(isset($_POST["sub"])){
	if(is_uploaded_file($_FILES['file']['tmp_name'])){
 //$upfile="../upload_file/".$_FILES["file"]["name"];
 $name=time();//定义变量，保存图片名，以防图片的名字相同
 echo $name;
 $name.=strrchr($_FILES["file"]["name"],".");//上传文件的名称
 echo $name;
 $num=rand(1,10);
 $type=$_FILES["file"]["type"];
 $size=$_FILES["file"]["size"];
 $tmp_name=$_FILES["file"]["tmp_name"];
 echo $tmp_name;
 if($_FILES["fiel"]["error"]>0){
  echo "上传文件有误:".$_FILES["file"]["error"]."<br/>";
 }else{
  echo "上传文件名为：".$name."<br>";
  echo "上传文件类型为：".$type."<br>";
  echo "上传文件大小为：".($size/1024)."<br>";
  echo "上传到：".$tmp_name."<br>";
  if(file_exists("$name")){
   echo "已经存在";
  }else{
  	$uploaddir="../upload_file/";  //相册封面所在路径
     if(!file_exists($uploaddir)){
     	$a = mkdir($uploaddir);
     	chmod($uploaddir,0777);
     	if ($a) echo "true"; else
      echo "false";
     }
      $filepath=$uploaddir.$name;
      echo "<br>".$filepath."<br>";
   if(move_uploaded_file($tmp_name,$filepath)){
    echo $name."上传成功";
   }else{
    echo $name."上传失败";
   }
  }
 }
} else {echo "失败";}
}

?>
<form method="post" action="php_upload.php" enctype="multipart/form-data">
<label for="file">上传图片文件：</label>
<input type="file" name="file">
<input type="submit" name="sub" value="upload">
<input type="reset" name="res" value="reset"/>
</form>