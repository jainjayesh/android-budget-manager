package de.zainodis.balancemanager.component.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.commons.component.ui.widget.CancellableProgressDialog;

/**
 * Displayed when the application is started. If the application has not yet
 * been configured, the appropriate measures will be taken now. If the
 * application has already been configured, an overview is displayed.
 * 
 * @author zainodis
 * 
 */
public class WelcomeScreen extends Activity {

   CancellableProgressDialog waitSpinner = null;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.a_empty);
	 setTitle(getString(R.string.app_name));

	 // Show wait spinner while loading the application
	 waitSpinner = new CancellableProgressDialog(this, getString(R.string.please_wait),
		  getString(R.string.launching_application));
	 waitSpinner.show();
   }

   @Override
   protected void onResume() {
	 super.onResume();

	 if (new BudgetCyclePersister().getActiveCycle() != null) {
	    startActivity(new Intent(this, BudgetOverview.class));

	 } else {
	    startActivity(new Intent(this, BudgetSettings.class));

	 }
	 waitSpinner.dismiss();
	 finish();
   }
}
