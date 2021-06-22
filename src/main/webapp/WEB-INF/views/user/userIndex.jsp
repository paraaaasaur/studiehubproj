<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User首頁</title>
<script>
window.onload = function(){
    var logout = document.getElementById("logout");
    logout.onclick = function(){
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "<c:url value='/logout.controller' />", true);
        xhr.send();
        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4 && xhr.status == 200){
                var result = JSON.parse(xhr.responseText);
                if(result.success){
                    alert(result.success);
                    top.location = '<c:url value='/gotoUserIndex.controller' />';
                }else if(result.fail){
                    alert(result.fail);                    
                    top.location = '<c:url value='/gotoUserIndex.controller' />';
                }
            }
        }
    }
}
</script>

</head>
<body>
<div align='center'>
  <h2>User首頁</h2>
  <hr>
</div>
<br/>
<div style="text-align: center;">
<div style="display: inline-block; text-align: center; line-height:30px;">
  <a href="<c:url value='/gotologin.controller' />" >登入</a><br>
  <a href="<c:url value='/gotosignup.controller' />" >註冊</a><br>
  <a href="<c:url value='/gotoShowAllUser.controller' />" >查看全部會員資料</a><br>
  <a href="<c:url value='/gotoUpdateUserinfo.controller' />" >修改會員資料</a><br>
  <a href="<c:url value='/logout.controller' />" id="logout">登出</a><br>
</div>
  <hr>
  <div align='center'>
  <a href="<c:url value='/' />">首頁</a>
  </div>
</div>
</body>
</html>