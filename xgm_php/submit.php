<?php
 function getImage($url,$filename,$pictype){
    if($url==''){return -2;}
//    if($filename==''){
 //       $ext=strrchr($url,'.');
 //       if($ext!='.gif' && $ext!='.jpg' && $ext!='.png'){return -2;}
 //       $filename=time().$ext;
 //   }
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
    if ($filename != null) {
    	$fp2=@fopen($filename,'a');  
    	if (!$fp2) return -1;
    	fwrite($fp2,$img);
    	fclose($fp2);
    }
    if (($img == null) || ($size == 0)) return -2;
    else return $img;
}

Function getID($table, $id) {
	$sql = "SELECT MAX($id) FROM $table";
//	echo "<script> alert('$sql')</script>";
	$result = mysql_query($sql);
	if (!$result) return false;
	$row = mysql_fetch_row($result);
	if ($row == null) return 0;
	if ($row[0] == null) return 0;
	return $row[0];
}

function addTeam($table, $id, $title, $simple, $imagePath, $limit) {
	$count = getID($table, 'tid');
	if (!$count) return -1;
	$tid = $count + 1;
	$aid = $id;
	$creator = 2;
	$subject = "\"".$title."\"小队";
	$content = $simple;
	$image = $imagePath;
	$createtime = time();
	$limitpeople = $limit;
	$isauthorize = 0;
	$isprivate = 0;
	$a = "(tid,aid,creator,subject,content,image,create_time,limitpeople,isauthorize,isprivate)";
	$b = "VALUES ($tid,$aid,$creator,'$subject','$content','$image',$createtime,$limitpeople,$isauthorize,$isprivate)";
	$insert = "INSERT INTO $table $a $b";
	mysql_query("set names utf8");
//	if (json_encode($sql) == null) $sql = iconv('gbk','utf8',$sql);
	$insert = json_encode($insert);
	$result = mysql_query(json_decode($insert));
	if (!$result)
		return -2;
	else 
		return $tid;
}

function isSubmitLegal($key, $var, $type) {
	if ($var == null) return false;
	if ($type == 0) {
	switch ($key) {
		case 'id': 
		case '类型':
		case '城市':
		case '区域':
		case '开始时间':
		case '结束时间':
			$result = intval($var, 10); 
			return ($result >= 0);
			break;
		default:
			return ($var != null);
	} 
	} else 
		switch ($key) {
		case '类型':
		case '区域':
		case '开始时间':
		case '结束时间':
			$result = intval($var, 10); 
			return ($result >= 0);
			break;
		default:
			return ($var != null);
	}
}

Function submit($con,$actTable,$subTable,$modTable,$teamTable,$imgPath) {

	setOnlineSchema($con);	
	$contents = array();
	$raw_id = $_GET['id']; 
	$getid = getID($subTable,'aid');
	if ($getid)
		$id = $getid + 1;
	else 
		$id = null;
	$title = $_POST['title'];
	$url = $_POST['url'];
	$time = $_POST['time'];
	$starttimeStr = $_POST['starttime'];
	$endtimeStr = $_POST['endtime'];
	$city = $_POST['city'];
//	$code = unserialize($seri);
//	$arr = json_decode($code);	
//	$city = $arr->id;
	$district = $_POST['district'];
	$place = $_POST['place'];
	$type = $_POST['typeslt'];
	$host = $_POST['host'];
	$img = $_POST['img'];
	$content = $_POST['content'];
	if ($_POST['edit'] == null) $edit = 1; 
	else $edit = $_POST['edit'] + 1;
	$content = str_replace("\"", "\\\"", $content);
	
	$starttime = strtotime($starttimeStr);
	$endtime = strtotime($endtimeStr);
	$imgName = $imgPath.$id.".jpg";
	
	//检查填写内容是否合法
	if ($id == null) {
		failedlog("{ActSubmit} ID<$raw_id> $title","Fail to get the submission id from table $subTable");
		echo "<script> alert('线上活动ID获取失败！请检查数据库配置和表 $subTable 设置！')</script>";  
		$ok = false;
	}
	
	$contents['标题'] = $title;
	$contents['类型'] = $type;
	$contents['城市'] = $city;
	$contents['区域'] = $district;
	$contents['地点'] = $place;
	$contents['海报地址'] = $imgName;
	$contents['详情'] = $content;
	$contents['开始时间'] = $starttime;
	$contents['结束时间'] = $endtime;
	
	$ok = true;
	foreach($contents as $key=>$value) {
		if (!isSubmitLegal($key, $value, 0)) {
			failedlog("{ActSubmit} ID<$raw_id> $title","The value of activity:$key is wrong");
			echo "<script> alert('$key 填写错误！请重新填写！')</script>";  
			$ok = false; 
			break;
		}
	}
	
	if ($ok) {
	if ($endtime < $starttime) {
		failedlog("{ActSubmit} ID<$raw_id> $title","The start time $starttimeStr is later than the end time $endtimeStr");
			echo "<script> alert('开始时间 $starttimeStr 晚于结束时间 $endtimeStr ！请重新填写！')</script>";  
			$ok = false; 
	}
	
	//提交活动内容修改    
	if ($ok) {	
    date_default_timezone_set('PRC');
	$nowtime = time();
	$a = "VALUES ($id,'$title',$type,$city,$district,'$place','$imgName','$host',1,2,$nowtime,1,'$content',$starttime,$endtime,0)";
	$b = "(aid,subject,cate,city,district,geo,coverimage,host,isprivate,creator,create_time,issquare,content,start_time,end_time,isdelete)";
	$insert0 = "INSERT INTO $subTable $b $a";
    mysql_query("set names utf8");
    $insert = json_encode($insert0);
	$result = mysql_query(json_decode($insert));
	if (!$result) {
		failedlog("{ActSubmit} ID<$raw_id> $title",
				  "The submission of activity content fails, table $subTable");
		echo "<script> alert('活动内容提交失败！\n 请检查数据库配置和表 $subTable 设置，并重新提交！')</script>";  
		$ok = false;
	} else  
	succeedlog("{ActSubmit} ID<$raw_id> $title","The submission of activity text content is successful.");
	
	//提交活动海报
	if ($ok) {
	if (count(parse_url($img)) <= 1) $pictype = 1; else $pictype = 0;
    $imgresult = getImage($img, $imgName, $pictype); 
	if ($imgresult == -1) {
		failedlog("{ActSubmit} ID<$raw_id> $title","Cannot create the image file:$imgName.");
		echo ("<script> alert('创建图片文件失败！请检查图片保存路径 $imgName 和用户权限！')</script>");
		$ok = false;
	} else 
	if ($imgresult == -2) {
		failedlog("{ActSubmit} ID<$raw_id> $title","Fail to download the image:$imgName.");
		echo ("<script> alert('图片文件下载失败！请检查图片下载路径 $img 和网络连接！')</script>");
		$ok = false;
	} else
    	succeedlog("{ActSubmit} ID<$raw_id> $title","The submission of image:$imgName is successful.");
	
	//提交修改记录
	if ($ok) {
	$modsql = "INSERT INTO $modTable VALUES ($id,'$raw_id',2)";
	$result=mysql_query($modsql);
	if (!$result) {
		failedlog("{ActSubmit} ID<$raw_id> $title",
				  "The submission of modification:$id<=>$raw_id fails, table $modTable");
		echo ("<script> alert('修改记录提交失败！请检查数据库配置和表 $modTable 设置，并重新提交！')</script>");  
		$ok = false;
	};
	
	//更新编辑记录
	if ($ok) {
	setOfflineSchema($con);
	$update = "UPDATE $actTable SET edit = $edit, recent = 'anonymous' WHERE activity ='$raw_id'";
	mysql_query("set names utf8");
	$result = mysql_query($update);
	if (!$result) {
		failedlog("{ActSubmit} ID<$raw_id> $title",
		"The submission of edit:$edit and recent:$rencent fails, table $actTable");
		echo "<script> alert('编辑记录提交失败！请检查数据库配置和表 $actTable 设置，并重新提交！')</script>";
		$ok = false;
	} else 
	succeedlog("{*ActSubmit} ID<$raw_id> $title","The $edit submission is successful.");
	
	//全部提交成功
	if ($ok) {
		echo "<script> alert('第 $edit 次修改 $raw_id 提交成功') </script>";
	
	//创建对应小队
	$simple = mb_substr($content, 0, 100, 'utf-8');
	
	setOnlineSchema($con);
	$tid = addTeam($teamTable, $id, $title, $simple, $img, 30);
	if ($tid < 0) {
		if ($tid == -1) $reason = "generating team ID fails.";
		else $reason = "inserting team information fails, table $teamTable";
		failedlog("{TeamSubmit} ID<$raw_id> $title","The team cannot be created: $reason");
		echo "<script> alert('创建小队失败！请检查数据库配置和表 $teamTable 设置，并重新提交！')</script>";
		$ok = false;
	} else {
		succeedlog("{TeamSubmit} ID<$raw_id> $title","The team<$tid> creation is successful.");
		echo "<script> alert('创建小队:$tid 成功！') </script>";
	}
	}
	}
	}
	}
	}
	}
}

Function save($con, $actTable, $postTable,$constTable) {
	setOfflineSchema($con);
	$id = $_GET['id'];
	$title = $_POST['title'];
	$url = $_POST['url'];
	$time = $_POST['time'];
	$starttimeStr = $_POST['starttime'];
	$endtimeStr = $_POST['endtime'];
	$city = $_POST['cityName'];
	$district = $_POST['district'];
	$place = $_POST['place'];
	$type = $_POST['type'];
	$typeslt = $_POST['typeslt'];
	$host = $_POST['host'];
	$img = $_POST['img'];
	$content = $_POST['content'];
	$content = str_replace("\"", "\\\"", $content);
    	
	$starttime = strtotime($starttimeStr);
	$endtime = strtotime($endtimeStr);
	
    //提交活动内容修改
	$contents['id'] = $id;
	$contents['链接'] = $url;
	$contents['标题'] = $title;
	$contents['时间'] = $time;
	$contents['开始时间'] = $starttime;
	$contents['结束时间'] = $endtime;
	$contents['城市'] = $city;
	$contents['区域'] = $district;
	$contents['地点'] = $place;
	$contents['类型'] = $type;
	$contents['类别'] = $typeslt;
	$contents['主办方'] = $host;
	$contents['海报地址'] = $img;
	$contents['详情'] = $content;
	
	$ok = true;
	foreach($contents as $key=>$value) {
		if (!isSubmitLegal($key, $value, 1)) {
			failedlog("{ActSave} ID<$id> $title","The value of activity:$key is wrong");
			echo "<script> alert('$key 填写错误！请重新填写！')</script>";  
			$ok = false; 
			break;
		}
	}
	
	if ($ok) {
	if ($endtime < $starttime) {
		failedlog("{ActSave} ID<$id> $title","The start time $starttimeStr is later than the end time $endtimeStr");
			echo "<script> alert('开始时间 $starttimeStr 晚于结束时间 $endtimeStr ！请重新填写！')</script>";  
			$ok = false; 
	}

	if ($ok) {
    $values = "VALUES ('$id','$url','$title','$time','$starttimeStr','$endtimeStr','$city',$district,
    					'$place','$type',$typeslt,'$host','$img','$content',0,2)";
	$update = "REPLACE INTO $actTable $values";
    mysql_query("set names utf8");
    $update = json_encode($update);
	$result = mysql_query(json_decode($update));
	if (!$result) {
    	failedlog("{ActSave} ID<$id> $title", "The save of activity fails, table $actTable");
    	echo "<script> alert('活动内容保存失败！\n 请检查数据库配置和表 $actTable 设置，并重新提交！')</script>"; 
    	$ok = false;
    } else
    	succeedlog("{ActSave} ID<$id> $title","The save of activity text content is successful.");
    
    if ($ok) {
    	//提交海报修改
	if (count(parse_url($img)) <= 1) $pictype = 1; else $pictype = 0;
	
	$imgresult = getImage($img, null, $pictype); 
	
	if ($imgresult == -2) {
		failedlog("{ActSubmit} ID<$raw_id> $title","Fail to download the image:$imgName.");
		echo ("<script> alert('图片文件下载失败！请重新设置图片路径！')</script>");
		$ok = false;
	};
	
	if ($ok) {
    $mysqlPicture = addslashes($imgresult);  
    $replace = "REPLACE INTO $postTable VALUES ('$id','$mysqlPicture')";
    $result = mysql_query($replace);
    if (!$result) {
    	failedlog("{ActSave} ID<$id> $title", "The save of poster:$img fails, table $postTable");
    	echo "<script> alert('图片保存失败！请检查数据库配置并重新提交！')</script>"; 
    	$ok = false;
    } else 
    	succeedlog("{ActSave} ID<$id> $title","The save of poster:$img is successful.");
    	
    if ($ok) {
  		succeedlog("{*ActSave} ID<$id> $title","The save of whole activity is successful.");
    	echo "<script> alert(\"修改保存成功\") </script>";
    }  
	}
	}
	}
	}
}
?>