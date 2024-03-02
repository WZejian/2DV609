package plantpal.model;

public class Order {
  private int productId;
  private int sellerId;
  private int buyerId;
  private String orderTime;
  private String proofTime;
  private String status;
  private int proofId;


  public Order() {
  }

  public Order(int productId, int sellerId, int buyerId, String orderTime, String proofTime, String status, int proofId) {
    this.productId = productId;
    this.sellerId = sellerId;
    this.buyerId = buyerId;
    this.orderTime = orderTime;
    this.proofTime = proofTime;
    this.status = status;
    this.proofId = proofId;
  }

  public int getProductId() {
    return productId;
  }

  public int getSellerId() {
    return sellerId;
  }

  public int getBuyerId() {
    return buyerId;
  }

  public String getOrderTime() {
    return orderTime;
  }

  public String getProofTime() {
    return proofTime;
  }

  public String getStatus() {
    return status;
  }


  public void setProductId(int productId) {
    this.productId = productId;
  }

  public void setSellerId(int sellerId) {
    this.sellerId = sellerId;
  }

  public void setBuyerId(int buyerId) {
    this.buyerId = buyerId;
  }

  public void setOrderTime(String orderTime) {
    this.orderTime = orderTime;
  }

  public void setProofTime(String proofTime) {
    this.proofTime = proofTime;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public int getProofId() {
    return proofId;
  }

  public void setProofId(int proofId) {
    this.proofId = proofId;
  }



}
