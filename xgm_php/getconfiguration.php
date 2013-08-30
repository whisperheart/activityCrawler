<?php 
include("conn.php");
function getRootElement() {	
	global $xmlfile;
	$dom = new DOMDocument();
	$dom->load($xmlfile);
	return $dom->documentElement;
}

function getMainElement() {	
	$arr = getArray(getRootElement());
	$main = $arr['webeditor'][0];
	return $main;
}

function getActivityElement() {	
	$arr = getArray(getRootElement());
	$activity = $arr['activity'][0];
	return $activity;
}

function getActivityDefault($valueName) {
	$actEle = getActivityElement();
	$defaultEle = $actEle['default'][0];
	$value = $defaultEle[$valueName][0]['#text'];
	return $value;
}

function getDatabase() {
	$main = getMainElement();
	$database = $main['database'][0];
	return $database;
}

function getConnection() {
	$database = getDatabase();	
	$dburl = $database['dburl'][0]['#text'];
	$schema = $database['schema'][0];
	$online = $schema['online'][0]['#text'];
	$offline = $schema['offline'][0]['#text'];
	$user = $database['user'][0]['#text'];
	$pwdEle = $database['pwd'][0];
	if (isset($pwdEle['#text']))
		$pwd = $pwd['#text'];
	else
		$pwd = "";
	$conn = connect($dburl, $user, $pwd);
	return $conn;
}

function getSchema() {
	$database = getDatabase();
	$schema = $database['schema'][0];
	return $schema;
}

function setOnlineSchema($con) {
	$schema = getSchema();
	$online = $schema['online'][0]['#text'];
	schema($con, $online);
}

function setOfflineSchema($con) {
	$schema = getSchema();
	$offline = $schema['offline'][0]['#text'];
	schema($con, $offline);
}

function getTable($tableName) {
	$database = getDatabase();
	$table = $database['table'][0];
	if (isset($table[$tableName]))
		return $table[$tableName][0]['#text'];
	else 
		return null;	
}

function getImageElement() {
	$main = getMainElement();
	$image = $main['image'][0];
	return $image;
}

function getImgPath() {
	$image = getImageElement();
	$imgPath = $image['imgpath'][0]['#text'];
	return $imgPath;
}

function getUploadPath() {
	$image = getImageElement();
	$uploadPath = $image['uploadpath'][0]['#text'];
	return $uploadPath;
}

function getDefaultPoster() {
	$image = getImageElement();
	$defaultposter = $image['defaultposter'][0]['#text'];
	return $defaultposter;
}

function getLogo() {
	$image = getImageElement();
	$logo = $image['logo'][0]['#text'];
	return $logo;
}

function getDefaultValue($value) {
	$main = getMainElement();
	$default = $main['default'][0];
	$defaultValue = $default[$value][0]['#text'];
	return $defaultValue;
}

function getEditLog() {
	$main = getMainElement();
	$editlog = $main['log'][0]['#text'];
	return $editlog;
}

function getArray($node) {
  $array = false;
  if ($node->hasAttributes()) {
    foreach ($node->attributes as $attr) {
      $array[$attr->nodeName] = $attr->nodeValue;
    }
  }

  if ($node->hasChildNodes()) {
    if ($node->childNodes->length == 1) {
      $array[$node->firstChild->nodeName] = getArray($node->firstChild);
    } else {
      foreach ($node->childNodes as $childNode) {
      if ($childNode->nodeType != XML_TEXT_NODE) {
        $array[$childNode->nodeName][] = getArray($childNode);
      }
    }
  }
  } else {
    return $node->nodeValue;
  }
  return $array;
}

$xmlfile = "D:/xampp/htdocs/manager/config/ActConfig.xml";
?>