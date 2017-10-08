package com.resume.horan.eugene.eugenehoranresume.util.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class CustomTopBar extends CoordinatorLayout.Behavior<CustomLinerLayout> {
    //Required to instantiate as a default behavior
    public CustomTopBar() {
    }

    //Required to attach behavior via XML
    public CustomTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //This is called to determine which views this behavior depends on
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   CustomLinerLayout child,
                                   View dependency) {
        //We are watching changes in the AppBarLayout
        return dependency instanceof AppBarLayout;
    }

    //This is called for each change to a dependent view
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          CustomLinerLayout child,
                                          View dependency) {
        int offset = -dependency.getTop();
        child.setTranslationY(offset);
        return true;
    }
}
