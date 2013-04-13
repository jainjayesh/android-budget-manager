package de.zainodis.balancemanager.component.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import de.zainodis.balancemanager.R;
import de.zainodis.commons.LogCat;

public class BudgetActivity extends SherlockFragmentActivity {

   private static final String TAG = BudgetActivity.class.getName();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(getLayoutInflater().inflate(R.layout.a_fragment_container, null));

	 if (findViewById(R.id.fragment_container) != null) {

	    if (savedInstanceState != null) {
		  LogCat.i(TAG, "Created based on saved instance.");
		  // To avoid stale listeners
		  updateListeners((EditBudgetFragment) getSupportFragmentManager().findFragmentByTag(TAG));
		  // Prevents overlapping fragments
		  return;
	    }
	    LogCat.i(TAG, "Created from scratch.");

	    // Create an instance of ExampleFragment
	    EditBudgetFragment fragment = new EditBudgetFragment();

	    // Pass on possible intent extras
	    fragment.setArguments(getIntent().getExtras());

	    // To avoid stale listeners
	    updateListeners(fragment);

	    // Add the fragment to the 'fragment_container' FrameLayout
	    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, TAG)
			.commit();

	 }
   }

   protected void updateListeners(EditBudgetFragment target) {
	 /*
	  * ALWAYS update listener references, otherwise the references of already
	  * dead activities may be called.
	  */
	 target.setSettingsSelectedListener(onOpenSettings);
	 target.setAddEntrySelectedListener(onAddEntry);
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
	 super.onSaveInstanceState(outState);
	 LogCat.i(TAG, "Instance has been saved.");
   }

   @Override
   protected void onPause() {
	 super.onPause();
	 LogCat.i(TAG, "has been paused.");
   }

   @Override
   protected void onDestroy() {
	 super.onDestroy();
	 LogCat.i(TAG, "has been destroyed.");
   }

   /**
    * Starts the edit entry dialog.
    * 
    * @author fzarrai
    * 
    */
   private final OnAddEntrySelectedListener onAddEntry = new OnAddEntrySelectedListener() {
	 @Override
	 public void onAddEntrySelected() {
	    // TODO currently this simply replaces other fragments, change so it
	    // adds depending on screen size
	    Bundle bundle = new Bundle();
	    bundle.putBoolean(EditEntryFragment.INTENT_EXTRA_IS_RECURRING, false);
	    bundle.putBoolean(EditEntryFragment.INTENT_EXTRA_DISABLE_EDITING, false);

	    Fragment fragment = SherlockFragment.instantiate(BudgetActivity.this,
			EditEntryFragment.class.getName(), bundle);
	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    transaction.replace(R.id.fragment_container, fragment, TAG);
	    transaction.addToBackStack(null);
	    transaction.commit();
	 }

   };

   /**
    * Starts the settings dialog (not inline but as a new activity screen).
    * 
    * @author fzarrai
    * 
    */
   private final OnSettingsSelectedListener onOpenSettings = new OnSettingsSelectedListener() {
	 @Override
	 public void onSettingsSelected() {
	    Intent i = new Intent(BudgetActivity.this, SettingsActivity.class);
	    startActivity(i);
	 }
   };

}
