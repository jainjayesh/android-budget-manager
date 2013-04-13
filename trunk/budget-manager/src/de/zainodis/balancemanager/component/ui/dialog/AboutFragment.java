package de.zainodis.balancemanager.component.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import de.zainodis.balancemanager.R;

/**
 * Displays the about section of the application
 * 
 * @author zainodis
 * 
 */
public class AboutFragment extends SherlockFragment {

   public static final String TAG = "About";

   @Override
   public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setRetainInstance(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 return inflater.inflate(R.layout.f_settings, container, false);
   }

}
