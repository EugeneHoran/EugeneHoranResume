package com.resume.horan.eugene.eugenehoranresume.util.ui;

import android.support.design.widget.AppBarLayout;


public class AppBarCollapsedListener implements AppBarLayout.OnOffsetChangedListener {

    private boolean isCollapsed = false;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        isCollapsed = verticalOffset == -appBarLayout.getTotalScrollRange();
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

}