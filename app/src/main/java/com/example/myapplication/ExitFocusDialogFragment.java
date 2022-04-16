package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

public class ExitFocusDialogFragment extends DialogFragment {
    Consumer<Boolean> activityCallback;

    ExitFocusDialogFragment(Consumer<Boolean> callback) {
        super();
        activityCallback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final EditText input = new EditText(getContext());
        //Setting up the questions Hashmap
        Questions questions = new Questions();
        questions.Questions();
        
        //Getting hashmap
        HashMap exitQuestions = questions.getMap();
        
        //Getting random number
        Random rand = new Random();
        int size = exitQuestions.size();
        int random = rand.nextInt(size);

        String[] questionsAnswers = (String[]) exitQuestions.get(random);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext())
                .setView(input)
                .setTitle("Emergency Exit")
                .setMessage("Please answer to exit\n" + questionsAnswers[0])
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().toString().equals(questionsAnswers[1])){
                            activityCallback.accept(true);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle a negative answer
                        activityCallback.accept(false);
                        dialog.dismiss();
                    }
                });
        return dialogBuilder.create();
    }
}
