<FORM method="post" action="checkTest.php"> 
<INPUT name="test[]" type="checkbox" value="1" /> 
<INPUT type="checkbox" name="test[]" value="2" /> 
<INPUT type="checkbox" name="test[]" value="3" /> 
<INPUT type="checkbox" name="test[]" value="4" /> 
<INPUT type="checkbox" name="test[]" value="5" /> 
<INPUT type="submit" name="Submit" value="Submit" /> 
</FORM> 
<?php 
//echo implode(",",$_POST['test']); 
print_r($_POST['test']); 
?> 