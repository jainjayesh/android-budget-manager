package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.zainodis.balancemanager.model.Setting;

/**
 * Persists an {@link Setting} to the database.
 * 
 * @author zainodis
 * 
 */
public class SettingDao extends BaseDaoImpl<Setting, String> {

   public static final String TABLE_NAME = "setting";
   public static final String NAME_FIELD = "name";
   public static final String VALUE_FIELD = "value";

   public SettingDao(ConnectionSource connectionSource) throws SQLException {
	 super(connectionSource, Setting.class);
   }

   public SettingDao(ConnectionSource connectionSource, DatabaseTableConfig<Setting> tableConfig)
	    throws SQLException {
	 super(connectionSource, tableConfig);
   }

   /**
    * Creates or updates the given {@link Setting}.
    * 
    * @return the number of rows that have been modified.
    * @throws SQLException
    *            on error.
    */
   public int save(Setting setting) throws SQLException {
	 CreateOrUpdateStatus status = createOrUpdate(setting);
	 return status.isCreated() || status.isUpdated() ? 1 : 0;
   }
}
