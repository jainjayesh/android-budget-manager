package de.zainodis.balancemanager.core;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.content.Context;
import de.zainodis.balancemanager.R;
import de.zainodis.balancemanager.component.LocaleComponent;
import de.zainodis.commons.component.Component;
import de.zainodis.commons.component.Entity;
import de.zainodis.commons.component.IEntity;

@ReportsCrashes(formKey = "", mailTo = "zainodis@gmail.com", customReportContent = {
	 ReportField.APP_VERSION_CODE, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
	 ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT }, 
	 mode = ReportingInteractionMode.TOAST, resToastText = R.string.sending_crash_report)
public class Application extends android.app.Application {

   private static Application self = null;

   protected IEntity rootEntity = new Entity();

   @Override
   public void onCreate() {
	 super.onCreate();
	 self = this;

	 // Add required components
	 rootEntity.addComponent(new LocaleComponent(rootEntity));

	 // Initialize the ACRA bug reporter
	 ACRA.init(this);
   }

   /**
    * Beware! You can't just use the {@link android.app.Application}
    * {@link Context} for everything, where a context is required! UI stuff for
    * instance requires an actual activity context.
    * 
    * @return the {@link android.app.Application} {@link Context}.
    */
   public static Application getInstance() {
	 return self;
   }

   public <T extends Component> T getComponent(Class<T> requested) {
	 return rootEntity.getComponent(requested);
   }
}
