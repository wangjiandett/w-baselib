/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moa.baselib.view.checkable;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This is a simple wrapper for {@link LinearLayout} that implements the {@link Checkable}
 * interface by keeping an internal 'checked' state flag.
 * <p>
 * This can be used as the root view for a custom list item layout for
 * {@link android.widget.AbsListView} elements with a
 * {@link android.widget.AbsListView#setChoiceMode(int) choiceMode} set.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] CHECKED_VALUE_SET = {android.R.attr.checked};

    private boolean mChecked = false;

    public CheckableRelativeLayout(Context context) {
        this(context, null);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, CHECKED_VALUE_SET);
        mChecked = typedArray.getBoolean(0, mChecked);
        setChecked(mChecked);
        typedArray.recycle();
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();
            invalidate();
        }
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
