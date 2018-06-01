package com.accengage.appdemo.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.accengage.appdemo.R;
import com.ad4screen.sdk.Message;

import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = MessageActivity.class.getName();

    public static final String MESSAGE_KEY = "message";

    private Message message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        retrieveMessageObject();
    }

    private void retrieveMessageObject() {
        Bundle extras = getIntent().getExtras();
        message = extras != null ? (Message) extras.getParcelable(MESSAGE_KEY) : null;
    }
}
