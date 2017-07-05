package cn.dazhou.im.entity;

/**
 * Created by hooyee on 2017/7/5.
 */

public class ProcessEvent {
    String filePath;
    int process;

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
}
