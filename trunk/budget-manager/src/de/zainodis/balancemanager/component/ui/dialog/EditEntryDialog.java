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
import de.zainodis.balancemanager.model.Category;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.balancemanager.model.persistence.CategoryPersister;
import de.zainodis.commons.component.ui.widget.CurrencyField;
import de.zainodis.commons.model.CurrencyAmount;

public class EditEntryDialog extends Activity {

   /** Expected as a {@link Boolean} */
   public static final String INTENT_EXTRA_IS_RECURRING = "is-recurring";
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

	 if (intent.hasExtra(INTENT_EXTRA_IS_RECURRING)) {
	    CheckBox isRecurringBox = (CheckBox) findViewById(R.id.d_edit_entry_is_recurring);
	    isRecurringBox.setChecked(intent.getBooleanExtra(INTENT_EXTRA_IS_RECURRING, false));
	    if (isEditingDisabled) {
		  isRecurringBox.setEnabled(false);
	    }
	 }

	 AutoCompleteTextView categoryField = (AutoCompleteTextView) findViewById(R.id.d_edit_entry_category);
	 // Retrieve suggestion for the category
	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.w_suggestion,
		  new ArrayList<String>(new CategoryPersister().getAll()));
	 // Set the suggestions via the adapter
	 categoryField.setAdapter(adapter);
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
	 CurrencyAmount amount = ((CurrencyField) findViewById(R.id.d_edit_entry_amount)).getAmount();

	 String category = String.valueOf(((TextView) findViewById(R.id.d_edit_entry_category))
		  .getText());
	 CashflowDirection direction = CashflowDirection.fromName(this, String
		  .valueOf(((Spinner) findViewById(R.id.d_edit_entry_cashflow_direction))
			   .getSelectedItem()));
	 boolean isRecurring = ((CheckBox) findViewById(R.id.d_edit_entry_is_recurring)).isChecked();
	 Entry entry = new Entry(direction, category, isRecurring, amount);
	 // Persist the new entry to the database
	 new EntryPersister().save(entry);
	 // Persist category
	 new CategoryPersister().save(new Category(category));

	 // Wrap this up, we're done
	 setResult(RESULT_OK);
	 finish();
   }

   private void onCancel() {
	 setResult(RESULT_CANCELED);
	 finish();
   }
}
