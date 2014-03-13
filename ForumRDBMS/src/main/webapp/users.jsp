<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum RDBMS - Users table</title>
</head>
<body>


	<!-- Header import -->
	<c:import url="resources/header.jsp" />


	<!-- Sidebar and form -->
	<table border="1" align="center" width="100%">
		<tr height="30%">
			<td width="15%"><b>Users</b>
				<ul>
					<li><a href="users.jsp?action=selectAll">Select all</a></li>
					<li><a href="users.jsp?action=delete">Delete</a></li>
					<li><a href="users.jsp?action=insert">Insert</a></li>
					<li><a href="users.jsp?action=search">Search</a></li>
				</ul></td>
			<td width="85%">
				<form action="users.do?action=${param.action}" method="post">
					<table border="0" align="center">
						<tr>
							<td align="center" colspan='2'><b>${param.action}</b></td>
						</tr>
						<!-- View all text boxes needed -->
						<c:if test="${userId}">
							<tr>
								<td><b>User id: </b></td>
								<td><input type='text' name='userId' size='40'
									maxlength='10' /></td>
							</tr>
						</c:if>
						<c:if test="${userName}">
							<tr>
								<td><b>Username: </b></td>
								<td><input type="text" name='userName' size='40'
									maxlength='15' /></td>
							</tr>
						</c:if>
						<c:if test="${userEmail}">
							<tr>
								<td><b>Email: </b></td>
								<td><input type='text' name='userEmail' size='40'
									maxlength='40' /></td>
							</tr>
						</c:if>
						<c:if test="${userPassword}">
							<tr>
								<td><b>Password: </b></td>
								<td><input type='text' name='userPassword' size='40'
									maxlength='40' /></td>
							</tr>
						</c:if>
						<!-- Submit button -->
						<c:if test="${param.action != 'selectAll'}">
							<tr>
								<td colspan="2" align="center"><input type="submit"
									value="Ok" /></td>
							</tr>
						</c:if>
					</table>
				</form>
			</td>
		</tr>
	</table>


	<!-- Result message -->
	<br>
	<br>
	<div align='center'>${actionResult}</div>
	<br>
	<br>


	<!-- Table with selected data -->
	<table border="1" align="center" width="100%">
		<tr>
			<th>User id</th>
			<th>User name</th>
			<th>User email</th>
			<th>User password</th>
		</tr>
		<c:forEach items="${sessionScope.modelUsersToView}" var="currentUser">
			<tr>
				<td>${currentUser.id}</td>
				<td>${currentUser.name}</td>
				<td>${currentUser.email}</td>
				<td>${currentUser.password}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>