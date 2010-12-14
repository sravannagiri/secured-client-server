<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<form method="post" action="<%=request.getContextPath() %>/login">
	<table border="1">
		<tr>
			<td>Username</td>
			<td><input type="text" name="username" /></td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input type="password" name="password" /></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Submit" />
				<input type="reset" value="Reset" />
			</td>
		</tr>
	</table>
	</form>
	
	<div>
	<%
		if ( request.getAttribute("errorMessage") != null ) {
			String errorMessage = (String) request.getAttribute("errorMessage");
			%>
			<font color="red"><%=errorMessage %></font>	
			<%
		}
	%>
	
	</div>

</body>
</html>