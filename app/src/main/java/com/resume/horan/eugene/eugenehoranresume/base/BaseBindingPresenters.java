package com.resume.horan.eugene.eugenehoranresume.base;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedUserRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.ui.viewimage.ViewImageActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseBindingPresenters {

    @BindingAdapter("bind:imageString")
    public static void loadImageTransition(ImageView imageView, Object object) {
        final ImageView view = imageView;
        if (object instanceof String) {
            String image = (String) object;
            Picasso.with(view.getContext())
                    .load(image)
                    .into(view, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (view.getContext() instanceof ViewImageActivity) {
                                ViewImageActivity activity = (ViewImageActivity) view.getContext();
                                activity.supportStartPostponedEnterTransition();
                            }
                        }

                        @Override
                        public void onError() {
                            if (view.getContext() instanceof ViewImageActivity) {
                                ViewImageActivity activity = (ViewImageActivity) view.getContext();
                                activity.supportStartPostponedEnterTransition();
                            }
                        }
                    });
        }
    }

    @BindingAdapter("bind:bitmap")
    public static void loadBitmap(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter({"bind:imageUrl", "bind:error"})
    public static void loadImage(View view, String url, Drawable error) {
        if (view instanceof CircleImageView) {
            Drawable drawable = DrawableCompat.wrap(error);
            drawable.mutate(); // to not share its state with any other drawable
            DrawableCompat.setTint(drawable, ContextCompat.getColor(view.getContext(), R.color.greyIconNormal));
            Picasso.with(view.getContext()).load(url).error(drawable).into((CircleImageView) view);
        } else if (view instanceof ImageView) {
            Drawable drawable = DrawableCompat.wrap(error);
            drawable.mutate(); // to not share its state with any other drawable
            DrawableCompat.setTint(drawable, ContextCompat.getColor(view.getContext(), R.color.colorAccentBlue));
            Picasso.with(view.getContext()).load(url).error(drawable).into((ImageView) view);
        }
    }

    @BindingAdapter("bind:adapter")
    public static void setRecyclerAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
    }


    @BindingAdapter({"bind:items", "bind:itemsAdd"})
    public static void setAdapterItems(RecyclerView recyclerView, List<Object> objectList, List<Object> newObjectList) {
        if (recyclerView.getAdapter() != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter instanceof FeedRecyclerAdapter) {
                FeedRecyclerAdapter mAdapter = (FeedRecyclerAdapter) adapter;
                if (objectList != null) {
                    mAdapter.setItems(objectList);
                }
                if (newObjectList != null) {
                    mAdapter.addItems(newObjectList);
                }
            } else if (adapter instanceof FeedUserRecyclerAdapter) {
                FeedUserRecyclerAdapter mAdapter = (FeedUserRecyclerAdapter) adapter;
                List<User> userList = new ArrayList<>();
                if (objectList != null) {
                    for (Object user : objectList) {
                        userList.add((User) user);
                    }
                    mAdapter.setItems(userList);
                }
            }
        }
    }
}
