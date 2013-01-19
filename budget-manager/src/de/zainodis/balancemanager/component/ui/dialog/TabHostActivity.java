package de.zainodis.balancemanager.component.ui.dialog;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import de.zainodis.balancemanager.R;

public class TabHostActivity extends TabActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.v_tab_host);

	 TabHost tabHost = getTabHost();

	 TabSpec photospec = tabHost.newTabSpec(BudgetOverview.TAG);
	 photospec.setIndicator(getString(R.string.budget_overview));
	 Intent photosIntent = new Intent(this, BudgetOverview.class);
	 photospec.setContent(photosIntent);

	 TabSpec songspec = tabHost.newTabSpec(BudgetSettings.TAG);
	 songspec.setIndicator(getString(R.string.budget_settings));
	 Intent songsIntent = new Intent(this, BudgetSettings.class);
	 songspec.setContent(songsIntent);

	 tabHost.addTab(photospec);
	 tabHost.addTab(songspec);
   }
}
