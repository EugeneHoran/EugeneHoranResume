
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bullet implements Parcelable {

    private String order;
    private String bullet;

    /**
     * No args constructor for use in serialization
     */
    public Bullet() {
    }

    /**
     * @param order
     * @param bullet
     */
    public Bullet(String order, String bullet) {
        super();
        this.order = order;
        this.bullet = bullet;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getBullet() {
        return bullet;
    }

    public void setBullet(String bullet) {
        this.bullet = bullet;
    }


    /**
     * Parcel
     */
    public final static Creator<Bullet> CREATOR = new Creator<Bullet>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Bullet createFromParcel(Parcel in) {
            return new Bullet(in);
        }

        public Bullet[] newArray(int size) {
            return (new Bullet[size]);
        }
    };

    protected Bullet(Parcel in) {
        this.order = ((String) in.readValue((String.class.getClassLoader())));
        this.bullet = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(order);
        dest.writeValue(bullet);
    }

    public int describeContents() {
        return 0;
    }

}
