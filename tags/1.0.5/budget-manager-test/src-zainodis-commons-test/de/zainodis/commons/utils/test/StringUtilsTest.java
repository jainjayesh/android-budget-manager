package de.zainodis.commons.utils.test;

import de.zainodis.commons.utils.StringUtils;
import junit.framework.TestCase;

/**
 * Class under test: {@link StringUtils}.
 * 
 * @author fzarrai
 * 
 */
public class StringUtilsTest extends TestCase {

   public void testTrim() throws Exception {
	 String toTrim = "Hello world";

	 assertTrue(toTrim.equals(StringUtils.trim(toTrim)));
	 // Trailing whitespace
	 assertTrue(toTrim.equals(StringUtils.trim(String.format("%s ", toTrim))));
	 // Leading and trailing whitespace
	 assertTrue(toTrim.equals(StringUtils.trim(String.format(" %s ", toTrim))));
	 // Trailing whitespace
	 assertTrue(toTrim.equals(StringUtils.trim(String.format("%s ", toTrim))));
	 // Tab characters and leading and trailing whitespace whitespace
	 assertTrue(toTrim.equals(StringUtils.trim(String.format(" \t  %s   ", toTrim))));
	 // Tab characters, new line and leading and trailing whitespace whitespace
	 assertTrue(toTrim.equals(StringUtils.trim(String.format(" \n  %s \t   ", toTrim))));

   }
}
