package de.zainodis.balancemanager.model;

import android.content.Context;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;

public enum EntryScope {
   RECURRING(Application.getInstance().getString(R.string.recurring_entries)), NON_RECURRING(
	    Application.getInstance().getString(R.string.non_recurrent_entries)), ALL(Application
	    .getInstance().getString(R.string.all));

   private final String name;

   private EntryScope(String value) {
	 this.name = value;
   }

   public String getUIName() {
	 return name;
   }

   public static EntryScope fromName(Context context, String selectedValue) {
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
