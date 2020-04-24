package com.inihood.facey.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareRL extends RelativeLayout {

	public SquareRL(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SquareRL(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("NewApi") public SquareRL(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); // Snap to
																		// width
	}

}
