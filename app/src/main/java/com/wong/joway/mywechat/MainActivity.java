package com.wong.joway.mywechat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LogPrinter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends Activity {


    private String content;

    private ListView msgListView;

    private EditText inputText;

    private MsgAdapter adapter;

    private List<com.wong.joway.mywechat.Msg> msgList = new ArrayList<>();

    private Thread thread;

    private String request;
    private String response;

    private Handler handler;

    final private int UPDATE = 1000;

    public static void LogPrint(String e){
        Log.d("ErrorLog", e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //UI 初始化
        inputText = (EditText) findViewById(R.id.input_text);
        Button send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);

        // 监听子线程过来的消息以更新UI
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE:
                        adapter.notifyDataSetChanged();//通知UI数据已经更改
                        msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                        break;
                }
            }
        };

        initMsgs();//欢迎语
        adapter = new MsgAdapter(MainActivity.this, R.layout.activity_msg, msgList);//适配器
        msgListView.setAdapter(adapter);

        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);

                    adapter.notifyDataSetChanged();//通知UI数据已经更改
                    msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                    inputText.setText("");//清空输入框中的内容

                    try {
                        //唤起子线程
                        request = content;
                        UpdateMsgThread updateMsgThread = new UpdateMsgThread();
                        thread = new Thread(updateMsgThread);
                        thread.start();
                    } catch (Exception e) {
                        LogPrint(e.toString());
                    }
                }
            }
        });

        msgListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                msgListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(0, 0, 0, "复制");
                    }


                });
                Toast.makeText(MainActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    public class UpdateMsgThread implements Runnable {

        @Override
        public void run() {
            try {
                response = TuringRobot.getResponse(request);
                if(!response.isEmpty()) {
                    Msg re_msg = new Msg(response, Msg.TYPE_RECEIVED);
                    msgList.add(re_msg);
                }
                Message message = new Message();
                message.what = UPDATE;
                handler.sendMessage(message);
                LogPrint(response);
            }
            catch (Exception e){
                LogPrint(e.toString());
            }
        }
    }

    private void initMsgs() {
        Msg msg1 = new Msg("你好呀，我是可爱又迷人的知心大哥————隔壁王:)   欢迎调戏~.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
    }

}
