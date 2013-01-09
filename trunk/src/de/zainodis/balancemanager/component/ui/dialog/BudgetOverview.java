package de.zainodis.balancemanager.component.ui.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.persistence.EntryPersister;

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
	 setTitle(getString(R.string.budget_overview));
	 setContentView(R.layout.a_budget_overview);

	 // Fill the layout with data

   }

   protected void updateAvailableBudget() {
	 /*
	  * TODO move to common base class and include the budget overview layout
	  * instead of copy pasting it into BudgetSettings.
	  */
	 TextView budgetAvailable = (TextView) findViewById(R.id.w_available_budget_amount);
	 budgetAvailable.setText(new EntryPersister().getCurrentBudget().format());

   }
}
