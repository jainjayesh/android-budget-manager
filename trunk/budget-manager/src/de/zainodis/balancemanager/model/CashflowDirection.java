package de.zainodis.balancemanager.model;

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

   private final String localized;

   private CashflowDirection(String name) {
	 localized = name;
   }

   public String getLocalized() {
	 return localized;
   }

   public static CashflowDirection parse(String value) throws IllegalArgumentException {
	 if (value == null || value.isEmpty()) {
	    throw new IllegalArgumentException(
			"Must provide a value when parsing the cashflow direction.");
	 }

	 if (value.equals(INCOME.getLocalized())) {
	    return INCOME;
	 } else if (value.equals(EXPENSE.getLocalized())) {
	    return EXPENSE;
	 }
	 throw new IllegalArgumentException("No matching cashflow direction found for " + value);

   }
}
