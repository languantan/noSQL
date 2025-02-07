package com.battlehack.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeyHelper implements OnTouchListener {
	public static enum Action {
		LEFTRIGHT, // Left to Right
		RIGHTLEFT, // Right to Left
		TOPDOWN, // Top to bottom
		BOTTOMUP, // Bottom to Top
		NONE // when no action was detected
	}

	private static final int MIN_DISTANCE = 100;
	private static final int MIN_VERTICAL_DISTANCE = 5;
	private float downX, downY, upX, upY;
	private Action mSwipeDetected = Action.NONE;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			mSwipeDetected = Action.NONE;
			return false; // allow other events like Click to be processed
		}

		case MotionEvent.ACTION_MOVE: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;
			if (Math.abs(deltaX) > MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {
					mSwipeDetected = Action.LEFTRIGHT;
					return true;
				}
				if (deltaX > 0) {
					mSwipeDetected = Action.RIGHTLEFT;
					return true;
				}
			} else {
				if (Math.abs(deltaY) > MIN_VERTICAL_DISTANCE) {
					// top or down
					if (deltaY < 0) {
						mSwipeDetected = Action.TOPDOWN;
						return false;
					}
					if (deltaY > 0) {
						mSwipeDetected = Action.BOTTOMUP;
						return false;
					}
				}
				return true;
			}
		}
		}//switch case
		return false;
	}

	public boolean swipeDetected() {
		return mSwipeDetected != Action.NONE;
	}

	public Action getAction() {
		return mSwipeDetected;
	}

}
