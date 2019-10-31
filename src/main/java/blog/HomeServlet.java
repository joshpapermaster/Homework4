package blog;


import com.google.appengine.api.datastore.Key;

import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import static com.googlecode.objectify.ObjectifyService.ofy;




public class HomeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {

      ObjectifyService.register(BlogPost.class);

  }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user != null) {
			resp.setContentType("text/plain");
			resp.sendRedirect("/post.jsp");
		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)

              throws IOException {

      UserService userService = UserServiceFactory.getUserService();

      User user = userService.getCurrentUser();

      // We have one entity group per Guestbook with all Greetings residing

      // in the same entity group as the Guestbook to which they belong.

      // This lets us run a transactional ancestor query to retrieve all

      // Greetings for a given Guestbook.  However, the write rate to each

      // Guestbook should be limited to ~1/second.
      String guestbookName = req.getParameter("guestbookName");

      Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
      
      if (req.getParameter("type").equals("Cancel Post")) {
          resp.sendRedirect("/home.jsp");
      }
      else {
	      String title = req.getParameter("title");
	
	      String content = req.getParameter("content");
	
	      BlogPost greeting = new BlogPost (user, title, content, guestbookName);
	      
	      ofy().save().entity(greeting).now();  
	      
	
	      resp.sendRedirect("/home.jsp");
      }

  }

}