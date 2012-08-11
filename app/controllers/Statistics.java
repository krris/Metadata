package controllers;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import utils.SXRequest;

import models.File;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.statistics.summary;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class Statistics extends Controller
{
	// holds id of a user
	private static Long currentUserId = 0L;
	// holds id of an uploaded file
	private static Long currentFileId = 0L;
	
    /**
     * Display the paginated list of files and a list of users.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
	public static Result list(int nrOfFilePage, String fileSortBy, String fileOrder, String filenameFilter)
	{
		int PAGE_SIZE = 10;
		
		Page<File> pageFile = File.page(nrOfFilePage, PAGE_SIZE, fileSortBy, fileOrder, filenameFilter);
		List<User> userList = User.getAllUsers();
		return ok(summary.render(pageFile, fileSortBy, fileOrder, filenameFilter, userList));
	}
	
	/**
	 * Save information about a user (ip, number of visitis) and
	 * information about a file (filename, date of uploading). 
	 * A rating is not saved.
	 */
	public static void saveStatistics()
	{
		String clientIp = getClientIp();
		
		if(User.ifIpExists(clientIp) == false)
		{
			createNewUser(clientIp);
		}
		currentUserId = User.findUserbyIp(clientIp);
		increaseNumberOfVisits(currentUserId);
		
		createNewFile(currentUserId);
	
	}
	
	public static String getClientIp()
	{
		// DO NOT use request().remoteAddress() from play.mvc.Http.RequestHeader.
		// It gives a wrong answer!
		return SXRequest.getRemoteAddress();
	}
	
	/**
	 * Save the raitng of a file in database.
	 * @param rating
	 */
	public static void saveRatingOfFile(int rating)
	{
		File file = File.finder.byId(currentFileId);
		file.rate = rating;
		Ebean.save(file);
	}
	
	private static void createNewUser(String ip)
	{
		User user = new User();
		user.ip = ip;
		user.numberOfVisits = 0L;
		Ebean.save(user);
	}
	
	private static void increaseNumberOfVisits(Long userId)
	{
		User user = User.finder.byId(userId);
		user.numberOfVisits++;
		Ebean.save(user);
	}
	
	private static void createNewFile(Long userId)
	{
		File file = new File();
		
		file.userId = userId;
		file.filename = MetadataController.getUploadedFileName();
		
		Calendar cal = new GregorianCalendar();
		file.dateOfUploading = cal.getTime();
			
		Ebean.save(file);
		
		currentFileId = file.id;
	}
	


}
