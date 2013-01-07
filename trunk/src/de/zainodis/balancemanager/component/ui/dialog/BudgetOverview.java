package de.zainodis.balancemanager.component.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import de.zainodis.balancemanager.R;

/**
 * Displays a budget overview of any given month. By default it shows the
 * current month/time period.
 * 
 * @author zainodis
 * 
 */
public class BudgetOverview extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setTitle(getString(R.string.settings));

   }

}
