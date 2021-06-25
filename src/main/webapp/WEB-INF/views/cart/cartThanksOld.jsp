<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thank Page</title>
</head>
<body>
<h1>您的本次購買已被受理，感謝您！</h1>
<h1>自動導回首頁中...</h1>

<script>
    setTimeout(function() {
        window.location.href = "<c:url value='/' />";
    }, 2500);
</script>

</body>
</html>