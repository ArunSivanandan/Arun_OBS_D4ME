package org.custom_font_library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * CustomTextView TextView widget with a typeface done directly using style.
 */
public class CustomEditText extends EditText {
    private Context context;
    public CustomEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
        {
            this.context=context;
            FontCustomTextViewHelper.initialize(this, context, attrs);
        }
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        setCompoundDrawables(null, null, icon, null);
    }

}
