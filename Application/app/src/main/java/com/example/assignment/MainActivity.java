package com.example.assignment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText changeTextEditor;
    private Button changeTextButton;
    private TextView changeTextTextView;
    // This is the activity main thread Handler.
    private Handler updateUIHandler = null;
    // Message type code.
    private final static int MESSAGE_UPDATE_TEXT_CHILD_THREAD =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("dev2qa.com - Update Ui In Child Thread Example.");
        // Initialize Handler.
        createUpdateUiHandler();
        // User input text editor.
        changeTextEditor = (EditText)findViewById(R.id.change_text_editor);
        // Change text button.
        changeTextButton = (Button)findViewById(R.id.change_text_in_child_thread_button);
        // Show text textview.
        changeTextTextView = (TextView)findViewById(R.id.change_text_textview);
        // Click this button to start a child thread.
        changeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread workerThread = new Thread()
                {
                    @Override
                    public void run() {
                        // Can not update ui component directly when child thread run.
                        // updateText();
                        // Build message object.
                        Message message = new Message();
                        // Set message type.
                        message.what = MESSAGE_UPDATE_TEXT_CHILD_THREAD;
                        // Send message to main thread Handler.
                        updateUIHandler.sendMessage(message);
                    }
                };
                workerThread.start();
            }
        });
    }
    private void updateText()
    {
        String userInputText = changeTextEditor.getText().toString();
        changeTextTextView.setText(userInputText);
    }
    /* Create Handler object in main thread. */
    private void createUpdateUiHandler()
    {
        if(updateUIHandler == null)
        {
            updateUIHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                    // Means the message is sent from child thread.
                    if(msg.what == MESSAGE_UPDATE_TEXT_CHILD_THREAD)
                    {
                        // Update ui in main thread.
                        updateText();
                    }
                }
            };
        }
    }
}
