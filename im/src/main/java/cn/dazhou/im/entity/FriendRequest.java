package cn.dazhou.im.entity;

/**
 * Created by hooyee on 2017/5/24.
 */

public class FriendRequest {
    private String jid;
    private Type type;

    public FriendRequest(String jid, Type type) {
        this.jid = jid;
        this.type = type;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {

        available,

        unavailable,

        subscribe,

        subscribed,

        unsubscribe,

        unsubscribed,

        error,

        probe,
    }

    public static class RequestResult {
        public Result result;
        public String jid;

        public RequestResult(String jid, Result result) {
            this.jid = jid;
            this.result = result;
        }
    }
    public enum Result {
        ACCEPT,
        REJECT
    }
}
