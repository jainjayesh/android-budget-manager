package de.zainodis.balancemanager.model;

import android.content.Context;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;

public enum EntryFilter {

   BY_GROUP(Application.getInstance().getString(R.string.group)), BY_CASHFLOW_DIRECTION(Application
	    .getInstance().getString(R.string.cashflow_direction)), BY_DATE(Application.getInstance()
	    .getString(R.string.date)), NONE(Application.getInstance().getString(R.string.no_sorting));

   private final String name;

   private EntryFilter(String value) {
	 this.name = value;
   }

   public String getUIName() {
	 return name;
   }

   public static EntryFilter fromName(Context context, String selectedValue) {
	 if (BY_GROUP.name.equals(selectedValue)) {
	    return BY_GROUP;
	 } else if (BY_CASHFLOW_DIRECTION.name.equals(selectedValue)) {
	    return BY_CASHFLOW_DIRECTION;
	 } else if (BY_DATE.name.equals(selectedValue)) {
	    return BY_DATE;
	 } else {
	    return NONE;
	 }
   }
}
