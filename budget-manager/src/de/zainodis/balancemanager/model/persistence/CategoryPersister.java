package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.Category;
import de.zainodis.balancemanager.persistence.Persister;
import de.zainodis.commons.LogCat;

public class CategoryPersister extends Persister<CategoryDao> {

   private static final String TAG = "CategoryPersister";

   @Override
   protected CategoryDao getDao() throws SQLException {
	 return getHelper().getCategoryDao();
   }

   /**
    * Saves the given {@link Entry}.
    * 
    * @param newcategory
    *           the new category to save.
    * @return true if the given category was saved successfully; false
    *         otherwise.
    */
   public boolean save(Category newcategory) {
	 try {
	    return getDao().save(newcategory) == 1;
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	    return false;
	 }
   }

   /**
    * Deletes all categories that are {@link Category#isFixed()} false, i.e.
    * custom.
    */
   public void clearCustomCategories() {
	 try {
	    CategoryDao dao = getDao();
	    DeleteBuilder<Category, String> builder = dao.deleteBuilder();
	    builder.where().eq(CategoryDao.IS_FIXED_FIELD, false);
	    // Delete all custom entries
	    dao.delete(builder.prepare());
	 } catch (SQLException e) {
	    LogCat.e(TAG, "clearCustomCategories failed.", e);
	 }
   }

   /**
    * @return a list of all category names; or an empty list if there are none.
    */
   public Collection<String> getAll() {
	 try {
	    return getDao().getAll();
	 } catch (SQLException e) {
	    LogCat.e(TAG, "getAll failed.", e);
	    // TODO It's bad, that we need to know it's a hashset
	    return new HashSet<String>();
	 }
   }

   /**
    * Persists a set of standard categories.
    */
   public void saveStandardCategories() {
	 save(new Category(Application.getInstance().getString(R.string.medicine), true));
	 save(new Category(Application.getInstance().getString(R.string.clothes), true));
	 save(new Category(Application.getInstance().getString(R.string.groceries), true));
	 save(new Category(Application.getInstance().getString(R.string.accessories), true));
	 save(new Category(Application.getInstance().getString(R.string.travel), true));
	 save(new Category(Application.getInstance().getString(R.string.pets), true));
	 save(new Category(Application.getInstance().getString(R.string.books), true));
	 save(new Category(Application.getInstance().getString(R.string.presents), true));
	 save(new Category(Application.getInstance().getString(R.string.rent), true));
	 save(new Category(Application.getInstance().getString(R.string.insurance), true));
	 save(new Category(Application.getInstance().getString(R.string.salary), true));
   }

   public void clearTable() {
	 try {
	    TableUtils.clearTable(getConnectionSource(), Category.class);

	 } catch (SQLException e) {
	    LogCat.e(TAG, "clearTable failed.", e);
	 }
   }
}
