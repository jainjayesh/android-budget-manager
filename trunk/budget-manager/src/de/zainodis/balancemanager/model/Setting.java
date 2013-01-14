package de.zainodis.balancemanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.model.persistence.SettingDao;

/**
 * Simply speaking, settings are used to keep track of a user’s preferences and
 * other application-related settings.
 * 
 * @author zainodis
 * 
 */
@DatabaseTable(tableName = SettingDao.TABLE_NAME, daoClass = SettingDao.class)
public class Setting {

   @DatabaseField(columnName = SettingDao.NAME_FIELD, id = true)
   private String name;
   @DatabaseField(columnName = SettingDao.VALUE_FIELD)
   private String value;

   protected Setting() {
	 // for ormlite
   }

   public Setting(String name, String value) {
	 this.name = name;
	 this.value = value;
   }

   public String getName() {
	 return name;
   }

   public String getValue() {
	 return value;
   }

}
