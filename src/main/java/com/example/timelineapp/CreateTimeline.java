package com.example.timelineapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CreateTimeline extends DialogFragment {

    public abstract static class ClickHandler{
        public abstract void positive(String title, String description, boolean showTimes);
    };

    public CreateTimeline(ClickHandler theHandler){
        handler = theHandler;
        this.message = R.string.make_timeline;
    }
    public CreateTimeline(ClickHandler theHandler, int message, boolean showCheckbox){
        handler = theHandler;
        this.message = message;
        this.showCheckbox = showCheckbox;
    }

    private ClickHandler handler;
    private boolean showCheckbox;
    private int message;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_make_timeline,null))
                .setMessage(this.message)
                .setPositiveButton("Create", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Dialog theDialog = (Dialog) dialog;
                    EditText theTitle = (EditText) theDialog.findViewById(R.id.enterName);
                    EditText theDescription = (EditText) theDialog.findViewById(R.id.enterDescription);
                    CheckBox theCheckbox = (CheckBox) theDialog.findViewById(R.id.showTimes);
                    if (!showCheckbox){
                        theCheckbox.setVisibility(View.GONE);
                    }
                    handler.positive(theTitle.getText().toString(),theDescription.getText().toString(),theCheckbox.isChecked());
                })
                .setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {

                });
        return builder.create();
    }
}
