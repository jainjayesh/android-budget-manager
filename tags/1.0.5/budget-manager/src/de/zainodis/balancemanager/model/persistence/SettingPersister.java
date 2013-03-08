package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;

import com.j256.ormlite.table.TableUtils;

import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.persistence.Persister;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.communication.local.CallbackPort;

public class SettingPersister extends Persister<SettingDao> {

   private static final String TAG = "SettingPersister";

   public static final CallbackPort OnSettingsUpdated = new CallbackPort(Setting.class);

   @Override
   protected SettingDao getDao() throws SQLException {
	 return getHelper().getSettingDao();
   }

   /**
    * Saves the given {@link Setting}.
    * 
    * @param toSave
    *           the setting to save.
    * @return true if the given entry was saved successfully; false otherwise.
    */
   public boolean save(Setting toSave) {
	 try {
	    int rowsAltered = getDao().save(toSave);
	    if (rowsAltered == 1) {
		  // Notify subscribers
		  OnSettingsUpdated.send(toSave);
		  return true;
	    }
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	 }
	 return false;
   }

   /**
    * @param name
    *           the name of the setting to query for.
    * @return a {@link Setting} matching the given name; null otherwise.
    */
   public Setting get(String name) {
	 try {
	    return getDao().queryForId(name);
	 } catch (SQLException e) {
	    LogCat.e(TAG, "get failed.", e);
	    return null;
	 }
   }

   /**
    * 
    * @param name
    *           the setting to query for.
    * @return true if a {@link Setting} with the given name exists; false
    *         otherwise.
    */
   public boolean exists(String name) {
	 return get(name) != null;
   }

   public void clearTable() {
	 try {
	    TableUtils.clearTable(getConnectionSource(), Setting.class);

	 } catch (SQLException e) {
	    LogCat.e(TAG, "clearTable failed.", e);
	 }
   }
}
