package cn.dazhou.im.core.function;

import org.jivesoftware.smack.roster.Roster;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public interface IChat {
    void chatWith(String jid, String info) throws Exception;

    Roster getRoster();
}
