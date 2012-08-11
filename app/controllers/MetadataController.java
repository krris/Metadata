package controllers;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import models.Rating;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import utils.Metadata;
import views.html.metadata.summary;

public class MetadataController extends Controller
{
	private static FilePart uploadedFile;
	private static Metadata metadata;

	private static String rating;
	
	private static String xml;
	
	
	private static Form<Rating> ratingForm = form(Rating.class); 
	
	public static Result uploadAndProcessData()
	{
		if(upload() == true)
		{
			// TODO process data and get metadata
			metadata = processData(uploadedFile);
			xml = generateXML();
		
			// save statistics
			Statistics.saveStatistics();
			
			// and show the result on website
			return ok(summary.render(metadata.getAllMetadataAsMap(), ratingForm));
		}
		else
			return redirect(routes.Application.index()); 
	}
	
	private static boolean upload()
	{
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("file");
		
		if(filePart != null)
		{
			if(isPdfFile(filePart) == false)
			{
				flash("error", "You can upload only PDF files");
				return false;
			}
			else
			{
				uploadedFile = filePart;
				return true;
			}
		}
		else
		{
			flash("error", "Missing file");
			return false;
		}
	}
	
	private static boolean isPdfFile(FilePart filePart)
	{
		if(filePart.getContentType().equals("application/pdf"))
			return true;
		else
			return false;
	}
	
	public static String getUploadedFileName()
	{
		return metadata.getFilename();
	}
	
	public static Result downloadXML()
	{
		String filename = metadata.getFilename();
		int extensionIndex = filename.lastIndexOf(".");
		filename = filename.substring(0, extensionIndex );
		
		response().setHeader("Content-Disposition", "attachment; filename=" + filename + ".xml");
		
		return ok(xml.getBytes()).as("application/xml");
	}
	
	public static Result getRawMetadata()
	{
		if(upload() == true)
		{
			// TODO process data
			metadata = processData(uploadedFile);
			xml = generateXML();
			
			// save statistics
			Statistics.saveStatistics();
			
			// and show the result on website
			return downloadXML();
		}
		else
			return badRequest("File uploading error!");
	}
	
	/**
	 * Get rating from a form on a website and save it in the database.
	 */
	public static Result ratingHandling()
	{
		Form<Rating> filledForm= ratingForm.bindFromRequest();
		if(filledForm.hasErrors()) 
		{
		    return ok("error");
		} 
		else 
		{
			rating = filledForm.get().ratingValue;
			
			Statistics.saveRatingOfFile(convertRatingToInteger(rating));
			
			return ok(summary.render(metadata.getAllMetadataAsMap(), ratingForm));
		}
		
	}
	
	private static int convertRatingToInteger(String rating)
	{
		if(rating == null)
			return 0;
		
		return Integer.parseInt(rating);
	}
	
	private static Metadata processData(FilePart uploadedFile)
	{
		Metadata metadata = new Metadata();
		
		metadata.setFilename(uploadedFile.getFilename());
		
		return metadata;
	}

	private static String generateXML()
	{
		String xmlString = "";
		
		//Creating sample XML Document
		try{
			
			//We need a Document
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
				
			//Creating the XML tree
			
			//create the root element and add it to the document
			Element root = document.createElement("root");
			document.appendChild(root);
			
			//create a comment and put it in the root element
			Comment comment = document.createComment("Just a thought");
			root.appendChild(comment);
			
			for(String key : metadata.getAllMetadataAsMap().keySet())
			{				
				for(String elemInList : metadata.getAllMetadataAsMap().get(key))
				{
					//create child element, add an attribute, and add to root
					Element child = document.createElement(key);
					root.appendChild(child);
					//add a text element to the child
					Text text = document.createTextNode(elemInList);
					child.appendChild(text);
				}
			}
			
			//Output the XML
			
			//set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			//create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(document);
			trans.transform(source, result);
			xmlString = sw.toString();
		
		}catch (Exception e) {
				e.printStackTrace();
        }
		
		return xmlString;
	}
	
	
	
	
	
}
