package org.custom_font_library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * CustomTextView TextView widget with a typeface done directly using style.
 */
public class CustomButton extends Button {

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            FontCustomTextViewHelper.initialize(this, context, attrs);
        }
    }

}
