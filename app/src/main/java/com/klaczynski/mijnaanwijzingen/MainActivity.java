package com.klaczynski.mijnaanwijzingen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.klaczynski.mijnaanwijzingen.io.InOutOperator;
import com.klaczynski.mijnaanwijzingen.io.MockData;
import com.klaczynski.mijnaanwijzingen.misc.Definitions;
import com.klaczynski.mijnaanwijzingen.obj.Aanwijzing;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean warningAcknowledged = false;
    public static ArrayList<Aanwijzing> aanwijzingen = new ArrayList<>();
    private InOutOperator io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        io = new InOutOperator(MainActivity.this);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (!Definitions.DEBUG)
        warningDialog(MainActivity.this);
        try {
            aanwijzingen = io.loadList(Definitions.LIJST_KEY);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e.fillInStackTrace());
        }
        //MockData.addData();

        AanwijzingenAdapter adapter = new AanwijzingenAdapter(this.getApplicationContext(), aanwijzingen);
        ListView lijst = findViewById(R.id.lijst);
        lijst.setAdapter(adapter);
        lijst.setClickable(true);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.aanwijzingen_menu);
                dialog.show();

                Button buttonVR = dialog.findViewById(R.id.buttonVR);
                buttonVR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.vr_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonOVW = dialog.findViewById(R.id.buttonOVW);
                buttonOVW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.ovw_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonSB = dialog.findViewById(R.id.buttonSB);
                buttonSB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.sb_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonSTS = dialog.findViewById(R.id.buttonSTS);
                buttonSTS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.sts_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonSTSN = dialog.findViewById(R.id.buttonSTSN);
                buttonSTSN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getRootView().getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.stsn_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
                Button buttonTTV = dialog.findViewById(R.id.buttonTTV);
                buttonTTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), CreationActivity.class);
                        i.putExtra("TYPE", R.layout.ttv_create);
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        //if(!warningAcknowledged) finishAffinity();
        updateView();
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Definitions.DEBUG)
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_disclaimer:
                warningDialog(MainActivity.this);
                break;
            case R.id.action_testActivity:
                Intent i = new Intent(this, CreationActivity.class);
                i.putExtra("TYPE", R.layout.vr_create);
                startActivity(i);
                break;
            case R.id.action_clearData:
                aanwijzingen = new ArrayList<>();
                updateView();
                io.saveList(aanwijzingen, Definitions.LIJST_KEY);
                break;
            case R.id.action_MockData:
                aanwijzingen = io.loadMockJson();
                updateView();
                io.saveList(aanwijzingen, Definitions.LIJST_KEY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void warningDialog(Activity a) {
        AlertDialog alertDialog = new AlertDialog.Builder(a).create();
        alertDialog.setTitle("Waarschuwing");
        alertDialog.setMessage("Het gebruik van deze app is op eigen risico. De ontwikkelaar van deze app kan " +
                "niet verantwoordelijk worden gehouden voor gebruik van deze app, evenals eventuele gevolgen hiervan. " +
                "De machinist is ten alle tijden zelf verantwoordelijk voor het juist aannemen, opvolgen en bewaren van aanwijzingen");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Akkoord",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        warningAcknowledged = true;
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void updateView() {
            ListView lijst = findViewById(R.id.lijst);
            /*((ArrayAdapter) lijst.getAdapter()).clear();
            ((ArrayAdapter) lijst.getAdapter()).addAll(aanwijzingen);*/
            ((ArrayAdapter) lijst.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        io.saveList(aanwijzingen, Definitions.LIJST_KEY);
        super.onStop();
    }
}