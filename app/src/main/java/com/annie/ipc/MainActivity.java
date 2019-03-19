package com.annie.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.annie.ipc.bean.Person;
import com.annie.ipc.service.MyAidlService;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    private TextView tvShowResult,tvAdd;
    private static final String TAG = "=========MainActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG,"=======跳转服务");
        Intent intent = new Intent(MainActivity.this, MyAidlService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        tvShowResult = findViewById(R.id.tvShowResult);
        tvAdd = findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Random random = new Random();
                Person person = new Person("shixin" + random.nextInt(10));

                try {
                    mAidl.addPerson(person);
                    List<Person> personList = mAidl.getPersonList();
                    tvShowResult.setText("显示结果"+"\n"+personList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Activity 中创建一个服务连接对象，在其中调用 IMyAidl.Stub.asInterface() 方法将 Binder 转为 AIDL 类
     */
    private IMyAidl mAidl;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到Binder，转换成AIDL，在不同的进程会返回这个代理
            Log.e(TAG,"onServiceConnected");
            mAidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy：解绑服务");
        unbindService(mServiceConnection);
    }
}
