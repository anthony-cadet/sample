package com.accengage.appdemo.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.accengage.appdemo.R;
import com.ad4screen.sdk.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = MessageActivity.class.getName();

    public static final String MESSAGE_KEY = "message";

    private Message message;
    private List<Message.Button> buttons;
    private ButtonMessageAdapter mAdapter;

    @BindView(R.id.rv_buttons) RecyclerView rvButtons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        retrieveMessageObject();
        setupAdapter();
    }

    private void retrieveMessageObject() {
        // Retrieve Message
        Bundle extras = getIntent().getExtras();
        message = extras != null ? (Message) extras.getParcelable(MESSAGE_KEY) : null;

        // Retrieve Buttons
        int buttonNbr = 0;
        buttons = new ArrayList<>();

        if (message != null && (buttonNbr = message.countButtons()) <= 0) {
            Log.d(TAG, "This message haven't buttons");
            return;
        }

        for (int i = 0; i < buttonNbr; i++) {
            buttons.add(message.getButton(i));
        }

        buttons.get(0).click(getApplicationContext());
    }

    private void setupAdapter() {
        mAdapter = new ButtonMessageAdapter(getApplicationContext(), buttons);
        rvButtons.setAdapter(mAdapter);
        rvButtons.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }
}
