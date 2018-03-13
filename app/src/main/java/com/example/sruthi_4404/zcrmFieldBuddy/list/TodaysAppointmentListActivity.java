package com.example.sruthi_4404.zcrmFieldBuddy.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sruthi_4404.zcrmFieldBuddy.MapsActivity;
import com.example.sruthi_4404.zcrmFieldBuddy.R;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.library.exception.ZCRMException;

import java.util.Iterator;

/**
 * Created by sruthi-4404 on 05/10/17.
 */

public class TodaysAppointmentListActivity extends ListViewHandler{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void recordList() throws ZCRMException
    {
        ZCRMRecord zcrmRecord;
        Iterator itr = ListViewAdapter.records.iterator();
        while (itr.hasNext())
        {
            zcrmRecord = (ZCRMRecord) itr.next();
            super.addRecordToList(zcrmRecord,new RecordListAdapter(this));
        }

        super.setPageRefreshingOff();
    }

    private class RecordListAdapter extends ArrayAdapter<ZCRMRecord>
    {
        private final Activity context;
        public RecordListAdapter(Activity context)
        {
            super( context, R.layout.list_item, ListViewAdapter.storeList);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, View rowView, @NonNull ViewGroup parent) {

            if (rowView == null)
            {
                rowView = context.getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            ZCRMRecord record = ListViewAdapter.storeList.get(position);

            try {
                TextView name = (TextView) rowView.findViewById(R.id.textView7);
                name.setText(String.valueOf(record.getFieldValue("Name"))); //No I18N
                TextView contact = (TextView) rowView.findViewById(R.id.textView8);
                contact.setText(((ZCRMRecord)record.getFieldValue("Contact")).getLookupLabel());
            } catch (ZCRMException e) {
                e.printStackTrace();
            }

            return rowView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.map:
                Intent loadMap = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(loadMap);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.list_page, menu);
        return true;
    }
}
