package de.zainodis.balancemanager.model.options;

import static junit.framework.Assert.assertEquals;

import java.util.Locale;

import de.zainodis.commons.utils.StringUtils;

public class LocaleOption extends Option<Locale> {

   private static final String DIVIDER = StringUtils.COMMA;

   public LocaleOption() {
	 super();
   }

   public LocaleOption(String value) {
	 super(value);
   }

   public LocaleOption(Locale locale) {
	 super(locale);
   }

   @Override
   public void execute() {
   }

   @Override
   protected Locale parse(String value) {
	 /*
	  * We expect the serialised locale and a language that goes with the
	  * locale. The language is required to re-create the locale. The language
	  * is expected first, then the country.
	  */
	 String[] localeData = value.split(DIVIDER);
	 assertEquals("To create a locale, the language and country are required.", 2,
		  localeData.length);
	 return new Locale(localeData[0], localeData[1]);
   }

   @Override
   public String getOptionName() {
	 return LocaleOption.class.getSimpleName();
   }

   public String format() {
	 if (getValue() != null) {
	    return String.format("%s%s%s", getValue().getLanguage(), DIVIDER, getValue().getCountry());
	 }
	 return null;
   }
}
