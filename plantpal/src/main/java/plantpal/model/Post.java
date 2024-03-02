package plantpal.model;

import javafx.scene.image.Image;
import plantpal.model.SQLqueries.SQLGeneralQueries;

import java.util.ArrayList;

public class Post {
    private int postid;
    private int authorid;
    private String date;
    private String title;
    private String textContent;
    private ArrayList<Image> imageLst = new ArrayList<>();
    private ArrayList<String> tagLst = new ArrayList<>();

    public Post(int postid) {
        this.postid = postid;
    }

    public Post() {
    }

    public int getPostid() {
        return postid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Iterable<Image> getAllImages() {
        return this.imageLst;
    }


    public Image getImageAt(int index) {
        return imageLst.get(index);
    }

    public void addToImageLst(Image img) {
        imageLst.add(img);
    }

    public void clearImageLst() {
        imageLst.clear();
    }

    public Iterable<String> getTagLst() {
        return tagLst;
    }

    public void addToTagLst(String tag) {
        tagLst.add(tag);
    }

    public Image getAuthorAvatar() {
        return SQLGeneralQueries.getProfileImg(authorid);
    }

    public String getAuthorName() {
        return SQLGeneralQueries.getNicknameByid(authorid);
    }

    /**
     * How many images does the post contain.
     * @return number in int
     */
    public int getImageNb() {
        return imageLst.size();
    }
}
