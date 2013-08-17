<?php 
/*
*功能：php多种方式完美实现下载远程图片保存到本地
*参数：文件url,保存文件名称，使用的下载方式
*当保存文件名称为空时则使用远程文件原来的名称
*/
function getImage($url,$filename='',$type=0){
    if($url==''){return false;}
    if($filename==''){
        $ext=strrchr($url,'.');
        if($ext!='.gif' && $ext!='.jpg'){return false;}
        $filename=time().$ext;
    }
    //文件保存路径 
    if($type){
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
    return $filename;
}
?>
	<form method="post" action="" enctype="multipart/form-data">
	<label for="file">上传图片文件：</label>
	<input type="file" name="file" style="width:150px;height:30px;" value="选择图片">
	<input type="submit" name="psubmit" style="width:100px;background:#ffd;height:20px;" value="上传图片">
	<input type="reset" name="preset" style="width:100px;background:#ffd;height:20px;" value="重置图片"/>
	</form>
	
<?php 
$ok = false;
if(isset($_POST["psubmit"])){
	if(is_uploaded_file($_FILES['file']['tmp_name'])){
 //$upfile="../upload_file/".$_FILES["file"]["name"];
 $name=time();//定义变量，保存图片名，以防图片的名字相同
 $name.=strrchr($_FILES["file"]["name"],".");//上传文件的名称
 $num=rand(1,10);
 $type=$_FILES["file"]["type"];
 $size=$_FILES["file"]["size"];
 $tmp_name=$_FILES["file"]["tmp_name"];
 if($_FILES["fiel"]["error"]>0){
  echo "上传文件有误:".$_FILES["file"]["error"]."<br/>";
 }else{
  echo "上传文件名为：".$name."<br>";
  echo "上传文件类型为：".$type."<br>";
  echo "上传文件大小为：".($size/1024)."<br>";
  echo "上传到：".$tmp_name."<br>";
  if(file_exists("$name")){
   echo "已经存在"."<br>";
  }else{
  	$uploaddir="../upload_file/";  
     if(!file_exists($uploaddir)){
     	$mkd = mkdir($uploaddir);
     	chmod($uploaddir,0777);
     	if ($mkd) echo "创建文件夹成功"."<br>"; else
      echo "创建文件夹失败"."<br>";
     }
      $filepath=$uploaddir.$name;
      echo $filepath."<br>";
   if(move_uploaded_file($tmp_name,$filepath)){
    echo $name."上传成功"."<br>";
    $ok = true;
   }else{
    echo $name."上传失败"."<br>";
   }
  }
 }
} else {echo "无效的图片文件"."<br>";}
}
if (!$ok) 
$filepath = $_POST["img"];
?>
	<form method="post" action="" enctype="multipart/form-data">
<table id="tableData">
	 <tr>
       <td>&nbsp;海报&nbsp;</td>
       <td>	<input type="text" name="img" size="150" maxlength="100" value="<?php echo($filepath)?>" /></td>
    </tr>
    <tr>
       <td>&nbsp;海报&nbsp;</td>
       <td>	<img src= "<?php echo getImage($filepath,$filename='',$type=0) ?>"></td>
    </tr>
    </table>
    	<input type="submit" name="提交" style="width:100px;background:#ffd;height:20px;" value="上传图片">
    </form>
 