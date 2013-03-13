package de.zainodis.balancemanager.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.component.LocaleComponent;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.options.LocaleOption;
import de.zainodis.balancemanager.model.persistence.BudgetCycleDao;
import de.zainodis.commons.model.CurrencyAmount;
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
   /**
    * The locale with which this cycle was created, so it can be re-constructed
    * by {@link LocaleOption}. Each cycle has only one locale based on which the
    * currencie's are processed. If the currency is changed, a new cycle has to
    * be started.
    */
   @DatabaseField(columnName = BudgetCycleDao.LOCALE_FIELD)
   private String locale;

   protected BudgetCycle() {
	 // for ormlite
   }

   public BudgetCycle(Date start) {
	 this.start = start;
	 // Calculate the end of the cycle
	 Calendar result = DateTimeUtils.toCalendar(this.start);
	 // Advance by one month, this way we'll know our end-date
	 result.add(Calendar.MONTH, 1);
	 // Set the locale to either default or the one set in the options
	 Locale cyclesLocale = Application.getInstance().getComponent(LocaleComponent.class)
		  .getLocale();
	 this.locale = new LocaleOption(cyclesLocale).format();
   }

   /**
    * @return the starting date formatted using
    *         {@link DateTimeUtils#DATE_FORMAT}.
    */
   public String getStartAsString() {
	 return start != null ? DateTimeUtils.format(DateTimeUtils.toCalendar(start),
		  DateTimeUtils.DATE_FORMAT) : Application.getInstance().getString(R.string.unknown);
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

   public Locale getLocale() {
	 return new LocaleOption(locale).getValue();
   }

   /**
    * @return an instance of {@link CurrencyAmount} initialised to zero with the
    *         locale of this {@link BudgetCycle}.
    */
   public CurrencyAmount createEmptyCurrency() {
	 return new CurrencyAmount(0, getLocale());
   }

   /**
    * @return true, if the budget cycle has already ended; false otherwise
    */
   public boolean hasEnded() {
	 return hasEnded;
   }

}
