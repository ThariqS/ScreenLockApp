package org.uzero.android.crope;

import java.util.ArrayList;

import org.uzero.android.crope.CircularSeekBar.OnSeekChangeListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class LockView extends View {

	private static final int INVALID_POINTER_ID = -1;

	/** The listener to listen for changes */
	private OnAnswerListener mListener;

    private Drawable mImage;
    private float mPosX;
    private float mPosY;
    private boolean locked = false;
    
    private String question = "";
    private ArrayList<String> answers;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    
	public interface OnAnswerListener {
		public void onAnswer(LockView view, int answerIndex);
	}
	
	public void setOnAnswerListener(OnAnswerListener listener) {
		mListener = listener;
	}

	public OnAnswerListener getOnAnswerListener() {
		return mListener;
	}

    public LockView(Context context) {
        this(context, null, 0);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mImage = getResources().getDrawable(R.drawable.lock_blue);
        mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();

            mLastTouchX = x;
            mLastTouchY = y;
            mActivePointerId = ev.getPointerId(0);
            break;
        }

        case MotionEvent.ACTION_MOVE: {
            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
            final float x = ev.getX(pointerIndex);
            final float y = ev.getY(pointerIndex);

            // Only move if the ScaleGestureDetector isn't processing a gesture.
            if (!mScaleDetector.isInProgress()) {
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;
                
                if (!locked) {
	                if (mPosX > 0 && mPosX < getWidth()/5 && mPosY > getHeight()/4
	                		&& mPosY > getHeight()/4 && mPosY < 2*getHeight()/5) {
	                	//Log.d("Answer: ","" + -1);
	                	mListener.onAnswer(this, -1);
	                	locked = true;
	                } else if (mPosX > getWidth()/5 && mPosX < 2 * getWidth()/5 && mPosY > getHeight()/4
	                		&& mPosY > getHeight()/4 && mPosY < 2*getHeight()/5) {
	                	//Log.d("Answer: ","" + 0);
	                	mListener.onAnswer(this, 0);
	                	locked = true;
	                } else if (mPosX > 2 * getWidth()/5 && mPosX < 3 * getWidth()/5 && mPosY > getHeight()/4
	                		&& mPosY > getHeight()/4 && mPosY < 2*getHeight()/5) {
	                	//Log.d("Answer: ","" + 1);
	                	mListener.onAnswer(this, 1);
	                	locked = true;
	                } else if (mPosX > 3 * getWidth()/5 && mPosX < 4 * getWidth()/5 && mPosY > getHeight()/4
	                		&& mPosY > getHeight()/4 && mPosY < 2*getHeight()/5) {
	                	//Log.d("Answer: ","" + 2);
	                	mListener.onAnswer(this, 2);
	                	locked = true;
	                } else if (mPosX > 4 * getWidth()/5 && mPosX < 5 * getWidth()/5 && mPosY > getHeight()/4
	                		&& mPosY > getHeight()/4 && mPosY < 2*getHeight()/5) {
	                	//Log.d("Answer: ","" + -1);
	                	mListener.onAnswer(this, -1);
	                	locked = true;
	                }
                }
                invalidate();
            }

            mLastTouchX = x;
            mLastTouchY = y;

            break;
        }

        case MotionEvent.ACTION_UP: {
            mActivePointerId = INVALID_POINTER_ID;
            mPosX = getWidth()/2 - mScaleFactor * mImage.getIntrinsicWidth()/2;
            mPosY = 3 *getHeight()/5;
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
            mActivePointerId = INVALID_POINTER_ID;
            break;
        }

        case MotionEvent.ACTION_POINTER_UP: {
            final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int pointerId = ev.getPointerId(pointerIndex);
            if (pointerId == mActivePointerId) {
                // This was our active pointer going up. Choose a new
                // active pointer and adjust accordingly.
                final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                mLastTouchX = ev.getX(newPointerIndex);
                mLastTouchY = ev.getY(newPointerIndex);
                mActivePointerId = ev.getPointerId(newPointerIndex);
            }
            break;
        }
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        canvas.save(); 
        Paint textPaint = new Paint();
        textPaint.setColor(Color.TRANSPARENT); 
        textPaint.setStyle(Style.FILL); 
        canvas.drawPaint(textPaint);
        
        textPaint.setColor(Color.WHITE); 
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTextSize(30); 
        canvas.drawText(question, getWidth()/2, getHeight()/4, textPaint);
        canvas.drawText("Ignore", 1*getWidth()/10, 2*getHeight()/5, textPaint);
        canvas.drawText(answers.get(0), 3*getWidth()/10, 2*getHeight()/5, textPaint);
        canvas.drawText(answers.get(1), 5*getWidth()/10, 2*getHeight()/5, textPaint);
        canvas.drawText(answers.get(2), 7*getWidth()/10, 2*getHeight()/5, textPaint);
        canvas.drawText("More...", 9*getWidth()/10, 2*getHeight()/5, textPaint);
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        mImage.draw(canvas);
        canvas.restore();
    }
    
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      mPosX = getWidth()/2 - mScaleFactor * mImage.getIntrinsicWidth()/2;
      mPosY = 3 *getHeight()/5;
    }
    
    public void setQuestion(String q) {
    	Log.d("DEBUG",q);
    	question = q;
    }
    
    public void setAnswers(ArrayList<String> a) {
    	answers = a;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }
    

}
