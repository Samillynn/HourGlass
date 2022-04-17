package com.example.myapplication.focus_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.models.SharedData;

import java.util.function.Consumer;

public class ForceQuitFocusFragment extends DialogFragment {

    Consumer<Boolean> activityCallback;

    ForceQuitFocusFragment(Consumer<Boolean> activityCallback) {
        this.activityCallback = activityCallback;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (SharedData.getInstance().getForceExitChances() > 0) {
            return confirmQuitDialog();
        } else {
            return noQuitChanceDialog();
        }
    }

    private Dialog confirmQuitDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Force Quit")
                .setMessage("You only have "
                        + SharedData.getInstance().getForceExitChances()
                        + " more chances to force quit focus mode. Are you sure?")
                .setPositiveButton(R.string.force_exit_confirm_quit, (dialog, i) -> {
                    dismiss();
                    new MathQuestionFragment(activityCallback).show(getParentFragmentManager(), "Math Quit");
                })
                .setNegativeButton(android.R.string.cancel, (dialog, i) -> {
                    dismiss();
                    activityCallback.accept(false);
                });

        return dialogBuilder.create();
    }

    private Dialog noQuitChanceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Force Quit")
                .setMessage("Sorry, you can't force quit because you have run out of force quit chances")
                .setNegativeButton(android.R.string.cancel, (dialog, i) -> {
                    dismiss();
                    activityCallback.accept(false);
                });

        return dialogBuilder.create();

    }
}