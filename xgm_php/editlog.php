<?php
	function succeedlog($tag, $message) {
		editlog("[Succeed] $tag", $message);
	}
	
	function failedlog($tag, $message) {
		editlog("[Failure] $tag", $message);
	}
	
	function editlog($tag, $message) {
		date_default_timezone_set('PRC');
		$nowtime = time();
		$full = date('Y-m-d H:i:s',$nowtime);
		$ymd = date('Y-m-d',$nowtime);
		$editlog = getEditLog();
		$filename = $editlog.'_'.$ymd;
		$logmsg = "($full) $tag => $message \n";
		$fp2=@fopen($filename,'a');
    	fwrite($fp2,$logmsg);
    	fclose($fp2);
	}
?>