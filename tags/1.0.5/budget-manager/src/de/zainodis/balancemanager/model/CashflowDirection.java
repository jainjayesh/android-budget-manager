package de.zainodis.balancemanager.model;

import android.content.Context;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;

/**
 * The direction of the cashflow.
 * 
 * @author fzarrai
 * 
 */
public enum CashflowDirection {

   INCOME(Application.getInstance().getString(R.string.income)), EXPENSE(Application.getInstance()
	    .getString(R.string.expenses));

   private final String name;

   private CashflowDirection(String value) {
	 this.name = value;
   }

   public String getUIName() {
	 return name;
   }

   public static CashflowDirection fromName(Context context, String selectedValue) {
	 if (INCOME.name.equals(selectedValue)) {
	    return INCOME;
	 } else if (EXPENSE.name.equals(selectedValue)) {
	    return EXPENSE;
	 } else {
	    throw new IllegalArgumentException("No matching cashflow direction found for "
			+ selectedValue);
	 }
   }
}
