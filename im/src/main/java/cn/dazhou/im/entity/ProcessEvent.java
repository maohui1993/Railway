package cn.dazhou.im.entity;

/**
 * Created by hooyee on 2017/7/5.
 */

public class ProcessEvent {
    String filePath;
    int process;
    int type;
    long date;
    ChatMessageEntity.Type dataType;

    public ProcessEvent(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ChatMessageEntity.Type getDataType() {
        return dataType;
    }

    public void setDataType(ChatMessageEntity.Type dataType) {
        this.dataType = dataType;
    }
}
