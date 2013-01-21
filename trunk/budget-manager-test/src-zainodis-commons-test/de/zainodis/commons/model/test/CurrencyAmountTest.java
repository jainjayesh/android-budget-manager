package de.zainodis.commons.model.test;

import java.util.Locale;

import de.zainodis.commons.model.CurrencyAmount;
import junit.framework.TestCase;

/**
 * Class under test: {@link CurrencyAmount}.
 * 
 * @author fzarrai
 * 
 */
public class CurrencyAmountTest extends TestCase {

   public void testCreate() throws Exception {

	 CurrencyAmount amount = new CurrencyAmount(20);
	 assertEquals(20.0, amount.getAsDouble());

	 // For UK locale
	 Locale locale = Locale.UK;

	 amount = new CurrencyAmount("400", locale);
	 assertEquals(400.0, amount.getAsDouble());

	 amount = new CurrencyAmount("123.456", locale);
	 assertEquals(123.456, amount.getAsDouble());

	 amount = new CurrencyAmount("123,456", locale);
	 assertEquals(123456.0, amount.getAsDouble());

	 try {
	    amount = new CurrencyAmount("+3456");
	    fail("Invalid currency value.");
	 } catch (IllegalArgumentException e) {
	    // Expected
	 }

	 try {
	    amount = new CurrencyAmount("5b56");
	    fail("Invalid currency value.");
	 } catch (IllegalArgumentException e) {
	    // Expected
	 }

	 try {
	    amount = new CurrencyAmount("7*90");
	    fail("Invalid currency value.");
	 } catch (IllegalArgumentException e) {
	    // Expected
	 }

	 // For German locale
	 locale = Locale.GERMANY;

	 amount = new CurrencyAmount("400", locale);
	 assertEquals(400.0, amount.getAsDouble());

	 amount = new CurrencyAmount("123.456", locale);
	 assertEquals(123456.0, amount.getAsDouble());

	 amount = new CurrencyAmount("123,456", locale);
	 assertEquals(123.456, amount.getAsDouble());

   }

   public void testGetDecimalSeparator() throws Exception {

	 // Decimal separator for UK is a dot
	 assertTrue(CurrencyAmount.getDecimalSeparator(Locale.UK) == '.');
	 // Decimal separator for Germany is a comma
	 assertTrue(CurrencyAmount.getDecimalSeparator(Locale.GERMANY) == ',');
	 // Decimal separator for France is a comma
	 assertTrue(CurrencyAmount.getDecimalSeparator(Locale.FRANCE) == ',');
	 // Decimal separator for US is a dot
	 assertTrue(CurrencyAmount.getDecimalSeparator(Locale.US) == '.');

   }
}
