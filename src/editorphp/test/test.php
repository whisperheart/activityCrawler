<script language="javascript">
function openDialogWin()
{
    var result = window.showModalDialog("test.html",
                                        "",
                                        "dialogHeight:100px;dialogWidth:280px;status:no;help:no;scroll:no");
    if (result == "Open")
        window.alert("You clicked Open!");
    else if (result == "Save")
        window.alert("You clicked Save!");
    else
        window.alert("Cancel");
                
}
</script>
 
<input type="button" value="ShowModelDialog" onclick="openDialogWin()"/>