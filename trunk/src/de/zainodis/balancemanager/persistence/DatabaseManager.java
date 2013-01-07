package de.zainodis.balancemanager.persistence;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.Group;
import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.model.persistence.BudgetCycleDao;
import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.model.persistence.GroupDao;
import de.zainodis.balancemanager.model.persistence.GroupPersister;
import de.zainodis.balancemanager.model.persistence.SettingDao;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
   private static final String TAG = "DatabaseManager";

   /** Any time database objects change, this version MUST be incremented */
   private static final int DATABASE_VERSION = 11;

   // Name of the applications database file
   private static String DATABASE_NAME = "balance-manager.db";

   private GroupDao groupDao = null;
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
	    TableUtils.createTable(connectionSource, Group.class);
	    TableUtils.createTable(connectionSource, Setting.class);
	    TableUtils.createTable(connectionSource, BudgetCycle.class);

	    // Persist a set of standard suggestions
	    new GroupPersister().saveStandardGroups();

	    Log.i(TAG, "Created and initialized database " + DATABASE_NAME);

	 } catch (SQLException e) {
	    Log.e(TAG, "Failed to create or initialize database.", e);
	 }
   }

   @Override
   public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource,
	    int oldVersion, int newVersion) {
	 // TODO add upgrades here
   }

   public EntryDao getEntryDao() throws SQLException {
	 if (entryDao == null) {
	    entryDao = getDao(Entry.class);
	 }
	 return entryDao;
   }

   public GroupDao getGroupDao() throws SQLException {
	 if (groupDao == null) {
	    groupDao = getDao(Group.class);
	 }
	 return groupDao;
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
	 groupDao = null;
	 entryDao = null;
	 settingDao = null;
	 budgetCycleDao = null;
   }

}
