package com.example.ecosynergy;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();

    private EditText messageInput;
    private ImageView sendButton;

    private DatabaseReference messagesRef;
    private String currentUserId;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Setup the toolbar with back button
        setupToolbar(true);

        getSupportActionBar().setTitle("Discussion Forum"); // Set the title of the activity

        // Setup bottom navigation
        setupBottomNavigation();

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Get current user ID and group ID
        currentUserId = FirebaseAuth.getInstance().getUid();
        groupId = getIntent().getStringExtra("groupId"); // Pass groupId via Intent
        if (groupId == null) {
            groupId = "defaultGroupId"; // Replace with your default logic
        }

        // Initialize Firebase Database
        messagesRef = FirebaseDatabase.getInstance().getReference("GroupMessages");

        // Setup RecyclerView
        chatAdapter = new ChatAdapter(messageList, currentUserId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Load messages for the group
        loadMessages();

        // Send a message
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message != null) {
                        // Fetch the username from the Users node
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(message.getSenderId());
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                if (userSnapshot.exists()) {
                                    String username = userSnapshot.child("username").getValue(String.class);
                                    message.setSenderName(username != null ? username : message.getSenderName());
                                }
                                chatAdapter.notifyDataSetChanged();
                                chatRecyclerView.scrollToPosition(messageList.size() - 1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    }
                    messageList.add(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        String senderName = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Replace with display name if available
        String messageId = messagesRef.push().getKey();

        Message message = new Message(currentUserId, senderName, groupId, messageText, System.currentTimeMillis());
        messagesRef.child(messageId).setValue(message);

        messageInput.setText(""); // Clear input
    }
}
