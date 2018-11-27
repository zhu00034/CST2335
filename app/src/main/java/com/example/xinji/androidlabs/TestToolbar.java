package com.example.xinji.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    private String snackbarMes = "Replace with your own action";
    private String item1Mes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        //Lab8 -step 5
        Toolbar lab8_toolbar = (Toolbar) findViewById(R.id.lab8_toolbar);
        setSupportActionBar(lab8_toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, snackbarMes, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //Lab8 -step 5
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        // Handle presses on the action bar items
        switch (mi.getItemId()) {
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");
                if (item1Mes == null) {
                    Snackbar.make(findViewById(R.id.action_one), "You selected item 1", Snackbar.LENGTH_SHORT)
                            .setAction("action", null).show();
                } else {
                    Snackbar.make(findViewById(R.id.action_one), item1Mes, Snackbar.LENGTH_SHORT)
                            .setAction("action", null).show();
                }

                break;

            case R.id.action_two:
                Log.d("Toolbar","Option 2 selected");
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle(R.string.alertAction2_toolbar);
                //add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                    //user clicked ok button
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    //user cancelled the dialog
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

                builder2.setTitle(R.string.alertAction3_toolbar);
                LayoutInflater inflater = this.getLayoutInflater();
                final View view=inflater.inflate(R.layout.custome_dialog,null);
                builder2.setView(view);
                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText text=(EditText)view.findViewById(R.id.customeMessage);
                        item1Mes=text.getText().toString();
                    }
                });
                builder2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();

                break;

            case R.id.about:

                Log.d("Toolbar", "about is selected");
                //show a Toast
                Toast toast = Toast.makeText(this, "Version 1.0, by Xinji Zhu", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }

}