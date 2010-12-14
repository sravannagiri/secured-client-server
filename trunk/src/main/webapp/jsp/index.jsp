<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="javascript">
function doCommand() {
	var command = document.getElementById('command').value;
	var params = document.getElementById('params').value;

	alert(command + ' ' + params);

	var queryString = "?command=" + command + "&params=" + params;
	var contextPath = document.getElementById('contextPath').value;
	var url = contextPath + "/doCommand" + queryString;

	alert(url);
}
</script>
</head>
<body>
<h2>Secured-Client-Server-Application web interface</h2>

<input type="hidden" id="contextPath" value="<%=request.getContextPath() %>" />

<select id="command">
	<option value="" selected>[COMMAND]</option>
	<option value="adduser">adduser</option>
	<option value="userslist">userslist</option>
	<option value="userdetails">userdetails</option>
</select>

<input type="text" id="params" size="50" />
<input type="button" value="Submit" onclick="doCommand()" />

</body>
</html>