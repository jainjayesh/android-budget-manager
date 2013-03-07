package de.zainodis.balancemanager.component.ui.dialog;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Category;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.persistence.CategoryPersister;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.component.ui.widget.CurrencyField;
import de.zainodis.commons.model.CurrencyAmount;

public class EditEntryDialog extends SherlockFragment {

   public static final String TAG = "EditEntryDialog";

   /** Expected as a {@link Boolean} */
   public static final String INTENT_EXTRA_IS_RECURRING = "is-recurring";
   /** Expected as a {@link String} */
   public static final String INTENT_EXTRA_CASHFLOW_DIRECTION = "cashflow-direction";
   /** Expected as a {@link Boolean} */
   public static final String INTENT_EXTRA_DISABLE_EDITING = "disable-editing";

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 return inflater.inflate(R.layout.a_edit_entry, container, false);
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);

	 // Check if extras have been passed
	 Intent intent = getSherlockActivity().getIntent();
	 boolean isEditingDisabled = intent.getBooleanExtra(INTENT_EXTRA_DISABLE_EDITING, false);

	 if (intent.hasExtra(INTENT_EXTRA_CASHFLOW_DIRECTION)) {
	    CashflowDirection direction = CashflowDirection.fromName(getSherlockActivity(),
			intent.getStringExtra(INTENT_EXTRA_CASHFLOW_DIRECTION));

	    Spinner cashflowSpinner = (Spinner) getSherlockActivity().findViewById(
			R.id.d_edit_entry_cashflow_direction);
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
	    CheckBox isRecurringBox = (CheckBox) getSherlockActivity().findViewById(
			R.id.d_edit_entry_is_recurring);
	    isRecurringBox.setChecked(intent.getBooleanExtra(INTENT_EXTRA_IS_RECURRING, false));
	    if (isEditingDisabled) {
		  isRecurringBox.setEnabled(false);
	    }
	 }

	 AutoCompleteTextView categoryField = (AutoCompleteTextView) getSherlockActivity()
		  .findViewById(R.id.d_edit_entry_category);
	 // Retrieve suggestion for the category
	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSherlockActivity(),
		  R.layout.w_suggestion, new ArrayList<String>(new CategoryPersister().getAll()));
	 // Set the suggestions via the adapter
	 categoryField.setAdapter(adapter);
	 adapter.notifyDataSetChanged();

	 // Add on click listener for save and cancel
	 getSherlockActivity().findViewById(R.id.d_edit_entry_save).setOnClickListener(
		  new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			   onSave();
			}
		  });

	 getSherlockActivity().findViewById(R.id.d_edit_entry_cancel).setOnClickListener(
		  new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			   onCancel();
			}
		  });
   }

   private void onSave() {
	 // TODO implement validity check
	 CurrencyAmount amount = ((CurrencyField) getSherlockActivity().findViewById(
		  R.id.d_edit_entry_amount)).getAmount();

	 String category = String.valueOf(((TextView) getSherlockActivity().findViewById(
		  R.id.d_edit_entry_category)).getText());
	 CashflowDirection direction = CashflowDirection.fromName(
		  getSherlockActivity(),
		  String.valueOf(((Spinner) getSherlockActivity().findViewById(
			   R.id.d_edit_entry_cashflow_direction)).getSelectedItem()));
	 boolean isRecurring = ((CheckBox) getSherlockActivity().findViewById(
		  R.id.d_edit_entry_is_recurring)).isChecked();
	 Entry entry = new Entry(direction, category, isRecurring, amount);
	 // Persist the new entry to the database
	 new EntryPersister().save(entry);
	 // Persist category
	 new CategoryPersister().save(new Category(category));

	 // Wrap this up, we're done
	 getSherlockActivity().setResult(SherlockActivity.RESULT_OK);
	 getSherlockActivity().finish();
   }

   private void onCancel() {
	 getSherlockActivity().setResult(SherlockActivity.RESULT_CANCELED);
	 getSherlockActivity().finish();
   }
}
