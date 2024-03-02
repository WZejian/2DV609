package plantpal.model.SQLqueries;

import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.scene.image.Image;
import plantpal.model.Comment;
import plantpal.model.ImageHandler;
import plantpal.model.Post;

/**
 * Finsihed! --Meichen
 */
public class SQLPostQueries {
    /**
     * get the number of likes of a post
     * 
     * @param postid post id
     */
    public static int getPostLikeCount(int postid) {
        int likeCount = 0;

        try {
            String query = "SELECT COUNT(*) FROM postlike WHERE postid = ?;";
            ResultSet rs = SQLConnector.runQuery(query, postid);
            while (rs.next()) {
                likeCount = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return likeCount;
    }

    /**
     * return a post object by postid.
     */
    public static Post getPostByPostid(int postid) {
        Post post = new Post(postid);
        try {
            String postQuery = "SELECT * FROM post WHERE id = ?;";
            // get data from table post
            ResultSet rsPost = SQLConnector.runQuery(postQuery, postid);
            while (rsPost.next()) {
                post.setAuthorid(rsPost.getInt("authorid"));
                post.setDate(rsPost.getString("postdate"));
                post.setTitle(rsPost.getString("title"));
                post.setTextContent(rsPost.getString("postcontent"));
            }

            // get data from table postimg
            String postImgQuery = "SELECT * FROM postimg " +
                    "INNER JOIN image " +
                    "ON postimg.imageid = image.id " +
                    "WHERE postimg.postid = ? " +
                    "ORDER BY postimg.imageid;";
            ResultSet rsPostImage = SQLConnector.runQuery(postImgQuery, postid);
            while (rsPostImage.next()) {
                String picId = rsPostImage.getString("image.id");
                Blob picBlob = rsPostImage.getBlob("image.imageblob");
                Image img = ImageHandler.getFileFromBlob(picId, picBlob);
                post.addToImageLst(img);
            }

            // get data from table posttag
            String postTagQuery = "SELECT * FROM posttag " +
                    "INNER JOIN tag " +
                    "ON posttag.tagid = tag.tagid " +
                    "WHERE posttag.postid = ?;";
            ResultSet rsPostTag = SQLConnector.runQuery(postTagQuery, postid);
            while (rsPostTag.next()) {
                post.addToTagLst(rsPostTag.getString("tag.tagname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Get all post id in database.
     * 
     * @return a list of post id.
     */
    public static ArrayList<Integer> getAllPostid() {
        ArrayList<Integer> allPostId = new ArrayList<>();

        try {
            String query = "SELECT id FROM post ORDER BY postdate DESC;";
            ResultSet rs = SQLConnector.runQuery(query);
            while (rs.next()) {
                allPostId.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allPostId;
    }

    public static int getAuthoridByPostid(int postid) {
        int authorid = -1;
        try {
            String query = "SELECT * FROM post WHERE id = ?;";
            ResultSet rs = SQLConnector.runQuery(query, postid);
            while (rs.next()) {
                authorid = rs.getInt("authorid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorid;
    }

    public static Iterable<Comment> getAllComments(int postid) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            String query = "SELECT * FROM comments " +
                    "INNER JOIN postcomment " +
                    "ON postcomment.commentid = comments.commentid " +
                    "WHERE postcomment.postid = ? " +
                    "ORDER BY comments.commentdate;";
            ResultSet rs = SQLConnector.runQuery(query, postid);
            while (rs.next()) {
                Comment oneComment = new Comment();
                oneComment.setCommentid(rs.getInt("comments.commentid"));
                oneComment.setAuthorid(rs.getInt("comments.authorid"));
                oneComment.setCommentTime(rs.getString("comments.commentdate"));
                oneComment.setCommentText(rs.getString("comments.content"));
                oneComment.setAuthorImage(SQLGeneralQueries.getProfileImg(oneComment.getAuthorid()));
                oneComment.setNickname(SQLGeneralQueries.getNicknameByid(oneComment.getAuthorid()));
                comments.add(oneComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    public static int getCommentCount(int postid) {
        int commentCount = 0;
        try {
            String query = "SELECT COUNT(*) FROM postcomment WHERE postid = ?;";
            ResultSet rs = SQLConnector.runQuery(query, postid);
            while (rs.next()) {
                commentCount = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentCount;
    }

    public static void insertComment(int postid, int authorid, String content) {
        try {
            String insertComment = "INSERT INTO comments(authorid, commentdate, content) "
                    + "VALUES (?, NOW(), ?);";
            SQLConnector.runUpdateQuery(insertComment, authorid, content);

            int commentid = -1;

            String getCommentid = "SELECT max(commentid) FROM comments WHERE authorid = ?;";
            ResultSet rs = SQLConnector.runQuery(getCommentid, authorid);
            while (rs.next()) {
                commentid = rs.getInt(1);
            }

            String insertPostcomment = "INSERT INTO postcomment(postid, commentid) "
                    + "VALUES (?, ?);";
            SQLConnector.runUpdateQuery(insertPostcomment, postid, commentid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteComment(int commentid) {
        try {
            String deleteComment = "DELETE FROM comments WHERE commentid = ?;";
            SQLConnector.runUpdateQuery(deleteComment, commentid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * see if the post is already liked by a user.
     * 
     * @param postid post.id
     * @param userid userprofile.userid
     * @return true or false
     */
    public static boolean postIsLikedByUser(int postid, int userid) {
        int rowCount = 0;

        try {
            String query = "SELECT COUNT(*) FROM postlike WHERE postid = ? AND userid = ?;";
            ResultSet rs = SQLConnector.runQuery(query, postid, userid);
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
     * see if the post is already saved by a user.
     * 
     * @param postid post.id
     * @param userid userprofile.userid
     * @return true or false
     */
    public static boolean postIsSavedByUser(int postid, int userid) {
        int rowCount = 0;

        try {
            String query = "SELECT COUNT(*) FROM postsave WHERE postid = ? AND userid = ?;";
            ResultSet rs = SQLConnector.runQuery(query, postid, userid);
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

    public static void likeOnePost(int postid, int userid) {
        try {
            String insertlike = "INSERT INTO postlike(postid, userid) VALUES (?, ?);";
            SQLConnector.runUpdateQuery(insertlike, postid, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unlikeOnePost(int postid, int userid) {
        try {
            String dellike = "DELETE FROM postlike WHERE postid=? AND userid=?;";
            SQLConnector.runUpdateQuery(dellike, postid, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOnePost(int postid, int userid) {
        try {
            String insertsave = "INSERT INTO postsave(postid, userid) VALUES (?, ?);";
            SQLConnector.runUpdateQuery(insertsave, postid, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unsaveOnePost(int postid, int userid) {
        try {
            String delsave = "DELETE FROM postsave WHERE postid=? AND userid=?;";
            SQLConnector.runUpdateQuery(delsave, postid, userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a post into plantpal.post table.
     * also insert into postimg and posttag if needed.
     */
    public static void insertPost(Post post) {
        try {
            // insert into post table
            String insertpost = "INSERT INTO post(authorid, postdate, title, postcontent) VALUES (?, NOW(), ?, ?);";
            SQLConnector.runUpdateQuery(insertpost, post.getAuthorid(), post.getTitle(), post.getTextContent());

            // get post id
            int postid = -1;
            String getPostid = "SELECT max(id) FROM post WHERE authorid = ?;";
            ResultSet rs = SQLConnector.runQuery(getPostid, post.getAuthorid());
            while (rs.next()) {
                postid = rs.getInt(1);
            }

            // insert images
            for (Image img : post.getAllImages()) {
                int imgid = SQLGeneralQueries.insertImage(img, post.getAuthorid());

                String insertPostimg = "INSERT INTO postimg(postid, imageid) VALUES (?, ?);";
                SQLConnector.runUpdateQuery(insertPostimg, postid, imgid);
            }

            // insert tags
            for (String tagStr : post.getTagLst()) {
                int tagid = SQLGeneralQueries.getTagidByTagname(tagStr);

                String insertPosttag = "INSERT INTO posttag(postid, tagid) VALUES (?, ?);";
                SQLConnector.runUpdateQuery(insertPosttag, postid, tagid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deletePost(int postid) {
        try {
            String delPost = "DELETE FROM post WHERE id=?;";
            SQLConnector.runUpdateQuery(delPost, postid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> searchByTitle(String content) {
        ArrayList<Integer> resPostId = new ArrayList<>();
        try {
            String query = "SELECT id FROM post " +
                    "WHERE title LIKE '%" + content + "%' " +
                    "ORDER BY postdate DESC;";
            ResultSet rs = SQLConnector.runQuery(query);
            while (rs.next()) {
                resPostId.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPostId;
    }

    public static ArrayList<Integer> searchByAuthor(String content) {
        ArrayList<Integer> resPostId = new ArrayList<>();
        try {
            String query = "SELECT post.id FROM post " +
                    "INNER JOIN userprofile " +
                    "WHERE post.authorid = userprofile.userid AND " +
                    "userprofile.nickname LIKE '%" + content + "%' " +
                    "ORDER BY postdate DESC;";
            ResultSet rs = SQLConnector.runQuery(query);
            while (rs.next()) {
                resPostId.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPostId;
    }

}
