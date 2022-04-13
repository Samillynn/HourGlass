package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A {@link android.preference.Preference} that displays a number picker as a dialog.
 */
public class NumberPickerPreference extends DialogPreference {

    // allowed range
    public static final int MAX_VALUE = 90;
    public static final int MIN_VALUE = 0;
    public static final int DEFAULT_VALUE = 15;
    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    Supplier<Integer> getValue;
    Consumer<Integer> setValue;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateDialogView() {
        Log.i("Dialog", "onCreateDialogView");
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        Log.i("Dialog", "onBindDialogView");
        super.onBindDialogView(view);
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(MAX_VALUE);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue.get());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        Log.i("Dialog", "onDialogClose");
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue.accept(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        Log.i("Dialog", "onGetDefaultValue");
        return a.getInt(index, MIN_VALUE);
    }

    public void setGetter(Supplier<Integer> getter) {
        getValue = () -> getter.get() / 60;
    }

    public void setSetter(Consumer<Integer> setter) {
        setValue = minutes -> setter.accept(60 * minutes);
    }


}