package com.zoho.crm_field_buddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sruthi_4404.zcrmFieldBuddy.R;
import com.zoho.crm_field_buddy.list.ListViewAdapter;
import com.zoho.crm.library.crud.ZCRMModule;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.library.exception.ZCRMException;

import org.json.JSONException;


/**
 * Created by sruthi-4404 on 08/09/16.
 */

public class AppointmentsViewHandler extends AppCompatActivity
{
	private ZCRMRecord zcrmRecord;
	ProgressDialog dialog;


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appoinment_view);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(ListViewAdapter.title);
		setForm();
	}

	public void setForm()
	{
		dialog = ProgressDialog.show(AppointmentsViewHandler.this, "",
				"Loading. Please wait...", true); //No I18N

		APImodeRunner runner = new APImodeRunner();
		runner.execute();
	}

	public void loadForm() throws JSONException, ZCRMException {
		TextView subj = (TextView) findViewById(R.id.textView12);
		subj.setText(String.valueOf(zcrmRecord.getFieldValue("Name"))); //No I18N
		TextView contact = (TextView) findViewById(R.id.textView16);
		contact.setText(((ZCRMRecord)zcrmRecord.getFieldValue("Contact")).getLookupLabel());
		TextView status = (TextView) findViewById(R.id.textView18);
		status.setText((CharSequence) zcrmRecord.getFieldValue("Status"));
		TextView desc = (TextView) findViewById(R.id.textView21);
		desc.setText((CharSequence) zcrmRecord.getFieldValue("Summary"));

		Button complete = (Button) findViewById(R.id.complete);
		if(ListViewAdapter.title.equals("Scheduled Appointments"))
		{
			complete.setVisibility(View.VISIBLE);
		}
		complete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent survey = new Intent(getApplicationContext(), JobCardsCreation.class);
				startActivity(survey);
			}
		});

		/*mProgress.setVisibility(ProgressBar.INVISIBLE);
		refreshLayout.setRefreshing(false);*/
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
			} catch (JSONException | ZCRMException e) {
				e.printStackTrace();
			}
		}
	}
}
