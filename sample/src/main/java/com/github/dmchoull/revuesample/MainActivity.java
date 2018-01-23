package com.github.dmchoull.revuesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.github.dmchoull.revue.Revue;
import com.github.dmchoull.revue.builder.RevueDialogBuilder;
import com.github.dmchoull.revue.builder.SimpleDialogBuilder;

public class MainActivity extends AppCompatActivity {
    // usually would be provided via dependency injection with configuration already done
    private Revue revue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        revue = ((SampleApplication) getApplication()).getRevue();

        RadioGroup radioDialogType = findViewById(R.id.radioDialogType);
        radioDialogType.setOnCheckedChangeListener((group, checkedId) -> setDialogBuilder(checkedId));
        setDialogBuilder(radioDialogType.getCheckedRadioButtonId());
    }

    private void setDialogBuilder(int checkedId) {
        RevueDialogBuilder builder;

        if (checkedId == R.id.radioSimple) {
            builder = new SimpleDialogBuilder()
                    .title("Enjoy using this app?")
                    .message(R.string.rating_message)
                    .negativeButtonListener(
                            (dialog, which) ->
                                    Toast.makeText(this, "Thanks Anyway", Toast.LENGTH_SHORT).show()
                    );

            revue.setDialogBuilder(builder);
        } else {
            Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
        }
    }

    public void onShowNowClick(View view) {
        revue.showNow(this);
    }

    public void onShowMaybeClick(View view) {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }
}
