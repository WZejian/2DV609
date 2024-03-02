package plantpal.model;

import javafx.scene.image.Image;

public class Proof {
  private int proofId;
  private int productId;
  private int uploaderId;
  private Image proofImg;

  public Proof(int proofId, int productId, int uploaderId, Image proofImg) {
    this.proofId = proofId;
    this.productId = productId;
    this.uploaderId = uploaderId;
    this.proofImg = proofImg;
  }

  public int getProofId() {
    return proofId;
  }

  public void setProofId(int proofId) {
    this.proofId = proofId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getUploaderId() {
    return uploaderId;
  }

  public void setUploaderId(int uploaderId) {
    this.uploaderId = uploaderId;
  }

  public Image getProofImg() {
    return proofImg;
  }

  public void setProofImg(Image proofImg) {
    this.proofImg = proofImg;
  }
}
