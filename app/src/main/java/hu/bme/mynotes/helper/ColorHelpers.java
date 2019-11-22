package hu.bme.mynotes.helper;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import androidx.core.content.ContextCompat;

import hu.bme.mynotes.R;

public abstract class ColorHelpers {
    private static Integer brightColor;

    public static void init(Context ctx) {
        brightColor = ContextCompat.getColor(ctx, R.color.colorPrimaryBright);
    }

    public static Spanned formatTag(Context ctx, String tagName) {
        return Html.fromHtml(String.format(
            "<font color=\"#%s\">#</font><i>%s</i>",
            Integer.toHexString(
                brightColor & 0x00ffffff
            ),

            tagName.substring(1)
        ));
    }

    public static int getBrightColor() {
        if(brightColor == null) {
            throw new RuntimeException("Uninitialized");
        }
        return brightColor;
    }
}
