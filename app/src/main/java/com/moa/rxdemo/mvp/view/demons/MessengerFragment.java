package com.moa.rxdemo.mvp.view.demons;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.View;

import com.moa.baselib.base.ui.BaseFragment;
import com.moa.baselib.utils.LogUtils;
import com.moa.rxdemo.R;
import com.moa.rxdemo.mvp.bean.ProvinceItem;
import com.moa.rxdemo.mvp.bean.WebSocketBean;
import com.moa.rxdemo.mvp.view.service.WebSocketService;

import org.jetbrains.annotations.NotNull;

/**
 * 类或文件描述
 * <p>
 * Created by：wangjian on 2017/12/22 14:40
 */
public class MessengerFragment extends BaseFragment {
    
    //用于启动MyService的Intent对应的action
    private static final String SERVICE_ACTION = "com.rxdemo.action.MYSERVICE";
    private static final int SEND_MESSAGE_CODE = 0x0001;
    
    private static final int RECEIVE_MESSAGE_CODE = 0x0002;
    
    private Messenger clientMessenger;
    private boolean isBound = false;
    
   
    
    //serviceMessenger表示的是Service端的Messenger，其内部指向了MyService的ServiceHandler实例
    //可以用serviceMessenger向MyService发送消息
    private Messenger serviceMessenger = null;
    
    //clientMessenger是客户端自身的Messenger，内部指向了ClientHandler的实例
    //MyService可以通过Message的replyTo得到clientMessenger，从而MyService可以向客户端发送消息，
    //并由ClientHandler接收并处理来自于Service的消息
    
    public MessengerFragment() {
        Handler clientHandler = new Handler(msg -> {
            WebSocketBean item = (WebSocketBean) msg.getData().getSerializable("data");
            LogUtils.d("接收到来自service的消息：" + item.msg);
            return false;
        });
        clientMessenger = new Messenger(clientHandler);
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.tt_fragment_messenger;
    }
    
    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);

        findViewById(R.id.bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单击了bindService按钮
                if (!isBound) {
                    LogUtils.d("客户端调用bindService方法");
                    isBound = true;
                    WebSocketService.bindService(getActivity(), conn);
                }
            }
        });
        
        findViewById(R.id.unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单击了unbindService按钮
                if (isBound) {
                    LogUtils.d("客户端调用unbindService方法");
                    isBound = false;
                    WebSocketService.unBindService(getActivity(), conn);
                }
            }
        });
        findViewById(R.id.sendmsg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", new ProvinceItem(2, "xxxxxxxxxxxxxxxxxx"));
                    WebSocketService.sendMsg2Server(serviceMessenger, bundle);
                }
            }
        });
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //客户端与Service建立连接
            Log.i("DemoLog", "客户端 onServiceConnected");
            //我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
            serviceMessenger = new Messenger(binder);
            WebSocketService.sendConnectedMsg(serviceMessenger, clientMessenger);
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 客户端与Service失去连接
            // 该方法只在Service 被破坏了或者被杀死的时候调用.
            Log.i("DemoLog", "客户端 onServiceDisconnected");
            serviceMessenger = null;
        }
    };
    
}
