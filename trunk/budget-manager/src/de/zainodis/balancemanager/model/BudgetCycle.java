package de.zainodis.balancemanager.model;

import java.util.Calendar;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.model.persistence.BudgetCycleDao;
import de.zainodis.commons.utils.DateTimeUtils;

/**
 * Represents a complete budget cycle.
 * 
 * @author zainodis
 * 
 */
@DatabaseTable(tableName = BudgetCycleDao.TABLE_NAME, daoClass = BudgetCycleDao.class)
public class BudgetCycle {

   @DatabaseField(columnName = BudgetCycleDao.ID_FIELD, generatedId = true)
   private long id;
   /** Start of the budget cycle */
   @DatabaseField(columnName = BudgetCycleDao.START_FIELD, dataType = DataType.DATE_STRING, format = DateTimeUtils.DATETIME_FORMAT_ISO8601)
   private Date start;
   /** End of the budget cycle */
   @DatabaseField(columnName = BudgetCycleDao.END_FIELD, dataType = DataType.DATE_STRING, format = DateTimeUtils.DATETIME_FORMAT_ISO8601)
   private Date end;
   @DatabaseField(columnName = BudgetCycleDao.HAS_ENDED_FIELD)
   private boolean hasEnded = false;

   protected BudgetCycle() {
	 // for ormlite
   }

   public BudgetCycle(Date start) {
	 this.start = start;
	 // Calculate the end of the cycle
	 Calendar result = DateTimeUtils.toCalendar(this.start);
	 // Advance by one month, this way we'll know our end-date
	 result.add(Calendar.MONTH, 1);
   }

   public Date getStart() {
	 return start;
   }

   public Date getEnd() {
	 return end;
   }

   public long getId() {
	 return id;
   }

   /**
    * @return true, if the budget cycle has already ended; false otherwise
    */
   public boolean hasEnded() {
	 return hasEnded;
   }

}
