<?php
//只能通过post方式访问
//	if ($_SERVER['REQUEST_METHOD'] == 'GET') {
//		header('HTTP/1.1 404 Not Found'); 
//		die('亲,页面不存在');
//	}
	session_start();
	$fs1=$_POST['a'];
	$fs2=$_POST['b'];
//防刷新时间,单位为秒
	$allowTime = 30;
//读取访客ip，以便于针对ip限制刷新
/*获取真实ip开始*/
	if (!function_exists('GetIP')){		
		function GetIP(){
			static $ip = NULL;
			if ($ip !== NULL){
				return $ip;
			}
			if (isset($_SERVER)){
			if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])){
				$arr = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);
/* 取X-Forwarded-For中第x个非unknown的有效IP字符? */
				foreach ($arr as $xip){
					$xip = trim($xip);
					if ($xip != 'unknown'){
						$ip = $xip;
						break;
					}
				}
			} elseif (isset($_SERVER['HTTP_CLIENT_IP'])) {
				$ip = $_SERVER['HTTP_CLIENT_IP'];
			} else {
				if (isset($_SERVER['REMOTE_ADDR'])) {
					$ip = $_SERVER['REMOTE_ADDR'];
				} else {
					$ip = '0.0.0.0';
				}
				}
			} else {
				if (getenv('HTTP_X_FORWARDED_FOR')) {
					$ip = getenv('HTTP_X_FORWARDED_FOR');
				} elseif (getenv('HTTP_CLIENT_IP')) {
					$ip = getenv('HTTP_CLIENT_IP');
				} else {
					$ip = getenv('REMOTE_ADDR');
				}
			}
			preg_match("/[\d\.]{7,15}/", $ip, $onlineip);
			$ip = !empty($onlineip[0]) ? $onlineip[0] : '0.0.0.0';
			return $ip;
			}
		}
/*获取真实ip结束*/
		$reip = GetIP();
//相关参数md5加密
		$allowT = md5($reip.$fs1.$fs2);
		if(!isset($_SESSION[$allowT])){
			$_SESSION[$allowT] = time();
		} else 
			if(time() - $_SESSION[$allowT]-->$allowTime){
				$_SESSION[$allowT] = time();
			}
//如果刷新过快，则直接给出404header头以及提示
			else {
				header('HTTP/1.1 404 Not Found'); 
				die('来自'.$ip.'的亲,您刷新过快了');
			}
?>
