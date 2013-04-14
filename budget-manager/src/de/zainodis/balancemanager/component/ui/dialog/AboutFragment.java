package de.zainodis.balancemanager.component.ui.dialog;

import java.util.Collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.AboutEntry;
import de.zainodis.balancemanager.model.parsing.AboutParser;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.utils.StringUtils;

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
	 return inflater.inflate(R.layout.f_container_layout, container, false);
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);
	 // Parse credits
	 AboutParser parser = new AboutParser();
	 try {
	    parser.parse(getResources().getXml(R.xml.credits));
	 } catch (Throwable e) {
	    LogCat.e(TAG, "Failed to parse credits.", e);
	    return;
	 }

	 LinearLayout container = (LinearLayout) getSherlockActivity().findViewById(
		  R.id.f_container_layout);
	 LayoutInflater inflater = getSherlockActivity().getLayoutInflater();

	 Collection<AboutEntry> credits = parser.getResult();

	 String currentHeader = StringUtils.EMPTY;
	 for (AboutEntry credit : credits) {
	    if (!credit.getCategory().equals(currentHeader)) {
		  // Update the current category heading
		  currentHeader = credit.getCategory();
		  TextView categoryHeader = (TextView) inflater.inflate(R.layout.w_textview_header, null);
		  categoryHeader.setText(currentHeader);
		  container.addView(categoryHeader);
	    }

	    // Add the actual entry to the layout
	    TextView entry = (TextView) inflater.inflate(R.layout.w_textview, null);
	    entry.setText(credit.getText());
	    container.addView(entry);
	 }
   }
}
