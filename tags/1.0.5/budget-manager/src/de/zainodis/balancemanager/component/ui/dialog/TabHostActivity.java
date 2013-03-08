package de.zainodis.balancemanager.component.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import de.zainodis.balancemanager.R;

public class TabHostActivity extends SherlockFragmentActivity {

   private static final String TAG = "TabHostActivity";

   private static final String SELETED_TAB_INDEX = "tabIndex";

   public static final String SETTINGS_TAG = Settings.TAG;
   public static final String BUDGET_OVERVIEW_TAG = BudgetOverview.TAG;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);

	 // Setup the action bar
	 ActionBar actionBar = getSupportActionBar();

	 // Show the navigation bar
	 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	 // Add the products tab
	 Tab budgetTab = actionBar
		  .newTab()
		  .setText(getString(R.string.budget_overview))
		  .setTabListener(
			   new MyTabsListener<BudgetOverview>(this, BUDGET_OVERVIEW_TAG,
				    BudgetOverview.class));
	 actionBar.addTab(budgetTab);

	 // Add the about tab
	 Tab settingsTab = actionBar.newTab().setText(getString(R.string.settings))
		  .setTabListener(new MyTabsListener<Settings>(this, SETTINGS_TAG, Settings.class));
	 actionBar.addTab(settingsTab);

	 if (savedInstanceState != null) {
	    int index = savedInstanceState.getInt(SELETED_TAB_INDEX);
	    actionBar.setSelectedNavigationItem(index);
	 }

   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
	 super.onSaveInstanceState(outState);
	 outState.putInt(SELETED_TAB_INDEX, getSupportActionBar().getSelectedTab().getPosition());
   }

   private class MyTabsListener<T extends Fragment> implements ActionBar.TabListener {
	 private Fragment fragment;
	 private final SherlockFragmentActivity host;
	 private final Class<Fragment> type;
	 private String tag;

	 public MyTabsListener(SherlockFragmentActivity parent, String tag, Class type) {
	    this.host = parent;
	    this.tag = tag;
	    this.type = type;
	 }

	 @Override
	 public void onTabSelected(Tab tab, FragmentTransaction transaction) {
	    /*
	     * The fragment which has been added to this listener may have been
	     * replaced (can be the case for lists when drilling down), but if the
	     * tag has been retained, we should find the actual fragment that was
	     * showing in this tab before the user switched to another.
	     */
	    Fragment currentlyShowing = host.getSupportFragmentManager().findFragmentByTag(tag);

	    // Check if the fragment is already initialised
	    if (currentlyShowing == null) {
		  // If not, instantiate and add it to the activity
		  fragment = SherlockFragment.instantiate(host, type.getName());
		  transaction.add(android.R.id.content, fragment, tag);
	    } else {
		  // If it exists, simply attach it in order to show it
		  transaction.attach(currentlyShowing);
	    }
	 }

	 public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
	    /*
	     * The fragment which has been added to this listener may have been
	     * replaced (can be the case for lists when drilling down), but if the
	     * tag has been retained, we should find the actual fragment that's
	     * currently active.
	     */
	    Fragment currentlyShowing = host.getSupportFragmentManager().findFragmentByTag(tag);
	    if (currentlyShowing != null) {
		  // Detach the fragment, another tab has been selected
		  fragmentTransaction.detach(currentlyShowing);
	    } else if (this.fragment != null) {
		  fragmentTransaction.detach(fragment);
	    }
	 }

	 public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
	    // This tab is already selected
	 }

   }
}
