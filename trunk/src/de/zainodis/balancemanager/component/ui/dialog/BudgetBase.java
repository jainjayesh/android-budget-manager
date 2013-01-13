package de.zainodis.balancemanager.component.ui.dialog;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.model.persistence.EntryPersister;

public abstract class BudgetBase extends FragmentActivity {

   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	 super.onCreateContextMenu(menu, v, menuInfo);
	 MenuInflater inflater = getMenuInflater();
	 inflater.inflate(R.menu.m_edit_entry, menu);
   }

   @Override
   public boolean onContextItemSelected(MenuItem item) {
	 Intent intent = getIntent();
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
	 Intent intent = new Intent(this, EditEntryDialog.class);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_IS_MONTHLY, false);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_DISABLE_EDITING, false);

	 startActivityForResult(intent, EditEntryDialog.REQUEST_CODE_EDIT_ENTRY);
   }

   protected void startEditEntryDialog(boolean isMonthly, CashflowDirection direction,
	    boolean disableEditing) {
	 Intent intent = new Intent(this, EditEntryDialog.class);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_CASHFLOW_DIRECTION, direction.getLocalized());
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_IS_MONTHLY, isMonthly);
	 intent.putExtra(EditEntryDialog.INTENT_EXTRA_DISABLE_EDITING, disableEditing);

	 startActivityForResult(intent, EditEntryDialog.REQUEST_CODE_EDIT_ENTRY);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 super.onActivityResult(requestCode, resultCode, data);
	 switch (requestCode) {
	 case EditEntryDialog.REQUEST_CODE_EDIT_ENTRY:
	    switch (resultCode) {
	    case RESULT_OK:
		  // The user has modified the budget
		  updateBudgetAmount();
		  updateEntries();
		  break;
	    }
	    break;
	 }
   }

   protected void updateBudgetAmount() {
	 TextView budgetAvailable = (TextView) findViewById(R.id.w_available_budget_amount);
	 budgetAvailable.setText(new EntryPersister().getCurrentBudget().format());
   }

   protected abstract void updateEntries();

}
