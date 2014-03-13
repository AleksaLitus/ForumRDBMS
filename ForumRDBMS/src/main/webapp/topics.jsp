<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum RDBMS - Topics table</title>
</head>
<body>


	<!-- Header import -->
	<c:import url="resources/header.jsp" />


	<!-- Sidebar and form -->
	<table border="1" align="center" width="100%">
		<tr height="30%">
			<td width="15%"><b>Topics</b>
				<ul>
					<li><a href="topics.jsp?action=selectAll">Select all</a></li>
					<li><a href="topics.jsp?action=delete">Delete</a></li>
					<li><a href="topics.jsp?action=insert">Insert</a></li>
					<li><a href="topics.jsp?action=search">Search</a></li>
					<li><a href="topics.jsp?action=mostPopular">Most popular</a></li>
				</ul></td>
			<td width="85%">
				<form action="topics.do?action=${param.action}" method="post">
					<table border="0" align="center">
						<tr>
							<td align="center" colspan='2'><b>${param.action}</b></td>
						</tr>
						<!-- View all text boxes needed -->
						<c:if test="${topicId}">
							<tr>
								<td><b>Topic id: </b></td>
								<td><input type='text' name='topicId' size='50'
									maxlength='10' /></td>
							</tr>
						</c:if>
						<c:if test="${topicName}">
							<tr>
								<td><b>Topic name: </b></td>
								<td><input type="text" name='topicName' size='50'
									maxlength='50' /></td>
							</tr>
						</c:if>
						<c:if test="${topicAuthorId}">
							<tr>
								<td><b>Author id: </b></td>
								<td><input type='text' name='topicAuthorId' size='50'
									maxlength='10' /></td>
							</tr>
						</c:if>
						<c:if test="${topicMessagesCount}">
							<tr>
								<td><b>Messages count: </b></td>
								<td><input type='text' name='topicMessagesCount' size='50'
									maxlength='10' /></td>
							</tr>
						</c:if>
						<c:if test="${topicMostPopularCountToView}">
							<tr>
								<td><b>Count to view: </b></td>
								<td><input type='text' name='topicMostPopularCountToView' size='50'
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
			<th>Topic id</th>
			<th>Topic name</th>
			<th>Author id</th>
			<th>Messages count</th>
		</tr>
		<c:forEach items="${sessionScope.modelTopicsToView}" var="topic">
			<tr>
				<td>${topic.id}</td>
				<td>${topic.name}</td>
				<td>${topic.authorId}</td>
				<td>${topic.messagesCount}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>