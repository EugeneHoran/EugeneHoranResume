package com.resume.horan.eugene.eugenehoranresume.viewimage;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;

import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;

public class ViewImageViewHolder extends BaseObservable {

    public ViewImageViewHolder(AlbumImage albumImage) {
        setBackgroundColor(albumImage.getImageColorHEX());
        setImageUrl(albumImage.getImageUrl());
    }

    public ViewImageViewHolder(String albumImage) {
        setImageUrl(albumImage);
    }

    private String imageUrl;

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    private void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyChange();
    }

    private String backgroundColor = "#00000000";

    @Bindable
    public int getBackgroundColor() {
        return Color.parseColor(backgroundColor);
    }

    private void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyChange();
    }
}
