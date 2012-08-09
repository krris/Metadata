package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Page;

/**
 * File entity managed by Ebean
 */
@Entity
public class File 
{
	@Id
	public Long id;
	
	@Required
	public Long userId;
	
	@Required
	public String filename;
	
	@Formats.DateTime(pattern="yyyy/MM/dd")
	public Date dateOfUploading;
	
	@Min(0) @Max(5)
	public int rate;
	
    /**
     * Generic query helper for entity Computer with id Long
     */
	public static Finder<Long, File> finder = new Finder<Long, File>(Long.class, File.class);
	
    /**
     * Return a page of files
     *
     * @param page Page to display
     * @param pageSize Number of files per page
     * @param sortBy File property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
	public static Page<File> page(int page, int pageSize, String sortBy, String order, String filenameFilter)
	{
		return finder.where()
                .ilike("filename", "%" + filenameFilter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(pageSize)
                .getPage(page);
	}
	
	public static List<File> getAllFiles() 
	{
		return finder.all();
	}
}
