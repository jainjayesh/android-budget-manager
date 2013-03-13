package de.zainodis.balancemanager.model;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;

/**
 * Sorts entries by the given criterion.
 * 
 * @author fzarrai
 * 
 */
public enum EntrySort {

   BY_CATEGORY(Application.getInstance().getString(R.string.category)), BY_CASHFLOW_DIRECTION(
	    Application.getInstance().getString(R.string.cashflow_direction)), BY_DATE(Application
	    .getInstance().getString(R.string.date)), NONE(Application.getInstance().getString(
	    R.string.no_sorting));

   private final String name;

   private EntrySort(String value) {
	 this.name = value;
   }

   public String getUIName() {
	 return name;
   }

   public static EntrySort fromName(String selectedValue) {
	 if (BY_CATEGORY.name.equals(selectedValue)) {
	    return BY_CATEGORY;
	 } else if (BY_CASHFLOW_DIRECTION.name.equals(selectedValue)) {
	    return BY_CASHFLOW_DIRECTION;
	 } else if (BY_DATE.name.equals(selectedValue)) {
	    return BY_DATE;
	 } else {
	    return NONE;
	 }
   }
}
