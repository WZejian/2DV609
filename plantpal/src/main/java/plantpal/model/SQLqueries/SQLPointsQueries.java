package plantpal.model.SQLqueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import plantpal.model.Tree;
import plantpal.model.TreeHistory;

public class SQLPointsQueries {
    public static int getPointsbyId(int userid) {
        int userPoints = -1;
        try {
            String getPoints = "SELECT points FROM userprofile WHERE userid=?;";
            ResultSet rs = SQLConnector.runQuery(getPoints, userid);
            while (rs.next()) {
                // it used to be id, now changed to points
                userPoints = rs.getInt("points");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userPoints;
    }

    /**
     * Get an arraylist of all product id in product tabel.
     */
    public static ArrayList<Integer> getAllTreeIds() {
        String query = "SELECT planid FROM treeplans;";
        try {
            ResultSet rs = SQLConnector.runQuery(query);
            ArrayList<Integer> treeIds = new ArrayList<Integer>();
            while (rs.next()) {
                treeIds.add(rs.getInt("planid"));
            }
            return treeIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Tree> getAllTrees() {
        ArrayList<Integer> treeIds = getAllTreeIds();
        ArrayList<Tree> trees = new ArrayList<Tree>();
        for (int i = 0; i < treeIds.size(); i++) {
            trees.add(getTree(treeIds.get(i)));
        }
        return trees;
    }

    /**
     * Get a tree by tree id.
     */
    public static Tree getTree(int treeid) {
        String query = "SELECT * FROM treeplans WHERE planid = ?;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, treeid);
            Tree tree = new Tree();
            while (rs.next()) {
                tree.setTreeId(rs.getInt("planid"));
                tree.setName(rs.getString("treetitle"));
                tree.setDescription(rs.getString("treedesc"));
                tree.setCredits(rs.getInt("pointsprice"));
                tree.setLocation(rs.getString("location"));
                tree.setImageId(rs.getInt("imageid"));
            }

            return tree;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertTree(int userid, int treeid) {
        try {
            // add tree in database (usertree)
            // certid should be hardcoded with 1
            // date --- NOW(), check post quesry
            String insertTree = "INSERT INTO usertree(userid, treeid, plantdate, certid) "
                    + "VALUES (?, ?, NOW(), 1);";

            SQLConnector.runUpdateQuery(insertTree, userid, treeid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Iterable<TreeHistory> getTreeHistory(int userid) {
        String query = "SELECT * FROM usertree "
                + "INNER JOIN treeplans "
                + "WHERE usertree.userid = ? AND usertree.treeid=treeplans.planid;;";

        ArrayList<TreeHistory> allHis = new ArrayList<>();
        try {
            ResultSet rs = SQLConnector.runQuery(query, userid);

            while (rs.next()) {
                TreeHistory treeHis = new TreeHistory();
                treeHis.setOwner(userid);
                treeHis.setTreeid(rs.getInt("usertree.treeid"));
                treeHis.setPlantDate(rs.getString("usertree.plantdate"));
                treeHis.setTreeName(rs.getString("treeplans.treetitle"));
                treeHis.setTreeImg(SQLGeneralQueries.getImageByImageid(rs.getInt("treeplans.imageid")));
                allHis.add(treeHis);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return allHis;
    }
}
