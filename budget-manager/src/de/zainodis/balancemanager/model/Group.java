package de.zainodis.balancemanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.model.persistence.GroupDao;
import de.zainodis.commons.utils.StringUtils;

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

   /** Fixed groups are never deleted */
   @DatabaseField(columnName = GroupDao.IS_FIXED_FIELD)
   private boolean isFixed = false;

   protected Group() {
	 // for ormlite
   }

   public Group(String groupName, boolean isFixed) {
	 this.name = groupName;
	 this.isFixed = isFixed;
   }

   public Group(String groupName) {
	 this.name = groupName;
   }

   public String getName() {
	 return name;
   }

   public boolean isFixed() {
	 return isFixed;
   }

   /**
    * Removes leading and trailing whitespace from the name.
    */
   public void trim() {
	 if (name != null) {
	    /*
	     * Remove spaces, tabs and new lines from the beginning and end of the
	     * word.
	     */
	    name = StringUtils.trim(name);
	 }
   }
}
