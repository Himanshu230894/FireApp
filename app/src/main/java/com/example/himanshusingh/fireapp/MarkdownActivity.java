package com.example.himanshusingh.fireapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import us.feras.mdv.MarkdownView;

public class MarkdownActivity extends AppCompatActivity {

    private EditText markdownEditText;
    private MarkdownView markdownView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mTextViewer;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);

        markdownEditText = (EditText) findViewById(R.id.markdownText);
        markdownView = (MarkdownView) findViewById(R.id.markdownView);
        mTextViewer = (Button)findViewById(R.id.textViewer);
        mProgress = new ProgressDialog(this);

        mAuth =FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        String text = getResources().getString(R.string.md_sample_data);
        markdownEditText.setText(text);
        updateMarkdownView();

        mTextViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startPosting();
                final String content = markdownEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(content)) {
                    mProgress.setMessage("posting to blog...");
                    mProgress.show();
                    mDatabase.child("message").setValue(content);
                    mProgress.dismiss();
                    startActivity(new Intent(MarkdownActivity.this, TextViewer.class));
                }
            }
        });

        markdownEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMarkdownView();
            }
        });



    }

    private void updateMarkdownView() {
        markdownView.loadMarkdown(markdownEditText.getText().toString());
    }
}