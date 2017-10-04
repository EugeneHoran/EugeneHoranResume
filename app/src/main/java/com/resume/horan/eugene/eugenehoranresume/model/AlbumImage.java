package com.resume.horan.eugene.eugenehoranresume.model;


import android.app.Activity;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.ui.viewimage.ViewImageActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AlbumImage implements Parcelable {
    private int imageType;
    private String imageName;
    private String imageUrl;
    private String imageColorHEX;
    private String imageWidth;
    private String imageHeigh;
    private String imageSize;
    private float rotate;
    private String imageOrientation;
    private String imageLocation;

    @BindingAdapter("load_image")
    public static void loadImage(final ImageView view, AlbumImage object) {
        Picasso.with(view.getContext())
                .load(object.getImageUrl())
                .rotate(object.getRotate())
                .resize(object.getFormattedWidth(), object.getFormattedHeight())
                .onlyScaleDown()
                .centerInside()
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


    public int getCardBackgroundColor() {
        return Color.parseColor(imageColorHEX);
    }

    public int getFormattedWidth() {
        return (int) Integer.parseInt(getImageWidth()) / 3;
    }

    public int getFormattedHeight() {
        return (int) Integer.parseInt(getImageHeigh()) / 3;
    }

    public AlbumImage() {
    }

    public AlbumImage(int imageType, String imageName, String imageUrl, String imageColorHEX, String imageWidth, String imageHeigh, String imageSize, float rotate, String imageOrientation, String imageLocation) {
        this.imageType = imageType;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.imageColorHEX = imageColorHEX;
        this.imageWidth = imageWidth;
        this.imageHeigh = imageHeigh;
        this.imageSize = imageSize;
        this.rotate = rotate;
        this.imageOrientation = imageOrientation;
        this.imageLocation = imageLocation;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageColorHEX() {
        return imageColorHEX;
    }

    public void setImageColorHEX(String imageColorHEX) {
        this.imageColorHEX = imageColorHEX;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeigh() {
        return imageHeigh;
    }

    public void setImageHeigh(String imageHeigh) {
        this.imageHeigh = imageHeigh;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public String getImageOrientation() {
        return imageOrientation;
    }

    public void setImageOrientation(String imageOrientation) {
        this.imageOrientation = imageOrientation;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imageType);
        dest.writeString(this.imageName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageColorHEX);
        dest.writeString(this.imageWidth);
        dest.writeString(this.imageHeigh);
        dest.writeString(this.imageSize);
        dest.writeFloat(this.rotate);
        dest.writeString(this.imageOrientation);
        dest.writeString(this.imageLocation);
    }

    protected AlbumImage(Parcel in) {
        this.imageType = in.readInt();
        this.imageName = in.readString();
        this.imageUrl = in.readString();
        this.imageColorHEX = in.readString();
        this.imageWidth = in.readString();
        this.imageHeigh = in.readString();
        this.imageSize = in.readString();
        this.rotate = in.readFloat();
        this.imageOrientation = in.readString();
        this.imageLocation = in.readString();
    }

    public static final Creator<AlbumImage> CREATOR = new Creator<AlbumImage>() {
        @Override
        public AlbumImage createFromParcel(Parcel source) {
            return new AlbumImage(source);
        }

        @Override
        public AlbumImage[] newArray(int size) {
            return new AlbumImage[size];
        }
    };
}
