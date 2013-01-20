package de.zainodis.balancemanager.component.ui.dialog;

import java.util.Collection;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.EntryFilter;
import de.zainodis.balancemanager.model.EntryScope;
import de.zainodis.balancemanager.model.persistence.EntryDao;
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
public class BudgetOverview extends BudgetBase {
   public static final String TAG = "BudgetOverview";

   // Standard settings, may be changed by spinner
   private EntryScope scope = EntryScope.ALL;
   private EntryFilter filter = EntryFilter.BY_GROUP;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setTitle(getString(R.string.budget_overview));
	 setContentView(R.layout.a_budget_overview);

	 // Add a listener to the scope selector
	 Spinner spinner = (Spinner) findViewById(R.id.a_budget_overview_spinner_scope);
	 spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View selectedItem, int position, long id) {
		  Object selected = parent.getItemAtPosition(position);
		  String selectedValue = selected != null ? selected.toString() : null;

		  // Update scope
		  scope = EntryScope.fromName(BudgetOverview.this, selectedValue);

		  // Re-load the entries with the current filter/scope settings
		  updateEntries();
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	    }
	 });

	 // Add a listener to the filter selector
	 spinner = (Spinner) findViewById(R.id.a_budget_overview_spinner_filter);
	 spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View selectedItem, int position, long id) {
		  Object selected = parent.getItemAtPosition(position);
		  String selectedValue = selected != null ? selected.toString() : null;

		  // Update scope
		  filter = EntryFilter.fromName(BudgetOverview.this, selectedValue);

		  // Re-load the entries with the current filter/scope settings
		  updateEntries();
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	    }
	 });
   }

   @Override
   protected void onResume() {
	 super.onResume();
	 // Update current budget
	 updateBudgetAmount();
	 // Load Budget items, based on current main filter
	 updateEntries();
   }

   protected void updateEntries() {
	 // Clear stale items
	 TableLayout table = (TableLayout) findViewById(R.id.a_budget_overview_table);
	 table.removeAllViews();
	 filter(table);
   }

   protected void filter(TableLayout table) {
	 // Obtain all entries filter by specified filter
	 Collection<Entry> result = new EntryPersister().getFilteredEntries(scope, filter);

	 String currentHeader = StringUtils.EMPTY;
	 for (final Entry entry : result) {
	    String newHeader = StringUtils.EMPTY;

	    switch (filter) {
	    case BY_CASHFLOW_DIRECTION:
		  newHeader = entry.getCashflowDirection().getUIName();
		  break;
	    case BY_GROUP:
		  newHeader = entry.getGroup();
		  break;
	    default:
		  // Covers none and by date
		  newHeader = DateTimeUtils.format(DateTimeUtils.toCalendar(entry.getDate()),
			   DateTimeUtils.DATE_FORMAT);
		  break;
	    }
	    if (!newHeader.equals(currentHeader)) {
		  // Draw the group header if it's a new group
		  TableRow header = (TableRow) getLayoutInflater().inflate(R.layout.w_table_row_header,
			   null);
		  TextView text = (TextView) header.findViewById(R.id.w_table_row_header_text);
		  text.setText(newHeader);
		  table.addView(header);
		  currentHeader = newHeader;
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

	    // Add listener for a context menu
	    row.setOnLongClickListener(new OnLongClickListener() {

		  @Override
		  public boolean onLongClick(View v) {
			// Pretty inventive (not to say "nasty"), huh :P ?
			setIntent(getIntent().putExtra(EntryDao.ID_FIELD, entry.getId()));
			registerForContextMenu(v);
			openContextMenu(v);
			unregisterForContextMenu(v);
			// Display a context menu for the current entry
			return true;
		  }
	    });
	    table.addView(row);
	 }
   }
}
