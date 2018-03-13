package com.example.sruthi_4404.zcrmFieldBuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.sruthi_4404.zcrmFieldBuddy.list.CompletedAppointmentsListActivity;
import com.example.sruthi_4404.zcrmFieldBuddy.list.ContactListActivity;
import com.example.sruthi_4404.zcrmFieldBuddy.list.JobCardsListActivity;
import com.example.sruthi_4404.zcrmFieldBuddy.list.ListViewAdapter;
import com.zoho.crm.sdk.android.zcrmandroid.activity.ZCRMBaseActivity;

/**
 * Created by sruthi-4404 on 05/10/17.
 */

public class MainActivity extends ZCRMBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton contacts = (ImageButton) findViewById(R.id.imageButton29);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewAdapter.moduleAPIname = "Contacts";
                ListViewAdapter.cvID = null;
                ListViewAdapter.title = "Contacts";
                Intent loadList = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivity(loadList);
            }
        });

        ImageButton todaysAppointment = (ImageButton) findViewById(R.id.imageButton27);
        todaysAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewAdapter.moduleAPIname = "Appointments";
                ListViewAdapter.cvID = 880428000003643075l;
                ListViewAdapter.title = "Scheduled Appointments";
                Intent loadList = new Intent(getApplicationContext(), MapsActivity.class);//TodaysAppointmentListActivity.class);
                startActivity(loadList);
            }
        });

        ImageButton completedAppointments = (ImageButton) findViewById(R.id.imageButton28);
        completedAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewAdapter.moduleAPIname = "Appointments";
                ListViewAdapter.cvID = 880428000003643101l;
                ListViewAdapter.title = "Completed Appointments";
                Intent loadList = new Intent(getApplicationContext(), CompletedAppointmentsListActivity.class);
                startActivity(loadList);
            }
        });

        ImageButton jobcards = (ImageButton) findViewById(R.id.imageButton32);
        jobcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListViewAdapter.moduleAPIname = "Surveys";
                ListViewAdapter.cvID = null;
                ListViewAdapter.title = "Job Cards";
                Intent loadList = new Intent(getApplicationContext(), JobCardsListActivity.class);
                startActivity(loadList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Sign Out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // user doesn't want to logout
                            }
                        })
                        .show();
                return true;
            default:
                return false;
        }
    }

    private void logout()
    {
        super.logout(new OnLogoutListener() {
            @Override
            public void onLogoutSuccess() {
                System.out.println(">>> Logout Success...");
            }

            @Override
            public void onLogoutFailed() {
                System.out.println(">>> Logout failed...");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }
}
