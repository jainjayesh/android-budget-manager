package de.zainodis.balancemanager.component.ui.dialog;

import static junit.framework.Assert.assertTrue;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.component.ui.widget.DatePickerFragment;
import de.zainodis.commons.utils.DateTimeUtils;
import de.zainodis.commons.utils.StringUtils;

/**
 * Displayed when the application is started. If the application has not yet
 * been configured, the appropriate measures will be taken now. If the
 * application has already been configured, an overview is displayed.
 * 
 * @author zainodis
 * 
 */
public class BudgetSettings extends BudgetBase {
   private static final String TAG = "Settings";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setTitle(getString(R.string.budget_settings));
	 setContentView(R.layout.a_budget_settings);

	 // Add listener for adding income
	 ImageButton button = (ImageButton) findViewById(R.id.a_budget_settings_add_income);
	 button.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		  startEditEntryDialog(true, CashflowDirection.INCOME, true);
	    }
	 });

	 // Add listener for adding expenses
	 button = (ImageButton) findViewById(R.id.a_budget_settings_add_expense);
	 button.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		  startEditEntryDialog(true, CashflowDirection.EXPENSE, true);
	    }
	 });

   }

   @Override
   protected void onResume() {
	 super.onResume();
	 // Load current budget beginning
	 BudgetCycle cycle = new BudgetCyclePersister().getActiveCycle();
	 if (cycle != null) {
	    LogCat.i(TAG, "Loaded existing budget cycle beginning.");
	    startNewCycle(cycle);
	    updateEntries();
	 }

   }

   private final DatePickerDialog.OnDateSetListener onBudgetBeginningSelected = new DatePickerDialog.OnDateSetListener() {

	 @Override
	 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	    // Create a new budget cycle
	    Calendar start = Calendar.getInstance();
	    start.set(Calendar.YEAR, year);
	    start.set(Calendar.MONTH, monthOfYear);
	    start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    // End other ongoing cycles
	    new BudgetCyclePersister().endOngoingCycles();
	    // Save the new cycle
	    BudgetCycle newCycle = new BudgetCycle(start.getTime());
	    startNewCycle(newCycle);
	 }
   };

   protected void startNewCycle(BudgetCycle cycle) {
	 TextView beginning = (TextView) findViewById(R.id.a_budget_settings_cycle_beginning);
	 // End all other ongoing cycles
	 new BudgetCyclePersister().endOngoingCycles();
	 // Save the new cycle
	 assertTrue("Failed to save new budget cycle.", new BudgetCyclePersister().save(cycle));
	 if (cycle == null) {
	    // Reset the fields
	    beginning.setText(StringUtils.EMPTY);
	 } else {
	    beginning.setText(DateTimeUtils.format(DateTimeUtils.toCalendar(cycle.getStart()),
			DateTimeUtils.DATE_FORMAT));
	    // So the writing is visible
	    beginning.setTextColor(Color.LTGRAY);
	 }
	 // Update the budget cycle amounts
	 updateBudgetAmount();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	 MenuInflater inflater = getMenuInflater();
	 inflater.inflate(R.menu.m_budget_settings, menu);
	 return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
	 case R.id.m_budget_settings_start_new_cycle:
	    // Display the date picker
	    DialogFragment newFragment = new DatePickerFragment(onBudgetBeginningSelected);
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	    return true;
	 case R.id.m_budget_settings_budget_overview:
	    // Open budget settings dialog
	    startActivity(new Intent(this, BudgetOverview.class));
	    return true;
	 default:
	    return super.onOptionsItemSelected(item);
	 }
   }

   @Override
   protected void updateEntries() {
	 // Append existing incomes and expenses to their respective layouts
	 LinearLayout incomeLayout = (LinearLayout) findViewById(R.id.a_budget_settings_income_block);
	 LinearLayout expenseLayout = (LinearLayout) findViewById(R.id.a_budget_settings_expenses_block);

	 // Cleanup old fragments
	 incomeLayout.removeAllViews();
	 expenseLayout.removeAllViews();

	 for (final Entry entry : new EntryPersister().getEntries(true)) {

	    LinearLayout entryLayout = (LinearLayout) getLayoutInflater().inflate(
			R.layout.w_table_remove_text, null);
	    TextView entryDetails = (TextView) entryLayout
			.findViewById(R.id.w_table_remove_text_label);
	    entryDetails.setText(String.format(getString(R.string.entry_details), entry.getAmount()
			.format(), entry.getGroup(), entry.getFormattedDate()));
	    entryLayout.findViewById(R.id.w_table_remove_text_button).setOnClickListener(
			new OnClickListener() {

			   @Override
			   public void onClick(View v) {
				 // Listen for delete events
				 new EntryPersister().delete(entry.getId());
				 updateBudgetAmount();
				 updateEntries();
			   }
			});

	    if (CashflowDirection.INCOME.equals(entry.getCashflowDirection())) {
		  incomeLayout.addView(entryLayout);
	    } else {
		  expenseLayout.addView(entryLayout);
	    }
	 }
   }

}
