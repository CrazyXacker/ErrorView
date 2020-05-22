/*
 * Copyright (C) 2015 Ihsan Isik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tr.xip.errorview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

import java.util.Map;

/**
 * @author Ihsan Isik
 *         <p/>
 *         A custom view that displays an error image, a title, and a subtitle given an HTTP status
 *         code. It can be used for various other purposes like displaying other kinds of errors or
 *         just messages with images.
 * @see #setError(int)
 * <p/>
 */
public class ErrorView extends LinearLayout {
    public static final int ALIGNMENT_LEFT = 0;
    public static final int ALIGNMENT_CENTER = 1;
    public static final int ALIGNMENT_RIGHT = 2;

    private ImageView mErrorImageView;
    private TextView mTitleTextView;
    private TextView mSubtitleTextView;
    private TextView mRetryButton;
    private TextView mSecondSubtitleTextView;
    private TextView mSecondRetryButton;
    private TextView mThirdSubtitleTextView;
    private TextView mThirdRetryButton;

    private RetryListener mListener;
    private RetryListener mSecondListener;
    private RetryListener mThirdListener;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ev_style);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs);
        init(attrs, defStyle, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ErrorView, defStyle, defStyleRes);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.error_view_layout, this, true);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        /* Set android:animateLayoutChanges="true" programmatically */
        setLayoutTransition(new LayoutTransition());

        mErrorImageView = findViewById(R.id.error_image);
        mTitleTextView = findViewById(R.id.error_title);
        mSubtitleTextView = findViewById(R.id.error_subtitle);
        mRetryButton = findViewById(R.id.error_retry);
        mSecondSubtitleTextView = findViewById(R.id.error_second_subtitle);
        mSecondRetryButton = findViewById(R.id.error_second_retry);
        mThirdSubtitleTextView = findViewById(R.id.error_third_subtitle);
        mThirdRetryButton = findViewById(R.id.error_third_retry);

        int imageRes;

        String title;
        int titleColor;

        String subtitle;
        int subtitleColor;

        String secondSubtitle;
        int secondSubtitleColor;

        String thirdSubtitle;
        int thirdSubtitleColor;

        boolean showTitle;
        boolean showSubtitle;
        boolean showSecondSubtitle;
        boolean showThirdSubtitle;
        boolean showRetryButton;
        boolean showSecondRetryButton;
        boolean showThirdRetryButton;

        String retryButtonText;
        String secondRetryButtonText;
        String thirdRetryButtonText;
        int retryButtonBackground;
        int retryButtonTextColor;
        int secondRetryButtonBackground;
        int secondRetryButtonTextColor;
        int thirdRetryButtonBackground;
        int thirdRetryButtonTextColor;

        try {
            imageRes = a.getResourceId(R.styleable.ErrorView_ev_errorImage, R.drawable.error_view_cloud);
            title = a.getString(R.styleable.ErrorView_ev_title);
            titleColor = a.getColor(R.styleable.ErrorView_ev_titleColor,
                    getResources().getColor(R.color.error_view_text));
            subtitle = a.getString(R.styleable.ErrorView_ev_subtitle);
            subtitleColor = a.getColor(R.styleable.ErrorView_ev_subtitleColor,
                    getResources().getColor(R.color.error_view_text_light));
            secondSubtitle = a.getString(R.styleable.ErrorView_ev_second_subtitle);
            secondSubtitleColor = a.getColor(R.styleable.ErrorView_ev_secondSubtitleColor,
                    getResources().getColor(R.color.error_view_text_light));
            thirdSubtitle = a.getString(R.styleable.ErrorView_ev_third_subtitle);
            thirdSubtitleColor = a.getColor(R.styleable.ErrorView_ev_thirdSubtitleColor,
                    getResources().getColor(R.color.error_view_text_light));
            showTitle = a.getBoolean(R.styleable.ErrorView_ev_showTitle, true);
            showSubtitle = a.getBoolean(R.styleable.ErrorView_ev_showSubtitle, true);
            showSecondSubtitle = a.getBoolean(R.styleable.ErrorView_ev_showSecondSubtitle, false);
            showThirdSubtitle = a.getBoolean(R.styleable.ErrorView_ev_showThirdSubtitle, false);
            showRetryButton = a.getBoolean(R.styleable.ErrorView_ev_showRetryButton, true);
            retryButtonText = a.getString(R.styleable.ErrorView_ev_retryButtonText);
            retryButtonBackground = a.getResourceId(R.styleable.ErrorView_ev_retryButtonBackground,
                    R.drawable.error_view_retry_button_background);
            retryButtonTextColor = a.getColor(R.styleable.ErrorView_ev_retryButtonTextColor,
                    getResources().getColor(R.color.error_view_text_dark));
            int alignInt = a.getInt(R.styleable.ErrorView_ev_subtitleAlignment, 1);

            showSecondRetryButton = a.getBoolean(R.styleable.ErrorView_ev_showSecondRetryButton, false);
            secondRetryButtonText = a.getString(R.styleable.ErrorView_ev_secondRetryButtonText);
            secondRetryButtonBackground = a.getResourceId(R.styleable.ErrorView_ev_secondRetryButtonBackground,
                    R.drawable.error_view_retry_button_background);
            secondRetryButtonTextColor = a.getColor(R.styleable.ErrorView_ev_secondRetryButtonTextColor,
                    getResources().getColor(R.color.error_view_text_dark));
            int secondAlignInt = a.getInt(R.styleable.ErrorView_ev_secondSubtitleAlignment, 1);

            showThirdRetryButton = a.getBoolean(R.styleable.ErrorView_ev_showThirdRetryButton, false);
            thirdRetryButtonText = a.getString(R.styleable.ErrorView_ev_thirdRetryButtonText);
            thirdRetryButtonBackground = a.getResourceId(R.styleable.ErrorView_ev_thirdRetryButtonBackground,
                    R.drawable.error_view_retry_button_background);
            thirdRetryButtonTextColor = a.getColor(R.styleable.ErrorView_ev_thirdRetryButtonTextColor,
                    getResources().getColor(R.color.error_view_text_dark));
            int thirdAlignInt = a.getInt(R.styleable.ErrorView_ev_thirdSubtitleAlignment, 1);

            if (imageRes != 0) {
                setImage(imageRes);
            }

            if (title != null) {
                setTitle(title);
            }

            if (subtitle != null) {
                setSubtitle(subtitle);
            }

            if (secondSubtitle != null) {
                setSecondSubtitle(secondSubtitle);
            }

            if (thirdSubtitle != null) {
                setThirdSubtitle(thirdSubtitle);
            }

            if (retryButtonText != null) {
                mRetryButton.setText(retryButtonText);
            }

            if (secondRetryButtonText != null) {
                mSecondRetryButton.setText(secondRetryButtonText);
            }

            if (thirdRetryButtonText != null) {
                mThirdRetryButton.setText(thirdRetryButtonText);
            }

            if (!showTitle) {
                mTitleTextView.setVisibility(GONE);
            }

            if (!showSubtitle) {
                mSubtitleTextView.setVisibility(GONE);
            }

            if (!showSecondSubtitle) {
                mSecondSubtitleTextView.setVisibility(GONE);
            }

            if (!showThirdSubtitle) {
                mThirdSubtitleTextView.setVisibility(GONE);
            }

            if (!showRetryButton) {
                mRetryButton.setVisibility(GONE);
            }

            if (!showSecondRetryButton) {
                mSecondRetryButton.setVisibility(GONE);
            }

            if (!showThirdRetryButton) {
                mThirdRetryButton.setVisibility(GONE);
            }

            mTitleTextView.setTextColor(titleColor);
            mSubtitleTextView.setTextColor(subtitleColor);
            mSecondSubtitleTextView.setTextColor(secondSubtitleColor);
            mThirdSubtitleTextView.setTextColor(thirdSubtitleColor);

            mRetryButton.setTextColor(retryButtonTextColor);
            mRetryButton.setBackgroundResource(retryButtonBackground);
            mSecondRetryButton.setTextColor(secondRetryButtonTextColor);
            mSecondRetryButton.setBackgroundResource(secondRetryButtonBackground);
            mThirdRetryButton.setTextColor(thirdRetryButtonTextColor);
            mThirdRetryButton.setBackgroundResource(thirdRetryButtonBackground);

            setSubtitleAlignment(mSubtitleTextView, alignInt);
            setSubtitleAlignment(mSecondSubtitleTextView, secondAlignInt);
            setSubtitleAlignment(mThirdSubtitleTextView, thirdAlignInt);
        } finally {
            a.recycle();
        }

        mRetryButton.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onRetry();
            }
        });

        mSecondRetryButton.setOnClickListener(view -> {
            if (mSecondListener != null) {
                mSecondListener.onRetry();
            }
        });

        mThirdRetryButton.setOnClickListener(view -> {
            if (mThirdListener != null) {
                mThirdListener.onRetry();
            }
        });
    }

    public ErrorView setTint(int color) {
        DrawableCompat.setTint(mErrorImageView.getDrawable(), color);
        return this;
    }

    /**
     * Attaches a listener that to the view that reports retry events.
     *
     * @param listener {@link tr.xip.errorview.ErrorView.RetryListener} to be notified when a retry
     *                 event occurs.
     */
    public ErrorView setOnRetryListener(RetryListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * Attaches a listener that to the view that reports retry events.
     *
     * @param listener {@link tr.xip.errorview.ErrorView.RetryListener} to be notified when a retry
     *                 event occurs.
     */
    public ErrorView setSecondOnRetryListener(RetryListener listener) {
        mSecondListener = listener;
        return this;
    }

    /**
     * Attaches a listener that to the view that reports retry events.
     *
     * @param listener {@link tr.xip.errorview.ErrorView.RetryListener} to be notified when a retry
     *                 event occurs.
     */
    public ErrorView setThirdOnRetryListener(RetryListener listener) {
        mThirdListener = listener;
        return this;
    }

    /**
     * Sets error subtitle to the description of the given HTTP status code
     *
     * @param errorCode HTTP status code
     */
    public ErrorView setError(int errorCode) {
        Map<Integer, String> mCodes = HttpStatusCodes.getCodesMap();

        if (mCodes.containsKey(errorCode)) {
            setSubtitle(errorCode + " " + mCodes.get(errorCode));
        }

        return this;
    }

    /**
     * Sets error image to a given drawable resource
     *
     * @param res drawable resource.
     * @deprecated Use {@link #setImage(int)} instead.
     */
    @Deprecated
    public ErrorView setImageResource(int res) {
        return setImage(res);
    }

    /**
     * Sets the error image to a given {@link android.graphics.drawable.Drawable}.
     *
     * @param drawable {@link android.graphics.drawable.Drawable} to use as error image.
     * @deprecated Use {@link #setImage(Drawable)} instead.
     */
    @Deprecated
    public ErrorView setImageDrawable(Drawable drawable) {
        return setImage(drawable);
    }

    /**
     * Sets the error image to a given {@link android.graphics.Bitmap}.
     *
     * @param bitmap {@link android.graphics.Bitmap} to use as error image.
     * @deprecated Use {@link #setImage(Bitmap)} instead.
     */
    @Deprecated
    public ErrorView setImageBitmap(Bitmap bitmap) {
        return setImage(bitmap);
    }

    /**
     * Sets error image to a given drawable resource
     *
     * @param res drawable resource.
     */
    public ErrorView setImage(int res) {
        mErrorImageView.setImageResource(res);
        return this;
    }

    /**
     * Sets the error image to a given {@link android.graphics.drawable.Drawable}.
     *
     * @param drawable {@link android.graphics.drawable.Drawable} to use as error image.
     */
    public ErrorView setImage(Drawable drawable) {
        mErrorImageView.setImageDrawable(drawable);
        return this;
    }

    /**
     * Sets the error image to a given {@link android.graphics.Bitmap}.
     *
     * @param bitmap {@link android.graphics.Bitmap} to use as error image.
     */
    public ErrorView setImage(Bitmap bitmap) {
        mErrorImageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Returns the current error iamge
     */
    public Drawable getImage() {
        return mErrorImageView.getDrawable();
    }

    /**
     * Sets the error title to a given {@link java.lang.String}.
     *
     * @param text {@link java.lang.String} to use as error title.
     */
    public ErrorView setTitle(String text) {
        mTitleTextView.setText(text);
        return this;
    }

    /**
     * Sets the error title to a given string resource.
     *
     * @param res string resource to use as error title.
     */
    public ErrorView setTitle(int res) {
        mTitleTextView.setText(res);
        return this;
    }

    /**
     * Returns the current title string.
     */
    public String getTitle() {
        return mTitleTextView.getText().toString();
    }

    /**
     * Sets the error title text to a given color.
     *
     * @param res color resource to use for error title text.
     */
    public ErrorView setTitleColor(int res) {
        mTitleTextView.setTextColor(res);
        return this;
    }

    /**
     * Returns the current title text color.
     */
    public int getTitleColor() {
        return mTitleTextView.getCurrentTextColor();
    }

    /**
     * Sets the error subtitle to a given {@link java.lang.String}.
     *
     * @param exception {@link java.lang.String} to use as error subtitle.
     */
    public ErrorView setSubtitle(String exception) {
        mSubtitleTextView.setText(exception);
        return this;
    }

    /**
     * Sets the error subtitle to a given string resource.
     *
     * @param res string resource to use as error subtitle.
     */
    public ErrorView setSubtitle(int res) {
        mSubtitleTextView.setText(res);
        return this;
    }

    /**
     * Returns the current subtitle.
     */
    public String getSubtitle() {
        return mSubtitleTextView.getText().toString();
    }

    /**
     * Sets the second error subtitle to a given {@link java.lang.String}.
     *
     * @param exception {@link java.lang.String} to use as error subtitle.
     */
    public ErrorView setSecondSubtitle(String exception) {
        mSecondSubtitleTextView.setText(exception);
        return this;
    }

    /**
     * Sets the second error subtitle to a given string resource.
     *
     * @param res string resource to use as error subtitle.
     */
    public ErrorView setSecondSubtitle(int res) {
        mSecondSubtitleTextView.setText(res);
        return this;
    }

    /**
     * Returns the current second subtitle.
     */
    public String getSecondSubtitle() {
        return mSecondSubtitleTextView.getText().toString();
    }

    /**
     * Sets the third error subtitle to a given {@link java.lang.String}.
     *
     * @param exception {@link java.lang.String} to use as error subtitle.
     */
    public ErrorView setThirdSubtitle(String exception) {
        mThirdSubtitleTextView.setText(exception);
        return this;
    }

    /**
     * Sets the third error subtitle to a given string resource.
     *
     * @param res string resource to use as error subtitle.
     */
    public ErrorView setThirdSubtitle(int res) {
        mThirdSubtitleTextView.setText(res);
        return this;
    }

    /**
     * Returns the current third subtitle.
     */
    public String getThirdSubtitle() {
        return mThirdSubtitleTextView.getText().toString();
    }

    /**
     * Sets the error subtitle text to a given color
     *
     * @param res color resource to use for error subtitle text.
     */
    public ErrorView setSubtitleColor(int res) {
        mSubtitleTextView.setTextColor(res);
        return this;
    }

    /**
     * Returns the current subtitle text color.
     */
    public int getSubtitleColor() {
        return mSubtitleTextView.getCurrentTextColor();
    }

    /**
     * Sets the second error subtitle text to a given color
     *
     * @param res color resource to use for error subtitle text.
     */
    public ErrorView setSecondSubtitleColor(int res) {
        mSecondSubtitleTextView.setTextColor(res);
        return this;
    }

    /**
     * Returns the current second subtitle text color.
     */
    public int getSecondSubtitleColor() {
        return mSecondSubtitleTextView.getCurrentTextColor();
    }

    /**
     * Sets the third error subtitle text to a given color
     *
     * @param res color resource to use for error subtitle text.
     */
    public ErrorView setThirdSubtitleColor(int res) {
        mThirdSubtitleTextView.setTextColor(res);
        return this;
    }

    /**
     * Returns the current third subtitle text color.
     */
    public int getThirdSubtitleColor() {
        return mThirdSubtitleTextView.getCurrentTextColor();
    }

    /**
     * Sets the retry button's text to a given {@link java.lang.String}.
     *
     * @param text {@link java.lang.String} to use as retry button text.
     */
    public ErrorView setRetryButtonText(String text) {
        mRetryButton.setText(text);
        return this;
    }

    /**
     * Sets the retry button's text to a given string resource.
     *
     * @param res string resource to be used as retry button text.
     */
    public ErrorView setRetryButtonText(int res) {
        mRetryButton.setText(res);
        return this;
    }

    /**
     * Returns the current retry button text.
     */
    public String getRetryButtonText() {
        return mRetryButton.getText().toString();
    }

    /**
     * Sets the second retry button's text to a given {@link java.lang.String}.
     *
     * @param text {@link java.lang.String} to use as retry button text.
     */
    public ErrorView setSecondRetryButtonText(String text) {
        mSecondRetryButton.setText(text);
        return this;
    }

    /**
     * Sets the second retry button's text to a given string resource.
     *
     * @param res string resource to be used as retry button text.
     */
    public ErrorView setSecondRetryButtonText(int res) {
        mSecondRetryButton.setText(res);
        return this;
    }

    /**
     * Returns the current second retry button text.
     */
    public String getSecondRetryButtonText() {
        return mSecondRetryButton.getText().toString();
    }

    /**
     * Sets the third retry button's text to a given {@link java.lang.String}.
     *
     * @param text {@link java.lang.String} to use as retry button text.
     */
    public ErrorView setThirdRetryButtonText(String text) {
        mThirdRetryButton.setText(text);
        return this;
    }

    /**
     * Sets the third retry button's text to a given string resource.
     *
     * @param res string resource to be used as retry button text.
     */
    public ErrorView setThirdRetryButtonText(int res) {
        mThirdRetryButton.setText(res);
        return this;
    }

    /**
     * Returns the current third retry button text.
     */
    public String getThirdRetryButtonText() {
        return mThirdRetryButton.getText().toString();
    }

    /**
     * Sets the retry button's text color to a given color.
     *
     * @param color int color to be used as text color.
     */
    public ErrorView setRetryButtonTextColor(int color) {
        mRetryButton.setTextColor(color);
        return this;
    }

    /**
     * Returns the current retry button text color.
     */
    public int getRetryButtonTextColor() {
        return mRetryButton.getCurrentTextColor();
    }

    /**
     * Sets the second retry button's text color to a given color.
     *
     * @param color int color to be used as text color.
     */
    public ErrorView setSecondRetryButtonTextColor(int color) {
        mSecondRetryButton.setTextColor(color);
        return this;
    }

    /**
     * Returns the current second retry button text color.
     */
    public int getSecondRetryButtonTextColor() {
        return mSecondRetryButton.getCurrentTextColor();
    }

    /**
     * Sets the third retry button's text color to a given color.
     *
     * @param color int color to be used as text color.
     */
    public ErrorView setThirdRetryButtonTextColor(int color) {
        mThirdRetryButton.setTextColor(color);
        return this;
    }

    /**
     * Returns the current third retry button text color.
     */
    public int getThirdRetryButtonTextColor() {
        return mThirdRetryButton.getCurrentTextColor();
    }

    /**
     * Shows or hides the error title
     */
    public ErrorView showTitle(boolean show) {
        mTitleTextView.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the title is currently visible.
     */
    public boolean isTitleVisible() {
        return mTitleTextView.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides error subtitle.
     */
    public ErrorView showSubtitle(boolean show) {
        mSubtitleTextView.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the subtitle is currently visible.
     */
    public boolean isSubtitleVisible() {
        return mSubtitleTextView.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides second error subtitle.
     */
    public ErrorView showSecondSubtitle(boolean show) {
        mSecondSubtitleTextView.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the third subtitle is currently visible.
     */
    public boolean isSecondSubtitleVisible() {
        return mSecondSubtitleTextView.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides third error subtitle.
     */
    public ErrorView showThirdSubtitle(boolean show) {
        mThirdSubtitleTextView.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the second subtitle is currently visible.
     */
    public boolean isThirdSubtitleVisible() {
        return mThirdSubtitleTextView.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides the retry button.
     */
    public ErrorView showRetryButton(boolean show) {
        mRetryButton.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the retry button is visible.
     */
    public boolean isRetryButtonVisible() {
        return mRetryButton.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides the second retry button.
     */
    public ErrorView showSecondRetryButton(boolean show) {
        mSecondRetryButton.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the second retry button is visible.
     */
    public boolean isSecondRetryButtonVisible() {
        return mSecondRetryButton.getVisibility() == VISIBLE;
    }

    /**
     * Shows or hides the third retry button.
     */
    public ErrorView showThirdRetryButton(boolean show) {
        mThirdRetryButton.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    /**
     * Indicates whether the third retry button is visible.
     */
    public boolean isThirdRetryButtonVisible() {
        return mThirdRetryButton.getVisibility() == VISIBLE;
    }

    /**
     * Sets the subtitle's alignment to a given value
     */
    private void setSubtitleAlignment(TextView subtitleView, int alignment) {
        if (alignment == ALIGNMENT_LEFT) {
            subtitleView.setGravity(Gravity.LEFT);
        } else if (alignment == ALIGNMENT_CENTER) {
            subtitleView.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            subtitleView.setGravity(Gravity.RIGHT);
        }
    }

    public ErrorView setSubtitleAlignment(int alignment) {
        setSubtitleAlignment(mSubtitleTextView, alignment);
        return this;
    }

    public ErrorView setSecondSubtitleAlignment(int alignment) {
        setSubtitleAlignment(mSecondSubtitleTextView, alignment);
        return this;
    }

    public ErrorView setThirdSubtitleAlignment(int alignment) {
        setSubtitleAlignment(mThirdSubtitleTextView, alignment);
        return this;
    }

    private int getAlignmentByGravity(int gravity) {
        if (gravity == Gravity.LEFT) {
            return ALIGNMENT_LEFT;
        } else if (gravity == Gravity.CENTER_HORIZONTAL) {
            return ALIGNMENT_CENTER;
        } else {
            return ALIGNMENT_RIGHT;
        }
    }
    /**
     * Returns the current subtitle allignment
     */
    public int getSubtitleAlignment() {
        return getAlignmentByGravity(mSubtitleTextView.getGravity());
    }

    public int getSecondSubtitleAlignment() {
        return getAlignmentByGravity(mSubtitleTextView.getGravity());
    }

    public int getThirdSubtitleAlignment() {
        return getAlignmentByGravity(mSubtitleTextView.getGravity());
    }

    public interface RetryListener {
        void onRetry();
    }
}