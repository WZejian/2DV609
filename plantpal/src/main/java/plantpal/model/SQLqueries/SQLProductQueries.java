package plantpal.model.SQLqueries;

import java.sql.ResultSet;
import java.util.ArrayList;

import plantpal.model.Product;

public class SQLProductQueries {

  /**
   * Insert a new product into the database.
   */
  public static void insertProduct(int authorid, String price, String textdesc, int locationId, int imageId) {
    String query = "INSERT INTO product (authorid, price, textdesc, locationid, imageid, publishtime, issold) VALUES (?, ?, ?, ?, ?, NOW(), ?);";
    try {
      SQLConnector.runUpdateQuery(query, authorid, price, textdesc, locationId, imageId, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get a product by productid.
   */
  public static Product getProduct(int productid) {
    String query = "SELECT * FROM product WHERE productid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, productid);
      Product product = new Product();
      while (rs.next()) {
        product.setProductId(rs.getInt("productid"));
        product.setAuthorId(rs.getInt("authorid"));
        product.setPrice(rs.getString("price"));
        product.setLocationId(rs.getInt("locationid"));
        product.setPublishTime(rs.getString("publishtime"));
        product.setDescription(rs.getString("textdesc"));
        product.setImageId(rs.getInt("imageid"));
        int soldInfo = rs.getInt("issold");
        Boolean isSold = false;
        if (soldInfo == 1) {
          isSold = true;
        }

        product.setSold(isSold);
      }
      return product;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get an arraylist of all product id in product tabel.
   */
  public static ArrayList<Integer> getAllProductIds() {
    String query = "SELECT productid FROM product;";
    try {
      ResultSet rs = SQLConnector.runQuery(query);
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
   * Get an arraylist of all products in product tabel.
   */
  public static ArrayList<Product> getAllProducts() {
    ArrayList<Integer> productIds = getAllProductIds();
    ArrayList<Product> products = new ArrayList<Product>();
    for (int i = 0; i < productIds.size(); i++) {
      products.add(getProduct(productIds.get(i)));
    }
    return products;
  }

  /**
   * Delete a product by productid.
   */
  public static void deleteProductById(int productid) {
    String query = "DELETE FROM product WHERE productid = ?;";
    try {
      SQLConnector.runUpdateQuery(query, productid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if a product exists by productid.
   */
  public static boolean productExists(int productid) {
    String query = "SELECT * FROM product WHERE productid = ?;";
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

  // Check if a product exists by user id and publish time.
  public static boolean productExists(int authorid, String textdesc, String priceInfo) {
    String query = "SELECT * FROM product WHERE authorid = ? AND textdesc = ? AND price = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, authorid, textdesc, priceInfo);
      if (rs.next()) {
        return true;

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Delete a product by productid.
   */
  public static void deleteProductByproductid(int productId) {
    String query = "DELETE FROM product WHERE productid = ?;";
    try {
      SQLConnector.runQuery(query, productId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get all product id in the product table.
   */
  public static ArrayList<Integer> getAllProductId() {
    ArrayList<Integer> allProductId = new ArrayList<>();

    try {
      String query = "SELECT * FROM product ORDER BY publishtime DESC;";
      ResultSet rs = SQLConnector.runQuery(query);
      while (rs.next()) {
        allProductId.add(rs.getInt("productid"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return allProductId;
  }

  /**
   * Check if a product is sold by productid.
   */
  public static boolean isSold(int productid) {
    String query = "SELECT issold FROM product WHERE productid = ?;";
    try {
      ResultSet rs = SQLConnector.runQuery(query, productid);
      if (rs.next()) {
        if (rs.getInt("issold") == 1) {
          return true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Update a product's sold status by productid.
   */
  public static void updateSoldStatus(int productid) {
    int soldStatus = 1;
    String query = "UPDATE product SET issold = ? WHERE productid = ?;";
    try {
      SQLConnector.runUpdateQuery(query, soldStatus, productid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get an arraylist of all product id which has a value of 0 in issold column.
   */
  public static ArrayList<Integer> getAllUnsoldProductIds() {
    String query = "SELECT productid FROM product WHERE issold = 0;";
    try {
      ResultSet rs = SQLConnector.runQuery(query);
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
   * Get an arraylist of all product id which are only in product table but not in
   * ordertable.
   */
  public static ArrayList<Integer> getAllProductIdsNotInOrder() {
    String query = "SELECT productid FROM product WHERE productid NOT IN (SELECT productid FROM "
        + "ordertable);";
    try {
      ResultSet rs = SQLConnector.runQuery(query);
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
   * Get an arraylist of all product id which are only in product table but not in
   * ordertable.
   * 
   * @param desc  text description of the product
   * @param locid location id
   * @return
   */
  public static ArrayList<Integer> searchByLocAndDesc(String desc, int locid) {
    ArrayList<Integer> productIds = new ArrayList<>();
    try {
      String query = "SELECT productid " +
          "FROM product " +
          "WHERE locationid = " + locid + " AND " +
          "textdesc LIKE '%" + desc + "%' " +
          "AND productid NOT IN (SELECT productid FROM ordertable) " +
          "ORDER BY publishtime DESC;";
      ResultSet rs = SQLConnector.runQuery(query);
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return productIds;
  }

    public static ArrayList<Integer> searchByLoc(int locid) {
      ArrayList<Integer> productIds = new ArrayList<>();
      try {
        String query = "SELECT productid " +
            "FROM product " +
            "WHERE locationid = ? " +
            "AND productid NOT IN (SELECT productid FROM ordertable) " +
            "ORDER BY publishtime DESC;";
        ResultSet rs = SQLConnector.runQuery(query, locid);
        while (rs.next()) {
          productIds.add(rs.getInt("productid"));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    return productIds;
  }

  public static ArrayList<Integer> searchByDesc(String desc) {
    ArrayList<Integer> productIds = new ArrayList<>();
    try {
      String query = "SELECT productid " +
          "FROM product " +
          "Where textdesc LIKE '%" + desc + "%' " +
          "AND productid NOT IN (SELECT productid FROM ordertable) " +
          "ORDER BY publishtime DESC;";
      ResultSet rs = SQLConnector.runQuery(query);
      while (rs.next()) {
        productIds.add(rs.getInt("productid"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return productIds;
  }

}
