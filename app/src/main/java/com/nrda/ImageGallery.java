package com.nrda;
/**
 * Created by Diwash Choudhary on 20-07-2017.
 */
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ImageGallery extends AppCompatActivity {

    private ViewFlipper mViewFlipper;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private Context mContext;
    private Animation.AnimationListener mAnimationListener;
    private TextView tool_title;
    private ImageView tool_back_icon;

    @SuppressWarnings("deprecation")
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        mContext = this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);

        tool_title = (TextView) findViewById(R.id.appTitle);

        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_ImageGallery.this,MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.image_gallery));

        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            mViewFlipper.setAutoStart(true);
        }
        mViewFlipper.setFlipInterval(4000);
        mViewFlipper.startFlipping();

        mAnimationListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                //animation started event
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                //TODO animation stopped event
            }
        };

    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    // controlling animation
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                    // controlling animation
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
