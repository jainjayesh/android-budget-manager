package de.zainodis.balancemanager.component.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import de.zainodis.balancemanager.R;

public class BudgetActivity extends SherlockFragmentActivity {

   private static final String TAG = BudgetActivity.class.getName();

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
	    EditBudgetFragment firstFragment = new EditBudgetFragment();

	    // Pass on possible intent extras
	    firstFragment.setArguments(getIntent().getExtras());
	    firstFragment.setSettingsSelectedListener(onOpenSettings);
	    firstFragment.setAddEntrySelectedListener(onAddEntry);

	    // Add the fragment to the 'fragment_container' FrameLayout
	    getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, firstFragment, TAG).commit();
	 }
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
