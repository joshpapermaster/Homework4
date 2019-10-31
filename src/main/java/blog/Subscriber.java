
package blog;

import java.util.Date;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import blog.Guestbook;


@Entity
public class Subscriber implements Comparable<Subscriber> {
	@Parent Key<Guestbook> guestbookName;
    @Id Long id;
    @Index User user;
    @Index Date date;
    
    private Subscriber() {}
    
    public Subscriber(User user, String guestbookName) {
        this.user = user;
        this.guestbookName = Key.create(Guestbook.class, guestbookName);
        date = new Date();
    }
    public User getUser() {
        return user;
    }

    @Override
    public int compareTo(Subscriber other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
     }
}