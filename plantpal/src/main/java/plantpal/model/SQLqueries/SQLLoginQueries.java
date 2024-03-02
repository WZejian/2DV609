package plantpal.model.SQLqueries;

import java.sql.ResultSet;

public class SQLLoginQueries {

  /**
   * See if username exists in database userlogin table.
   * 
   * @param username username
   * @return true - exists. false - not exists.
   */
  public static boolean usernameExists(String username) {
    int rowCount = 0;

    try {
      String query = "SELECT COUNT(*) FROM userlogin WHERE username = ?;";
      ResultSet rs = SQLConnector.runQuery(query, username);
      while (rs.next()) {
        rowCount = rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (rowCount == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Try to login, return user id.
   * 
   * @return if "0" is returned, the username and password does not match
   */
  public static int getUserIdWithLogin(String username, String password) {
    int userid = 0;

    try {
      String query = "SELECT * FROM userlogin WHERE username = ? AND userpassword = ?;";
      ResultSet rs = SQLConnector.runQuery(query, username, password);
      while (rs.next()) {
        userid = rs.getInt("userid");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return userid;
  }

  /**
   * Insert a user by username and password into UserLogin table.
   * 
   * @param username       username
   * @param hashedPassword hashedPassword
   */
  public static void insertUserLogin(String username, String hashedPassword, String useremail) {
    try {
      String query = "INSERT INTO userlogin (username, userpassword, useremail) VALUES (?, ?, ?);";
      SQLConnector.runUpdateQuery(query, username, hashedPassword, useremail);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if an email exists in the database.
   */
  public static boolean emailExists(String email) {
    int rowCount = 0;

    try {
      String query = "SELECT COUNT(*) FROM userlogin WHERE useremail = ?;";
      ResultSet rs = SQLConnector.runQuery(query, email);
      while (rs.next()) {
        rowCount = rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (rowCount == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Update a user's password.
   */
  public static void resetPassword(String username, String newHashedPassword) {
    try {
      String query = "UPDATE userlogin SET userpassword = ? WHERE username = ?;";
      SQLConnector.runUpdateQuery(query, newHashedPassword, username);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Insert a user by user id into UserProfile table.
   */
  public static void insertNewUserProfile(int userid) {
    try {
      String query = "INSERT INTO userprofile (userid) VALUES (?);";
      SQLConnector.runUpdateQuery(query, userid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Write a function to return useremail by username.
   */
  public static String getUserEmail(String username) {
    String useremail = "";
    try {
      String query = "SELECT useremail FROM userlogin WHERE username = ?;";
      ResultSet rs = SQLConnector.runQuery(query, username);
      while (rs.next()) {
        useremail = rs.getString("useremail");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return useremail;
  }

  /**
   * Reture username by userid.
   */
  public static String getUsernameByUserId(int userid) {
    String username = "";
    try {
      String query = "SELECT username FROM userlogin WHERE userid = ?;";
      ResultSet rs = SQLConnector.runQuery(query, userid);
      while (rs.next()) {
        username = rs.getString("username");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return username;
  }

}
