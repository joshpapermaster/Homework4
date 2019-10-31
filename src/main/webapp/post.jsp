<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="java.util.Collections" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import="com.googlecode.objectify.*" %>

<%@ page import="blog.BlogPost" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Post</title>
</head>
<body>

	

	<%

	    String guestbookName = request.getParameter("guestbookName");
	
	    if (guestbookName == null) {
	
	        guestbookName = "real";
	
	    }
	
	    pageContext.setAttribute("guestbookName", guestbookName);
	
	    UserService userService = UserServiceFactory.getUserService();
	
	    User user = userService.getCurrentUser();
	
	    if (user != null) {
	
	      pageContext.setAttribute("user", user);
	    }

	%>
	

    <form action="post" method="Post">
	  
	  <div><p>TITLE:</p></div>	
	  
	  <div><textarea name="title" rows="1" cols="60"></textarea></div>	
	  
	  <div><p>CONTENT:</p></div>	
	  	
      <div><textarea name="content" rows="5" cols="60"></textarea></div>

      <div><input type="submit" name="type" value="Cancel Post"/><input type="submit" name="type" value="Submit Post"/></div>
      
      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>

    </form>

</body>
</html>