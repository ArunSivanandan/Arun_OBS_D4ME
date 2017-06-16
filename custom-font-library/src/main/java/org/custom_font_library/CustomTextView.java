package org.custom_font_library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * CustomTextView TextView widget with a typeface done directly using style.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            FontCustomTextViewHelper.initialize(this, context, attrs);
        }
    }
}
