package com.example.timelineapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CreateTimeline extends DialogFragment {

    public abstract static class ClickHandler{

        public abstract void positive(String title, String description, boolean showTimes);

    };

    public CreateTimeline(ClickHandler theHandler){
        handler = theHandler;
    }

    private ClickHandler handler;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_make_timeline,null))
                .setMessage("Add Timeline")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog theDialog = (Dialog) dialog;
                        EditText theTitle = (EditText) theDialog.findViewById(R.id.enterName);
                        EditText theDescription = (EditText) theDialog.findViewById(R.id.enterDescription);
                        CheckBox theCheckbox = (CheckBox) theDialog.findViewById(R.id.showTimes);
                        handler.positive(theTitle.getText().toString(),theDescription.getText().toString(),theCheckbox.isChecked());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
