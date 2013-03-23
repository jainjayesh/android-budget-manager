package de.zainodis.balancemanager.component.ui.dialog;

import static junit.framework.Assert.assertTrue;

import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.BundleAttributes;
import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.model.options.LocaleOption;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.balancemanager.model.persistence.SettingPersister;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.component.ui.widget.DatePickerFragment;
import de.zainodis.commons.utils.DateTimeUtils;
import de.zainodis.commons.utils.StringUtils;
import de.zainodis.commons.utils.Toaster;

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
   public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setRetainInstance(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 return inflater.inflate(R.layout.a_settings, container, false);
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);

	 Button button = (Button) getSherlockActivity().findViewById(R.id.a_settings_start_cycle);
	 button.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		  onStartNewCycle();
	    }
	 });
   }

   @Override
   public void onResume() {
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
	    TextView view = (TextView) getSherlockActivity().findViewById(
			R.id.a_settings_cycle_beginning);
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
	    // End all other ongoing cycles
	    new BudgetCyclePersister().endOngoingCycles();
	    // Save the new cycle
	    BudgetCycle newCycle = new BudgetCycle(start.getTime());
	    assertTrue("Failed to save new budget cycle.", new BudgetCyclePersister().save(newCycle));
	    loadBudgetCycle(newCycle);
	 }
   };

   public void onStartNewCycle() {
	 DatePickerFragment picker = new DatePickerFragment(onBudgetBeginningSelected);
	 picker.show(getSherlockActivity().getSupportFragmentManager(), TAG);
   }

   // TODO currently inactive
   public void onSelectCurrency(View requestedBy) {
	 // Only supported by API level 10 or greater
	 if (Build.VERSION_CODES.GINGERBREAD_MR1 == Build.VERSION.SDK_INT) {
	    // Start the select currency dialog
	    // startActivityForResult(new Intent(this, SelectCurrency.class),
	    // RequestCodes.SELECT_CURRENCY_REQUEST_CODE);
	 } else {
	    Toaster.longToast(getString(R.string.feature_not_supported), getSherlockActivity());
	 }
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	 if (requestCode == RequestCodes.SELECT_CURRENCY_REQUEST_CODE) {
	    if (resultCode == SherlockActivity.RESULT_OK) {
		  // Retrieve selected locale
		  Locale locale = (Locale) data.getSerializableExtra(BundleAttributes.OBJECT);
		  // Create or update the setting
		  LocaleOption option = new LocaleOption(locale);
		  if (new SettingPersister().save(new Setting(option.getOptionName(), option.format()))) {
			LogCat.i(
				 TAG,
				 String.format("Saved option %s with value %s", option.getOptionName(),
					  option.format()));
		  }

	    }
	 }
	 super.onActivityResult(requestCode, resultCode, data);
   }

   /**
    * Loads the given budget cycle.
    * 
    * @param cycle
    */
   protected void loadBudgetCycle(BudgetCycle cycle) {
	 TextView beginning = (TextView) getSherlockActivity().findViewById(
		  R.id.a_settings_cycle_beginning);
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