package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
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
	 // Set the budget cycle id of the currently active cycle
	 newEntry.setBudgetCycleId(new BudgetCyclePersister().getActiveCyclesId(true));
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

   /**
    * 
    * @param monthlyOnly
    *           true if only monthly recurring entries should be returned; false
    *           if all entries should be returned.
    * @return a collection of all {@link Entry}s for the currently active
    *         {@link BudgetCycle}; an empty list, if there are none.
    */
   public Collection<Entry> getEntries(boolean monthlyOnly) {
	 try {
	    EntryDao dao = getDao();
	    // Get currently active budget cycle id
	    long cycleId = new BudgetCyclePersister().getActiveCyclesId(false);
	    if (cycleId > 0) {
		  QueryBuilder<Entry, Long> builder = dao.queryBuilder();
		  Where where = builder.where();
		  if (monthlyOnly) {
			where.and(where.eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycleId),
				 where.eq(EntryDao.IS_MONTHLY_FIELD, true));
		  } else {
			where.eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycleId);
		  }
		  return dao.query(builder.prepare());
	    }

	 } catch (SQLException e) {
	    LogCat.e(TAG, "getEntries failed.", e);
	 }
	 return new ArrayList<Entry>();
   }

   /**
    * @return all non-recurring entries, sorted by their group for the current
    *         {@link BudgetCycle}.
    */
   public Collection<Entry> getEntriesByGroup() {
	 try {
	    EntryDao dao = getDao();
	    // Get currently active budget cycle id
	    long cycleId = new BudgetCyclePersister().getActiveCyclesId(false);
	    if (cycleId > 0) {
		  QueryBuilder<Entry, Long> builder = dao.queryBuilder();
		  Where where = builder.where();
		  where.and(where.eq(EntryDao.FK_BUDGET_CYCLE_ID_FIELD, cycleId),
			   where.eq(EntryDao.IS_MONTHLY_FIELD, false));
		  builder.orderBy(EntryDao.GROUP_FIELD, false);
		  return dao.query(builder.prepare());
	    }

	 } catch (SQLException e) {
	    LogCat.e(TAG, "getEntries failed.", e);
	 }
	 return new ArrayList<Entry>();
   }

   /**
    * 
    * @return the overall budget currently available based on income and
    *         expenses, monthly as well as unique for the current budget cycle.
    */
   public CurrencyAmount getCurrentBudget() {
	 CurrencyAmount result = new CurrencyAmount(0);
	 Collection<Entry> entries = getEntries(false);

	 for (Entry entry : entries) {
	    if (CashflowDirection.EXPENSE.equals(entry.getCashflowDirection())) {
		  result.substract(entry.getAmount());
	    } else {
		  result.add(entry.getAmount());
	    }
	 }
	 return result;
   }

   public Collection<Entry> getLastCyclesMonthlyEntries() {
	 try {
	    return getDao().getLastCyclesMonthlyEntries();
	 } catch (SQLException e) {
	    LogCat.e(TAG, "getLastCyclesMonthlyEntries failed.", e);
	 }
	 return new ArrayList<Entry>();
   }
}
