<?php
/* ************************************************
 * 作     者:孙丰伟 长春信息技术职业学院 计算机系
 * Blog    :http://hi.baidu.com/j2ee_cn
 * 创建时间:2012-3-15
 * 文 件 名:setPages.php
 **************************************************/
//定义函数使用function关键字,不需要写返回的类型,&$sql：引用传递,其他的是值传递
/*&$sql:翻页的select语句,$tbl_name:表名,$targetpage:当前文件名,$limit:一页显示的行数*/
function setPages(&$sql,$strWhere,$tbl_name, $targetpage,$limit,$isSelect)
{
    /*定义变量时前面加global表示为全局变量*/
    global $total_pages,$page,$lastpage,$pagination;
    //$tbl_name="db_gonggao";        //要查询的数据库表名
    // How many adjacent pages should be shown on each side?
    $adjacents = 3;

    /*
     First get total number of rows in data table.
    获取表的总行数
    If you have a WHERE clause in your query, make sure you mirror it here.
    如果查询条件有WHERE子句，把条件写在下面的查询语句中
    */
    $query = "SELECT COUNT(*) as num FROM $tbl_name ".$strWhere;
    mysql_query("set names utf8");
    $result = mysql_query($query) or die("Cannot query");
    $total_pages = mysql_fetch_array($result);
    $total_pages = $total_pages['num'];

    /* Setup vars for query. 设置URL参数*/
    //$targetpage = "messageList.php";             //当前文件名        your file name  (the name of this file)
    //$limit = 4;                                 //每页显示的行数    how many items to show per page
    if(isset($_GET['page']))
    {
        $page = $_GET['page'];
    }
    else
    {
        $page=1;
    }
    if($page)
        $start = ($page - 1) * $limit;             //first item to display on this page
    else
        $start = 0;                                //if no page var is given, set start to 0

    /* Get data. */
    $sql = "SELECT * FROM $tbl_name $strWhere LIMIT $start, $limit";
//    echo $sql;
    $result = mysql_query($sql);

    /* Setup page vars for display. */
    if ($page == 0) $page = 1;                    //if no page var is given, default to 1.
    $prev = $page - 1;                            //previous page is page - 1
    $next = $page + 1;                            //next page is page + 1
    $lastpage = ceil($total_pages/$limit);        //lastpage is = total pages / items per page, rounded up.
    $lpm1 = $lastpage - 1;                        //last page minus 1

    /*
        Now we apply our rules and draw the pagination object.
    We're actually saving the code to a variable in case we want to draw it more than once.
    */
    $pagination = "";
    if($lastpage > 1)
    {
        $pagination .= "<div class=\"pagination\">";

        //previous button
        if ($page > 1)
        {
            //加入第一页 孙丰伟
            $pagination.= "<a href='$targetpage?page=1'>第一页</a>";
            $pagination.= "<a href=\"$targetpage?page=$prev\"> 前一页 </a>";
        }
        else
        {
            //加入第一页 孙丰伟
            $pagination.= "<span class=\"disabled\"> 第一页 </span>";
            $pagination.= "<span class=\"disabled\"> 前一页 </span>";
        }

        //pages
        if ($lastpage < 7 + ($adjacents * 2))    //not enough pages to bother breaking it up
        {
            for ($counter = 1; $counter <= $lastpage; $counter++)
            {
                if ($counter == $page)
                    $pagination.= "<span class=\"current\"> $counter </span>";
                else
                    $pagination.= "<a href=\"$targetpage?page=$counter\">$counter</a>";
            }
        }
        elseif($lastpage > 5 + ($adjacents * 2))    //enough pages to hide some
        {
            //close to beginning; only hide later pages
            if($page < 1 + ($adjacents * 2))
            {
                for ($counter = 1; $counter < 4 + ($adjacents * 2); $counter++)
                {
                    if ($counter == $page)
                        $pagination.= "<span class=\"current\">$counter</span>";
                    else
                        $pagination.= "<a href=\"$targetpage?page=$counter\">$counter</a>";
                }
                $pagination.= "...";
                $pagination.= "<a href=\"$targetpage?page=$lpm1\">$lpm1</a>";
                $pagination.= "<a href=\"$targetpage?page=$lastpage\">$lastpage</a>";
            }
            //in middle; hide some front and some back
            elseif($lastpage - ($adjacents * 2) > $page && $page > ($adjacents * 2))
            {
                $pagination.= "<a href=\"$targetpage?page=1\"> 1 </a>";
                $pagination.= "<a href=\"$targetpage?page=2\"> 2 </a>";
                $pagination.= "...";
                for ($counter = $page - $adjacents; $counter <= $page + $adjacents; $counter++)
                {
                    if ($counter == $page)
                        $pagination.= "<span class=\"current\"> $counter </span>";
                    else
                        $pagination.= "<a href=\"$targetpage?page=$counter\">$counter</a>";
                }
                $pagination.= "...";
                $pagination.= "<a href=\"$targetpage?page=$lpm1\"> $lpm1 </a>";
                $pagination.= "<a href=\"$targetpage?page=$lastpage\"> $lastpage </a>";
            }
            //close to end; only hide early pages
            else
            {
                $pagination.= "<a href=\"$targetpage?page=1\">1</a>";
                $pagination.= "<a href=\"$targetpage?page=2\">2</a>";
                $pagination.= "...";
                for ($counter = $lastpage - (2 + ($adjacents * 2)); $counter <= $lastpage; $counter++)
                {
                    if ($counter == $page)
                        $pagination.= "<span class=\"current\">$counter</span>";
                    else
                        $pagination.= "<a href=\"$targetpage?page=$counter\">$counter</a>";
                }
            }
        }

        //next button
        if ($page < $counter - 1)
        {
            $pagination.= "<a href=\"$targetpage?page=$next\">下一页 </a>";
            //加入最后一页
            $pagination.="<a href=\"$targetpage?page=$lastpage\">最后一页</a>";
        }
        else
        {
            $pagination.= "<span class=\"disabled\">下一页 </span>";
            //加入最后一页
            $pagination.= "<span class=\"disabled\">最后一页 </span>";
        }

        if($isSelect)
        {
            //孙丰伟　2012-3-7
            //添加下拉列表

            $select="<select name='webUrl' id='webUrl' onchange='window.location.href=this.value'>\n";

            for($i=1;$i<=$lastpage;$i++)
            {
                $select.="<option value='$targetpage?page=$i'>$i</option>\n";
            }

         $select."</select>";

         $pagination.=$select;
        }
            
        //添加下拉列表结束
            
        $pagination.= "</div>\n";
    }
    //return $pagination;
}
?>