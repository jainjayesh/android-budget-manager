package de.zainodis.balancemanager.core;

import android.content.Context;
import de.zainodis.balancemanager.component.LocaleComponent;
import de.zainodis.commons.component.Component;
import de.zainodis.commons.component.Entity;
import de.zainodis.commons.component.IEntity;

public class Application extends android.app.Application {

   private static Application self = null;

   protected IEntity rootEntity = new Entity();

   @Override
   public void onCreate() {
	 super.onCreate();
	 self = this;

	 // Add required components
	 rootEntity.addComponent(new LocaleComponent(rootEntity));
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
