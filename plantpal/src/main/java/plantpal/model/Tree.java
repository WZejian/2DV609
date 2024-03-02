package plantpal.model;

public class Tree {

  private int treeId;
  private String treeName; 
  private String description;
  private int credits;
  private String location;
  private int imageId;


  public Tree() {

  }

  Tree(int treeId) {
  }

  public void setTreeId(int treeId){
    this.treeId = treeId;
  }

  public int getTreeId(){
    return treeId;
  }

  public void setName(String name) {
    this.treeName = name;
  }

  public String getTreeName() {
    return treeName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  public int getCredits() {
    return credits;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getLocation(){
    return location;
  }

  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }


}
