package plantpal.model.SQLqueries;

import javafx.scene.image.Image;
import plantpal.model.ImageHandler;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SQLGeneralQueries {

  // ------------------------IMAGES QUERIES--------------------

  /**
   * Returns an javafx Image of avatar by the given user id.
   */
  public static Image getProfileImg(int userid) {
    String query = "SELECT * FROM userprofile " +
        "INNER JOIN image " +
        "ON userprofile.pictureid = image.id " +
        "WHERE userprofile.userid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, userid);
      while (rs.next()) {
        String picId = rs.getString("image.id");
        Blob picBlob = rs.getBlob("image.imageblob");
        Image avatar = ImageHandler.getFileFromBlob(picId, picBlob);
        return ImageHandler.cropToSquare(avatar);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * Get javafx image obj from database table image.
   * 
   * @param imageid image.id
   */
  public static Image getImageByImageid(int imageid) {
    String query = "SELECT * FROM image WHERE id = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, imageid);
      while (rs.next()) {
        String picId = rs.getString("id");
        Blob picBlob = rs.getBlob("imageblob");
        Image theimg = ImageHandler.getFileFromBlob(picId, picBlob);
        return theimg;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * insert an image to db.
   * 
   * @param img        the javafx image
   * @param uploaderid uploader id
   * @return the image id of the image inserted just now
   */
  public static int insertImage(Image img, int uploaderid) {
    int imageid = -1;

    try {
      // insert image
      Blob imageBlob = ImageHandler.imageToBlob(img);
      String insertImage = "INSERT INTO image(imageblob, uploaderid) VALUES (?, ?);";
      SQLConnector.runUpdateQuery(insertImage, imageBlob, uploaderid);

      // get image id of the image inserted just now
      String getImageid = "SELECT max(id) FROM image WHERE uploaderid = ?;";
      ResultSet rs = SQLConnector.runQuery(getImageid, uploaderid);
      while (rs.next()) {
        imageid = rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return 1; // id of "img not avaliable" image
    }
    return imageid;
  }

  // ------------------------NICKNAME QUERIES--------------------

  /**
   * returns nickname by the given user id.
   */
  public static String getNicknameByid(int userid) {
    String nickname = "";
    String query = "SELECT * FROM userprofile " +
        "WHERE userprofile.userid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, userid);
      while (rs.next()) {
        nickname = rs.getString("nickname");

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return nickname;
  }

  // ------------------------TAG QUERIES--------------------

  /**
   * all avaiable tags in tag table.
   * 
   * @return a list of tagnames
   */
  public static ArrayList<String> getAllTagsInDB() {
    ArrayList<String> tagnames = new ArrayList<>();
    try {
      String query = "SELECT * FROM tag;";
      ResultSet rs = SQLConnector.runQuery(query);
      while (rs.next()) {
        String onetag = rs.getString("tagname");
        tagnames.add(onetag);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tagnames;
  }

  /**
   * get tag id by tagname.
   * 
   * @param tagname
   * @return tagid as int
   */
  public static int getTagidByTagname(String tagname) {
    int tagid = -1;
    try {
      String getTagid = "SELECT tagid FROM tag WHERE tagname=?;";
      ResultSet rs = SQLConnector.runQuery(getTagid, tagname);
      while (rs.next()) {
        tagid = rs.getInt("tagid");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return tagid;
  }

  // ------------------------LOCATION QUERIES--------------------
  
  /**
   * all avaiable locations in selocations table.
   * 
   * @return
   */
  public static ArrayList<String> getAllLocationsInDB() {
    ArrayList<String> locationNames = new ArrayList<>();
    try {
      String query = "SELECT * FROM selocations;";
      ResultSet rs = SQLConnector.runQuery(query);
      while (rs.next()) {
        String oneLocation = rs.getString("name");
        locationNames.add(oneLocation);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return locationNames;
  }

  /**
   * Get location name by location id.
   */
  public static String getLocationName(int locationId) {
    String locationName = "";
    String query = "SELECT * FROM selocations WHERE id = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, locationId);
      while (rs.next()) {
        locationName = rs.getString("name");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return locationName;
  }

  /**
   * get location id by location name.
   * @return location id as int
   */
  public static int getLocidByLocName(String locationname) {
    int locationId = -1;
    try {
      String getLocid = "SELECT id FROM selocations WHERE name=?;";
      ResultSet rs = SQLConnector.runQuery(getLocid, locationname);
      while (rs.next()) {
        locationId = rs.getInt("id");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return locationId;
  }


  // ------------------------POINTS QUERIES--------------------
  /**
   * Make sure the points is enough if deduct
   */
  public static void addPointsbyId(int userid, int num) {
    try {
      int userPoints = -1;
      String getPoints = "SELECT points FROM userprofile WHERE userid=?;";
      ResultSet rs = SQLConnector.runQuery(getPoints, userid);
      while (rs.next()) {

        // it used to be id, now changed to points
        userPoints = rs.getInt("points");
      }

      userPoints += num;

      String updatePoints = "UPDATE userprofile SET points=? WHERE userid=?;";
      SQLConnector.runUpdateQuery(updatePoints, userPoints, userid);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  // ------------------------TAG QUERIES--------------------

  

  
 


}
