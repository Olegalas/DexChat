package ua.dexchat.model;

/**
 * Created by dexter on 14.04.16.
 */
public class FileMessage {

    public String fileName;
    public String fileType;
    public int idSender;
    public int idReceiver;

    public FileMessage(String fileName, String filetype, int idSender, int idReceiver) {
        this.fileName = fileName;
        this.fileType = filetype;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", idSender=" + idSender +
                ", idReceiver=" + idReceiver +
                '}';
    }
}
