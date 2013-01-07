package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;

import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.persistence.Persister;
import de.zainodis.commons.LogCat;

public class SettingPersister extends Persister<SettingDao> {

   private static final String TAG = "SettingPersister";

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
	    return getDao().save(toSave) == 1;
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	    return false;
	 }
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

}
