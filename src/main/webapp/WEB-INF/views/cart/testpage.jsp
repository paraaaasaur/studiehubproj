<%@page import="tw.group5.controller.cart.*"%>
<%@page import="javax.naming.*"%>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="javax.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<body>
		<table id="dataArea">
			www
		</table>
		<div id="sss">
			a div.
		</div>
		
		<script src="/SpringMvcWebHW/js/jquery-3.6.0.min.js"></script>
		<script>
			$(function(){
				let dataArea = $('#dataArea');
				// let sss = $('#sss');
				let xhr = new XMLHttpRequest();
				let url = "<c:url value='/cart.controller/initAdminPageData' />";
				xhr.open("GET", url, true);
				xhr.send();
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						dataArea.html(parseSelectedRows(xhr.responseText));
					}
				};

				function parseSelectedRows(orderList) {
					   let orders = JSON.parse(orderList);
					   let segment = "";
					   let totalPrice = 0;

					   for (let i = 0; i < orders.length; i++) {
						   totalPrice += orders[i].p_price;
							segment +=	 "<tr>" + 
												"<td><input name='ckbox' id='ckbox' type='checkbox' value=' + " + i + "'></td>" +
												"<td style='background: aquamarine;'>" + 
													  "<input required name='" + i + "0' type='text' value='" + orders[i].o_id + "' readonly></td>" +
												"<td><input required name='" + i + "1'  type='text' value='" + orders[i].p_id + "' ></td>" +
												"<td><input required name='" + i + "2'  type='text' value='" + orders[i].p_name + "' ></td>" +
												"<td><input required name='" + i + "3'  type='text' value='" + orders[i].p_price + "' id='num'></td> <!--price-->" +
												"<td><input required name='" + i + "4'  type='text' value='" + orders[i].u_id + "' ></td>" +
												"<td><input required name='" + i + "5'  type='text' value='" + orders[i].u_firstname + "' ></td>" +
												"<td><input required name='" + i + "6'  type='text' value='" + orders[i].u_lastname + "' ></td>" +
												"<td><input required name='" + i + "7'  type='text' value='" + orders[i].u_email + "' ></td>" +
												"<td><input required name='" + i + "8'  type='text' value='" + orders[i].o_status + "' ></td>" +
												"<td><input required name='" + i + "9'  type='text' value='" + orders[i].o_data + "' ></td>" +
												"<td><input required name='" + i + "10' type='text' value='" + orders[i].o_amt + "' id='num'></td>" +
												"</tr>";
					   }
					   segment += "<div>小計：" + totalPrice + "</div>";
					   alert(segment);
					   return segment;
			};

					

			})
			</script>
</body>
</html>