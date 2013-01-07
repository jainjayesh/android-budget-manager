package de.zainodis.balancemanager.model.persistence;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.zainodis.balancemanager.model.Group;

/**
 * Persists a {@link Group} entry to the database.
 * 
 * @author zainodis
 * 
 */
public class GroupDao extends BaseDaoImpl<Group, String> {

   public static final String TABLE_NAME = "group";
   public static final String NAME_FIELD = "name";

   public GroupDao(ConnectionSource connectionSource) throws SQLException {
	 super(connectionSource, Group.class);
   }

   public GroupDao(ConnectionSource connectionSource, DatabaseTableConfig<Group> tableConfig)
	    throws SQLException {
	 super(connectionSource, tableConfig);
   }

   /**
    * Creates or updates the given group name.
    * 
    * @param groupName
    *           the group name to save.
    * @return the number of rows that have been modified.
    * @throws SQLException
    *            on error.
    */
   public int save(String groupName) throws SQLException {
	 CreateOrUpdateStatus status = createOrUpdate(new Group(groupName));
	 return status.isCreated() || status.isUpdated() ? 1 : 0;
   }

   /**
    * @return a list with all available groups, with no duplicates; or an empty
    *         list if there are none.
    * @throws SQLException
    *            on error.
    */
   public Collection<String> getAll() throws SQLException {
	 Collection<String> result = new HashSet<String>();
	 for (Group group : queryForAll()) {
	    result.add(group.getName());
	 }
	 return result;
   }
}
