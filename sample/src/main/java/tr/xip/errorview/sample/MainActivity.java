package tr.xip.errorview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import tr.xip.errorview.ErrorView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ErrorView mErrorView = (ErrorView) findViewById(R.id.error_view);

        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                Toast.makeText(MainActivity.this, R.string.info_retrying, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mErrorView.setConfig(ErrorView.Config.create()
                                .title(getString(R.string.error_title_damn))
                                .titleColor(getResources().getColor(R.color.apptheme_primary))
                                .subtitle(getString(R.string.error_subtitle_failed_one_more_time))
                                .retryText(getString(R.string.error_view_retry))
                                .build());
                    }
                }, 5000);
            }
        });
    }
}
