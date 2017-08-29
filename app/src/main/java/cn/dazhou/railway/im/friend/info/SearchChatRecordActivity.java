package cn.dazhou.railway.im.friend.info;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.dazhou.database.ChatMessageModel;
import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.chat.ChatActivity;

public class SearchChatRecordActivity extends AppCompatActivity {
    private static final String EXTRA_DATA = "_DATA";

    CalendarPickerView calendar;

    private String jid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat_record);

        jid = getIntent().getStringExtra(EXTRA_DATA);

        Calendar lastMonth = Calendar.getInstance(Locale.CHINA);
        lastMonth.add(Calendar.MONTH, -1);
        lastMonth.set(Calendar.DAY_OF_MONTH, 1);

        Calendar nextDay = Calendar.getInstance(Locale.CHINA);
        nextDay.add(Calendar.DATE, 1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(lastMonth.getTime(), nextDay.getTime(), Locale.CHINA)
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(today);

        List<CalendarCellDecorator> decorators = new ArrayList<>();
        CalendarCellDecorator cellDecorator = new CalendarCellDecorator() {
            @Override
            public void decorate(CalendarCellView cellView, Date date) {
                if (!DataHelper.hasMessagesInTimeBucket(jid, date.getTime(), date.getTime() + 24 * 3600 * 1000)) {
                    cellView.setClickable(false);
                    cellView.setSelectable(false);
                }
            }
        };
        decorators.add(cellDecorator);
        calendar.setDecorators(decorators);
        calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                Toast.makeText(SearchChatRecordActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
//                List<ChatMessageModel> chats = DataHelper.getChatMessages(jid, date);

                ChatActivity.startItself(SearchChatRecordActivity.this, jid, date);
                return false;
            }
        });

    }

    public static void startItself(Context context, String jid) {
        Intent intent = new Intent(context, SearchChatRecordActivity.class);
        intent.putExtra(EXTRA_DATA, jid);
        context.startActivity(intent);
    }
}
