package com.zoho.crm_field_buddy.list;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zoho.crm_field_buddy.R;
import com.zoho.crm_field_buddy.AppointmentsViewHandler;
import com.zoho.crm_field_buddy.JobCardsViewHandler;
import com.zoho.crm_field_buddy.RecordViewHandler;
import com.zoho.crm.library.crud.ZCRMModule;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.library.exception.ZCRMException;
import com.zoho.crm.library.setup.restclient.ZCRMRestClient;

import java.util.List;

/**
 * Created by sruthi-4404 on 27/02/17.
 */

public class ListViewHandler extends AppCompatActivity {

    ProgressBar mProgress;
    SwipeRefreshLayout refreshLayout;
    TextView emptylist;
    TextView loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ListViewAdapter.title);
        emptylist = (TextView) findViewById(R.id.textView32);
        emptylist.setText("");
        loading = (TextView) findViewById(R.id.loading);
        initiatePage();
    }

    public void initiatePage() {
        initiateList();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.modulerefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearList();
                ApiModeRunner runner = new ApiModeRunner();
                runner.execute();
            }
        });

        mProgress = (ProgressBar) findViewById(R.id.moduleprogress);
        mProgress.setVisibility(ProgressBar.VISIBLE);


        loading.setText("LOADING.. please wait."); //No I18N
        ApiModeRunner runner = new ApiModeRunner();
        runner.execute();
    }

    protected void setPageRefreshingOff() {
        refreshLayout.setRefreshing(false);
        mProgress.setVisibility(ProgressBar.INVISIBLE);
        loading.setText("");

        if(ListViewAdapter.records.isEmpty())
        {
            emptylist.setText("Seems you have nothing assigned.");
        }
    }

    public void initiateList() {
        ListViewAdapter.setContactList((ListView) findViewById(R.id.listView));
        ListViewAdapter.getContactList().setAdapter(ListViewAdapter.getAdapter());
        ListViewAdapter.records.clear();
        ListViewAdapter.storeList.clear();

        ListViewAdapter.RecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView adapterView, View view, final int position, long idy) {
                ZCRMRecord record = ListViewAdapter.storeList.get(position);
                ListViewAdapter.idClicked = record.getEntityId();
                try {
                    if(record.getModuleAPIName().equals("Surveys"))
                    {
                        ListViewAdapter.nameClicked = String.valueOf(record.getFieldValue("Name")); //No I18N
                        Intent intent = new Intent(getApplicationContext(), JobCardsViewHandler.class);
                        startActivity(intent);
                    }
                    else if(record.getModuleAPIName().equals("Appointments"))
                    {
                        ListViewAdapter.nameClicked = String.valueOf(record.getFieldValue("Name")); //No I18N
                        Intent intent = new Intent(getApplicationContext(), AppointmentsViewHandler.class);
                        startActivity(intent);
                    }else {
                        ListViewAdapter.nameClicked = String.valueOf(record.getFieldValue("Full_Name"));
                        Intent intent = new Intent(getApplicationContext(), RecordViewHandler.class);
                        startActivity(intent);
                    }

                } catch (ZCRMException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void clearList() {
        ListViewAdapter.records.clear();
        ListViewAdapter.storeList.clear();
    }

    public void addRecordToList(ZCRMRecord zcrmRecord, Object recordListHandler) {
        ListViewAdapter.storeList.add(zcrmRecord);
        ListViewAdapter.adapter = (ArrayAdapter<ZCRMRecord>) recordListHandler;
        ListViewAdapter.RecordList.setAdapter(ListViewAdapter.adapter);
    }

    public void recordList() throws ZCRMException {

    }

    public class ApiModeRunner extends AsyncTask<String, String, String> {
        private String resp;


        @Override
        protected String doInBackground(String... params) {
            try {
                ZCRMModule module = ZCRMRestClient.getInstance().getModuleInstance(ListViewAdapter.moduleAPIname);
                ListViewAdapter.records = (List<ZCRMRecord>) module.getRecords(ListViewAdapter.cvID,null).getData();
                resp = "success";

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                recordList();
            } catch (ZCRMException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

}