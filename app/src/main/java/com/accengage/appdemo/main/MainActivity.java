package com.accengage.appdemo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.accengage.appdemo.R;
import com.accengage.appdemo.messages.MessageActivity;
import com.accengage.appdemo.utils.InboxUtil;
import com.ad4screen.sdk.A4S;
import com.ad4screen.sdk.Inbox;
import com.ad4screen.sdk.Message;
import com.ad4screen.sdk.activities.A4SActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;


public class MainActivity extends A4SActivity implements MessageViewListener {

    private static final String TAG = MainActivity.class.getName();

    private Inbox inbox;
    private MessageInboxAdapter mAdapter;

    private List<Message> messages = new ArrayList<>();

    private A4S.Callback<Inbox> inboxCallback;

    @BindView(R.id.srl_inbox) SwipeRefreshLayout srlInbox;
    @BindView(R.id.rv_messages_inbox) RecyclerView rvMsgInbox;
    @BindView(R.id.tv_num_message) TextView tvNumMessages;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setNbrInbox(0);
        initPullToRefresh();
        retrieveInbox();
        setupAdapter();
    }

    private void initPullToRefresh() {
        srlInbox.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                A4S.get(MainActivity.this).getInbox(inboxCallback);
            }
        });
    }

    private void showRefreshAnimation() {
        srlInbox.post(new Runnable() {
            @Override
            public void run() {
                srlInbox.setRefreshing(true);
            }
        });
    }

    private void retrieveInbox() {
        inboxCallback = new A4S.Callback<Inbox>() {
            @Override
            public void onResult(Inbox result) {
                inbox = result;

                if (inbox == null || inbox.countMessages() == 0) {
                    setNbrInbox(0);
                    srlInbox.setRefreshing(false);
                    return;
                }

                retrieveMessages();
            }

            @Override
            public void onError(int error, String errorMessage) {
                Log.e(TAG, "errorCode : " + error + "errorMsg : " + errorMessage);
            }
        };

        getA4S().getInbox(inboxCallback);
    }

    private void retrieveMessages() {

        final int nbrMessage = inbox.countMessages();

        for(int i = 0; i < nbrMessage; i++) {

            inbox.getMessage(i, new A4S.MessageCallback() {
                @Override
                public void onResult(Message result, int index) {
                    try {
                        // If messages is already fill we replace the older ones
                        messages.set(index, result);
                        mAdapter.notifyItemChanged(index);
                    } catch (Exception e) {
                        // else we add it
                        messages.add(result);
                        mAdapter.notifyItemInserted(index);
                    }

                    if (index + 1 == nbrMessage) {
                        // At the end we stop the anim and set the number in the header
                        srlInbox.setRefreshing(false);
                        setNbrInbox(InboxUtil.getNumberInbox(messages));
                    }
                }

                @Override
                public void onError(int error, String errorMessage) {
                    Log.e(TAG, "errorCode : " + error + "errorMsg : " + errorMessage);
                }
            });
        }
    }

    private void setupAdapter() {
        mAdapter = new MessageInboxAdapter(getApplicationContext(), this,  messages);
        rvMsgInbox.setAdapter(mAdapter);
        rvMsgInbox.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setNbrInbox(int nbr) {
        String n = nbr > 0 ? " (" + nbr +")" : "";
        tvNumMessages.setText(String.format(getString(R.string.inbox), n));
    }

    // Message View Listener
    @Override
    public void gotToMessageActivity(Message message, int position) {
        // pass the message to the MessageActivity
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(MessageActivity.MESSAGE_KEY, message);

        // notify the adapter and the server that the message is read
        mAdapter.notifyItemChanged(position);
        A4S.get(MainActivity.this).updateMessages(inbox);

        // update the number in the header
        setNbrInbox(InboxUtil.getNumberInbox(messages));

        startActivity(intent);
    }

    // Hack
    private void delete() {
        for (Message msg : messages) {
            msg.setRead(true);
            msg.setArchived(true);
        }

        messages.clear();
        mAdapter.notifyDataSetChanged();

        A4S.get(MainActivity.this).updateMessages(inbox);
        setNbrInbox(InboxUtil.getNumberInbox(messages));
    }

}
