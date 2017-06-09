package cn.dazhou.railway.im.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by hooyee on 2017/6/8.
 */

@Table(database = RailwayDatabase.class)
public class ChatRoomModel extends BaseModel {
    @PrimaryKey
    String roomJid;
    @Column
    String possessor;

    List<ChatMessageModel> messages;
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "messages")
    public List<ChatMessageModel> getMyMessages() {
        if (messages == null || messages.isEmpty()) {
            messages = SQLite.select()
                    .from(ChatMessageModel.class)
                    .where(ChatMessageModel_Table.roomJid.eq(roomJid))
                    .queryList();
        }
        return messages;
    }

    public String getRoomJid() {
        return roomJid;
    }

    public void setRoomJid(String roomJid) {
        this.roomJid = roomJid;
    }

    public String getPossessor() {
        return possessor;
    }

    public void setPossessor(String possessor) {
        this.possessor = possessor;
    }
}
