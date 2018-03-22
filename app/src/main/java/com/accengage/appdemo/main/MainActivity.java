package com.accengage.appdemo.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.accengage.appdemo.R;
import com.ad4screen.sdk.A4S;
import com.ad4screen.sdk.Inbox;
import com.ad4screen.sdk.Log;
import com.ad4screen.sdk.Message;
import com.ad4screen.sdk.activities.A4SActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends A4SActivity {

    private Inbox inbox;
    private MessageInboxAdapter mAdapter;

    private List<Message> messages = new ArrayList<>();

    @BindView(R.id.rv_messages_inbox) RecyclerView rvMsgInbox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        retrieveInbox();
        setupAdapter();
    }

    private void retrieveInbox() {

        getA4S().getInbox(new A4S.Callback<Inbox>() {
            @Override
            public void onResult(Inbox result) {
                inbox = result;
                retrieveMessages();
            }

            @Override
            public void onError(int error, String errorMessage) {
                Log.error("errorCode : " + error + "errorMsg : " + errorMessage);
            }
        });
    }

    private void retrieveMessages() {

        if (inbox == null) {
            return;
        }

        for(int i = 0; i < inbox.countMessages(); i++) {

            inbox.getMessage(i, new A4S.MessageCallback() {
                @Override
                public void onResult(Message result, int index) {
                    messages.add(result);
                    mAdapter.notifyItemInserted(index);
                }

                @Override
                public void onError(int error, String errorMessage) {
                    Log.error("errorCode : " + error + "errorMsg : " + errorMessage);
                }
            });
        }
    }

    private void setupAdapter() {
        mAdapter = new MessageInboxAdapter(messages);
        rvMsgInbox.setAdapter(mAdapter);
        rvMsgInbox.setLayoutManager(new LinearLayoutManager(this));
    }
}
