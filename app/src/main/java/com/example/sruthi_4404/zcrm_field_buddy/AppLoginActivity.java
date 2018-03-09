package com.example.sruthi_4404.zcrm_field_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zoho.crm.sdk.android.zcrmandroid.activity.ZCRMLoginActivity;

/**
 * Created by sruthi-4404 on 14/02/18.
 */

public class AppLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ZCRMLoginActivity.class));
            }
        });
    }
}
