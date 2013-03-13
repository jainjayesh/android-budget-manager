package de.zainodis.balancemanager.component.ui.dialog;

import static junit.framework.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.component.ui.widget.StringSelectionDialog;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.EntryFilter;
import de.zainodis.balancemanager.model.EntrySort;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.component.ui.widget.DatePickerFragment;
import de.zainodis.commons.model.CurrencyAmount;
import de.zainodis.commons.utils.DateTimeUtils;
import de.zainodis.commons.utils.StringUtils;

/**
 * Displays a budget overview of any given month. By default it shows the
 * current month/time period.
 * 
 * @author zainodis
 * 
 */
public class EditBudgetFragment extends BudgetBase {

   public static final String TAG = EditBudgetFragment.class.getName();

   private OnSettingsSelectedListener settingsSelectedListener;
   private OnAddEntrySelectedListener addEntrySelectedListener;

   // Standard settings, may be changed by user
   private EntryFilter filterBy = EntryFilter.ALL;
   private EntrySort sortBy = EntrySort.BY_CATEGORY;

   private MenuItem sortByItem;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 setHasOptionsMenu(true);
	 return inflater.inflate(R.layout.a_budget_overview, container, false);
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);

	 ActionBar actionBar = getSherlockActivity().getSupportActionBar();

	 actionBar.setDisplayShowHomeEnabled(false);
	 actionBar.setDisplayShowTitleEnabled(false);
	 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

	 // Create drop down navigation (filter view)
	 SpinnerAdapter adapter = ArrayAdapter.createFromResource(getSherlockActivity(),
		  R.array.budget_filters, R.layout.sherlock_spinner_dropdown_item);

	 actionBar.setListNavigationCallbacks(adapter, new OnNavigationListener() {

	    @Override
	    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		  String[] strings = getResources().getStringArray(R.array.budget_filters);
		  String selected = strings[itemPosition];
		  if (!filterBy.getUIName().equals(selected)) {
			filterBy = EntryFilter.fromName(selected);
			// Re-load the entries with the new filter
			updateEntries();
		  }
		  return false;
	    }
	 });
   }

   @Override
   public void onResume() {
	 super.onResume();
	 // Update current budget
	 updateBudgetAmount();
	 // Load Budget items, based on current main filter
	 updateEntries();
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	 inflater.inflate(R.menu.m_budget_overview, menu);
	 // Update names of filter and sort with current settings
	 sortByItem = menu.findItem(R.id.m_budget_overview_setSortBy);
	 sortByItem.setTitle(sortBy.getUIName());
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	 int itemId = item.getItemId();

	 if (itemId == R.id.m_budget_overview_addEntry) {
	    // The user want's to add a new entry to the budget
	    addEntrySelectedListener.onAddEntrySelected();
	    return true;

	 } else if (itemId == R.id.m_budget_overview_settings) {
	    settingsSelectedListener.onSettingsSelected();
	    return true;
	 } else if (itemId == R.id.m_budget_overview_setSortBy) {
	    // User clicked on sort by, show available options
	    final String[] sort = getResources().getStringArray(R.array.budget_sort);

	    new StringSelectionDialog(getString(R.string.filter_by), sort, getSherlockActivity(),
			new DialogInterface.OnClickListener() {

			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				 String selected = sort[which];
				 // Only update if selection has changed
				 if (!sortBy.getUIName().equals(selected)) {
				    sortBy = EntrySort.fromName(selected);
				    // Update menu item
				    sortByItem.setTitle(sortBy.getUIName());
				    // Re-load the entries with the new filter
				    updateEntries();
				 }
			   }
			});

	 } else if (itemId == R.id.m_budget_overview_new_cycle) {
	    // Start a new budget cycle
	    onStartNewCycle();
	 }

	 return super.onOptionsItemSelected(item);
   }

   public void onStartNewCycle() {
	 DatePickerFragment picker = new DatePickerFragment(onBudgetBeginningSelected);
	 picker.show(getSherlockActivity().getSupportFragmentManager(), TAG);
   }

   private final DatePickerDialog.OnDateSetListener onBudgetBeginningSelected = new DatePickerDialog.OnDateSetListener() {

	 @Override
	 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	    // Create a new budget cycle
	    Calendar start = Calendar.getInstance();
	    start.set(Calendar.YEAR, year);
	    start.set(Calendar.MONTH, monthOfYear);
	    start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    // End all other ongoing cycles
	    new BudgetCyclePersister().endOngoingCycles();
	    // Save the new cycle
	    BudgetCycle newCycle = new BudgetCycle(start.getTime());
	    assertTrue("Failed to save new budget cycle.", new BudgetCyclePersister().save(newCycle));
	 }
   };

   protected void updateEntries() {
	 // Clear stale items
	 TableLayout table = (TableLayout) getSherlockActivity().findViewById(
		  R.id.a_budget_overview_table);
	 table.removeAllViews();
	 filter(table);
   }

   protected void filter(TableLayout table) {
	 // Obtain all entries filter by specified filter
	 Collection<Entry> result = new EntryPersister().getFilteredEntries(filterBy, sortBy);

	 String currentHeader = StringUtils.EMPTY;
	 CurrencyAmount currentHeaderAmount = new CurrencyAmount(0);
	 TextView headerText = null;

	 for (final Entry entry : result) {
	    String newHeader = StringUtils.EMPTY;

	    switch (sortBy) {
	    case BY_CASHFLOW_DIRECTION:
		  newHeader = entry.getCashflowDirection().getUIName();
		  break;
	    case BY_CATEGORY:
		  newHeader = entry.getCategory();
		  break;
	    default:
		  // Covers none and by date
		  newHeader = DateTimeUtils.format(DateTimeUtils.toCalendar(entry.getDate()),
			   DateTimeUtils.DATE_FORMAT);
		  break;
	    }
	    if (!newHeader.equals(currentHeader)) {
		  // Draw the category header if it's a new category
		  TableRow header = (TableRow) getSherlockActivity().getLayoutInflater().inflate(
			   R.layout.w_table_row_header, null);
		  headerText = (TextView) header.findViewById(R.id.w_table_row_header_text);
		  headerText.setText(newHeader);
		  table.addView(header);
		  currentHeader = newHeader;
		  currentHeaderAmount = new CurrencyAmount(0);
	    }

	    if (headerText != null) {
		  if (entry.getCashflowDirection().equals(CashflowDirection.EXPENSE)) {
			currentHeaderAmount.substract(entry.getAmount());
		  } else {
			currentHeaderAmount.add(entry.getAmount());
		  }
		  // Update header each time
		  headerText.setText(String.format("%s %s", currentHeader, currentHeaderAmount.format()));
	    }

	    // Add the entry as a new row
	    TableRow row = (TableRow) getSherlockActivity().getLayoutInflater().inflate(
			R.layout.w_table_row_entry_details, null);

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
			.getCategory(), DateTimeUtils.format(DateTimeUtils.toCalendar(entry.getDate()),
			DateTimeUtils.DATE_FORMAT)));

	    // Add listener for a context menu
	    row.setOnLongClickListener(new OnLongClickListener() {

		  @Override
		  public boolean onLongClick(View v) {
			// Pretty inventive (not to say "nasty"), huh :P ?
			getSherlockActivity().setIntent(
				 getSherlockActivity().getIntent().putExtra(EntryDao.ID_FIELD, entry.getId()));
			registerForContextMenu(v);
			getSherlockActivity().openContextMenu(v);
			unregisterForContextMenu(v);
			// Display a context menu for the current entry
			return true;
		  }
	    });
	    table.addView(row);
	 }
   }

   public void setSettingsSelectedListener(OnSettingsSelectedListener listener) {
	 this.settingsSelectedListener = listener;
   }

   public void setAddEntrySelectedListener(OnAddEntrySelectedListener listener) {
	 this.addEntrySelectedListener = listener;
   }

}
