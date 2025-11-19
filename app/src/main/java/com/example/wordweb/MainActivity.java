package com.example.wordweb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartGame = findViewById(R.id.btnStartGame);
        Button btnHowToPlay = findViewById(R.id.btnHowToPlay);

        btnStartGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        btnHowToPlay.setOnClickListener(v -> {
            showHowToPlayDialog();
        });
    }

    private void showHowToPlayDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("How to Play")
                .setMessage("🎮 Undercover Game Rules:\n\n• 4 players total\n• 3 players get SAME word\n• 1 player gets DIFFERENT word\n• Discuss and find the undercover!\n• Undercover wins if not caught!")
                .setPositiveButton("Got it!", null)
                .show();
    }
}