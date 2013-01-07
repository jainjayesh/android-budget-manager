package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.zainodis.balancemanager.model.Entry;

/**
 * Persists an {@link Entry} to the database.
 * 
 * @author zainodis
 * 
 */
public class EntryDao extends BaseDaoImpl<Entry, Long> {

   public static final String TABLE_NAME = "entry";
   public static final String ID_FIELD = "id";
   public static final String FK_BUDGET_CYCLE_ID_FIELD = "fk-budget-cycle-id";
   public static final String DATE_TIME_FIELD = "date-time";
   public static final String AMOUNT_FIELD = "amount";
   public static final String IS_MONTHLY_FIELD = "is-monthly";
   public static final String CASHFLOW_DIRECTION_FIELD = "cashflow-direction";
   public static final String GROUP_FIELD = "group";

   public EntryDao(ConnectionSource connectionSource) throws SQLException {
	 super(connectionSource, Entry.class);
   }

   public EntryDao(ConnectionSource connectionSource, DatabaseTableConfig<Entry> tableConfig)
	    throws SQLException {
	 super(connectionSource, tableConfig);
   }

}
