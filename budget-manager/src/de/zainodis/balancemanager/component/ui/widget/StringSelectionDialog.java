package de.zainodis.balancemanager.component.ui.widget;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * Displays a set of String options the user may chose from.
 * 
 * @author fzarrai
 * 
 */
public class StringSelectionDialog {

   public StringSelectionDialog(String title, String[] values, Context context,
	    OnClickListener onClick) {

	 AlertDialog.Builder builder = new Builder(context);
	 builder.setTitle(title);
	 builder.setItems(values, onClick);
	 builder.setCancelable(true);
	 builder.show();
   }
}
