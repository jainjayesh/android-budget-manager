package de.zainodis.balancemanager.component.ui.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import de.zainodis.commons.component.ui.widget.CurrencyField;

public class CustomCurrencyField extends CurrencyField {

   public CustomCurrencyField(final Context context, AttributeSet attributes) {
	 super(context, attributes);
	 // The closest we can get right now to currency input
	 setInputType(InputType.TYPE_CLASS_PHONE);

	 setOnFocusChangeListener(new OnFocusChangeListener() {

	    @Override
	    public void onFocusChange(View v, boolean hasFocus) {
		  if (hasFocus) {
			onGainedFocus();
		  } else {
			onLostFocus();
		  }
	    }
	 });
   }

}
