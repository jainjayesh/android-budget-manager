package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.persistence.Persister;
import de.zainodis.commons.LogCat;

public class BudgetCyclePersister extends Persister<BudgetCycleDao> {

   private static final String TAG = "BudgetCyclePersister";

   @Override
   protected BudgetCycleDao getDao() throws SQLException {
	 return getHelper().getBudgetCycleDao();
   }

   /**
    * Saves the given {@link BudgetCycle}.
    * 
    * @param newCycle
    *           the budget cycle to save.
    * @return true if the given budget cycle was saved successfully; false
    *         otherwise.
    */
   public boolean save(BudgetCycle newCycle) {
	 try {
	    return getDao().save(newCycle);
	 } catch (SQLException e) {
	    LogCat.e(TAG, "save failed.", e);
	    return false;
	 }
   }

   /**
    * If there is no currently active budget cycle, a new one is automatically
    * created.
    * 
    * @param createNew
    *           if true a new cycle will be created, if none exists.
    * @return the unique id, of the currently active {@link BudgetCycle}; zero
    *         if an error occurred.
    */
   public long getActiveCyclesId() {
	 try {
	    BudgetCycleDao dao = getDao();
	    long currentCyclesId = dao.getActiveCyclesId();
	    if (currentCyclesId == 0) {
		  LogCat.w(TAG, "No active budget cycle available.");
	    }
	    return currentCyclesId;

	 } catch (SQLException e) {
	    LogCat.e(TAG, "getActiveCyclesId failed.", e);
	    return 0;
	 }
   }

   /**
    * 
    * @return an instance of the currently active {@link BudgetCycle}; null if
    *         there is none.
    */
   public BudgetCycle getActiveCycle() {
	 try {
	    return getDao().getActiveCycle();

	 } catch (SQLException e) {
	    LogCat.e(TAG, "getActiveCycle failed.", e);
	    return null;
	 }
   }

   public void endOngoingCycles() {
	 try {
	    getDao().endOngoingCycles();

	 } catch (SQLException e) {
	    LogCat.e(TAG, "endOngoingCycles failed.", e);
	 }
   }

   /**
    * 
    * @return the id of the last budget cycle; zero if there was none or an
    *         error occurred.
    */
   public long getLastCyclesId() {
	 try {
	    BudgetCycleDao dao = getDao();
	    QueryBuilder<BudgetCycle, Long> builder = dao.queryBuilder();
	    builder.orderBy(BudgetCycleDao.ID_FIELD, false);
	    builder.where().eq(BudgetCycleDao.HAS_ENDED_FIELD, true);
	    builder.limit(1L);
	    BudgetCycle lastCycle = dao.queryForFirst(builder.prepare());

	    if (lastCycle != null) {
		  return lastCycle.getId();
	    }

	 } catch (SQLException e) {
	    LogCat.e(TAG, "getLastCyclesId failed.", e);
	 }

	 return 0;
   }

   public void clearTable() {
	 try {
	    TableUtils.clearTable(getConnectionSource(), BudgetCycle.class);

	 } catch (SQLException e) {
	    LogCat.e(TAG, "clearTable failed.", e);
	 }
   }
}
