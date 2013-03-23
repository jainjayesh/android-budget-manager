package de.zainodis.balancemanager.component.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.model.persistence.EntryPersister;

public abstract class BudgetBaseFragment extends SherlockFragment {

   public static final String TAG = "BudgetBase";

   @Override
   public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setRetainInstance(true);
   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	 super.onCreateContextMenu(menu, v, menuInfo);
	 MenuInflater inflater = getSherlockActivity().getMenuInflater();
	 inflater.inflate(R.menu.m_edit_entry, menu);
   }

   @Override
   public boolean onContextItemSelected(MenuItem item) {
	 Intent intent = getSherlockActivity().getIntent();
	 // Get id of the item that the user clicked on
	 long id = intent.getLongExtra(EntryDao.ID_FIELD, 0);
	 switch (item.getItemId()) {
	 case R.id.m_edit_entry_delete:
	    // Delete the selected entry
	    new EntryPersister().delete(id);
	    // Reload ui
	    updateBudgetAmount();
	    updateEntries();
	    return true;
	 default:
	    return super.onContextItemSelected(item);

	 }
   }

   protected void updateBudgetAmount() {
	 TextView budgetAvailable = (TextView) getSherlockActivity().findViewById(
		  R.id.w_available_budget_amount);
	 // Determine the amount
	 String amount = new EntryPersister().getCurrentBudget().format();
	 String text = null;

	 // Determine if a budget is available, if yes, retrieve it's start date
	 if (new BudgetCyclePersister().hasOngoingCycle()) {
	    text = String.format(getString(R.string.started_x), new BudgetCyclePersister()
			.getActiveCycle().getStartAsString());
	 } else {
	    text = getString(R.string.no_active_cycle_available);

	 }
	 budgetAvailable.setText(String.format("%s (%s)", amount, text));
   }

   protected abstract void updateEntries();

}
