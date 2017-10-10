package com.example.himanshusingh.fireapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.commonsware.cwac.anddown.AndDown;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.uncod.android.bypass.Bypass;
import us.feras.mdv.MarkdownView;

public class TextViewer extends AppCompatActivity {

    private MarkdownView mMarkdownView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView mBypassTextview;
    private WebView mAndDownWebview;
    private static final String TAG = "TEXTVIEWER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_viewer);

        mMarkdownView = (MarkdownView) findViewById(R.id.newMarkdownView);
        mBypassTextview = (TextView) findViewById(R.id.bypassTextView);
        mAndDownWebview = (WebView)findViewById(R.id.anddownWevView);
        mAuth =FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String content = (String) dataSnapshot.child("message").getValue();
                Log.d(TAG, "Value is: " + content);
                mMarkdownView.loadMarkdown(content);

                Bypass bypass = new Bypass();
                CharSequence string = bypass.markdownToSpannable(content);

                Typeface tf = Typeface.createFromAsset(getAssets(),
                        "fanwood_text.ttf");
                mBypassTextview.setTypeface(tf);

                mBypassTextview.setText(string);
                mBypassTextview.setMovementMethod(LinkMovementMethod.getInstance());

                AndDown andDown=new AndDown();
                String result=andDown.markdownToHtml(content);
                String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fanwood_text.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
                String pas = "</body></html>";
                String myHtmlString = pish + result + pas;
                mAndDownWebview.loadDataWithBaseURL(null,myHtmlString, "text/html", "UTF-8", null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
