package de.zainodis.balancemanager.model.persistence;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.zainodis.balancemanager.model.Category;
import de.zainodis.commons.LogCat;

/**
 * Persists a {@link Category} entry to the database.
 * 
 * @author zainodis
 * 
 */
public class CategoryDao extends BaseDaoImpl<Category, String> {

   private static final String TAG = "CategoryDao";

   public static final String TABLE_NAME = "category";
   public static final String NAME_FIELD = "name";
   public static final String IS_FIXED_FIELD = "is-fixed";

   public CategoryDao(ConnectionSource connectionSource) throws SQLException {
	 super(connectionSource, Category.class);
   }

   public CategoryDao(ConnectionSource connectionSource, DatabaseTableConfig<Category> tableConfig)
	    throws SQLException {
	 super(connectionSource, tableConfig);
   }

   /**
    * Creates or updates the given category.
    * 
    * @param newcategory
    *           the category to save.
    * @return the number of rows that have been modified.
    * @throws SQLException
    *            on error.
    */
   public int save(Category newcategory) throws SQLException {
	 // Trim the value of the category
	 if (newcategory != null) {
	    newcategory.trim();
	    try {
		  byte[] result = newcategory.getName().getBytes("utf-8");
		  LogCat.i(TAG, result.toString());
	    } catch (UnsupportedEncodingException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	    }
	 }
	 CreateOrUpdateStatus status = createOrUpdate(newcategory);
	 return status.isCreated() || status.isUpdated() ? 1 : 0;
   }

   /**
    * @return a list with all available categories, with no duplicates; or an
    *         empty list if there are none.
    * @throws SQLException
    *            on error.
    */
   public Collection<String> getAll() throws SQLException {
	 Collection<String> result = new HashSet<String>();
	 for (Category category : queryForAll()) {
	    result.add(category.getName());
	 }
	 return result;
   }
}
