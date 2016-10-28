package tw.com.flag.youtube_test_project;

import com.google.api.client.util.DateTime;

/**
 * Created by AllenLin on 2016/7/15.
 */
public class VideoItem {
    private String title;
    private DateTime datetime;
    private String thumbnailURL;
    private String id;
    private String personweight;
    private String communityweight;
    private String totalweight;

    public String getPersonWeight(){
        return personweight;
    }

    public void setPersonWeight(String weight) {
        this.personweight = weight;
    }

    public String getCommunityWeight(){
        return communityweight;
    }

    public void setCommunityWeight(String weight) {
        this.communityweight = weight;
    }

    public String getTotalWeight(){
        return totalweight;
    }

    public void setTotalWeight(String weight) {
        this.totalweight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }
}
