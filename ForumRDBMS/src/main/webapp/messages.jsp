<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum RDBMS - Messages table</title>
</head>
<body>


	<!-- Header import -->
	<c:import url="resources/header.jsp" />


	<!-- Sidebar and form -->
	<table border="1" align="center" width="100%">
		<tr height="30%">
			<td width="15%"><b>Messages</b>
				<ul>
					<li><a href="messages.jsp?action=selectAll">Select all</a></li>
					<li><a href="messages.jsp?action=delete">Delete</a></li>
					<li><a href="messages.jsp?action=insert">Insert</a></li>
					<li><a href="messages.jsp?action=search">Search</a></li>
				</ul></td>
			<td width="85%">
				<form action="messages.do?action=${param.action}" method="post">
					<table border="0" align="center">
						<tr>
							<td align="center" colspan='2'><b>${param.action}</b></td>
						</tr>
						<!-- View all text boxes needed -->
						<c:if test="${messageId}">
							<tr>
								<td><b>Message id: </b></td>
								<td><input type='text' name='messageId' size='40'
									maxlength='10' /></td>
							</tr>
						</c:if>
						<c:if test="${messageText}">
							<tr>
								<td><b>Message text: </b></td>
								<td><input type="text" name='messageText' size='40'
									maxlength='300' /></td>
							</tr>
						</c:if>
						<c:if test="${messageAuthorId}">
							<tr>
								<td><b>Author id: </b></td>
								<td><input type='text' name='messageAuthorId' size='40'
									maxlength='10' /></td>
							</tr>
						</c:if>
						<c:if test="${messageTopicId}">
							<tr>
								<td><b>Topic id: </b></td>
								<td><input type='text' name='messageTopicId' size='40'
									maxlength='10' /></td>
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
			<th>Message id</th>
			<th>Message text</th>
			<th>Message author id</th>
			<th>Message topic id</th>
		</tr>
		<c:forEach items="${sessionScope.modelMessagesToView}" var="message">
			<tr>
				<td>${message.id}</td>
				<td>${message.text}</td>
				<td>${message.authorId}</td>
				<td>${message.topicId}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>