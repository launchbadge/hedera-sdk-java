package com.hedera.sdk_android_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;

public class MainActivity extends AppCompatActivity {

    private TextView keyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyText = findViewById(R.id.key_text);
    }

    public void generateKey(View view) {
        Ed25519PrivateKey privateKey = Ed25519PrivateKey.generate();

        keyText.setText(privateKey.toString());
    }
}
