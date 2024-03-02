package plantpal.model.SQLqueries;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import plantpal.model.Order;
import plantpal.model.Product;

public class SQLOrderQueries {

  private final static String UNFINISHED = "not paid";
  private final static String FINISHED = "paid";

  /**
   * Check if a product is added to an order by product id.
   */
  public static boolean isProductInOrder(int productid) {
    String query = "SELECT * FROM ordertable WHERE productid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, productid);
      if (rs.next()) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Delete an order by product id.
   */
  public static void deleteOrderByProductId(int productid) {
    String query = "DELETE FROM ordertable WHERE productid = ?;";
    try {
      SQLConnector.runUpdateQuery(query, productid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * A user adds an order to the order table.
   */
  public static void addOrder(int userid, Product product) {
    int productId = product.getProductId();
    int seller = product.getAuthorId();
    int buyer = userid;
    String proofTime = null;
    String status = UNFINISHED;
    int proofid = 1;
    String query = "INSERT INTO ordertable (productid, seller, buyer, ordertime, prooftime, status, proofid) VALUES (?, ?, ?, NOW(), ?, ?, ?);";
    try {
      SQLConnector.runUpdateQuery(query, productId, seller, buyer, proofTime, status, proofid);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Get an arraylist of all product id by a given user id in order table.
   */
  public static ArrayList<Integer> getAllProductIdsByUserId(int userid) {
    String query = "SELECT productid FROM ordertable WHERE buyer = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, userid);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Return an order by product id.
   */
  public static Order getOrder(int productid) {
    String query = "SELECT * FROM ordertable WHERE productid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, productid);
      if (rs.next()) {
        int sellerId = rs.getInt("seller");
        int buyerId = rs.getInt("buyer");
        String orderTime = rs.getTimestamp("ordertime").toString();
        String proofTime;
        Timestamp proof = rs.getTimestamp("prooftime");
        if (proof == null) {
          proofTime = "unknown";
        } else {
          proofTime = proof.toString();
        }
        if (proofTime.equals("null")) {
          proofTime = null;
        }
        String status = rs.getString("status");
        int proofid = rs.getInt("proofid");
        return new Order(productid, sellerId, buyerId, orderTime, proofTime, status, proofid);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Check if an order is finished or not.
   */
  public static boolean isOrderFinished(int productid) {
    String query = "SELECT status FROM ordertable WHERE productid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, productid);
      if (rs.next()) {
        String status = rs.getString("status");
        if (status.equals(FINISHED)) {
          return true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Get an arraylist of all product id which have a status of "not paid" and the
   * seller id or the buyer id is the given user id.
   */
  public static ArrayList<Integer> getAllUnfinishedProductIdsByUserId(int userid) {
    String query = "SELECT productid FROM ordertable WHERE (seller = ? OR buyer = ?) AND status = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, userid, userid, UNFINISHED);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get an arraylist of all product id which have a status of "paid" and the
   * seller id or the buyer id is the given user id.
   */
  public static ArrayList<Integer> getAllFinishedProductIdsByUserId(int userid) {
    String query = "SELECT productid FROM ordertable WHERE (seller = ? OR buyer = ?) AND status = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, userid, userid, FINISHED);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Update an order's status and prooftime by product id.
   */
  public static void updateOrder(int productid, String status, Timestamp proofTime) {
    String query = "UPDATE ordertable SET status = ?, prooftime = ? WHERE productid = ?;";
    try {
      SQLConnector.runUpdateQuery(query, status, proofTime, productid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the proofid of the product by productid.
   */
  public static int getProofIdByProductId(int productId) {
    int proofId = 1;
    try {
      String getProofId = "SELECT proofid FROM ordertable WHERE productid=?;";
      ResultSet rs = SQLConnector.runQuery(getProofId, productId);
      while (rs.next()) {
        proofId = rs.getInt("proofid");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return proofId;
  }

  /**
   * Buyer upload proof.
   */
  public static void updateProof(int productId, int proofId) {
    try {
      String updateProof = "UPDATE ordertable SET proofid=? WHERE productid=?;";
      SQLConnector.runUpdateQuery(updateProof, proofId, productId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get an arraylist of all product id which have a status of "paid".
   */
  public static ArrayList<Integer> getAllPaidProductIds() {
    String query = "SELECT productid FROM ordertable WHERE status = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, FINISHED);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get an arraylist of all product id which have a status of "paid" in order
   * table.
   * 
   * @param desc  the description of the product
   * @param locid the location id of the product
   */
  public static ArrayList<Integer> getAllPaidProductIdsByLocAndDesc(String desc, int locid) {
    String query = "SELECT o.productid " +
        "FROM ordertable o " +
        "left join product p on o.productid = p.productid " +
        "WHERE locationid = " + locid + " AND " +
        "status = ? AND " +
        "textdesc LIKE '%" + desc + "%' " +
        "ORDER BY publishtime DESC;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, FINISHED);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ArrayList<Integer> getAllPaidProductIdsByLoc(int locid) {
    String query = "SELECT o.productid " +
        "FROM ordertable o " +
        "left join product p on o.productid = p.productid " +
        "WHERE locationid = ? AND " +
        "status = ? " +
        "ORDER BY publishtime DESC;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, locid, FINISHED);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ArrayList<Integer> getAllPaidProductIdsByDesc(String desc) {

    String query = "SELECT o.productid " +
        "FROM ordertable o " +
        "left join product p on o.productid = p.productid " +
        "WHERE status = ? AND " +
        "textdesc LIKE '%" + desc + "%' " +
        "ORDER BY publishtime DESC;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, FINISHED);
      ArrayList<Integer> productIds = new ArrayList<Integer>();
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
      return productIds;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
