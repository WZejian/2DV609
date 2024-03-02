package plantpal.model.SQLqueries;

import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.scene.image.Image;
import plantpal.model.ImageHandler;
import plantpal.model.User;

public class SQLProfileQueries {

    /**
     * return a user object.
     */
    public static User getProfileInfo(int userid) {
        User theUser = new User(userid);
        String query = "SELECT * FROM userprofile " +
                "INNER JOIN selocations " +
                "ON userprofile.locationid = selocations.id " +
                "INNER JOIN image " +
                "ON userprofile.pictureid = image.id " +
                "WHERE userprofile.userid = ?;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                theUser.setNickName(rs.getString("userprofile.nickname"));
                theUser.setLocationName(rs.getString("selocations.name"));
                theUser.setDesc(rs.getString("userprofile.description"));
                theUser.setTel(rs.getString("userprofile.tel"));
                String picId = rs.getString("image.id");
                Blob picBlob = rs.getBlob("image.imageblob");
                Image pic = ImageHandler.getFileFromBlob(picId, picBlob);
                theUser.setAvatar(pic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theUser;
    }

    /**
     * Update a user's nickname.
     * 
     * @param userid
     * @param nickname
     */
    public static void updateNickname(String nickname, int userid) {
        try {
            String query = "UPDATE userprofile SET nickname = ? WHERE userid = ?;";
            SQLConnector.runUpdateQuery(query, nickname, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * how many people is the user following.
     */
    public static int getFollowingNb(int userid) {
        try {
            String query = "SELECT COUNT(*) FROM userfollow WHERE userid=?;";
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * how many following the user.
     */
    public static int getFollowerNb(int userid) {
        try {
            String query = "SELECT COUNT(*) FROM userfollow WHERE followingid=?;";
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static Iterable<Integer> getMyPostids(int userid) {
        ArrayList<Integer> myPostIDs = new ArrayList<>();
        try {
            String query = "SELECT id FROM post WHERE authorid=?;";
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                myPostIDs.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myPostIDs;
    }

    
    public static Iterable<Integer> getSavedPostids(int userid) {
        ArrayList<Integer> savedPostIDs = new ArrayList<>();
        try {
            String query = "SELECT postid FROM postsave WHERE userid=?;";
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                savedPostIDs.add(rs.getInt("postid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedPostIDs;
    }

}
