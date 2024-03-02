package plantpal.model.SQLqueries;

import java.sql.ResultSet;
import java.util.ArrayList;

import plantpal.model.Message;

public class SQLChatQueries {
    public static Iterable<Integer> getAllContacts(int userid) {
        ArrayList<Integer> contactsLst = new ArrayList<>();
        String query = "SELECT receiverid, senderid, msgdate FROM chatmsg WHERE receiverid = ? OR senderid = ? ORDER BY msgdate DESC;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, userid, userid);
            while (rs.next()) {
                int receiverid = rs.getInt("receiverid");
                int senderid = rs.getInt("senderid");

                if (!contactsLst.contains(receiverid) && receiverid != userid) {
                    contactsLst.add(receiverid);
                }

                if (!contactsLst.contains(senderid) && senderid != userid) {
                    contactsLst.add(senderid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactsLst;
    }

    /**
     * Check if the newest message is read.
     */
    public static boolean withUnreadMsg(int receiver, int sender) {
        String query = "SELECT receiverid, senderid, msgdate, isread FROM chatmsg WHERE senderid = ? AND receiverid = ? ORDER BY msgdate DESC LIMIT 1;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, sender, receiver);
            while (rs.next()) {
                if ((rs.getInt("isread")) == 0) {       // unread Msg exists
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Only updates the first row of history.
     */
    public static void readNewestMsg(int receiver, int sender) {
        try {
            String selectNewestMsg = "SELECT msgid FROM chatmsg WHERE senderid = ? AND receiverid = ? ORDER BY msgdate DESC LIMIT 1;";
            ResultSet rs = SQLConnector.runQuery(selectNewestMsg, sender, receiver);
            while (rs.next()) {
                String updateIsRead = "UPDATE chatmsg SET isread = 1 WHERE msgid = ?";
                SQLConnector.runUpdateQuery(updateIsRead, rs.getInt("msgid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Get all messages between two users.
     */
    public static Iterable<Message> getMsgByTwoId(int user1, int user2) {
        ArrayList<Message> msgLst = new ArrayList<>();
        String query = "SELECT * FROM chatmsg WHERE (senderid = ? AND receiverid = ?) OR (senderid = ? AND receiverid = ?) ORDER BY msgdate;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, user1, user2, user2, user1);
            while (rs.next()) {
                Message oneMsg = new Message();
                oneMsg.setMsgId(rs.getInt("msgid"));
                oneMsg.setReceiverId(rs.getInt("receiverid"));
                oneMsg.setSenderId(rs.getInt("senderid"));
                oneMsg.setMsgDate(rs.getString("msgdate"));
                oneMsg.setContent(rs.getString("content"));
                msgLst.add(oneMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgLst;
    }

    /**
     * Insert a message to db.
     */
    public static void insertMessage(Message msg) {
        try {
            String insertMsg = "INSERT INTO chatmsg(receiverid, senderid, msgdate, content) VALUES (?, ?, NOW(), ?);";
            SQLConnector.runUpdateQuery(insertMsg, msg.getReceiverId(), msg.getSenderId(), msg.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Check if the id entered existed.
     */
    public static boolean idExists(int userid) {
        String query = "SELECT userid FROM userprofile WHERE userid = ?;";
        try {
            ResultSet rs = SQLConnector.runQuery(query, userid);
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
