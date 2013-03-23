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

	 // For German locale
	 locale = Locale.GERMANY;

	 amount = new CurrencyAmount("400", locale);
	 assertEquals(400.0, amount.getAsDouble());

	 amount = new CurrencyAmount("123.456", locale);
	 assertEquals(123456.0, amount.getAsDouble());

	 amount = new CurrencyAmount("123,456", locale);
	 assertEquals(123.456, amount.getAsDouble());

   }

   public void testCreateStringBased() throws Exception {

	 // For US locale
	 CurrencyAmount amount = new CurrencyAmount("$25.46", Locale.US);
	 assertEquals(25.46, amount.getAsDouble());

	 // For French locale
	 amount = new CurrencyAmount("£25.46", Locale.UK);
	 assertEquals(25.46, amount.getAsDouble());

	 // For German locale
	 amount = new CurrencyAmount("25,46€", Locale.GERMANY);
	 assertEquals(25.46, amount.getAsDouble());

	 // For French locale
	 amount = new CurrencyAmount("25,46€", Locale.FRANCE);
	 assertEquals(25.46, amount.getAsDouble());

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

   public void testGetCurrencySymbol() throws Exception {
	 assertTrue(CurrencyAmount.getCurrencySymbol(
		  new Locale(Locale.UK.getLanguage(), Locale.UK.getCountry())).equals("£"));
	 assertTrue(CurrencyAmount.getCurrencySymbol(
		  new Locale(Locale.GERMANY.getLanguage(), Locale.GERMANY.getCountry())).equals("€"));
	 assertTrue(CurrencyAmount.getCurrencySymbol(
		  new Locale(Locale.US.getLanguage(), Locale.US.getCountry())).equals("$"));
   }
}
