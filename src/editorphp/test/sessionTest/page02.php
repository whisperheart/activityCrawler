<?php 
     session_start();
     for($i=0;$i<3;$i++)
     {
             echo $_SESSION['connection'][$i].'<br />';
     }
?>