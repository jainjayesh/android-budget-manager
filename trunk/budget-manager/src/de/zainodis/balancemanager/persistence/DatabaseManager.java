package de.zainodis.balancemanager.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.Category;
import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.model.persistence.BudgetCycleDao;
import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.model.persistence.CategoryDao;
import de.zainodis.balancemanager.model.persistence.CategoryPersister;
import de.zainodis.balancemanager.model.persistence.SettingDao;
import de.zainodis.commons.LogCat;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
   private static final String TAG = "DatabaseManager";

   /** Any time database objects change, this version MUST be incremented */
   private static final int DATABASE_VERSION = 1;

   // Name of the applications database file
   private static String DATABASE_NAME = "balance-manager.db";

   private CategoryDao categoryDao = null;
   private EntryDao entryDao = null;
   private SettingDao settingDao = null;
   private BudgetCycleDao budgetCycleDao = null;

   public DatabaseManager(Context context, String databaseName, int databaseVersion) {
	 super(context, databaseName, null, databaseVersion);
   }

   public DatabaseManager(Context context) {
	 super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
	 try {
	    TableUtils.createTable(connectionSource, Entry.class);
	    TableUtils.createTable(connectionSource, Category.class);
	    TableUtils.createTable(connectionSource, Setting.class);
	    TableUtils.createTable(connectionSource, BudgetCycle.class);

	    // Persist a set of standard suggestions
	    new CategoryPersister().saveStandardCategories();

	    Log.i(TAG, "Created and initialized database " + DATABASE_NAME);

	 } catch (SQLException e) {
	    Log.e(TAG, "Failed to create or initialize database.", e);
	 }
   }

   @Override
   public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource,
	    int oldVersion, int newVersion) {
	 // A list with update statements
	 List<String> statements = new ArrayList<String>();

	 // Execute all accumulated statements
	 executeAsTransaction(statements, sqliteDatabase);

   }

   public EntryDao getEntryDao() throws SQLException {
	 if (entryDao == null) {
	    entryDao = getDao(Entry.class);
	 }
	 return entryDao;
   }

   public CategoryDao getCategoryDao() throws SQLException {
	 if (categoryDao == null) {
	    categoryDao = getDao(Category.class);
	 }
	 return categoryDao;
   }

   public SettingDao getSettingDao() throws SQLException {
	 if (settingDao == null) {
	    settingDao = getDao(Setting.class);
	 }
	 return settingDao;
   }

   public BudgetCycleDao getBudgetCycleDao() throws SQLException {
	 if (budgetCycleDao == null) {
	    budgetCycleDao = getDao(BudgetCycle.class);
	 }
	 return budgetCycleDao;
   }

   /**
    * Close the database connections and clear any cached DAOs.
    */
   @Override
   public void close() {
	 super.close();
	 // Reset all cached daos
	 categoryDao = null;
	 entryDao = null;
	 settingDao = null;
	 budgetCycleDao = null;
   }

   /**
    * Adds a new column with the default type "TEXT".
    * 
    * @param table
    *           the table to which the given column should be added.
    * @param column
    *           the column that should be added to the given table.
    * @return the sql statement which commands adding of the given column in the
    *         given table of the given type.
    */
   protected String addColumn(String table, String column, SQLiteDatatypes type) {
	 return String.format("ALTER TABLE %s ADD COLUMN %s %s", table, column, type.toString());
   }

   /**
    * Runs the given update statements within on transaction.
    * 
    * @param statements
    *           the statements to execute.
    * @param sqliteDatabase
    *           the database to perform the upgrade on.
    * @return the number of statements that have been executed; or zero if an
    *         error occurred during the execution of the statements.
    */
   protected int executeAsTransaction(List<String> statements, SQLiteDatabase sqliteDatabase) {
	 /*
	  * Prevent threading issues (we run in multiple threads and so this is
	  * required). See
	  * http://stackoverflow.com/questions/7657223/sqlite-exception
	  * -database-is-locked-issue
	  */
	 synchronized (this) {
	    LogCat.i(TAG, String.format("About to process %s update statements.", statements.size()));
	    if (statements != null && statements.size() > 0) {
		  int executionCount = 0;
		  sqliteDatabase.beginTransaction(); // beginTransction() locks the
									  // database exclusively for us.
		  try {
			for (String statement : statements) {
			   try {
				 sqliteDatabase.execSQL(statement);
				 LogCat.i(TAG, "Executed statement " + statement);
				 executionCount++;
			   } catch (Throwable e) {
				 LogCat.e(TAG,
					  "Failed to execute upgrade statement, proceeding with next statement.",
					  e);
			   }
			}
		  } finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
			LogCat.i(TAG, String.format("Upgraded database, executed %s udpate statements.",
				 executionCount));
		  }
		  return executionCount;
	    }
	    return 0;
	 }
   }
}
