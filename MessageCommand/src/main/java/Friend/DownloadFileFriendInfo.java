package Friend;

import Message.Message;

public class DownloadFileFriendInfo implements Message {
    private String nickFriend;
    private String nameFile;
    private long fileSize;

    public DownloadFileFriendInfo(String nameFile, String nickFriend) {
        this.nameFile = nameFile;
        this.nickFriend = nickFriend;
    }

    public DownloadFileFriendInfo(String nameFile, long fileSize) {
        this.nameFile = nameFile;
        this.fileSize = fileSize;
    }

    public String getNameFile() {
        return nameFile;
    }

    public String getNickFriend() {
        return nickFriend;
    }

    public long getFileSize() {
        return fileSize;
    }
}
