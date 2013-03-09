package de.zainodis.balancemanager.component.ui.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.persistence.CategoryPersister;

/**
 * Allows the user to see existing categories, add new ones and delete existing
 * ones.
 */
public class EditCategories extends SherlockListFragment {

   public static final String TAG = "EditCategories";

   private CategoryAdapter listAdapter;

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);

	 listAdapter = new CategoryAdapter(getSherlockActivity());
	 setListAdapter(listAdapter);
   }

   private class CategoryAdapter extends BaseAdapter {

	 private ArrayList<String> entries;
	 private LayoutInflater layoutInflater;

	 public CategoryAdapter(Context context) {
	    this.layoutInflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    this.entries = new ArrayList<String>(new CategoryPersister().getAll());
	 }

	 @Override
	 public int getCount() {
	    return entries.size();
	 }

	 @Override
	 public boolean isEnabled(int position) {
	    // Makes list items un-selecteable
	    return false;
	 }

	 @Override
	 public Object getItem(int position) {
	    return entries.get(position);
	 }

	 @Override
	 public long getItemId(int position) {
	    return position;
	 }

	 @Override
	 public View getView(final int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    if (convertView == null) {
		  view = layoutInflater.inflate(R.layout.w_list_item, null);
	    }

	    TextView field = (TextView) view.findViewById(R.id.w_list_item_text);
	    field.setText(entries.get(position));

	    ImageButton button = (ImageButton) view.findViewById(R.id.w_list_item_delete);

	    button.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
			// Delete entry from list and database
			new CategoryPersister().delete(entries.remove(position));
			// Refresh listview
			notifyDataSetChanged();
		  }
	    });

	    return view;
	 }
   }
}
