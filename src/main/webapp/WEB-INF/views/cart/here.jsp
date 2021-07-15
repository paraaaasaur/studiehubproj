<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">



</head>

<body>

    <br><br><br><br><br><br><br><br><br><br>
    <div id='here'></div>


    <button onclick="dothis()">click me</button>



	<!-- Scripts -->
	<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script>
        // function dothis(){
        //     let a = [1, 2, 3, 4, ['a', 'b', 'c']];
		// 	JSON.parse(a);
		// 	console.log(a)
		// 	$('#div').html(a);
            
        // }
        function dothis(){
            let xhr = new XMLHttpRequest();
            xhr.open('POST', 'http://localhost:8080/studiehub/test04');
            let a = [1, 2, 3, 4, ['a', 'b', 'c']];	
			let b = {
				1 : 'a',
				2 : 'b',
				3 : 'd',
				4 : {
					17 : 'c',
					18 : ['a', 'b', 'c'],
					19 : ['b', 'c'],
					20 : {
						77 : 'a',
						78 : a
					}
				}
			}
            console.log(b);
			// xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); // ❓
			xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8"); // ❓
            // xhr.send('a=' + JSON.stringify(a));
            xhr.send(b);
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    let myJson = JSON.parse(xhr.responseText);
                    console.log(JSON.stringify(myJson));
                    $('#here').html(myJson);
                }
            }
        }

        $(function(){

        })

    </script>

</body>
</html>