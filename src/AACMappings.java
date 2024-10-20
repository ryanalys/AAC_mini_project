import edu.grinnell.csc207.util;
import java.io.BufferedReader;
import java.io.StringWriter;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & YOUR NAME HERE
 *
 */
public class AACMappings implements AACPage {
	
	public static AssociativeArray<String, AACCategory> AACMap;
	public static AssociativeArray<String, String> defCat;
	public static AssociativeArray<String, String> current;
	

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
	 */
	public AACMappings(String filename) {
		defCat.name = "";
		
		BufferedReader reader = new BufferedReader(new StringReader(filename));
		String line;
		while (line = reader.nextLine() != null){
			//Determines if the line is talking about an image or a category
			String[] parts = line.split(" ");
			if (line.charAt(0) == '>') {
				String imageLoc = parts[0];
				String itemName = parts[1];
				current.addItem(imageLoc, itemName);
			} else {
				reset();
				String catLoc = parts[0];
				String catName = parts[1];
				AACCategory newCat = new AACCategory(catName);
				AACMap.addItem(catName, newCat);
				current = newCat;
			} //Add category or item to the aac
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
		if (current.hasKey(imageLoc) == false) {
			throw NoSuchElementException;
		} else if (current.key.equals("")) {
			int index = AACMap.find(imageLoc);
			current = pairs[index].value;
			return "";
		} else {
			return current.get(imageLoc);
		}
		return "";
	}
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		String[] imageLocs = current.getImageLocs();
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
			filename = filename + AACMap.pairs[i].key + " " + AACMap.pairs[i].value.name + "\n";
			current = AACMap.pairs[i].value;
			for(int j = 0; j < AACMap.pairs[i].value.size; j++){
				filename = filename + current.key + " " + current.value + "\n";
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
		return current.hasKey(imageLoc);
	}
}
