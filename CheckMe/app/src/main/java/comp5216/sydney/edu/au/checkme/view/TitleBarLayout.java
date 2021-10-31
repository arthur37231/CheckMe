package comp5216.sydney.edu.au.checkme.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import comp5216.sydney.edu.au.checkme.R;

/**
 * The customized action bar to replace the system default. It has a title with two buttons.
 * The title and button texts can be setup while the action bar is loaded. The two buttons
 * <code>onClickListener</code> can also setup to have different effects.
 */
public class TitleBarLayout extends LinearLayout {

    private final TextView title;
    private final Button operate;
    private final Button back;

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.title_bar, this);

        this.title = findViewById(R.id.activityTitle);
        this.operate = findViewById(R.id.activityOperation);
        this.back = findViewById(R.id.activityBack);
    }

    /**
     * Setup the text content of the title.
     *
     * @param resId The android string resource id defined in resources directory.
     */
    public TitleBarLayout setupTitle(int resId) {
        title.setText(resId);
        return this;
    }

    public TitleBarLayout operateInvisible() {
        operate.setVisibility(View.GONE);
        return this;
    }

    /**
     * Set up the operate button text content and corresponding
     * {@link OnClickListener}.
     *
     * @param resId The android string resource id defined in resources directory.
     * @param onClickListener OnClick method when the operate button is clicked.
     */
    public TitleBarLayout setupOperate(int resId, OnClickListener onClickListener) {
        operate.setText(resId);
        operate.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * Make the back button invisible. Generally used when the current activity does not allow to
     * go back.
     */
    public TitleBarLayout backInvisible() {
        back.setVisibility(View.GONE);
        title.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        return this;
    }

    /**
     * Setup the back button text content and corresponding
     * {@link OnClickListener}.
     *
     * @param onClickListener OnClick method when the back button is clicked.
     */
    public TitleBarLayout setupBack(OnClickListener onClickListener) {
        back.setOnClickListener(onClickListener);
        return this;
    }
}
