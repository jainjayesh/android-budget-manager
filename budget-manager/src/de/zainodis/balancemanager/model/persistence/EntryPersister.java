package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import de.zainodis.balancemanager.component.LocaleComponent;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.EntrySort;
import de.zainodis.balancemanager.model.EntryFilter;
import de.zainodis.balancemanager.persistence.Persister;
import de.zainodis.commons.LogCat;
import de.zainodis.commons.model.CurrencyAmount;

public class EntryPersister extends Persister<EntryDao> {

   private static final String TAG = "EntryPersister";

   @Override
   protected EntryDao getDao() throws SQLException {
	 return getHelper().getEntryDao();
   }

   /**
    * Saves the given {@link Entry}.
    * 
    * @param newEntry
    *           the entry to save.
    * @return true if the given entry was saved successfully; false otherwise.
    */
   public boolean save(Entry newEntry) {
	 boolean result = false;
	 // If there is no active budget cycle yet, create a new one
	 long id = new BudgetCyclePersister().getActiveCyclesId();

	 if (id == 0) {
	    LogCat.i(TAG, "Created a new budget cycle before saving entry.");
	    new BudgetCyclePersister().save(new BudgetCycle(new Date()));
	    // Retrieve the new budget cycle's id
	    id = new BudgetCyclePersister().getActiveCyclesId();
	 }
	 // Set the budget cycle id of the currently active cycle
	 newEntry.setBudgetCycleId(id);
	 try {
	    result = getDao().create(newEntry) == 1;
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	 }
	 return result;
   }

   /**
    * Deletes the {@link Entry} associated with the given entry id.
    * 
    * @param entryId
    *           the entry id of the entry that should be deleted.
    */
   public void delete(long entryId) {
	 try {
	    getDao().deleteById(entryId);
	 } catch (SQLException e) {
	    LogCat.e(TAG, "delete failed.", e);
	 }
   }

   public Collection<Entry> getFilteredEntries(BudgetCycle cycle, EntryFilter scope,
	    EntrySort filter) {
	 if (cycle != null) {
	    try {
		  EntryDao dao = getDao();
		  // Get currently active budget cycle id
		  long cycleId = cycle.getId();
		  QueryBuilder<Entry, Long> builder = dao.queryBuilder();
		  Where where = builder.where();

		  switch (scope) {
		  case ALL:
			where.eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycleId);
			break;
		  case RECURRING:
			where.and(where.eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycleId),
				 where.eq(EntryDao.IS_RECURRING_FIELD, true));
			break;
		  case NON_RECURRING:
			where.and(where.eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycleId),
				 where.eq(EntryDao.IS_RECURRING_FIELD, false));
			break;
		  }

		  switch (filter) {
		  case BY_CASHFLOW_DIRECTION:
			builder.orderBy(EntryDao.CASHFLOW_DIRECTION_FIELD, false);
			break;
		  case BY_CATEGORY:
			builder.orderBy(EntryDao.CATEGORY_FIELD, false);
			break;
		  default:
			// Use by date as the default
			builder.orderBy(EntryDao.DATE_TIME_FIELD, false);
			break;
		  }
		  return dao.query(builder.prepare());

	    } catch (SQLException e) {
		  LogCat.e(TAG, "getFilteredEntries failed.", e);
	    }
	 }

	 return new ArrayList<Entry>();
   }

   public Collection<Entry> getFilteredEntries(EntryFilter scope, EntrySort filter) {
	 return getFilteredEntries(new BudgetCyclePersister().getActiveCycle(), scope, filter);
   }

   /**
    * If there is no currently active cycle, an amount of zero is returned with
    * the current locale option.
    * 
    * @return the overall budget currently available based on income and
    *         expenses, recurring as well as unique for the current budget
    *         cycle.
    */
   public CurrencyAmount getCurrentBudget() {
	 // TODO optimise using SUM raw query
	 BudgetCycle cycle = new BudgetCyclePersister().getActiveCycle();
	 LocaleComponent locale = Application.getInstance().getComponent(LocaleComponent.class);
	 if (cycle != null) {
	    CurrencyAmount result = cycle.createEmptyCurrency(locale.getLocale());
	    Collection<Entry> entries = getFilteredEntries(cycle, EntryFilter.ALL,
			EntrySort.BY_CATEGORY);

	    for (Entry entry : entries) {
		  if (CashflowDirection.EXPENSE.equals(entry.getCashflowDirection())) {
			result.substract(entry.getAmount());
		  } else {
			result.add(entry.getAmount());
		  }
	    }
	    return result;
	 } else {
	    // Return the currency as zero with currently set locale
	    return new CurrencyAmount(0, locale.getLocale());
	 }
   }

   /**
    * @param cycle
    *           the {@link BudgetCycle} whose entries should be counted.
    * @return the number of entries associated with the given budget cycle; zero
    *         if an error occurred.
    */
   public long countEntries(BudgetCycle cycle) {
	 try {
	    EntryDao dao = getDao();
	    QueryBuilder<Entry, Long> builder = dao.queryBuilder();
	    builder.setCountOf(true);
	    // Count all associated entries
	    builder.where().eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycle.getId());
	    return dao.countOf(builder.prepare());
	 } catch (SQLException e) {
	    LogCat.e(TAG, "countEntries failed.", e);
	    return 0;
	 }
   }

   public Collection<Entry> getLastCyclesRecurringEntries() {
	 try {
	    return getDao().getLastCyclesRecurringEntries();
	 } catch (SQLException e) {
	    LogCat.e(TAG, "getLastCyclesRecurringEntries failed.", e);
	 }
	 return new ArrayList<Entry>();
   }

   /**
    * 
    * @return the number of entries stored in the database; -1 if an error
    *         occurred.
    */
   public long count() {
	 try {
	    return getDao().countOf();

	 } catch (SQLException e) {
	    LogCat.e(TAG, "count failed.", e);
	    return -1;
	 }
   }

   public void clearTable() {
	 try {
	    TableUtils.clearTable(getConnectionSource(), Entry.class);

	 } catch (SQLException e) {
	    LogCat.e(TAG, "clearTable failed.", e);
	 }
   }

}
