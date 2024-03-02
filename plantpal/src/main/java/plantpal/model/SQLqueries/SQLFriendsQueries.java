package plantpal.model.SQLqueries;

import java.sql.ResultSet;
import java.util.ArrayList;

import plantpal.model.User;

public class SQLFriendsQueries {

  /**
   * Check whether two users are following each other.
   */
  public static boolean isFriend(int userID, int anotherUserID) {

    String query = "SELECT * FROM userfollow AS u1" +
        " JOIN userfollow AS u2" +
        " ON u2.followingid = u1.userid" +
        " AND u1.followingid = u2.userid" +
        " WHERE u1.userid = ?" +
        " AND u1.followingid = ?;";
    
    try {
      ResultSet rs = SQLConnector.runQuery(query, userID, anotherUserID);
    
      while(rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Checks whether the user is following another user.
   * Combines the isFriend() method can check whether the user is following the other user but not the other way around.
   */
  public static boolean isFollowing(int userID, int anotherUserID) {

    String query = "SELECT * FROM userfollow WHERE userid = ? AND followingid = ?;";
    
    try {
      ResultSet rs = SQLConnector.runQuery(query, userID, anotherUserID);

      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Follows another user.
   */
  public static void follow(int userID, int anotherUserID) {

    String query = "INSERT INTO userfollow (userid, followingid) VALUES (?, ?);";
    try {
      SQLConnector.runUpdateQuery(query, userID, anotherUserID);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Unfollows another user.
   */
  public static void unFollow(int userID, int anotherID) {
    String query = "DELETE FROM userfollow WHERE userid = ? AND followingid = ?;";
    try {
      SQLConnector.runUpdateQuery(query, userID, anotherID);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets all following users by user ID.
   */
  public static ArrayList<User> getFollowingUsers(int userID) {
    ArrayList<User> followings = new ArrayList<>();
    String query = "SELECT * FROM userfollow " +
                   "WHERE userfollow.userid = ?;";

    try {
        ResultSet rs = SQLConnector.runQuery(query, userID);
        while(rs.next()) {
            int followingID = rs.getInt("userfollow.followingid");
            User followingUser = SQLProfileQueries.getProfileInfo(followingID);
            followings.add(followingUser);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return followings;
  }

  /**
   * Gets all followers by user ID.
   */
  public static ArrayList<User> getFollowers(int userID) {

    ArrayList<User> followers = new ArrayList<>();

    String query = "SELECT * FROM userfollow " +
                   "WHERE userfollow.followingid = ?;";

    try {
        ResultSet rs = SQLConnector.runQuery(query, userID);
        while(rs.next()) {
            int followerID = rs.getInt("userfollow.userid");
            User follower = SQLProfileQueries.getProfileInfo(followerID);
            followers.add(follower);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return followers;

  }
  

}
