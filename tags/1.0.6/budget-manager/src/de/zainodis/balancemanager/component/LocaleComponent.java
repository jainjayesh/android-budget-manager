package de.zainodis.balancemanager.component;

import java.util.Locale;

import de.zainodis.balancemanager.model.Setting;
import de.zainodis.balancemanager.model.options.LocaleOption;
import de.zainodis.balancemanager.model.persistence.SettingPersister;
import de.zainodis.commons.communication.local.Callback;
import de.zainodis.commons.component.Component;
import de.zainodis.commons.component.IEntity;

/**
 * Keeps track of the selected {@link Locale}.
 * 
 * @author fzarrai
 * 
 */
public class LocaleComponent extends Component {

   private Locale locale;

   public LocaleComponent(IEntity owner) {
	 super(owner);
	 // Subscribe to option changes
	 SettingPersister.OnSettingsUpdated.subscribe(callback);
	 updateCachedLocale();
   }

   private final Callback callback = new Callback() {

	 @Override
	 public void execute(Object... arguments) {
	    // Expect setting as first argument
	    Setting changed = (Setting) arguments[0];
	    if (changed.getName().equals(new LocaleOption().getOptionName())) {
		  // The currency setting has changed, update cached value
		  updateCachedLocale(changed);
	    }
	 }
   };

   protected void updateCachedLocale() {
	 updateCachedLocale(new SettingPersister().get(new LocaleOption().getOptionName()));
   }

   protected void updateCachedLocale(Setting setting) {
	 if (setting != null) {
	    locale = new LocaleOption(setting.getValue()).getValue();
	 }
   }

   /**
    * @return the locale set via the options; or the default locale if no locale
    *         has been set in the options.
    */
   public Locale getLocale() {
	 return locale != null ? locale : Locale.getDefault();
   }
}
