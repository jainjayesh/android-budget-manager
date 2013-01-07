package de.zainodis.balancemanager.persistence;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

/**
 * Class that handles obtaining and releasing of {@link OrmLiteSqliteOpenHelper}
 * and is used to run queries on it's target dao.
 * 
 * @author zainodis
 * 
 */
public abstract class Persister<T> extends HelperProvider {

   /**
    * Needs to be overridden by subclasses in order to return THEIR {@link Dao}
    * for internal use/database access.
    * 
    * @return a {@link Dao} of the same type as the {@link Persister}.
    * @throws SQLException
    *            if retrieving the dao failed.
    */
   protected abstract T getDao() throws SQLException;

}
