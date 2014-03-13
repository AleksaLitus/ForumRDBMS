<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<table border="1" align="center" width="100%">
	<tr height="10%" align="center">
		<td width="15%"><b><a href="users.jsp?action=search">Users</a></b></td>
		<td width="15%"><b><a href="topics.jsp?action=search">Topics</a></b></td>
		<td width="15%"><b><a href="messages.jsp?action=search">Messages</a></b></td>
		<td width="15%"><b><a href="admins.jsp?action=search">Admins</a></b></td>
		<td width="40%"><b>Hello, ${sessionScope.admin} <br>
				<a href="logout.do">Logout</a>
		</b></td>
	</tr>
</table>