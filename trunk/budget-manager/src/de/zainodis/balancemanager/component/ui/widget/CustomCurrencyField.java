package de.zainodis.balancemanager.component.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import de.zainodis.commons.component.ui.widget.CurrencyField;

public class CustomCurrencyField extends CurrencyField {

   CurrencyKeyboard keyboard;

   public CustomCurrencyField(final Context context, AttributeSet attributes) {
	 super(context, attributes);
	 // Disable standard keyboard
	 InputMethodManager imm = (InputMethodManager) context
		  .getSystemService(Context.INPUT_METHOD_SERVICE);
	 imm.hideSoftInputFromWindow(getWindowToken(), 0);

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

   @Override
   protected void onGainedFocus() {
	 super.onGainedFocus();
	 // We gained focus, show our keyboard
	 keyboard = new CurrencyKeyboard(getContext(), CustomCurrencyField.this);
	 keyboard.show();
   }

   @Override
   protected void onLostFocus() {
	 super.onLostFocus();
	 // We lost focus, hide our keyboard
	 if (keyboard != null) {
	    keyboard.cancel();
	    keyboard = null;
	 }
   }
}
