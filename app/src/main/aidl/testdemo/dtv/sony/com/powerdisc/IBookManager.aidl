// IBookManager.aidl
package testdemo.dtv.sony.com.powerdisc;
import testdemo.dtv.sony.com.powerdisc.Book;
// Declare any non-default types here with import statements
import testdemo.dtv.sony.com.powerdisc.IOnNewBookArrivedListener;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
            List<Book> getBookList();
            void addBook(in Book book);
            void registerNewBookArrivedListener(IOnNewBookArrivedListener listener);
            void unregisterNewBookArrivedListener(IOnNewBookArrivedListener listener);
}
