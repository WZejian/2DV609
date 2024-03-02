package plantpal.controller;

import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import plantpal.model.Message;
import plantpal.model.SQLqueries.SQLChatQueries;
import plantpal.model.SQLqueries.SQLGeneralQueries;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ChatPageController {
    @FXML
    private VBox contactVbox;

    @FXML
    private VBox historyVbox;

    @FXML
    private TextField idField;

    @FXML
    private Label contentCharLimit;

    @FXML
    private TextArea typeArea;

    @FXML
    private ScrollPane historyScrollpane;

    @FXML
    private Label chattingwithLabel;

    private final int contentMax = 1000;

    private final List<KeyCode> arrowkeys = Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN);

    private int userid;

    private int currentChatWith;

    public ChatPageController(int userid) {
        this.userid = userid;
    }

    @FXML
    private void initialize() {
        contentCharLimit.setText("0/" + contentMax);

        setLeftContacts();

        historyVbox.heightProperty().addListener(observable -> historyScrollpane.setVvalue(1D));
    }

    @FXML
    void contentTyped(KeyEvent event) {
        String content = typeArea.getText();
        if ((!arrowkeys.contains(event.getCode())) && (content.length() >= contentMax)) {
            typeArea.setText(content.substring(0, contentMax));
            typeArea.positionCaret(content.length());
        }
        contentCharLimit.setText(typeArea.getText().length() + "/" + contentMax);

    }

    @FXML
    void sendBtnClick(ActionEvent event) {
        if (!typeArea.getText().equals("")) {
            Message msg = new Message();
            msg.setReceiverId(this.currentChatWith);
            msg.setSenderId(this.userid);
            msg.setContent(typeArea.getText());
            SQLChatQueries.insertMessage(msg);
            typeArea.setText("");
            contentCharLimit.setText("0/" + contentMax);
            refresh();
        }
    }

    @FXML
    void newChatBtnClick(ActionEvent event) {
        if (!idField.getText().equals("")) { // id field is filled
            int typedId = Integer.parseInt(idField.getText());
            if (SQLChatQueries.idExists(typedId) && userid != typedId) { // id exists
                this.currentChatWith = typedId;
                loadAllMsgWith(currentChatWith);
            }
        }
        idField.clear();
    }

    @FXML
    void refreshClick(MouseEvent event) {
        refresh();
    }

    private void setLeftContacts() {
        contactVbox.getChildren().clear();
        for (int contact : SQLChatQueries.getAllContacts(userid)) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/ChatCard.fxml"));
                loader.setController(new ChatCardController(contact));
                contactVbox.getChildren().add((VBox) loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAllMsgWith(int anotherUserid) {
        this.currentChatWith = anotherUserid;
        typeArea.setText("");
        historyVbox.getChildren().clear();
        chattingwithLabel.setText("Chatting with: " + SQLGeneralQueries.getNicknameByid(currentChatWith));
        SQLChatQueries.readNewestMsg(userid, currentChatWith);
        for (Message msg : SQLChatQueries.getMsgByTwoId(userid, currentChatWith)) {
            try {
                if (msg.getSenderId() == userid) { // sent by user
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/ChatMsgMe.fxml"));
                    loader.setController(new ChatMsgMeController(msg));
                    historyVbox.getChildren().add((VBox) loader.load());
                } else { // sent by others
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/ChatMsgOther.fxml"));
                    loader.setController(new ChatMsgOtherController(msg));
                    historyVbox.getChildren().add((VBox) loader.load());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refresh() {
        setLeftContacts();
        loadAllMsgWith(currentChatWith);
    }

    protected class ChatCardController {

        @FXML
        private Label nicknameLabel;

        @FXML
        private ImageView reddotimg;

        @FXML
        private ImageView userImg;

        private int contactId;

        protected ChatCardController(int contactId) {
            this.contactId = contactId;
        }

        @FXML
        private void initialize() {
            if (SQLChatQueries.withUnreadMsg(userid, contactId)) {
                reddotimg.setVisible(true);
            } else {
                reddotimg.setVisible(false);
            }

            userImg.setImage(SQLGeneralQueries.getProfileImg(contactId));
            nicknameLabel.setText(SQLGeneralQueries.getNicknameByid(contactId));
        }

        @FXML
        void contactClick(MouseEvent event) {
            reddotimg.setVisible(false);
            loadAllMsgWith(contactId);
        }

    }

    protected class ChatMsgMeController {

        @FXML
        private Label chatMsgLabel;

        @FXML
        private ImageView userImg;

        @FXML
        private Label timeLabel;

        private Message msg;

        protected ChatMsgMeController(Message msg) {
            this.msg = msg;
        }

        @FXML
        private void initialize() {
            chatMsgLabel.setText(msg.getContent());
            timeLabel.setText(msg.getMsgDate());
            userImg.setImage(SQLGeneralQueries.getProfileImg(msg.getSenderId()));
        }

    }

    protected class ChatMsgOtherController {

        @FXML
        private Label chatMsgLabel;

        @FXML
        private ImageView userImg;

        @FXML
        private Label timeLabel;

        private Message msg;

        protected ChatMsgOtherController(Message msg) {
            this.msg = msg;
        }

        @FXML
        private void initialize() {
            chatMsgLabel.setText(msg.getContent());
            timeLabel.setText(msg.getMsgDate());
            userImg.setImage(SQLGeneralQueries.getProfileImg(msg.getSenderId()));
        }

    }

}
