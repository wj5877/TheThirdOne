package testdemo.dtv.sony.com.powerdisc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.view.View;
import android.widget.Button;


/**
 * Created by Administrator on 2016/3/22.
 */
public class BookManagerActivity extends Activity {

    private static String TAG= Activity.class.getCanonicalName();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                Log.i(TAG,"receive new book:"+msg.obj);
                break;
                default:
                super.handleMessage(msg);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(iBookManager!=null)
                        {
                            try {
                                iBookManager.getBookList();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
        Intent ServiceIntent = new Intent(this,BookManagerService.class);
        bindService(ServiceIntent, sc, BIND_AUTO_CREATE);
    }
    IBookManager iBookManager;
    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        try {
            if(iBookManager!=null&&iBookManager.asBinder().isBinderAlive()) {
                Log.i(TAG,"unregister "+listener);
                iBookManager.unregisterNewBookArrivedListener(listener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(sc);
        super.onDestroy();
    }

    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             iBookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> bookList = iBookManager.getBookList();
                Log.i("dsfd",bookList.getClass().getCanonicalName());
                for(Book book : bookList)
               {
                   Log.i("dsf",book.bookName+"========"+book.bookId);
               }
                iBookManager.registerNewBookArrivedListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void OnNewBookArrived(Book newBook) throws RemoteException {
                mHandler.obtainMessage(1,newBook).sendToTarget();
        }
    };

    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        super.onStop();
    }
}
