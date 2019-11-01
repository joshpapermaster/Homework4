package blog;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

public class SendMail extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

	    String guestbookName = req.getParameter("guestbookName");
	    
	    ObjectifyService.register(Subscriber.class);
	   	
	    List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
	    
	    ObjectifyService.register(BlogPost.class);
	 	
	 	List<BlogPost> greetings = ObjectifyService.ofy().load().type(BlogPost.class).list();
	 	Collections.sort(greetings);
	 	
	 	List<BlogPost> emailList = new ArrayList<>();
	 	
	 	Date date = new Date();
	 	Long time = (date.getTime() - 86400000);
	 	String message = "Posts from the last 24 hours: \n\n";
	 	
	 	int len = greetings.size();
    	for (int i=len-1; i>-1 && i>len-6; i--) {
    		BlogPost greet = greetings.get(i);
    		if(greet.date.getTime() > time) {
	 			String temp = greet.toString();
	 			emailList.add(greet);
	 			message = message + "Author: " + greet.getUser().getEmail() + "\n" + "Title: " + greet.getTitle() + "\n" + "Post: " + greet.getContent() + "\n\n";
	 		}
    	}
	 	
//	 	for (BlogPost greet : greetings) {
//	 		if(greet.date.getTime() > time) {
//	 			String temp = greet.toString();
//	 			emailList.add(greet);
//	 			message = message + "Author: " + greet.getUser().getEmail() + "\n" + "Title: " + greet.getTitle() + "\n" + "Post: " + greet.getContent() + "\n\n";
//	 		}
//	 	}
	    
	 	if(emailList.size() == 0) {
	 		resp.setStatus(202);
	 		return;
	 	}
	 	
	 	for (Subscriber sub : subscribers) {
	 		Properties props = new Properties();
	 	    Session session = Session.getDefaultInstance(props, null);

	 	    try {
	 	      Message msg = new MimeMessage(session);
	 	      msg.setFrom(new InternetAddress("ourblog@homework4-257718.appspotmail.com", "Your friendly blog subscription"));
	 	      msg.addRecipient(Message.RecipientType.TO,
	 	                       new InternetAddress(sub.user.getEmail()));
	 	      msg.setSubject("Your daily digest");
	 	      msg.setText(message);
	 	      Transport.send(msg);
	 	    } catch (AddressException e) {
	 	      // ...
	 	    } catch (MessagingException e) {
	 	      // ...
	 	    }
	 	}
	 	
	 	resp.setStatus(202);
	}
}
