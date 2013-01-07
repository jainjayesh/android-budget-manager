package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.persistence.Persister;
import de.zainodis.commons.LogCat;

public class GroupPersister extends Persister<GroupDao> {

   private static final String TAG = "GroupPersister";

   @Override
   protected GroupDao getDao() throws SQLException {
	 return getHelper().getGroupDao();
   }

   /**
    * Saves the given {@link Entry}.
    * 
    * @param newEntry
    *           the entry to save.
    * @return true if the given entry was saved successfully; false otherwise.
    */
   public boolean save(String newGroup) {
	 try {
	    return getDao().save(newGroup) == 1;
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	    return false;
	 }
   }

   /**
    * @return a list of all group names; or an empty list if there are none.
    */
   public Collection<String> getAll() {
	 try {
	    return getDao().getAll();
	 } catch (SQLException e) {
	    LogCat.e(TAG, "getAll failed.", e);
	    // TODO this is bad, that we need to know that it's a hashset
	    return new HashSet<String>();
	 }
   }

   /**
    * Persists a set of standard groups.
    */
   public void saveStandardGroups() {
	 save(Application.getInstance().getString(R.string.medicine));
	 save(Application.getInstance().getString(R.string.clothes));
	 save(Application.getInstance().getString(R.string.groceries));
	 save(Application.getInstance().getString(R.string.accessories));
	 save(Application.getInstance().getString(R.string.travel));
	 save(Application.getInstance().getString(R.string.pets));
	 save(Application.getInstance().getString(R.string.books));
	 save(Application.getInstance().getString(R.string.presents));
	 save(Application.getInstance().getString(R.string.rent));
	 save(Application.getInstance().getString(R.string.insurance));
	 save(Application.getInstance().getString(R.string.salary));
   }
}
