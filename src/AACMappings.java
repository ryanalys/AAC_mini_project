import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & Alyssa Ryan
 *
 */
public class AACMappings implements AACPage {
	
	public AssociativeArray<String, AACCategory> AACMap;
	public AACCategory defCat;
	public AACCategory current;
	

	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 * @throws FileNotFoundException 
	 */
	public AACMappings(String filename) throws FileNotFoundException{		
		AACMap = new AssociativeArray<String,AACCategory>();
		defCat = new AACCategory("");
		current = defCat;
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		try {
			while ((line = reader.readLine()) != null && line.isEmpty() == false){
				//Determines if the line is talking about an image or a category
				String[] parts = line.split(" ");
				if (line.contains(">")) {
					String imageLoc = parts[0].substring(1);
					String itemName = parts[parts.length-1];
					current.addItem(imageLoc, itemName);
				} else {
					reset();
					String catLoc = parts[0];
					String catName = parts[parts.length-1];
					AACCategory newCat = new AACCategory(catName);
					try {
						AACMap.set(catLoc, newCat);
					} catch (NullKeyException e) {

					}
					current = newCat;
				} //Add category or item to the aac
			}
			reader.close();
			reset();
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
		//If the imageLoc is for an image, it will have > at the beginning
		String loc = ">" + imageLoc;
		try{
			if (AACMap.hasKey(imageLoc) == false && current.hasImage(loc) == false) {
				throw new NoSuchElementException();
			} else {
				if(AACMap.hasKey(imageLoc) == true) {
					//The imageLoc provided is in the current directory, and it is a category
					int index = AACMap.find(imageLoc);
					current = AACMap.returnVal(index);
					return "";
				} else if (current.hasImage(loc)) {
					//The imageLoc provided is in the current directory, and it is an image
					int index = current.category.find(imageLoc);
					return current.category.returnVal(index);
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
	}
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		String[] imageLocs;
		if(current.name.equals("")){
			imageLocs = new String[AACMap.size];
			for(int i=0; i<AACMap.size; i++){
				imageLocs[i] = AACMap.returnKey(i);
			}
		} else{
			imageLocs = current.getImageLocs();
		}
		return imageLocs;
	}
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		current = defCat;
	}
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
		for(int i=0; i<AACMap.size; i++) {
			filename = filename + AACMap.returnKey(i) + " " + AACMap.returnVal(i).name + "\n";
			current = AACMap.returnVal(i);
			for(int j = 0; j < AACMap.returnVal(i).category.size; j++){
				filename = filename + current.category.returnKey(j) + " " + current.category.returnVal(j) + "\n";
			}
		}
	}
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		imageLoc = ">" + imageLoc;
		current.addItem(imageLoc, text);
	}


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
		return current.name;
	}


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		return current.category.hasKey(imageLoc);
	}
}
