package com.example.androidservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    Button bt_web, bt_email, bt_dial, bt_call, bt_text, bt_maps;
    EditText et_data;
    final static int PERMISSION_TO_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_web = (Button) findViewById(R.id.bt_web);
        bt_email = (Button) findViewById(R.id.bt_email);
        bt_dial = (Button) findViewById(R.id.bt_dial);
        bt_call = (Button) findViewById(R.id.bt_call);
        bt_text = (Button) findViewById(R.id.bt_text);
        bt_maps = (Button) findViewById(R.id.bt_maps);

        et_data = (EditText) findViewById(R.id.et_data);

        bt_web.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openWebPage(et_data.getText().toString());
            }
        });

        bt_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String[] addresses = new String[1];
                addresses[0] = et_data.getText().toString();
                composeEmail(addresses, "Hello from me");
            }
        });

        bt_dial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialPhoneNumber(et_data.getText().toString());
            }
        });

        bt_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callPhoneNumber(et_data.getText().toString());
            }
        });

        bt_text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                composeMmsMessage(et_data.getText().toString());
            }
        });

        bt_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String mapsQuery = "geo:00?q=" + et_data.getText().toString();
                Uri mapsuri = Uri.parse(mapsQuery);
                showMap(mapsuri);
            }
        });

    }

    private void openWebPage(String url) {
        if (!url.startsWith("http://") || !url.startsWith("https//"))
        {
            url = "http://" + url;
        }
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void callPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_TO_CALL);
        }
        else
        {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }


    }

    public void composeMmsMessage(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", "Hello I would like to chat");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_TO_CALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    callPhoneNumber(et_data.getText().toString());
                }
                else
                    {
                    Toast.makeText(this, "Cannot make a call without permission",
                            Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }
}