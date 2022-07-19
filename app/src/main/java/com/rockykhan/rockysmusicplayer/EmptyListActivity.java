package com.rockykhan.rockysmusicplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

public class EmptyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_list);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(EmptyListActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EmptyListActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.warning)
                .show();
    }
}