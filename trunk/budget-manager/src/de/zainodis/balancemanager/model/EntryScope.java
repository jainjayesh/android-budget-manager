package de.zainodis.balancemanager.model;

import android.content.Context;
import de.zainodis.balancemanager.R;

public enum EntryScope {
   RECURRING, NON_RECURRING, ALL;

   public static EntryScope fromName(Context context, String selectedValue) {
	 if (context.getString(R.string.recurring_entries).equals(selectedValue)) {
	    return RECURRING;
	 }
	 if (context.getString(R.string.non_recurrent_entries).equals(selectedValue)) {
	    return NON_RECURRING;
	 } else {
	    // The default
	    return ALL;
	 }
   }
}
