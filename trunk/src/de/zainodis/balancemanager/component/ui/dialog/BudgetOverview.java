package de.zainodis.balancemanager.component.ui.dialog;

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.BudgetViewFilter;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.utils.DateTimeUtils;
import de.zainodis.commons.utils.StringUtils;

/**
 * Displays a budget overview of any given month. By default it shows the
 * current month/time period.
 * 
 * @author zainodis
 * 
 */
public class BudgetOverview extends Activity {

   private BudgetViewFilter filter = BudgetViewFilter.BY_GROUPS;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setTitle(getString(R.string.budget_overview));
	 setContentView(R.layout.a_budget_overview);
   }

   @Override
   protected void onResume() {
	 super.onResume();
	 // Update current budget
	 updateBudgetAmount();
	 // Load Budget items, based on current main filter
	 loadBudgetItems();
   }

   protected void loadBudgetItems() {
	 // Clear stale items
	 TableLayout table = (TableLayout) findViewById(R.id.a_budget_overview_table);
	 table.removeAllViews();
	 switch (filter) {
	 case BY_CASHFLOW_DIRECTION:
	    break;
	 // TODO
	 case BY_GROUPS:
	    displayFilteredByGroups(table);
	    break;
	 }
   }

   protected void displayFilteredByGroups(TableLayout table) {
	 // Obtain all entries, ordered by their groups for the current cycle
	 Collection<Entry> result = new EntryPersister().getEntriesByGroup();

	 String currentGroup = StringUtils.EMPTY;
	 for (final Entry entry : result) {
	    if (!entry.getGroup().equals(currentGroup)) {
		  // Draw the group header if it's a new group
		  TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.w_table_row_header,
			   null);
		  TextView text = (TextView) header.findViewById(R.id.w_table_row_header_text);
		  text.setText(entry.getGroup());
		  table.addView(header);
	    }

	    // Add the entry as a new row
	    TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.w_table_row_entry_details,
			null);

	    // Set icon cashflows direction
	    ImageView image = (ImageView) row
			.findViewById(R.id.w_table_row_entry_details_cashflow_button);
	    if (CashflowDirection.EXPENSE.equals(entry.getCashflowDirection())) {
		  image.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
	    } else {
		  image.setBackgroundDrawable(getResources().getDrawable(R.drawable.plus));
	    }

	    // Set text for rows
	    TextView text = (TextView) row.findViewById(R.id.w_table_row_entry_details_text);
	    text.setText(String.format(getString(R.string.entry_details), entry.getAmount(), entry
			.getGroup(), DateTimeUtils.format(DateTimeUtils.toCalendar(entry.getDate()),
			DateTimeUtils.DATE_FORMAT)));

	    // Add on click listener for the remove
	    row.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
			// TODO remove entry and reload budget & items
		  }
	    });

	    table.addView(row);
	 }
   }

   protected void updateBudgetAmount() {
	 /*
	  * TODO move to common base class and include the budget overview layout
	  * instead of copy pasting it into BudgetSettings.
	  */
	 TextView budgetAvailable = (TextView) findViewById(R.id.w_available_budget_amount);
	 budgetAvailable.setText(new EntryPersister().getCurrentBudget().format());
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	 MenuInflater inflater = getMenuInflater();
	 inflater.inflate(R.menu.m_budget_overview, menu);
	 return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
	 case R.id.m_budget_overview_add_entry:
	    // Open entry dialog
	    startEditEntryDialog();
	    return true;
	 case R.id.m_budget_overview_budget_settings:
	    // Open budget settings dialog
	    startActivity(new Intent(this, BudgetSettings.class));
	    return true;
	 default:
	    return super.onOptionsItemSelected(item);
	 }
   }

   // TODO copied from budget settings, move to common base class
   protected void startEditEntryDialog() {
	 Intent intent = new Intent(BudgetOverview.this, EditEntryDialog.class);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_IS_MONTHLY, false);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_DISABLE_EDITING, false);

	 startActivityForResult(intent, EditEntryDialog.REQUEST_CODE_EDIT_ENTRY);
   }

   // TODO copied from budget settings, move to common base class
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 super.onActivityResult(requestCode, resultCode, data);
	 switch (requestCode) {
	 case EditEntryDialog.REQUEST_CODE_EDIT_ENTRY:
	    switch (resultCode) {
	    case RESULT_OK:
		  // The user has modified the budget
		  updateBudgetAmount();
		  loadBudgetItems();
		  break;
	    }
	    break;
	 }
   }
}