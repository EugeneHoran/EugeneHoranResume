
package com.resume.horan.eugene.eugenehoranresume.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Experience implements Parcelable {

    private String order;
    private String company;
    private String header;
    private String dateStart;
    private String dateEnd;
    private String dateRange;
    private String position;
    private String linkApp;
    private String linkSite;
    private List<Bullet> bullets = null;

    /**
     * No args constructor for use in serialization
     */
    public Experience() {
    }

    /**
     * @param position
     * @param dateRange
     * @param linkApp
     * @param order
     * @param company
     * @param linkSite
     * @param dateEnd
     * @param dateStart
     * @param header
     * @param bullets
     */
    public Experience(String order, String company, String header, String dateStart, String dateEnd, String dateRange, String position, String linkApp, String linkSite, List<Bullet> bullets) {
        super();
        this.order = order;
        this.company = company;
        this.header = header;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateRange = dateRange;
        this.position = position;
        this.linkApp = linkApp;
        this.linkSite = linkSite;
        this.bullets = bullets;
    }

    public String getPositionFormatted() {
        return String.format("%s %s",
                getCompany(),
                !getHeader().equalsIgnoreCase("") ? "(" + getHeader() + ")" : "");
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLinkApp() {
        return linkApp;
    }

    public void setLinkApp(String linkApp) {
        this.linkApp = linkApp;
    }

    public String getLinkSite() {
        return linkSite;
    }

    public void setLinkSite(String linkSite) {
        this.linkSite = linkSite;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    /**
     * Parcel
     */
    public final static Creator<Experience> CREATOR = new Creator<Experience>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Experience createFromParcel(Parcel in) {
            return new Experience(in);
        }

        public Experience[] newArray(int size) {
            return (new Experience[size]);
        }

    };

    protected Experience(Parcel in) {
        this.order = ((String) in.readValue((String.class.getClassLoader())));
        this.company = ((String) in.readValue((String.class.getClassLoader())));
        this.header = ((String) in.readValue((String.class.getClassLoader())));
        this.dateStart = ((String) in.readValue((String.class.getClassLoader())));
        this.dateEnd = ((String) in.readValue((String.class.getClassLoader())));
        this.dateRange = ((String) in.readValue((String.class.getClassLoader())));
        this.position = ((String) in.readValue((String.class.getClassLoader())));
        this.linkApp = ((String) in.readValue((String.class.getClassLoader())));
        this.linkSite = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.bullets, (Bullet.class.getClassLoader()));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(order);
        dest.writeValue(company);
        dest.writeValue(header);
        dest.writeValue(dateStart);
        dest.writeValue(dateEnd);
        dest.writeValue(dateRange);
        dest.writeValue(position);
        dest.writeValue(linkApp);
        dest.writeValue(linkSite);
        dest.writeList(bullets);
    }

    public int describeContents() {
        return 0;
    }

}
