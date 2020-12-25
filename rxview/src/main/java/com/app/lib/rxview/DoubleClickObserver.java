package com.app.lib.rxview;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DoubleClickObserver
        implements GestureDetector.OnGestureListener, View.OnTouchListener, GestureDetector.OnDoubleTapListener{
    private View view;
    private GestureDetector gestureDetector;
    private OnDoubleClickListener onDoubleClickListener;

    public DoubleClickObserver(View view){
        this.view = view;
        gestureDetector = new GestureDetector(view.getContext(),this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    public void subscribe(OnDoubleClickListener listener){
        if(view != null){
            view.setOnTouchListener(this);
        }
        onDoubleClickListener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e){
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e){
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e){
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e){
        if(onDoubleClickListener != null){
            onDoubleClickListener.onLongClick(view);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e){
        if(onDoubleClickListener != null){
            onDoubleClickListener.onSingleClick(view);
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e){
        if(onDoubleClickListener != null){
            onDoubleClickListener.onDoubleClick(view);
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e){
        return false;
    }
}
