package com.zoho.crm_field_buddy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zoho.crm_field_buddy.listActivity.ListViewAdapter;
import com.zoho.crm.library.crud.ZCRMRecord;
import com.zoho.crm.sdk.android.zcrmandroid.common.SDKCommonUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sruthi-4404 on 08/09/16.
 */

public class JobCardsCreation extends AppCompatActivity
{
	private ZCRMRecord zcrmRecord;
	private static final int PICK_FROM_FILE=3,PICK_FROM_CAMERA=1,PICK_FROM_GALLERY=2;
	private List<String> attachemnts = new ArrayList<>();
	private Uri selectedPath;
	Button save;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.survey_create);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(ListViewAdapter.nameClicked);
		loadForm();
	}

	public void loadForm() {
		zcrmRecord = new ZCRMRecord("Surveys");

		save = (Button) findViewById(R.id.button3);
		save.setEnabled(false);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//save.setText("SAVING..."); //No I18N
				APImodeRunner runner = new APImodeRunner();
				runner.execute();
			}
		});

		EditText details = (EditText) findViewById(R.id.editText3);
		details.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				zcrmRecord.setFieldValue("Details", s.toString());
			}
		});

		addAttachment();
	}

	private void addAttachment() {
		String[] items = new String[]{"From camera", "From Gallery", "From File Manager"};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Upload from");
		builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, PICK_FROM_CAMERA);
					dialog.dismiss();
				} else if (which == 1) {

					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
				} else {

					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("file/*");
					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		});
		final AlertDialog dialog = builder.create();
		Button addAttachment = (Button) findViewById(R.id.button5);
		addAttachment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
	}

	public void onActivityResult(int reqCode, int resCode, Intent data)
	{
		if (resCode == RESULT_OK) {
			if (reqCode == PICK_FROM_FILE || reqCode == PICK_FROM_GALLERY) {
				if (null == data) return;

				selectedPath = data.getData();

				selectedPath = Uri.parse(ImageFilePath.getPath(getApplicationContext(), selectedPath));
				Log.i("Image File Path", "" + selectedPath.getPath());

			}else if(reqCode == PICK_FROM_CAMERA)
			{
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
				File destination = new File(Environment.getExternalStorageDirectory(),"image" + String.valueOf(System.currentTimeMillis()) + ".jpg");//
				FileOutputStream fo;
				try {
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				selectedPath = Uri.parse(destination.getPath());//Uri.parse("/storage/emulated/0/"+destination.getPath());
			}
			attachemnts.add(selectedPath.getPath());
			/*TextView attachment = (TextView) findViewById(R.id.textView23);
			String text = String.valueOf(attachment.getText());
			attachment.setText(text + "\n" + selectedPath.getPath());*/
			ImageView imageView = (ImageView) findViewById(R.id.imageView3);
			imageView.setImageURI(selectedPath);
			save.setEnabled(true);
		}
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
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = ProgressDialog.show(JobCardsCreation.this, "","Saving...", true, false); //No I18N
		}

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				if(attachemnts.size() > 0) {
					zcrmRecord.setFieldValue("Name",ListViewAdapter.nameClicked); //No I18N
					zcrmRecord.setFieldValue("Appointment", ListViewAdapter.idClicked);
					zcrmRecord.setFieldValue("Visit_Time", SDKCommonUtil.millisecToISO(System.currentTimeMillis(),null)); //No I18N
					zcrmRecord.create();

					for (String path : attachemnts)
					{
						zcrmRecord.uploadAttachment(path);
					}

					ZCRMRecord appointment = ZCRMRecord.getInstance("Appointments", ListViewAdapter.idClicked);
					appointment.setFieldValue("Status","Completed");
					appointment.update();

				}else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),"Error! try uploading with Attachments!", Toast.LENGTH_LONG).show();
						}
					});
				}
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
			dialog.dismiss();
			Intent loadlist = new Intent(getApplicationContext(),  MapsActivity.class);
			startActivity(loadlist);
		}
	}
}
