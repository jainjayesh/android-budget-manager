package de.zainodis.balancemanager.model.parsing;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.AboutEntry;
import de.zainodis.commons.model.parsing.XmlFeedParser;
import de.zainodis.commons.utils.ResourceUtils;

public class AboutParser extends XmlFeedParser<AboutEntry> {

   /** A category element has a name attribute and contains entries. */
   private static final String CATEGORY = "category";
   /** An entry element has a name attribute. */
   private static final String ENTRY = "entry";
   /** Attribute that denotes a name for categories and entries. */
   private static final String NAME_ATTRIBUTE = "name";

   private Collection<AboutEntry> result = new LinkedList<AboutEntry>();
   private String currentCategory;

   @Override
   public void processParsedElements(Collection<AboutEntry> items) {
	 // Not required
   }

   @Override
   public void handleStartTag(String name, XmlPullParser parser) throws XmlPullParserException,
	    IOException {
	 if (CATEGORY.equals(name)) {
	    currentCategory = parser.getAttributeValue(null, NAME_ATTRIBUTE);
	    if (ResourceUtils.isAStringResource(currentCategory)) {
		  // Resolve @string resource
		  currentCategory = ResourceUtils.getString(Application.getInstance(),
			   ResourceUtils.extractStringResourceName(currentCategory));

	    }

	 } else if (ENTRY.equals(name)) {
	    result.add(new AboutEntry(currentCategory, parser.getAttributeValue(null, NAME_ATTRIBUTE)));
	 }
   }

   @Override
   public void handleEndTag(String name, XmlPullParser parser) throws XmlPullParserException,
	    IOException {
   }

   @Override
   public Collection<AboutEntry> getResult() {
	 return result;
   }
}
