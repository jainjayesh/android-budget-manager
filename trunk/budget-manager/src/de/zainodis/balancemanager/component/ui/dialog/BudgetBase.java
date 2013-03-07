package de.zainodis.balancemanager.component.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.LogCat;

public abstract class BudgetBase extends SherlockFragment {

   public static final String TAG = "BudgetBase";

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

   protected void startEditEntryDialog() {
	 String fragmentName = EditEntryDialog.class.getName();
	 Bundle bundle = new Bundle();
	 bundle.putBoolean(EditEntryDialog.INTENT_EXTRA_IS_RECURRING, false);
	 bundle.putBoolean(EditEntryDialog.INTENT_EXTRA_DISABLE_EDITING, false);

	 Fragment fragment = SherlockFragment.instantiate(getSherlockActivity(), fragmentName, bundle);

	 FragmentTransaction transaction = getFragmentManager().beginTransaction();
	 transaction.replace(android.R.id.content, fragment, BudgetBase.TAG);
	 transaction.addToBackStack(null);
	 transaction.commit();
   }

   protected void startEditEntryDialog(boolean isRecurring, CashflowDirection direction,
	    boolean disableEditing) {
	 Intent intent = new Intent(getSherlockActivity(), EditEntryDialog.class);
	 if (direction != null) {
	    intent.putExtra(EditEntryDialog.INTENT_EXTRA_CASHFLOW_DIRECTION, direction.getUIName());
	 }
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_IS_RECURRING, isRecurring);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_DISABLE_EDITING, disableEditing);

	 startActivityForResult(intent, RequestCodes.REQUEST_CODE_EDIT_ENTRY);
   }

   public void onAddEntry() {
	 startEditEntryDialog();
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	 super.onActivityResult(requestCode, resultCode, data);
	 switch (requestCode) {
	 case RequestCodes.REQUEST_CODE_EDIT_ENTRY:
	    switch (resultCode) {
	    case SherlockActivity.RESULT_OK:
		  LogCat.i(TAG, "New entry has been created, updating ui...");
		  // The user has modified the budget
		  updateBudgetAmount();
		  updateEntries();
		  break;
	    }
	    break;
	 }
   }

   protected void updateBudgetAmount() {
	 TextView budgetAvailable = (TextView) getSherlockActivity().findViewById(
		  R.id.w_available_budget_amount);
	 budgetAvailable.setText(new EntryPersister().getCurrentBudget().format());
   }

   protected abstract void updateEntries();

}
