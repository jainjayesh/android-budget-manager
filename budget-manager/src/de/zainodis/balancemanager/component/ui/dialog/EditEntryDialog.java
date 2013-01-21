package de.zainodis.balancemanager.component.ui.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.Group;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.balancemanager.model.persistence.GroupPersister;
import de.zainodis.commons.component.ui.widget.SplitCurrencyField;
import de.zainodis.commons.model.CurrencyAmount;

public class EditEntryDialog extends Activity {

   public static final int REQUEST_CODE_EDIT_ENTRY = 1;
   /** Expected as a {@link Boolean} */
   public static final String INTENT_EXTRA_IS_MONTHLY = "is-monthly";
   /** Expected as a {@link String} */
   public static final String INTENT_EXTRA_CASHFLOW_DIRECTION = "cashflow-direction";
   /** Expected as a {@link Boolean} */
   public static final String INTENT_EXTRA_DISABLE_EDITING = "disable-editing";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setTitle(getString(R.string.edit_entry));
	 setContentView(R.layout.a_edit_entry);

	 // Check if extras have been passed
	 Intent intent = getIntent();
	 boolean isEditingDisabled = intent.getBooleanExtra(INTENT_EXTRA_DISABLE_EDITING, false);

	 if (intent.hasExtra(INTENT_EXTRA_CASHFLOW_DIRECTION)) {
	    CashflowDirection direction = CashflowDirection.fromName(this,
			intent.getStringExtra(INTENT_EXTRA_CASHFLOW_DIRECTION));

	    Spinner cashflowSpinner = (Spinner) findViewById(R.id.d_edit_entry_cashflow_direction);
	    SpinnerAdapter adapter = cashflowSpinner.getAdapter();
	    String cashflowDirection = direction.getUIName();

	    for (int i = 0; i < adapter.getCount(); i++) {
		  if (cashflowDirection.equals(adapter.getItem(i).toString())) {
			// We found it, select this item
			cashflowSpinner.setSelection(i);
		  }
	    }
	    if (isEditingDisabled) {
		  cashflowSpinner.setEnabled(false);
	    }
	 }

	 if (intent.hasExtra(INTENT_EXTRA_IS_MONTHLY)) {
	    CheckBox isMonthlyBox = (CheckBox) findViewById(R.id.d_edit_entry_is_monthly);
	    isMonthlyBox.setChecked(intent.getBooleanExtra(INTENT_EXTRA_IS_MONTHLY, false));
	    if (isEditingDisabled) {
		  isMonthlyBox.setEnabled(false);
	    }
	 }

	 AutoCompleteTextView groupField = (AutoCompleteTextView) findViewById(R.id.d_edit_entry_group);
	 // Retrieve suggestion for the group
	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.w_list_item,
		  new ArrayList<String>(new GroupPersister().getAll()));
	 // Set the suggestions via the adapter
	 groupField.setAdapter(adapter);
	 adapter.notifyDataSetChanged();

	 // Add on click listener for save and cancel
	 findViewById(R.id.d_edit_entry_save).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		  onSave();
	    }
	 });

	 findViewById(R.id.d_edit_entry_cancel).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		  onCancel();
	    }
	 });
   }

   private void onSave() {
	 // TODO implement validity check
	 CurrencyAmount amount = ((SplitCurrencyField) findViewById(R.id.d_edit_entry_amount))
		  .getAmount();

	 String group = String.valueOf(((TextView) findViewById(R.id.d_edit_entry_group)).getText());
	 CashflowDirection direction = CashflowDirection.fromName(this, String
		  .valueOf(((Spinner) findViewById(R.id.d_edit_entry_cashflow_direction))
			   .getSelectedItem()));
	 boolean isMonthly = ((CheckBox) findViewById(R.id.d_edit_entry_is_monthly)).isChecked();
	 Entry entry = new Entry(direction, group, isMonthly, amount);
	 // Persist the new entry to the database
	 new EntryPersister().save(entry);
	 // Persist group
	 new GroupPersister().save(new Group(group));

	 // Wrap this up, we're done
	 setResult(RESULT_OK);
	 finish();
   }

   private void onCancel() {
	 setResult(RESULT_CANCELED);
	 finish();
   }
}
