package com.zoho.crm_field_buddy.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoho.crm_field_buddy.R;
import com.zoho.crm_field_buddy.MapsActivity;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.library.exception.ZCRMException;
import java.util.Iterator;

/**
 * Created by sruthi-4404 on 05/10/17.
 */

public class ContactListActivity extends ListViewHandler{

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
            super( context, R.layout.list_item_with_image, ListViewAdapter.storeList);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, View rowView, @NonNull ViewGroup parent) {

            if (rowView == null)
            {
                rowView = context.getLayoutInflater().inflate(R.layout.list_item_with_image, parent, false);
            }

            ZCRMRecord record = ListViewAdapter.storeList.get(position);

            try {
                ImageView contactImage = rowView.findViewById(R.id.imageView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    contactImage.setImageResource(R.mipmap.ic_user_icon);
                }
                TextView name = rowView.findViewById(R.id.textView4);
                name.setText( String.valueOf(record.getFieldValue("Full_Name")));
                TextView phone = rowView.findViewById(R.id.textView5);
                phone.setText((CharSequence) record.getFieldValue("Email"));
                TextView email = rowView.findViewById(R.id.textView6);
                email.setText((CharSequence) record.getFieldValue("Mobile"));
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

}
