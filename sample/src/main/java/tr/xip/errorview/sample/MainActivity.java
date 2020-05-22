package tr.xip.errorview.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import tr.xip.errorview.ErrorView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ErrorView mErrorView = findViewById(R.id.error_view);
        mErrorView.setTint(Color.rgb(100, 25, 0));

        mErrorView.setOnRetryListener(() -> {
            Toast.makeText(MainActivity.this, R.string.info_retrying, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> mErrorView.setTitle(getString(R.string.error_title_damn))
                    .setTitleColor(getResources().getColor(R.color.apptheme_primary))
                    .setSubtitle(getString(R.string.error_subtitle_failed_one_more_time))
                    .setRetryButtonText(getString(R.string.error_view_retry))
                    .setRetryRightButtonText("Open in browser")
                    .showRightRetryButton(true)
                    .setSecondSubtitle("Subtitle 2")
                    .setSecondRetryButtonText("Retry 2")
                    .setThirdSubtitle("Subtitle 3")
                    .setThirdRetryButtonText("Retry 3")
                    .showSecondSubtitle(false)
                    .showThirdSubtitle(true)
                    .showSecondRetryButton(true)
                    .showThirdRetryButton(true), 2000);
        });
    }
}
