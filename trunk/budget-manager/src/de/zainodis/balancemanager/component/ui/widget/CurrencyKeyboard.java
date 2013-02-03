package de.zainodis.balancemanager.component.ui.widget;

import java.util.Currency;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import de.zainodis.balancemanager.R;
import de.zainodis.commons.utils.StringUtils;

/**
 * A keyboard for {@link Currency} input using a non-model dialog.
 * 
 * @author fzarrai
 * 
 */
public class CurrencyKeyboard extends Dialog {

   private EditText textfield;

   // TODO we'll also need a reference to the edittext so we can modify it
   public CurrencyKeyboard(Context context, EditText fieldToEdit) {
	 super(context);
	 textfield = fieldToEdit;
	 requestWindowFeature(Window.FEATURE_NO_TITLE);
	 setContentView(R.layout.w_currency_keyboard);
	 setCancelable(true);
	 /*
	  * The following setup will make sure, that the dialog is not modal, and
	  * that the background activity is not dimmed/blurred.
	  */
	 Window window = getWindow();
	 window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
	 window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	 window.setGravity(Gravity.BOTTOM);

	 // Make the dialog take up the whole screen width
	 LayoutParams params = window.getAttributes();
	 params.width = LayoutParams.FILL_PARENT;
	 window.setAttributes((android.view.WindowManager.LayoutParams) params);

	 // Set keylistener for each button
	 findViewById(R.id.w_currency_keyboard_one).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedOne();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_two).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedTwo();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_three).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedThree();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_four).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedFour();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_five).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedFive();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_six).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedSix();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_seven).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedSeven();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_eight).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedEight();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_nine).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedNine();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_zero).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedZero();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_dot).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedDot();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_comma).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedComma();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_done).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedDone();
	    }
	 });
	 findViewById(R.id.w_currency_keyboard_back).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		  pressedBack();
	    }
	 });
   }

   public void pressedOne() {
	 textfield.append("1");
   }

   public void pressedTwo() {
	 textfield.append("2");
   }

   public void pressedThree() {
	 textfield.append("3");
   }

   public void pressedFour() {
	 textfield.append("4");
   }

   public void pressedFive() {
	 textfield.append("5");
   }

   public void pressedSix() {
	 textfield.append("6");
   }

   public void pressedSeven() {
	 textfield.append("7");
   }

   public void pressedEight() {
	 textfield.append("8");
   }

   public void pressedNine() {
	 textfield.append("9");
   }

   public void pressedZero() {
	 textfield.append("0");
   }

   public void pressedComma() {
	 textfield.append(StringUtils.COMMA);
   }

   public void pressedDot() {
	 textfield.append(StringUtils.DOT);
   }

   public void pressedBack() {
	 Editable text = textfield.getText();
	 if (text.length() > 0) {
	    textfield.setText(text.subSequence(0, text.length() - 1));
	 }
   }

   public void pressedDone() {
	 // Close the keyboard
	 cancel();
	 // Shift focus to next field
	 View nextField = textfield.focusSearch(View.FOCUS_DOWN);
	 if (nextField != null) {
	    nextField.requestFocus();
	 } else {
	    // The next best thing is to clear focus
	    textfield.clearFocus();
	 }
   }

}
