package de.zainodis.balancemanager.persistence;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import de.zainodis.balancemanager.core.Application;

/**
 * Provides an instance of {@link DatabaseManager}, required to obtain a dao and
 * perform database operations.
 * 
 * @author zainodis
 * 
 */
public class HelperProvider {
   protected DatabaseManager databaseHelper;

   /**
    * Lazy-loads the {@link OffencesDatabaseManager}.
    * 
    * @return an application specific implementation of
    *         {@link OrmLiteSqliteOpenHelper} for retrieving {@link Dao}s.
    */
   protected DatabaseManager getHelper() {
	 if (databaseHelper == null) {
	    databaseHelper = OpenHelperManager.getHelper(Application.getInstance(),
			DatabaseManager.class);
	 }
	 return databaseHelper;
   }

   /**
    * Must be called to explicitly release the helper after having completed any
    * queries, otherwise the helper will be released in finalize(), which is
    * unreliable and might cause issues.
    */
   protected void releaseHelper() {
	 if (databaseHelper != null) {
	    OpenHelperManager.releaseHelper();
	    databaseHelper = null;
	 }
   }

   protected ConnectionSource getConnectionSource() {
	 return getHelper().getConnectionSource();
   }

   @Override
   protected void finalize() throws Throwable {
	 // Auto release obtained helper on object destruction
	 releaseHelper();
	 super.finalize();
   }
}
