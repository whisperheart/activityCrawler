<?php 
session_start();
 unset($_SESSION['connect']);
if (!isset($_SESSION['connect']))  {
		$_SESSION['connect'] = array('aaa','456','789');
	}

?>
<a href="page02.php"><?php echo("page02")?></a>