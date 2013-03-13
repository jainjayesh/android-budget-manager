package de.zainodis.balancemanager.component.ui.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.model.Category;
import de.zainodis.balancemanager.model.persistence.CategoryPersister;
import de.zainodis.commons.utils.StringUtils;

/**
 * Allows the user to see existing categories, add new ones and delete existing
 * ones.
 */
public class EditCategories extends SherlockListFragment {

   public static final String TAG = EditCategories.class.getName();

   private CategoryAdapter listAdapter;

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);

	 // Make sure list adapter is null, so we don't get exceptions when adding
	 // the header view
	 setListAdapter(null);

	 // Inflate and create header
	 final View header = getSherlockActivity().getLayoutInflater().inflate(
		  R.layout.w_category_list_header_2, null);

	 // Add listeners to the headerview
	 ImageButton button = (ImageButton) header.findViewById(R.id.w_category_list_header_add);
	 button.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		  EditText valueField = (EditText) header.findViewById(R.id.w_category_list_header_value);
		  String value = valueField.getText() != null ? valueField.getText().toString() : null;

		  if (!StringUtils.isEmpty(value)) {
			// The user entered a value value, save it
			new CategoryPersister().save(new Category(value));
			// Update the adapter list
			listAdapter.updateList();
			// Trigger ui reload
			listAdapter.notifyDataSetChanged();
		  }

		  // Clear edittext
		  valueField.setText(StringUtils.EMPTY);
	    }
	 });

	 getListView().addHeaderView(header);

	 listAdapter = new CategoryAdapter(getSherlockActivity());
	 setListAdapter(listAdapter);
   }

   private class CategoryAdapter extends BaseAdapter {

	 private ArrayList<String> entries;
	 private LayoutInflater layoutInflater;

	 public CategoryAdapter(Context context) {
	    this.layoutInflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    updateList();
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

	 protected void updateList() {
	    entries = new ArrayList<String>(new CategoryPersister().getAll());
	 }
   }
}
