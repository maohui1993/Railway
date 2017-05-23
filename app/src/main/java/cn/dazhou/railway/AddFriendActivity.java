//package cn.dazhou.railway;
//
//
//
//        import org.jivesoftware.smack.PacketListener;
//        import org.jivesoftware.smack.XMPPConnection;
//        import org.jivesoftware.smack.XMPPException;
//        import org.jivesoftware.smack.filter.AndFilter;
//        import org.jivesoftware.smack.filter.PacketFilter;
//        import org.jivesoftware.smack.filter.PacketTypeFilter;
//        import org.jivesoftware.smack.packet.Packet;
//        import org.jivesoftware.smack.packet.Presence;
//        import org.jivesoftware.smack.roster.Roster;
//
//
//        import android.app.Activity;
//        import android.app.AlertDialog;
//        import android.app.AlertDialog.Builder;
//        import android.app.ProgressDialog;
//        import android.content.BroadcastReceiver;
//        import android.content.Context;
//        import android.content.DialogInterface;
//        import android.content.Intent;
//        import android.content.IntentFilter;
//        import android.graphics.drawable.Drawable;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.os.Message;
//        import android.view.View;
//        import android.view.View.OnClickListener;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.ImageView;
//        import android.widget.TextView;
//
///*
// *@author  Eric
// *@2015-9-7上午9:28:52
// */
//public class AddFriendActivity extends Activity {
//    private EditText edit_addfriend;
//    private Button btn_searchfriend;
//    private String name,password,response,acceptAdd,alertName,alertSubName;
//    private ImageView img_searchFriend,img_addFriend;
//    private TextView text_searchFriend,text_response;
//    private Roster roster;
//    private XMPPConnection con = ConnectServer.ConnectServer();
//    private static ProgressDialog dialog;
//    private AddFriendHandler handler;
//    private MyReceiver receiver;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_friend);
//
//        edit_addfriend = (EditText) findViewById(R.id.edit_addfriend);
//        btn_searchfriend = (Button) findViewById(R.id.btn_searchfriend);
//        img_searchFriend = (ImageView) findViewById(R.id.img_seachFriend);
//        img_addFriend = (ImageView) findViewById(R.id.img_addFriend);
//        text_searchFriend = (TextView) findViewById(R.id.text_searchFriend);
//        text_response = (TextView) findViewById(R.id.text_response);
//
//        name = getIntent().getStringExtra("name");
//        password = getIntent().getStringExtra("password");
//
//        roster = con.getRoster();
//        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
//        try {
//            con.login(name, password);
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        }
//
//        //注册广播
//        receiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.example.eric_jqm_chat.AddFriendActivity");
//        registerReceiver(receiver, intentFilter);
//
//
//        if(con!=null&&con.isConnected()&&con.isAuthenticated()){
//
//            //条件过滤器
//            PacketFilter filter = new AndFilter(new PacketTypeFilter(Presence.class));
//            //packet监听器
//            PacketListener listener = new PacketListener() {
//
//                @Override
//                public void processPacket(Packet packet) {
//                    System.out.println("PresenceService-"+packet.toXML());
//                    if(packet instanceof Presence){
//                        Presence presence = (Presence)packet;
//                        String from = presence.getFrom();//发送方
//                        String to = presence.getTo();//接收方
//                        if (presence.getType().equals(Presence.Type.subscribe)) {
//                            System.out.println("收到添加请求！");
//                            //发送广播传递发送方的JIDfrom及字符串
//                            acceptAdd = "收到添加请求！";
//                            Intent intent = new Intent();
//                            intent.putExtra("fromName", from);
//                            intent.putExtra("acceptAdd", acceptAdd);
//                            intent.setAction("com.example.eric_jqm_chat.AddFriendActivity");
//                            sendBroadcast(intent);
//                        } else if (presence.getType().equals(
//                                Presence.Type.subscribed)) {
//                            //发送广播传递response字符串
//                            response = "恭喜，对方同意添加好友！";
//                            Intent intent = new Intent();
//                            intent.putExtra("response", response);
//                            intent.setAction("com.example.eric_jqm_chat.AddFriendActivity");
//                            sendBroadcast(intent);
//                        } else if (presence.getType().equals(
//                                Presence.Type.unsubscribe)) {
//                            //发送广播传递response字符串
//                            response = "抱歉，对方拒绝添加好友，将你从好友列表移除！";
//                            Intent intent = new Intent();
//                            intent.putExtra("response", response);
//                            intent.setAction("com.example.eric_jqm_chat.AddFriendActivity");
//                            sendBroadcast(intent);
//                        } else if (presence.getType().equals(
//                                Presence.Type.unsubscribed)){
//                        } else if (presence.getType().equals(
//                                Presence.Type.unavailable)) {
//                            System.out.println("好友下线！");
//                        } else {
//                            System.out.println("好友上线！");
//                        }
//                    }
//                }
//            };
//            //添加监听
//            con.addPacketListener(listener, filter);
//        }
//
//
//        btn_searchfriend.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                //从服务器查询用户头像，并没有进行搜索，自行修改
//                Drawable drawable = new Login()
//                        .getUserImage(con, edit_addfriend.getText().toString());
//                if(drawable != null){
//                    img_searchFriend.setImageDrawable(drawable);
//                    text_searchFriend.setText(edit_addfriend.getText().toString());
//                }else{
//                    text_searchFriend.setText("抱歉，未找到该用户");
//                }
//
//            }
//        });
//
//        img_addFriend.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//                if(dialog == null){
//                    dialog = new ProgressDialog(AddFriendActivity.this);
//                }
//                dialog.setTitle("请等待");
//                dialog.setMessage("正在发送好友申请...");
//                dialog.setCancelable(true);
//                dialog.show();
//                //添加好友的副线程
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                            String friendName = edit_addfriend.getText().toString();
//                            boolean result = addFriend(roster, friendName, friendName);
//                            Message msg = new Message();
//                            Bundle b = new Bundle();
//                            b.putBoolean("result", result);
//                            msg.setData(b);
//                            handler.sendMessage(msg);
//                        } catch (Exception e) {
//                            System.out.println("申请发生异常！！");
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                //启动线程和实例化handler
//                thread.start();
//                handler = new AddFriendHandler();
//
//            }
//        });
//
//    }
//
//    //handler更新UI线程
//    public class AddFriendHandler extends Handler{
//        @Override
//        public void handleMessage(Message msg) {
//
//            if(dialog != null){
//                dialog.setMessage("发送成功！");
//                dialog.dismiss();
//            }
//            Bundle b = msg.getData();
//            Boolean res = b.getBoolean("result");
//            if (res == true){
//                System.out.println("button发送添加好友请求成功！！");
//            }
//
//
//        }
//    }
//
//    //添加好友
//    public  boolean addFriend(Roster roster,String friendName,String name){
//
//        try {
//            roster.createEntry(friendName.trim()+"@eric-pc", name, new String[]{"Friends"});
//            System.out.println("添加好友成功！！");
//            return true;
//        } catch (XMPPException e) {
//            e.printStackTrace();
//
//            System.out.println("失败！！"+e);
//            return false;
//        }
//    }
//
//
//    //广播接收器
//    public class MyReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //接收传递的字符串response
//            Bundle bundle = intent.getExtras();
//            response = bundle.getString("response");
//            System.out.println("广播收到"+response);
//            text_response.setText(response);
//            if(response==null){
//                //获取传递的字符串及发送方JID
//                acceptAdd = bundle.getString("acceptAdd");
//                alertName = bundle.getString("fromName");
//                if(alertName!=null){
//                    //裁剪JID得到对方用户名
//                    alertSubName = alertName.substring(0,alertName.indexOf("@"));
//                }
//                if(acceptAdd.equals("收到添加请求！")){
//                    //弹出一个对话框，包含同意和拒绝按钮
//                    AlertDialog.Builder builder  = new Builder(AddFriendActivity.this);
//                    builder.setTitle("添加好友请求" ) ;
//                    builder.setMessage("用户"+alertSubName+"请求添加你为好友" ) ;
//                    builder.setPositiveButton("同意",new DialogInterface.OnClickListener() {
//                        //同意按钮监听事件，发送同意Presence包及添加对方为好友的申请
//                        @Override
//                        public void onClick(DialogInterface dialog, int arg1) {
//                            Presence presenceRes = new Presence(Presence.Type.subscribed);
//                            presenceRes.setTo(alertName);
//                            con.sendPacket(presenceRes);
//
//                            addFriend(roster, alertSubName, alertSubName);
//                        }
//                    });
//                    builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
//                        //拒绝按钮监听事件，发送拒绝Presence包
//                        @Override
//                        public void onClick(DialogInterface dialog, int arg1) {
//                            Presence presenceRes = new Presence(Presence.Type.unsubscribe);
//                            presenceRes.setTo(alertName);
//                            con.sendPacket(presenceRes);
//                        }
//                    });
//                    builder.show();
//                }
//            }
//
//        }
//
//    }
//
//
//
//}
//
