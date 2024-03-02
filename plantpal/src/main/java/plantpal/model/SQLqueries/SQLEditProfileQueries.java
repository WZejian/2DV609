package plantpal.model.SQLqueries;

import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class SQLEditProfileQueries {
    /**
     * Check if the id entered existed.
     */
    public static String getUsernameById(int userid) {
        String query = "SELECT username FROM userlogin WHERE userid = ?;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                return rs.getString("username");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get all tags' names by userid.
     */
    public static Iterable<String> getTagsByUserid(int userid) {
        ArrayList<String> tagLst = new ArrayList<>();

        String query = "SELECT tagname FROM usertag INNER JOIN tag WHERE usertag.userid = ? AND usertag.tagid = tag.tagid;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                tagLst.add(rs.getString("tagname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagLst;
    }

    public static void updateNickname(int userid, String newNickname) {
        try {
            String updateNickname = "UPDATE userprofile SET nickname=? WHERE userid=?;";
            SQLConnector.runUpdateQuery(updateNickname, newNickname, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTEL(int userid, String newTEL) {
        try {
            String updateTEL = "UPDATE userprofile SET tel=? WHERE userid=?;";
            SQLConnector.runUpdateQuery(updateTEL, newTEL, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDesc(int userid, String newDesc) {
        try {
            String updateDesc = "UPDATE userprofile SET description=? WHERE userid=?;";
            SQLConnector.runUpdateQuery(updateDesc, newDesc, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLocation(int userid, String newLoc) {
        try {
            int locid = SQLGeneralQueries.getLocidByLocName(newLoc);
            String updateLoc = "UPDATE userprofile SET locationid=? WHERE userid=?;";
            SQLConnector.runUpdateQuery(updateLoc, locid, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAvatar(int userid, Image avatar) {
        try {
            int imgid = SQLGeneralQueries.insertImage(avatar, userid);
            String updateAvatar = "UPDATE userprofile SET pictureid=? WHERE userid=?;";
            SQLConnector.runUpdateQuery(updateAvatar, imgid, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateUserTags(int userid, ArrayList<String> tagLst) {
        try {
            String delTags = "DELETE FROM usertag WHERE userid = ?;";
            SQLConnector.runUpdateQuery(delTags, userid);
            for (String tag : tagLst) {
                int tagid = SQLGeneralQueries.getTagidByTagname(tag);
                String insertTag = "INSERT INTO usertag(userid, tagid) VALUES (?, ?);";
                SQLConnector.runUpdateQuery(insertTag, userid, tagid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
