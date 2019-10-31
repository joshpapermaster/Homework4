package blog;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class ViewHomeServlet extends HttpServlet {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			resp.setContentType("text/plain");
			resp.sendRedirect("/home.jsp");
		}
		
		public void doPost(HttpServletRequest req, HttpServletResponse resp)

	              throws IOException {

	      UserService userService = UserServiceFactory.getUserService();

	      User user = userService.getCurrentUser();
	      
	      String guestbookName = req.getParameter("guestbookName");

	      Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	      
	      ObjectifyService.register(Subscriber.class);
	     	
	      List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();   
		   	
	      boolean flag = true;
	      for (Subscriber sub : subscribers) {
	    	  if (sub.getUser() == null)
	    		  ofy().delete().entity(sub).now();
	    	  else if(sub.getUser().getEmail().equals(user.getEmail())) {
	    		  flag = false;
	    		  ofy().delete().entity(sub).now();
	    		  break;
	    	  }
	      }
	      if (flag) {
	    	  //Add user
	    	  Subscriber greeting = new Subscriber (user, guestbookName);
		      
		      ofy().save().entity(greeting).now(); 
	      }
		  resp.sendRedirect("/home.jsp?subscribe=" + flag);
	    
	  }
}
