package de.zainodis.balancemanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.model.persistence.GroupDao;

/**
 * Basically a suggestion list for names of any type of cashflow
 * (incoming/outgoing).
 * 
 * @author zainodis
 * 
 */
@DatabaseTable(tableName = GroupDao.TABLE_NAME, daoClass = GroupDao.class)
public class Group {

   @DatabaseField(columnName = GroupDao.NAME_FIELD, id = true)
   private String name;

   protected Group() {
	 // for ormlite
   }

   public Group(String groupName) {
	 this.name = groupName;
   }

   public String getName() {
	 return name;
   }
}
