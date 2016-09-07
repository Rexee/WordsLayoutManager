package android.support.v7.widget;


import android.content.Context;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import java.util.ArrayList;

public class WordsLayoutManager extends LinearLayoutManager {
    private int layoutWidth;

    public WordsLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(Recycler recycler, State state) {
        layoutWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        super.onLayoutChildren(recycler, state);
    }

    @Override
    void layoutChunk(Recycler recycler, State state, LayoutState layoutState, LayoutChunkResult result) {
        boolean scrollToTop = layoutState.mLayoutDirection == LayoutState.LAYOUT_START;
        View childView;
        LineModel currentLine = new LineModel();
        while (layoutState.hasMore(state)) {
            childView = layoutState.next(recycler);
            if (childView == null) {
                result.mFinished = true;
                break;
            } else {

                measureChildWithMargins(childView, 0, 0);
                ViewModel view = new ViewModel(childView);

                if (currentLine.canFit(view)) {
                    if (result.mConsumed == 0) {
                        result.mConsumed = mOrientationHelper.getDecoratedMeasurement(childView);
                    }

                    if (scrollToTop) {
                        currentLine.addView(view, 0);
                        addView(childView, 0);
                    } else {
                        currentLine.addView(view);
                        addView(childView);
                    }
                } else {
                    if (scrollToTop) {
                        layoutState.mCurrentPosition++;
                    } else {
                        layoutState.mCurrentPosition--;
                    }

                    break;
                }
            }
        }

        int left, top, bottom;
        if (scrollToTop) {
            bottom = layoutState.mOffset;
            top = layoutState.mOffset - result.mConsumed;
        } else {
            top = layoutState.mOffset;
            bottom = layoutState.mOffset + result.mConsumed;
        }
        left = getPaddingLeft();

        int prevChildWidth = 0;
        for (ViewModel child : currentLine.views) {
            layoutDecorated(child.view, left + prevChildWidth, top, left + prevChildWidth + child.width, bottom);
            prevChildWidth += child.width;
        }
    }

    private class ViewModel {
        private View view;
        private int  width;

        public ViewModel(View child) {
            view = child;
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            width = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        }
    }

    private class LineModel {
        private ArrayList<ViewModel> views;
        private int                  width;

        public LineModel() {
            views = new ArrayList<>();
        }

        public void addView(ViewModel child) {
            addView(child, views.size());
        }

        public void addView(ViewModel child, int pos) {
            views.add(pos, child);
            width += child.width;
        }

        public boolean canFit(ViewModel child) {
            return width + child.width <= layoutWidth;
        }
    }
}