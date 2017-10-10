package com.example.himanshusingh.fireapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button mSendData;

    private Button mPage2;
    private EditText mKeyValue;
    private EditText mValueField;
    private TextView mTextValue;
    private Firebase mRootRef;
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootRef = new Firebase("https://fireapp-c025e.firebaseio.com/");
        mRef = new Firebase("https://fireapp-c025e.firebaseio.com/Kallu");
        mSendData = (Button)findViewById(R.id.SendData);
        mKeyValue = (EditText) findViewById(R.id.keyValue);
        mValueField = (EditText) findViewById(R.id.valueField);
        mTextValue = (TextView) findViewById(R.id.textValue);
        mPage2 = (Button) findViewById(R.id.page2);

        mSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mKeyValue.getText().toString();
                String value = mValueField.getText().toString();
                Firebase mRefChild = mRootRef.child(key);
                mRefChild.setValue(value);
            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value1 = dataSnapshot.getValue(String.class);
                mTextValue.setText(value1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }



    public void Boom (View v){
        startActivity(new Intent(this,DataRetrieval.class));
    }

    public void loginpage(View v1)
    {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}
