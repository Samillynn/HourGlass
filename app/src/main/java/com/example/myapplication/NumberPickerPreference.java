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
    public int max_value = 90;
    public static final int MIN_VALUE = 0;
    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    // Supplier is an interface whose method id get()
    // It takes no argument and returns an integer
    Supplier<Integer> getValue;
    // Consumer is an interface whose method id accept()
    // It takes in an integer and does not return any value
    Consumer<Integer> setValue;

    // Constructor
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxValue(int maxValue){
        this.max_value = maxValue;
    }

    // Initialize and reset the NumberPicker when inflating the dialog view
    @Override
    protected void onBindDialogView(View view) {
        Log.i("Dialog", "onBindDialogView");
        super.onBindDialogView(view);
        picker = view.findViewById(R.id.number_picker);
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(max_value);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue.get());
    }

    // If ok is pressed, update the changed value in the SharedData pool
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue.accept(newValue);
            }
        }
    }

    // Get the value from the attribute of the number picker
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