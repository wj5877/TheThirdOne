package testdemo.dtv.sony.com.powerdisc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/3/22.
 */
public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    //自带线程同步。
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();
    //可以根据对象底层的binder来判断两个对象是否是同一个。
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            try {
                Thread.sleep(5000);//为了触发anr ，每5秒执行一次。
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
                mListenerList.register(listener);
        }

        @Override
        public void unregisterNewBookArrivedListener(IOnNewBookArrivedListener listener) throws RemoteException {
                mListenerList.unregister(listener);
        }

    };

    private void onNewBookArrived(Book book) throws RemoteException {
            mBookList.add(book);
        int num = mListenerList.beginBroadcast();//特殊的遍历方式，以beginBroadcast开头，finsihBroadcast结尾。
        for(int i=0;i<num;i++)
        {
            IOnNewBookArrivedListener iOnNewBookArrivedListener = mListenerList.getBroadcastItem(i);
            iOnNewBookArrivedListener.OnNewBookArrived(book);
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements  Runnable{
        @Override
        public void run() {
            while(!mIsServiceDestoryed.get())
            {
                try {
                    Thread.sleep(5000);
                    try {
                        onNewBookArrived(new Book((int) (System.currentTimeMillis()), "a moment in peiking"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(12, "ddddd"));
        mBookList.add(new Book(13,"addddd"));
        new Thread(new ServiceWorker()).start();
    }
}


