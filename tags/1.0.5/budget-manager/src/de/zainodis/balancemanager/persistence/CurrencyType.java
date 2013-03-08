package de.zainodis.balancemanager.persistence;

import java.lang.reflect.Field;
import java.sql.SQLException;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DoubleObjectType;
import com.j256.ormlite.support.DatabaseResults;

import de.zainodis.commons.model.CurrencyAmount;

/**
 * Converts {@link CurrencyAmount}s to an appropriate database format to store
 * them in a database and converts database values back to currency objects.
 * 
 * @author zainodis
 * 
 */
public class CurrencyType extends DoubleObjectType {

   private static final CurrencyType self = new CurrencyType();

   /**
    * Static method required by the ormlite framework. By convention those
    * methods are named "getInstance" - unfortunately in this case we're bound
    * to the convention of the ormlite framework, hence the name "getSingleton".
    * 
    * @return an instance of {@link CurrencyType}.
    */
   public static CurrencyType getSingleton() {
	 return self;
   }

   private CurrencyType() {
	 this(SqlType.DOUBLE, new Class<?>[] { CurrencyAmount.class });
   }

   protected CurrencyType(SqlType sqlType, Class<?>[] classes) {
	 super(sqlType, classes);
   }

   @Override
   public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
	 // Convert the currency to double
	 return ((CurrencyAmount) javaObject).getAsDouble();
   }

   @Override
   public Object resultToJava(FieldType fieldType, DatabaseResults results, int columnPos)
	    throws SQLException {
	 Double value = results.getDouble(columnPos);
	 return new CurrencyAmount(value);
   }

   @Override
   public boolean isValidForField(Field field) {
	 return CurrencyAmount.class.isAssignableFrom(field.getType());
   }
}
