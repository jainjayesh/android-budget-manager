package de.zainodis.balancemanager.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.model.persistence.CategoryDao;
import de.zainodis.commons.utils.StringUtils;

/**
 * Basically a suggestion list for names of any type of cashflow
 * (incoming/outgoing).
 * 
 * @author zainodis
 * 
 */
@DatabaseTable(tableName = CategoryDao.TABLE_NAME, daoClass = CategoryDao.class)
public class Category {

   @DatabaseField(columnName = CategoryDao.NAME_FIELD, id = true)
   private String name;

   /** Fixed categories are never deleted */
   @DatabaseField(columnName = CategoryDao.IS_FIXED_FIELD)
   private boolean isFixed = false;

   protected Category() {
	 // for ormlite
   }

   public Category(String categoryName, boolean isFixed) {
	 this.name = categoryName;
	 this.isFixed = isFixed;
   }

   public Category(String categoryName) {
	 this.name = categoryName;
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
