package de.zainodis.balancemanager.component.ui.dialog;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
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
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.balancemanager.model.persistence.GroupPersister;
import de.zainodis.commons.component.ui.widget.CurrencyField;
import de.zainodis.commons.model.CurrencyAmount;

public class EditEntryDialog extends Dialog {

   public EditEntryDialog(Context context) {
	 super(context);
	 setTitle(getContext().getString(R.string.edit_entry));
	 setContentView(R.layout.d_edit_entry);

	 AutoCompleteTextView groupField = (AutoCompleteTextView) findViewById(R.id.d_edit_entry_group);
	 // Retrieve suggestion for the group
	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
		  android.R.layout.simple_list_item_1, new ArrayList<String>(
			   new GroupPersister().getAll()));
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

   /**
    * Initialises isMonthly and the {@link CashflowDirection}.
    * 
    * @param context
    *           the {@link Context}.
    * @param isMonthly
    *           true if monthly; false otherwise.
    * @param direction
    *           the cashflow direction.
    * @param disableEditing
    *           true if isMonthly and direction should not be editable; false
    *           otherwise.
    */
   public EditEntryDialog(Context context, boolean isMonthly, CashflowDirection direction,
	    boolean disableEditing) {
	 this(context);

	 Spinner cashflowSpinner = (Spinner) findViewById(R.id.d_edit_entry_cashflow_direction);
	 SpinnerAdapter adapter = cashflowSpinner.getAdapter();
	 String cashflowDirection = direction.getLocalized();

	 for (int i = 0; i < adapter.getCount(); i++) {
	    if (cashflowDirection.equals(adapter.getItem(i).toString())) {
		  // We found it, select this item
		  cashflowSpinner.setSelection(i);
	    }
	 }

	 CheckBox isMonthlyBox = (CheckBox) findViewById(R.id.d_edit_entry_is_monthly);
	 isMonthlyBox.setChecked(isMonthly);

	 if (disableEditing) {
	    cashflowSpinner.setEnabled(false);
	    isMonthlyBox.setEnabled(false);
	 }
   }

   private void onSave() {
	 CurrencyAmount amount = ((CurrencyField) findViewById(R.id.d_edit_entry_amount)).getAmount();
	 String group = String.valueOf(((TextView) findViewById(R.id.d_edit_entry_group)).getText());
	 CashflowDirection direction = CashflowDirection.parse(String
		  .valueOf(((Spinner) findViewById(R.id.d_edit_entry_cashflow_direction))
			   .getSelectedItem()));
	 boolean isMonthly = ((CheckBox) findViewById(R.id.d_edit_entry_is_monthly)).isChecked();
	 Entry entry = new Entry(direction, group, isMonthly, amount);
	 // Persist the new entry to the database
	 new EntryPersister().save(entry);
	 // Close the dialog
	 dismiss();
   }

   private void onCancel() {
	 // Simply close the dialog and discard the changes
	 dismiss();
   }
}
