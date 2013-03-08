package de.zainodis.balancemanager.model;

import static junit.framework.Assert.assertEquals;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.model.persistence.EntryDao;
import de.zainodis.balancemanager.persistence.CurrencyType;
import de.zainodis.commons.model.CurrencyAmount;
import de.zainodis.commons.utils.DateTimeUtils;

/**
 * Represents a balance entry.
 * 
 * @author zainodis
 * 
 */
@DatabaseTable(tableName = EntryDao.TABLE_NAME, daoClass = EntryDao.class)
public class Entry {

   @DatabaseField(columnName = EntryDao.ID_FIELD, generatedId = true)
   private long id;
   /** The foreign key is maintained manually */
   @DatabaseField(columnName = EntryDao.FK_BUDGET_CYCLE_ID_FIELD)
   private long fkBudgetCycleId;
   @DatabaseField(columnName = EntryDao.DATE_TIME_FIELD, dataType = DataType.DATE_STRING, format = DateTimeUtils.DATETIME_FORMAT_ISO8601)
   private Date date;
   @DatabaseField(columnName = EntryDao.AMOUNT_FIELD, persisterClass = CurrencyType.class)
   private CurrencyAmount amount = new CurrencyAmount(0);
   @DatabaseField(columnName = EntryDao.CATEGORY_FIELD)
   private String category;
   @DatabaseField(columnName = EntryDao.IS_RECURRING_FIELD)
   private boolean isRecurring = false;
   @DatabaseField(columnName = EntryDao.CASHFLOW_DIRECTION_FIELD, dataType = DataType.ENUM_INTEGER)
   private CashflowDirection cashflowDirection;

   protected Entry() {
	 // for ormlite
   }

   public Entry(CashflowDirection direction, String category, boolean isRecurring, CurrencyAmount amount) {
	 this.date = new Date();
	 this.isRecurring = isRecurring;
	 if (amount == null) {
	    this.amount = new CurrencyAmount(0);
	 } else {
	    this.amount = amount;
	 }
	 this.category = category;
	 this.cashflowDirection = direction;
   }

   public CurrencyAmount getAmount() {
	 return amount;
   }

   public Date getDate() {
	 return date;
   }

   public String getFormattedDate() {
	 return DateTimeUtils.format(DateTimeUtils.toCalendar(date), DateTimeUtils.DATE_FORMAT);
   }

   public long getId() {
	 return id;
   }

   public CashflowDirection getCashflowDirection() {
	 return cashflowDirection;
   }

   public boolean isRecurring() {
	 return isRecurring;
   }

   public String getCategory() {
	 return category;
   }

   public void setBudgetCycleId(long budgetCycleId) {
	 assertEquals("Once set, the budget cycle id must not be changed.", fkBudgetCycleId, 0);
	 this.fkBudgetCycleId = budgetCycleId;
   }
}
