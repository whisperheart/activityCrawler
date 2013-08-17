<script>  
//1 设置简单的cookie  
document.cookie="loaddata=1"; 
document.cookie="a=aa,b=bb";  
//alert(document.cookie); 
  //2 discuz中的setcookie  
  function setcookie(cookieName, cookieValue, seconds, path, domain, secure) {   
	  var expires = new Date();   
	  expires.setTime(expires.getTime() + seconds);   
	  document.cookie = escape(cookieName) + '=' + escape(cookieValue)    
	  + (expires ? '; expires=' + expires.toGMTString() : '') //设置过期时间    
	  + (path ? '; path=' + path : '/')      //设置访问路径    
	  + (domain ? '; domain=' + domain : '')     //设置访问主机    
	  + (secure ? '; secure' : ''        //设置安全性   
	);  
	}  //discuz中的getcookie 

	function getcookie(name) {  
		 var cookie_start = document.cookie.indexOf(name);   
		 var cookie_end = document.cookie.indexOf(";", cookie_start);   
		 return cookie_start == -1 ? '' :  
			 unescape(document.cookie.substring(cookie_start + name.length + 1, 
					 (cookie_end > cookie_start ? cookie_end : document.cookie.length)));  }    //自己写一个删除cookie的函数 

	function delcookie(name)  {   
		this.setcookie(name,'',{expireDays:-1});//将过期时间设置为过去来删除一个cookie  
	}    
	setcookie('name','jack'); 
	alert(document.cookie); 
	delcookie('name');  
	alert(typeof(getcookie('name'))); 
</script> 