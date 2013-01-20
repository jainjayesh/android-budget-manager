package de.zainodis.balancemanager.component.ui.dialog;

import static junit.framework.Assert.assertTrue;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
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
public class Settings extends BudgetBase {

   public static final String TAG = "Settings";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setTitle(getString(R.string.settings));
	 setContentView(R.layout.a_settings);
   }

   @Override
   protected void onResume() {
	 super.onResume();
	 /*
	  * Set the content layout depending on whether we have an ongoing cycle or
	  * not.
	  */
	 BudgetCycle cycle = new BudgetCyclePersister().getActiveCycle();
	 if (cycle != null) {
	    LogCat.i(TAG, "Loading existing budget cycle beginning.");
	    loadBudgetCycle(cycle);
	 } else {
	    TextView view = (TextView) findViewById(R.id.a_settings_cycle_beginning);
	    view.setText(getString(R.string.no_active_cycle_available));
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
	    // Switch content view
	    setContentView(R.layout.a_settings);
	    // End all other ongoing cycles
	    new BudgetCyclePersister().endOngoingCycles();
	    // Save the new cycle
	    BudgetCycle newCycle = new BudgetCycle(start.getTime());
	    assertTrue("Failed to save new budget cycle.", new BudgetCyclePersister().save(newCycle));
	    loadBudgetCycle(newCycle);
	 }
   };

   public void onStartNewCycle(View requestedBy) {
	 DatePickerFragment picker = new DatePickerFragment(onBudgetBeginningSelected);
	 picker.show(getSupportFragmentManager(), TAG);
   }

   /**
    * Loads the given budget cycle.
    * 
    * @param cycle
    */
   protected void loadBudgetCycle(BudgetCycle cycle) {
	 TextView beginning = (TextView) findViewById(R.id.a_settings_cycle_beginning);
	 if (cycle == null) {
	    // Reset the fields
	    beginning.setText(StringUtils.EMPTY);
	 } else {
	    beginning.setText(DateTimeUtils.format(DateTimeUtils.toCalendar(cycle.getStart()),
			DateTimeUtils.DATE_FORMAT));
	    // So the writing is visible
	    beginning.setTextColor(Color.LTGRAY);
	 }
   }

   @Override
   protected void updateEntries() {

   }

}