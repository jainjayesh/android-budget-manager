package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.Date;

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
	    return getDao().create(newCycle) == 1;
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
   public long getActiveCyclesId(boolean createNew) {
	 try {
	    BudgetCycleDao dao = getDao();
	    long currentCyclesId = dao.getActiveCyclesId();

	    if (currentCyclesId == 0 && createNew) {
		  // There currently is no active cycle, start a new one
		  save(new BudgetCycle(new Date()));
		  // Now query again for the id
		  currentCyclesId = dao.getActiveCyclesId();
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
}
