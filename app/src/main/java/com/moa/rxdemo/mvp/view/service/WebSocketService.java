package com.moa.rxdemo.mvp.view.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import androidx.annotation.Nullable;

import com.moa.baselib.base.net.websocket.WsManager;
import com.moa.baselib.base.net.websocket.WsStatusListener;
import com.moa.baselib.utils.GsonHelper;
import com.moa.baselib.utils.LogUtils;
import com.moa.rxdemo.mvp.bean.WebSocketBean;
import com.moa.rxdemo.net.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 封装WebSocket功能
 * <p>
 * Created by：wangjian on 2018/1/24 16:47
 */
public class WebSocketService extends Service {

    Messenger mServerMessenger = null;
    Messenger mClientMessenger = null;
    private WsManager mWsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mServerMessenger = new Messenger(serviceHandler);
    }

    private void connectWebSocketServer() {
        if (mWsManager != null && mWsManager.isWsConnected()) {
            return;
        }
        mWsManager = new WsManager.Builder(getBaseContext())
                .client(new OkHttpClient()
                        .newBuilder()
                        .pingInterval(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true)
                .wsUrl(Constant.SOCKET_URL)
                .build();

        mWsManager.setWsStatusListener(new WsStatusListener() {

            @Override
            public void onMessage(String text) {
                super.onMessage(text);
                LogUtils.d("onMessage from server: " + text);
                WebSocketBean bean = GsonHelper.toObj(text, WebSocketBean.class);
                if (bean == null) {
                    bean = new WebSocketBean(-1, text + " data pass error");
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", bean);
                // 将服务端发来的数据，解析发送到指定的界面
                sendMsg2Client(mClientMessenger, bundle);
            }
        });
        mWsManager.startConnect();
    }

    private void disConnectWebSocketServer() {
        if (mWsManager != null && mWsManager.isWsConnected()) {
            mWsManager.stopConnect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("onStartCommand");
        connectWebSocketServer();
        return START_STICKY;
    }

    private Handler serviceHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg != null) {
                LogUtils.d("receive client msg.what:" + msg.what);
                // 客户端连接service消息
                if (msg.what == TYPE_CONNECTED) {
                    // 缓存 clientMessenger，用于与client通讯使用
                    mClientMessenger = msg.replyTo;
                } else {
                    // do nothing
                    // test send msg to webSocket server
                    mWsManager.sendMessage("{\"code\":3,\"msg\":\"tttttttttttttttttt\"}");
                }
            }

            return false;
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("onBind");
        return mServerMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
        mClientMessenger = null;
        mServerMessenger = null;
        disConnectWebSocketServer();
    }


    // ======================================================
    private static final int TYPE_CONNECTED = 1;
    private static final int TYPE_MSG_FROM_SERVER = 2;
    private static final int TYPE_MSG_FROM_CLIENT = 3;

    /**
     * 客户端发送已连接到服务的消息
     *
     * @param serviceMessenger 用于client向server发送消息
     * @param clientMessenger 用于server向client发送消息
     */
    public static void sendConnectedMsg(Messenger serviceMessenger, Messenger clientMessenger) {
        Message msg = Message.obtain();
        msg.what = TYPE_CONNECTED;
        // 此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        // Bundle data = new Bundle();
        // data.putString("msg", "你好，MyService，我是客户端");
        // msg.setData(data);
        // 需要将Message的replyTo设置为客户端的clientMessenger，
        // 以便Service可以通过它向客户端发送消息
        msg.replyTo = clientMessenger;
        try {
            serviceMessenger.send(msg);
            LogUtils.d("client connect success");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * server send msg to client
     *
     * @param clientMessenger use for sending msg to client
     * @param bundleMsg msg params
     */
    public static void sendMsg2Client(Messenger clientMessenger, Bundle bundleMsg) {
        Message msg = Message.obtain();
        msg.what = TYPE_MSG_FROM_SERVER;
        // 此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        msg.setData(bundleMsg);
        try {
            clientMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * client send msg to client
     *
     * @param serviceMessenger use for sending msg to server
     * @param bundleMsg msg params
     */
    public static void sendMsg2Server(Messenger serviceMessenger, Bundle bundleMsg) {
        Message msg = Message.obtain();
        msg.what = TYPE_MSG_FROM_CLIENT;
        // 此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
        msg.setData(bundleMsg);
        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, WebSocketService.class);
        context.startService(intent);
    }

    public static void bindService(Context context, ServiceConnection connection) {
        if (context != null && connection != null) {
            Intent intent = new Intent(context, WebSocketService.class);
            context.bindService(intent, connection, BIND_AUTO_CREATE);
        }
    }

    public static void unBindService(Context context, ServiceConnection connection) {
        if (context != null && connection != null) {
            context.unbindService(connection);
        }
    }
}
