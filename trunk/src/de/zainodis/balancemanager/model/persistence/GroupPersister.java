package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.j256.ormlite.stmt.DeleteBuilder;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.Group;
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
   public boolean save(Group newGroup) {
	 try {
	    return getDao().save(newGroup) == 1;
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	    return false;
	 }
   }

   /**
    * Deletes all groups that are {@link Group#isFixed()} false, i.e. custom.
    */
   public void clearCustomGroups() {
	 try {
	    GroupDao dao = getDao();
	    DeleteBuilder<Group, String> builder = dao.deleteBuilder();
	    builder.where().eq(GroupDao.IS_FIXED_FIELD, false);
	    // Delete all custom entries
	    dao.delete(builder.prepare());
	 } catch (SQLException e) {
	    LogCat.e(TAG, "clearCustomGroups failed.", e);
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
	 save(new Group(Application.getInstance().getString(R.string.medicine), true));
	 save(new Group(Application.getInstance().getString(R.string.clothes), true));
	 save(new Group(Application.getInstance().getString(R.string.groceries), true));
	 save(new Group(Application.getInstance().getString(R.string.accessories), true));
	 save(new Group(Application.getInstance().getString(R.string.travel), true));
	 save(new Group(Application.getInstance().getString(R.string.pets), true));
	 save(new Group(Application.getInstance().getString(R.string.books), true));
	 save(new Group(Application.getInstance().getString(R.string.presents), true));
	 save(new Group(Application.getInstance().getString(R.string.rent), true));
	 save(new Group(Application.getInstance().getString(R.string.insurance), true));
	 save(new Group(Application.getInstance().getString(R.string.salary), true));
   }
}
