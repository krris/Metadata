package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Min;
import play.db.ebean.Model.Finder;

/**
 * User entity managed by Ebean
 */
@Entity 
public class User 
{
	@Id
	public Long id;
	
	@Constraints.Required
	public String ip;
	
	@Min(0)
	public Long numberOfVisits;
		
    /**
     * Generic query helper for entity User with id Long
     */
	public static Finder<Long, User> finder = new Finder<Long, User>(Long.class, User.class);
	
	/**
	 * Find a user with a given ip.
	 * @param ip
	 * @return User id. If there are more than one user, return the first one in the list.
	 */
	public static Long findUserbyIp(String ip)
	{
		int FIRST_USER = 0;
		
		List<User> users =  finder.select("id")
					 .where()
					 	.eq("ip", ip)
					 .findList();
		
		if(users.isEmpty())
			return null;
		
		return users.get(FIRST_USER).id;					
	}
	
	public static boolean ifIpExists(String ip)
	{
		if(User.findUserbyIp(ip) != null)
			return true;
		return false;
	}
	
	public static List<User> getAllUsers() 
	{
		return finder.all();
	}

}
