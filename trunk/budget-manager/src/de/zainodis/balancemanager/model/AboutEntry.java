package de.zainodis.balancemanager.model;

/**
 * Represents an entry in the about section of the application. The about
 * section may have n entries, each of which belongs to a specific group.
 * 
 * @author fzarrai
 * 
 */
public class AboutEntry implements Comparable<AboutEntry> {

   private final String text;
   private final String category;

   public AboutEntry(String category, String text) {
	 this.text = text;
	 this.category = category;
   }

   public String getText() {
	 return text;
   }

   public String getCategory() {
	 return category;
   }

   @Override
   public int compareTo(AboutEntry another) {
	 if (another == null) {
	    return 1;
	 }

	 return category.compareTo(another.getCategory());
   }
}
