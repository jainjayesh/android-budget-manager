package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.zainodis.balancemanager.model.BudgetCycle;

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

   public BudgetCycleDao(ConnectionSource connectionSource) throws SQLException {
	 super(connectionSource, BudgetCycle.class);
   }

   public BudgetCycleDao(ConnectionSource connectionSource,
	    DatabaseTableConfig<BudgetCycle> tableConfig) throws SQLException {
	 super(connectionSource, tableConfig);
   }

   /**
    * @return the id of the currently active cycle; zero if there currently is
    *         no active cycle.
    * @throws SQLException
    *            on error.
    */
   public long getActiveCyclesId() throws SQLException {
	 QueryBuilder<BudgetCycle, Long> builder = queryBuilder();
	 builder.where().eq(HAS_ENDED_FIELD, false);
	 List<BudgetCycle> result = query(builder.prepare());
	 if (result != null && result.size() >= 1) {
	    return result.get(0).getId();
	 }
	 return 0;
   }

   public BudgetCycle getActiveCycle() throws SQLException {
	 QueryBuilder<BudgetCycle, Long> builder = queryBuilder();
	 builder.where().eq(HAS_ENDED_FIELD, false);
	 List<BudgetCycle> result = query(builder.prepare());
	 if (result != null && result.size() == 1) {
	    return result.get(0);
	 }
	 return null;
   }

   /**
    * 
    * @return the number of entries that have been updated.
    * @throws SQLException
    *            on error.
    */
   public int endOngoingCycles() throws SQLException {
	 UpdateBuilder<BudgetCycle, Long> builder = updateBuilder();
	 builder.updateColumnValue(HAS_ENDED_FIELD, true);
	 builder.setWhere(builder.where().eq(HAS_ENDED_FIELD, false));
	 return update(builder.prepare());
   }
}
