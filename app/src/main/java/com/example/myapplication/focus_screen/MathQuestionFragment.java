package com.example.myapplication.focus_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.models.SharedData;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

public class MathQuestionFragment extends DialogFragment {
    Consumer<Boolean> activityCallback;

    MathQuestionFragment(Consumer<Boolean> callback) {
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
                .setPositiveButton("Submit", (dialog, which) -> {
                    if(input.getText().toString().equals(questionsAnswers[1])){
                        SharedData.getInstance().setForceExitChances(SharedData.getInstance().getForceExitChances() - 1);
                        activityCallback.accept(true);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // Handle a negative answer
                    activityCallback.accept(false);
                    dialog.dismiss();
                });
        return dialogBuilder.create();
    }
}
