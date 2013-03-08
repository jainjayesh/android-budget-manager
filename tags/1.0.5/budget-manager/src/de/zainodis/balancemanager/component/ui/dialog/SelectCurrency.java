package de.zainodis.balancemanager.component.ui.dialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.BundleAttributes;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.model.CurrencyAmount;
import de.zainodis.commons.utils.StringUtils;

/**
 * Dialog that allows a user to select a currency.
 * 
 * @author fzarrai
 * 
 */
public class SelectCurrency extends ListActivity {

   private static final String TAG = "SelectCurrency";

   private static List<String> countries;
   private static List<Locale> locales;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 // Show a warning that this will reset the current budget cycle, if there
	 // is an ongoing cycle that already has one or more entries
	 BudgetCycle cycle = new BudgetCyclePersister().getActiveCycle();
	 if (cycle != null && new EntryPersister().countEntries(cycle) > 0) {
	    // we already have entries associated with the cycle, warn the user
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.warning);
	    builder.setMessage(R.string.changing_the_currency_will_create_a_new_cycle)
			.setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
				 /*
				  * The user want's to go ahead with changing the currency,
				  * however we don't close the active cycle yet, because he
				  * may still cancel the dialog. If he selects the same
				  * currency as before, the active cycle won't be closed
				  * neither.
				  */
			   }
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
				 // Go back to the previous dialog
				 onBackPressed();
			   }
			});
	    // Create the AlertDialog object and return it
	    builder.show();
	 }

	 // Only initialise once
	 if (countries == null || locales == null) {
	    countries = new ArrayList<String>();
	    locales = new ArrayList<Locale>();
	    // Explicitly check for Locales needed to create currencies
	    Locale[] locales = NumberFormat.getAvailableLocales();
	    for (Locale locale : locales) {
		  // Java 1.5 compliance
		  if (!locale.getDisplayCountry().equals(StringUtils.EMPTY)) {
			// Append the currency symbol to the country
			String country = locale.getDisplayCountry();
			String currencySymbol = CurrencyAmount.getCurrencySymbol(locale);
			try {
			   Currency currency = Currency.getInstance(locale);
			   // Currency currency = Currency.getInstance(locale);
			   String formatted = String.format("%s,  %s, %s", country, currencySymbol,
				    currency.getCurrencyCode());
			   LogCat.i(TAG, String.format("%s, %s", country, currencySymbol));
			   SelectCurrency.countries.add(formatted);
			   SelectCurrency.locales.add(locale);
			} catch (IllegalArgumentException e) {
			   // Skip these countries
			}
		  }
	    }
	 }
	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		  android.R.layout.simple_list_item_1, countries);
	 setListAdapter(adapter);

   }

   @Override
   protected void onListItemClick(ListView l, View v, int position, long id) {
	 // The user selected a country, store pass it back to the caller
	 Locale locale = locales.get(position);
	 Intent intent = getIntent();
	 intent.putExtra(BundleAttributes.OBJECT, locale);
	 setResult(RESULT_OK, intent);
	 finish();
	 super.onListItemClick(l, v, position, id);
   }
}
