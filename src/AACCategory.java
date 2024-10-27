import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.*;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Alyssa Ryan
 *
 */
public class AACCategory implements AACPage {

	public String name;
	public AssociativeArray<String, String> category;
	
	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		category = new AssociativeArray<String,String>();
		this.name = name;
	}
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text){
		imageLoc = ">" + imageLoc;
		try{
			category.set(imageLoc, text);
		} catch (Exception e) {
			
		} //try/catch
	}

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		String[] locs = new String[category.size];
		for(int i=0; i<category.size; i++){
			locs[i] = category.returnKey(i).substring(1);
		}
		return locs;
	}

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return name;
	}

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws KeyNotFoundException 
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc){
		try{
			imageLoc = ">" + imageLoc;
			int index = category.find(imageLoc);
			return category.returnVal(index);
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return category.hasKey(imageLoc);
	}
}
