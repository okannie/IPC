package com.annie.ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.annie.ipc.IMyAidl;
import com.annie.ipc.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class MyAidlService extends Service {

    private ArrayList<Person> mPersons;
    private static final String TAG = "=========MyAidlService:";

    /**
     * 创建生产的本地Binder对象，实现AIDL制定的方法
     */
    private IBinder mIBinder = new IMyAidl.Stub(){

        @Override
        public void addPerson(Person person) throws RemoteException {
            mPersons.add(person);
            Log.e(TAG,"addPerson");
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return mPersons;
        }
    };


    /**
     * 客户端与服务端绑定时的回调，返回mIBinder后客服端就可以通过它远程调用服务端的方法，既实现通讯
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        mPersons = new ArrayList<>();
        Log.e(TAG,"onBind");
        return mIBinder;
    }
}
