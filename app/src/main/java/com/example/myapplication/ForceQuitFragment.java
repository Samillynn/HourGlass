package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.function.Consumer;

public class ForceQuitFragment extends DialogFragment {

    Consumer<Boolean> activityCallback;

    ForceQuitFragment(Consumer<Boolean> activityCallback) {
        this.activityCallback = activityCallback;
    }

    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                activityCallback.accept(true);
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                activityCallback.accept(false);
            }
        }
    };

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
                .setPositiveButton(R.string.force_exit_confirm_quit, onClickListener)
                .setNegativeButton(android.R.string.cancel, onClickListener);

        return dialogBuilder.create();
    }

    private Dialog noQuitChanceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setTitle("Force Quit")
                .setMessage("Sorry, you can't force quit because you have run out of force quit chances")
                .setNegativeButton(android.R.string.cancel, onClickListener);

        return dialogBuilder.create();

    }
}