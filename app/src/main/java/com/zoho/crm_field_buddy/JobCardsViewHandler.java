package com.zoho.crm_field_buddy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.zoho.crm_field_buddy.R;
import com.zoho.crm_field_buddy.list.ListViewAdapter;
import com.zoho.crm.library.crud.ZCRMModule;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.sdk.android.zcrmandroid.common.SDKCommonUtil;


/**
 * Created by sruthi-4404 on 08/09/16.
 */

public class JobCardsViewHandler extends AppCompatActivity
{
	private ZCRMRecord zcrmRecord;
	ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.survey_view);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(ListViewAdapter.title);
		setForm();
	}

	public void setForm()
	{
		dialog = ProgressDialog.show(JobCardsViewHandler.this, "",
				"Loading. Please wait...", true); //No I18N

		APImodeRunner runner = new APImodeRunner();
		runner.execute();
	}

	public void loadForm() throws Exception {
		TextView subj = (TextView) findViewById(R.id.textView24);
		subj.setText(String.valueOf(zcrmRecord.getFieldValue("Name"))); //No I18N
		TextView contact = (TextView) findViewById(R.id.textView26);
		contact.setText(((ZCRMRecord)zcrmRecord.getFieldValue("Appointment")).getLookupLabel()); //No I18N
		TextView status = (TextView) findViewById(R.id.textView28);
		status.setText(SDKCommonUtil.isoStringToGMTTimestamp(String.valueOf( zcrmRecord.getFieldValue("Visit_Time"))).toString()); //No I18N
		TextView desc = (TextView) findViewById(R.id.textView30);
		desc.setText((CharSequence) zcrmRecord.getFieldValue("Details")); //No I18N
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// API 5+ solution
				onBackPressed();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onBackPressed()
	{
		finish();
	}

	class APImodeRunner extends AsyncTask<String, String, String>
	{
		private String resp;
		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				zcrmRecord = (ZCRMRecord) ZCRMModule.getInstance(ListViewAdapter.moduleAPIname).getRecord(ListViewAdapter.idClicked).getData();
				resp = "Success"; //no I18N

			} catch (Exception e)
			{
				e.printStackTrace();
				resp = e.getMessage();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result)
		{
			try {
				loadForm();
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
