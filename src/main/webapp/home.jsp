<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="java.util.Collections" %>

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import="com.googlecode.objectify.*" %>

<%@ page import="blog.BlogPost" %>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 

<html>
 <head>
   <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
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

%>

<p>Hello, ${fn:escapeXml(user.nickname)}! (You can

<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>

<%

    } else {

%>

<p>Hello!

<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>

to include your name with greetings you BlogPost.</p>

<%

    }

%>

 

<%
 	ObjectifyService.register(BlogPost.class);
 	
 	List<BlogPost> greetings = ObjectifyService.ofy().load().type(BlogPost.class).list();   
 	
 	Collections.sort(greetings); 

     // Run an ancestor query to ensure we see the most up-to-date

     // view of the Greetings belonging to the selected Guestbook.
     
     if (greetings.isEmpty()) {
 %>

        <p>There are no posts to display.</p>

        <%
        	} else {
        		
        	int len = greetings.size();
        	for (int i=len-1; i>-1 && i>len-6; i--) {
        			BlogPost greeting = greetings.get(i);
        			pageContext.setAttribute("greeting_title", greeting.getTitle());
                    pageContext.setAttribute("greeting_content", greeting.getContent());

                    if (greeting.getUser() == null) {
        %>

                <p>An anonymous person wrote:</p>

                <%

                    } else {

                        pageContext.setAttribute("greeting_user", greeting.getUser());

                        %>

                        <p id="title"><b>${fn:escapeXml(greeting_title)}</b>:</p>

                        <%

                    }

                    %>

                    <div id="content"><blockquote>${fn:escapeXml(greeting_content)}</blockquote></div>
        			<p id="signature"> Written by ${fn:escapeXml(greeting_user.nickname)}</p>
                    <%

        }

    }

%>
	<ul>
		<li><a href="all">Click Here To View All</a></li>
		<li><a href="post">Click Here To Post</a></li>
 	</ul>

  </body>

</html>