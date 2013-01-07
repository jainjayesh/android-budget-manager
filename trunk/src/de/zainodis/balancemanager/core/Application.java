package de.zainodis.balancemanager.core;

import android.content.Context;

public class Application extends android.app.Application {

   private static Application self = null;

   @Override
   public void onCreate() {
	 super.onCreate();
	 self = this;
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
}
