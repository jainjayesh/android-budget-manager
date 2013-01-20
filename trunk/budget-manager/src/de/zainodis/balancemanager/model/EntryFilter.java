package de.zainodis.balancemanager.model;

import android.content.Context;
import de.zainodis.balancemanager.R;

public enum EntryFilter {

   BY_GROUP, BY_CASHFLOW_DIRECTION, BY_DATE, NONE;

   public static EntryFilter fromName(Context context, String selectedValue) {
	 if (context.getString(R.string.group).equals(selectedValue)) {
	    return BY_GROUP;
	 } else if (context.getString(R.string.cashflow_direction).equals(selectedValue)) {
	    return BY_CASHFLOW_DIRECTION;
	 } else {
	    // The default
	    return BY_DATE;
	 }
   }
}
