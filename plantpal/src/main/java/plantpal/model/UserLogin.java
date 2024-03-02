package plantpal.model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserLogin {
  private int userId;
  private String userName;
  private String userEmail;
  private String hashedPassword;

  public UserLogin() {
  }

  public UserLogin(String name, String userEmail, String hashedPassword) {
    this.hashedPassword = hashedPassword;
    this.userName = name;
    this.userEmail = userEmail;
  }

  public UserLogin(int id, String name,String userEmail, String hashedPassword) {
    this.userId = id;
    this.hashedPassword = hashedPassword;
    this.userEmail = userEmail;
    this.userName = name;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  /**
   * Encode a user's password to a hashed string.
   * https://www.geeksforgeeks.org/sha-256-hash-in-java/.
   * @throws NoSuchAlgorithmException
   */
  public String passwordToHashString(String password) throws NoSuchAlgorithmException {
    String hashedPassword = null;
    
    MessageDigest md = MessageDigest.getInstance("SHA-256"); 
    // getBytes(): convert the encoding of string into the sequence of bytes
    // and keeps it in an array of bytes
    // digest(): calculate an input's message digest 
    byte[] messageDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));

    // Signum function helps determine the sign of the real value function
    // +1: positive input values of the function
    // -1: negative input values of the function. 
    BigInteger number = new BigInteger(1, messageDigest); 
    // Convert message digest into hex value 
    StringBuilder hexString = new StringBuilder(number.toString(16)); 
    // Pad with leading zeros
    while (hexString.length() < 32) { 
      // insert(): insert the string representation of the char argument 
      // into this sequence.
      hexString.insert(0, '0'); 
    } 
    hashedPassword = hexString.toString();

    return hashedPassword;
  }

  // Check if an email format is valid.
  public boolean isValidEmail(String email) {
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(regex);
  }
}
