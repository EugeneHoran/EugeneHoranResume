
package com.resume.horan.eugene.eugenehoranresume.model;


public class SocialMedia {

    private String account;
    private String url;
    private String visible;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SocialMedia() {
    }

    /**
     * 
     * @param visible
     * @param account
     * @param url
     */
    public SocialMedia(String account, String url, String visible) {
        super();
        this.account = account;
        this.url = url;
        this.visible = visible;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

}
