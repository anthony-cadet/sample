package com.accengage.appdemo.main;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accengage.appdemo.R;
import com.ad4screen.sdk.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by acadet on 22/03/2018.
 */

public class MessageInboxAdapter extends RecyclerView.Adapter<MessageInboxAdapter.MessageInboxHolder> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM//yyyy hh:mm:ss", Locale.FRANCE);

    private List<Message> messages;

    public MessageInboxAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageInboxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MessageInboxHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageInboxHolder holder, int position) {
        holder.bindMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_message_inbox;
    }

    class MessageInboxHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img) ImageView img;
        @BindView(R.id.tv_title) TextView title;
        @BindView(R.id.tv_date) TextView date;

        MessageInboxHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindMessage(Message message) {

            if (message == null) {
                return;
            }

            message.getIcon(new Message.onIconDownloadedListener() {
                @Override
                public void onIconDownloaded(Bitmap bitmap) {
                    img.setImageBitmap(bitmap);
                }
            });

            title.setText(message.getTitle());
            date.setText(dateFormat.format(message.getSendDate()));
        }
    }
}
