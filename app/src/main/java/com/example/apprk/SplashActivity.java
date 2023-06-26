package com.example.apprk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final long SPLASH_DELAY_MS = 2000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);


        TextView devNameTextView = findViewById(R.id.credit);
        String devNameString = getString(R.string.dev_name);

        SpannableString spannableString = new SpannableString(devNameString);

        int hyperlinkStartIndex = devNameString.indexOf("Rick Komori");
        int hyperlinkEndIndex = hyperlinkStartIndex + "Rick Komori".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle the click action for the hyperlink
                Uri uri = Uri.parse("https://www.youtube.com/channel/UC8DRNRhPwYsnMt0aFgv9gxQ");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, hyperlinkStartIndex, hyperlinkEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        devNameTextView.setText(spannableString);
        devNameTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
