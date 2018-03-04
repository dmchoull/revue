package com.github.dmchoull.revuesample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dmchoull.revue.Revue;
import com.github.dmchoull.revue.builder.DialogResult;
import com.github.dmchoull.revue.builder.ReviewPromptDialogBuilder;
import com.github.dmchoull.revue.builder.RevueDialogBuilder;

public class MainActivity extends AppCompatActivity {
    // usually would be provided via dependency injection with configuration already done
    private Revue revue = Revue.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup radioDialogType = findViewById(R.id.radioDialogType);
        radioDialogType.setOnCheckedChangeListener((group, checkedId) -> setDialogBuilder(checkedId));
        setDialogBuilder(radioDialogType.getCheckedRadioButtonId());

        // If you want to take some activity specific action based on the user's interaction with
        // the dialog, you can set a callback on an existing dialog builder

        //noinspection ConstantConditions
        revue.getPrePromptDialogBuilder().callback(result -> {
            if (result == DialogResult.NEGATIVE) {
                Toast.makeText(this, "We appreciate your feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDialogBuilder(int checkedId) {
        RevueDialogBuilder builder;

        if (checkedId == R.id.radioSimple) {
            // Replace the dialog builder if you want to override some default values

            builder = new ReviewPromptDialogBuilder()
                    .title("Thank you!")
                    .message(R.string.rating_message)
                    .negativeButtonListener(
                            (dialog, which) ->
                                    Toast.makeText(this, "Thanks Anyway", Toast.LENGTH_SHORT).show()
                    );

            revue.setReviewPromptDialogBuilder(builder);
        } else {
            Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
        }
    }

    public void onShowNowClick(View view) {
        revue.showNow(this);
    }

    public void onRequestClick(View view) {
        revue.request(this);
    }

    public void onMemoryLeakTestClick(View view) {
        startActivity(new Intent(this, LeakTestActivity.class));
        finish();
    }

    public void onResetClick(View view) {
        SharedPreferences prefs = getSharedPreferences("REVUE_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
