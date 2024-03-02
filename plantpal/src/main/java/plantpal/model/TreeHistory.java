package plantpal.model;

import javafx.scene.image.Image;

public class TreeHistory {
  private int owner;
  private int treeid;
  private Image treeImg;
  private String plantDate;
  private String treeName;

  public int getOwner() {
    return this.owner;
  }

  public void setOwner(int owner) {
    this.owner = owner;
  }

  public int getTreeid() {
    return this.treeid;
  }

  public void setTreeid(int treeid) {
    this.treeid = treeid;
  }

  public Image getTreeImg() {
    return this.treeImg;
  }

  public void setTreeImg(Image treeImg) {
    this.treeImg = treeImg;
  }

  public String getPlantDate() {
    return this.plantDate;
  }

  public void setPlantDate(String plantDate) {
    this.plantDate = plantDate;
  }

  public String getTreeName() {
    return this.treeName;
  }

  public void setTreeName(String treeName) {
    this.treeName = treeName;
  }


}