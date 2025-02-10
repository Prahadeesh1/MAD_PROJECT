package com.sp.mad_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class chat extends AppCompatActivity {

    TextView userNameTextView;
    ImageView userAvatarImageView;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<message> messageList;
    private EditText inputMessage;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Find the views
        userNameTextView = findViewById(R.id.username);
        userAvatarImageView = findViewById(R.id.imageView);


        recyclerView = findViewById(R.id.recycler_view);
        inputMessage = findViewById(R.id.input_message);
        sendButton = findViewById(R.id.send_button);
        messageList = new ArrayList<>();


        // Get data from intent
        String userName = getIntent().getStringExtra("userName");
        String userAvatar = getIntent().getStringExtra("userAvatar");

        // Set the data
        if (userName != null) {
            userNameTextView.setText(userName);
        }

        if (userAvatar != null) {
            Glide.with(this).load(userAvatar).into(userAvatarImageView);
        }

        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputMessage.getText().toString().trim();
                if (!text.isEmpty()) {
                    messageList.add(new message(text, true, new Date(), "https://example.com/user-avatar.png"));
                    chatAdapter.notifyDataSetChanged();
                    inputMessage.setText("");
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
        });
    }
}
