// IOnNewBookArrivedListener.aidl
package testdemo.dtv.sony.com.powerdisc;

import testdemo.dtv.sony.com.powerdisc.Book;
interface IOnNewBookArrivedListener {
    void OnNewBookArrived(in Book newBook);
}
