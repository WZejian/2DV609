package plantpal.model;

public class Product {
  private int productId;
  private int authoridId;
  private String price;
  private String description;
  private int locationId;
  private int imageId;
  private String publishTime;
  private Boolean isSold;

  public Product() {
  }

  public Product(int productid) {
    this.productId = productid;
  }

  public Product(int authoridId, String priceInfo, String description, int locationId, int imageId, String publishTime) {
    this.authoridId = authoridId;
    this.price = priceInfo;
    this.description = description;
    this.locationId = locationId;
    this.imageId = imageId;
    this.publishTime = publishTime;
    this.isSold = false;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productid) {
    this.productId = productid;
  }

  public int getAuthorId() {
    return authoridId;
  }

  public void setAuthorId(int authoridId) {
    this.authoridId = authoridId;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }

  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }

  public String getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(String publishTime) {
    this.publishTime = publishTime;
  }

  public Boolean isSold() {
    return isSold;
  }

  public void setSold(Boolean sold) {
    isSold = sold;
  }


}
