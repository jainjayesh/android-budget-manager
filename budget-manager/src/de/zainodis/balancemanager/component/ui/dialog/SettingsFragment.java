package de.zainodis.balancemanager.component.ui.dialog;

import java.util.Locale;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.BundleAttributes;
import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.model.options.LocaleOption;
import de.zainodis.balancemanager.model.persistence.SettingPersister;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.utils.Toaster;

/**
 * Allows the user to change application related settings.
 * 
 * @author zainodis
 * 
 */
public class SettingsFragment extends SherlockFragment {

   public static final String TAG = "Settings";

   @Override
   public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setRetainInstance(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 return inflater.inflate(R.layout.f_settings, container, false);
   }

   // TODO currently inactive
   public void onSelectCurrency(View requestedBy) {
	 // Only supported by API level 10 or greater
	 if (Build.VERSION_CODES.GINGERBREAD_MR1 == Build.VERSION.SDK_INT) {
	    // Start the select currency dialog
	    // startActivityForResult(new Intent(this, SelectCurrency.class),
	    // RequestCodes.SELECT_CURRENCY_REQUEST_CODE);
	 } else {
	    Toaster.longToast(getString(R.string.feature_not_supported), getSherlockActivity());
	 }
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	 if (requestCode == RequestCodes.SELECT_CURRENCY_REQUEST_CODE) {
	    if (resultCode == SherlockActivity.RESULT_OK) {
		  // Retrieve selected locale
		  Locale locale = (Locale) data.getSerializableExtra(BundleAttributes.OBJECT);
		  // Create or update the setting
		  LocaleOption option = new LocaleOption(locale);
		  if (new SettingPersister().save(new Setting(option.getOptionName(), option.format()))) {
			LogCat.i(
				 TAG,
				 String.format("Saved option %s with value %s", option.getOptionName(),
					  option.format()));
		  }

	    }
	 }
	 super.onActivityResult(requestCode, resultCode, data);
   }

}
