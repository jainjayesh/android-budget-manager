package de.zainodis.balancemanager.component.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import de.zainodis.balancemanager.R;

/**
 * Displays the settings menu. Sub Menus are either displayed to the right (on
 * tablets) or openes in a new window (phones).
 * 
 * @author fzarrai
 * 
 */
/*
 * TODO currently this simply replaces other fragments, change so it adds
 * depending on screen size.
 */
public class SettingsActivity extends SherlockFragmentActivity {

   private static final String TAG = SettingsActivity.class.getName();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(getLayoutInflater().inflate(R.layout.a_fragment_container, null));

	 // Add settings fragment if this activity starts for the first time
	 if (findViewById(R.id.fragment_container) != null) {

	    if (savedInstanceState != null) {
		  // Prevents overlapping fragments
		  return;
	    }

	    // Create an instance of ExampleFragment
	    SettingsFragment firstFragment = new SettingsFragment();

	    // Pass on possible intent extras
	    firstFragment.setArguments(getIntent().getExtras());

	    // Add the fragment to the 'fragment_container' FrameLayout
	    getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, firstFragment, TAG).commit();
	 }
   }

   public void onEditCategories(View requestedBy) {

	 Fragment fragment = SherlockFragment
		  .instantiate(this, EditCategoriesFragment.class.getName());

	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	 transaction.replace(R.id.fragment_container, fragment, TAG);
	 transaction.addToBackStack(null);
	 transaction.commit();

   }

   public void onViewAbout(View requestedBy) {
	 Fragment fragment = SherlockFragment.instantiate(this, AboutFragment.class.getName());

	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	 transaction.replace(R.id.fragment_container, fragment, TAG);
	 transaction.addToBackStack(null);
	 transaction.commit();

   }

}
