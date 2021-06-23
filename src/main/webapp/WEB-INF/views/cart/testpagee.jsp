<%@page import="tw.group5.controller.cart.*"%>
<%@page import="javax.naming.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="javax.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"></head>
<body>
    
    <input type="checkbox" id="ckbox" class='ckbox' value="">普通ckbox1
    <input type="checkbox" id="ckbox" class='ckbox' value="">普通ckbox2
    <hr>
    <div id="dataArea">div</div>



    <button id="delete" disabled>移除</button>

    <script src="/SpringMvcWebHW/js/jquery-3.6.0.min.js"></script>
    <script>
        $(function(){
            
            // 1 (showCart by AJAX) ✔
            let dataArea = $('#dataArea');
            $(window).on('load', function(){
                let segment = "";
                for (let i = 0; i < 8; i++) {
                    segment += "<input type='checkbox' name='ckbox' value='w' id='ckbox' class='ckbox'>手加的取消之 + " + i + "<br>";
                }
                dataArea.html(segment);
            });


            // DELETE功能防呆
            $('input.ckbox').on('click', function(){
                    alert('congrats :^)');
					let ckboxes = $('input.ckbox:checked');
					$('#delete').attr('disabled', true);
						if($(ckboxes).length == 0 || $(ckboxes).length == null) {
							console.log('(if)' + $(ckboxes).length);
						} else {
							$('#delete').attr('disabled', false);
							console.log('(else)' + $(ckboxes).length);		
						}
				})



            
            
        })
    </script>
</body>
</html>