<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
<font color="red">${loginResult}</font>
<br><br><br>
	<form action='login.do' method='post'>
		<table border='1' align='center'>
			<tr>
				<td><b>Login: </b></td>
				<td><input type='text' name='login' size='40' maxlength='15' /></td>
			</tr>

			<tr>
				<td><b>Password: </b></td>
				<td><input type="password" name="password" size="40" maxlength="40" /></td>
			</tr>

			<tr>
				<td colspan="2" align="center"><input type="submit" value="Login" /></td>
			</tr>
		</table>
	</form>
	letos_1994 <br>
	12characters
</body>
</html>