package cn.dazhou.railway.im.model;

import org.jivesoftware.smack.roster.RosterEntry;

/**
 * Created by hooyee on 2017/5/17.
 */

public class Friend {
    private RosterEntry rosterEntry;
    private String freshMessage;
    private int unreadCount;

    public Friend(RosterEntry rosterEntry) {
        this.rosterEntry = rosterEntry;
    }

    public RosterEntry getRosterEntry() {
        return rosterEntry;
    }

    public void setRosterEntry(RosterEntry rosterEntry) {
        this.rosterEntry = rosterEntry;
    }

    public String getFreshMessage() {
        return freshMessage;
    }

    public void setFreshMessage(String freshMessage) {
        this.freshMessage = freshMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
