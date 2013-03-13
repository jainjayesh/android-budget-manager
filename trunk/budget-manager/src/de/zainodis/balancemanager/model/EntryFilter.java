package de.zainodis.balancemanager.model;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;

/**
 * Filters entries depending on whether they're recurring, non-recurring etc..
 * 
 * @author fzarrai
 * 
 */
public enum EntryFilter {
   RECURRING(Application.getInstance().getString(R.string.recurring_entries)), NON_RECURRING(
	    Application.getInstance().getString(R.string.non_recurrent_entries)), ALL(Application
	    .getInstance().getString(R.string.all));

   private final String name;

   private EntryFilter(String value) {
	 this.name = value;
   }

   public String getUIName() {
	 return name;
   }

   public static EntryFilter fromName(String selectedValue) {
	 if (RECURRING.name.equals(selectedValue)) {
	    return RECURRING;
	 }
	 if (NON_RECURRING.name.equals(selectedValue)) {
	    return NON_RECURRING;
	 } else {
	    // The default
	    return ALL;
	 }
   }
}
