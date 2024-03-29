package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.Entry;

/**
 * Persists an {@link BudgetCycle} to the database.
 * 
 * @author zainodis
 * 
 */
public class BudgetCycleDao extends BaseDaoImpl<BudgetCycle, Long> {

   public static final String TABLE_NAME = "budget-cycle";
   public static final String ID_FIELD = "id";
   public static final String START_FIELD = "start";
   public static final String END_FIELD = "end";
   public static final String HAS_ENDED_FIELD = "has-ended";
   public static final String LOCALE_FIELD = "locale";

   public BudgetCycleDao(ConnectionSource connectionSource) throws SQLException {
	 super(connectionSource, BudgetCycle.class);
   }

   public BudgetCycleDao(ConnectionSource connectionSource,
	    DatabaseTableConfig<BudgetCycle> tableConfig) throws SQLException {
	 super(connectionSource, tableConfig);
   }

   /**
    * If there is an active cycle and it has reached it's end, a new cycle is
    * automatically created.
    * 
    * @return the id of the currently active cycle; zero if there currently is
    *         no active cycle.
    * @throws SQLException
    *            on error.
    */
   public long getActiveCyclesId() throws SQLException {
	 BudgetCycle cycle = getActiveCycle();
	 return cycle != null ? cycle.getId() : 0;
   }

   /**
    * If there is an active cycle and it has reached it's end, a new cycle is
    * automatically created.
    * 
    * @return the currently active cycle; null if there is none.
    * @throws SQLException
    *            on error.
    */
   public BudgetCycle getActiveCycle() throws SQLException {
	 QueryBuilder<BudgetCycle, Long> builder = queryBuilder();
	 builder.where().eq(HAS_ENDED_FIELD, false);
	 List<BudgetCycle> result = query(builder.prepare());

	 if (result != null && result.size() == 1) {
	    // Return the currently active cycle
	    return result.get(0);
	 }
	 return null;
   }

   /**
    * Ends all ongoing budget cycles by setting their end date and marking them
    * as closed.
    * 
    * @return the number of entries that have been updated.
    * @throws SQLException
    *            on error.
    */
   public int endOngoingCycles() throws SQLException {
	 UpdateBuilder<BudgetCycle, Long> builder = updateBuilder();
	 builder.updateColumnValue(HAS_ENDED_FIELD, true);
	 builder.updateColumnValue(END_FIELD, new Date());
	 builder.setWhere(builder.where().eq(HAS_ENDED_FIELD, false));
	 return update(builder.prepare());
   }

   /**
    * Saves the given {@link BudgetCycle} and adds all recurring entries to it.
    * 
    * @param newCycle
    *           the budget cycle to save.
    * @return true if the budget cycle was saved successfully; false otherwise.
    * @throws SQLException
    *            on error.
    */
   public boolean save(BudgetCycle newCycle) throws SQLException {
	 boolean result = create(newCycle) == 1;
	 if (result) {
	    EntryPersister persister = new EntryPersister();
	    // Add all recurring entries (from last month) to the new cycle
	    Collection<Entry> recurringEntries = persister.getLastCyclesRecurringEntries();
	    for (Entry entry : recurringEntries) {
		  // Create a new one so it's id is reset
		  Entry newEntry = new Entry(entry.getCashflowDirection(), entry.getCategory(),
			   entry.isRecurring(), entry.getAmount());
		  persister.save(newEntry);
	    }
	 }
	 return result;
   }

   /**
    * Deletes all budget cycles and their associated entries, which are older
    * than the given date.
    * 
    * @param olderThan
    *           entries older than the given date may be cleared.
    * @return the number of budget cycles that have been cleared.
    * @throws SQLException
    *            on error.
    */
   public int delete(Date olderThan) throws SQLException {
	 int result = 0;

	 QueryBuilder<BudgetCycle, Long> builder = queryBuilder();
	 builder.selectColumns(ID_FIELD, START_FIELD, HAS_ENDED_FIELD);

	 for (BudgetCycle cycle : query(builder.prepare())) {
	    // Only delete cycles that have already ended
	    if (cycle.getStart().before(olderThan) && cycle.hasEnded()) {
		  // Delete all associated entries
		  new EntryPersister().getDao().deleteEntries(cycle.getId());
		  // Delete the cycle itself
		  deleteById(cycle.getId());
		  result++;
	    }
	 }
	 return result;
   }

}
