package testdemo.dtv.sony.com.powerdisc;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

private static  String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.action_reboot)
        {
            /*Intent intent2 = new Intent(Intent.ACTION_REBOOT);
            intent2.putExtra("nowait", 1);
            intent2.putExtra("interval", 1);
            intent2.putExtra("window", 0);
            sendBroadcast(intent2);  permission denied */

          /*  String[] command = {"reboot"};
            ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*try{
                PowerManager
                Process proc =Runtime.getRuntime().exec(new String[]{"reboot -p"});  //关机
            }catch(Exception e){
                e.printStackTrace();
            }*/

            PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
            try {
                //获得ServiceManager类
                Class<?> ServiceManager = Class
                        .forName("android.os.ServiceManager");
                //获得ServiceManager的getService方法
                Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
                //调用getService获取RemoteService
                Object oRemoteService = getService.invoke(null,Context.POWER_SERVICE);
                //获得IPowerManager.Stub类
                Class<?> cStub = Class
                        .forName("android.os.IPowerManager$Stub");
                //获得asInterface方法
                Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
                //调用asInterface方法获取IPowerManager对象
                Object oIPowerManager = asInterface.invoke(null, oRemoteService);
                //获得shutdown()方法
                Method shutdown = oIPowerManager.getClass().getMethod("shutdown",boolean.class,boolean.class);
                //调用shutdown()方法
                shutdown.invoke(oIPowerManager,false,true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (id==R.id.action_remote)
        {
            startActivity(new Intent(this,BookManagerActivity.class));
        }
        else if(id==R.id.action_singleTask)
        {
            startActivity(new Intent(this,SingleActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        ActivityManager ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = ams.getRunningTasks(1);

        if(MainActivity.class.getName().equals(runningTasks.get(0).topActivity.getClassName()))
        {
            Log.d(TAG,"dd"+runningTasks.get(0).topActivity.getClassName());
            Log.d(TAG,"top is Mine");
        }
        else
        {
            Log.d(TAG,"dd"+runningTasks.get(0).topActivity.getClassName());
            Log.d(TAG,"top isn't mine");
        }
        Log.d(TAG, "onUserLeaveHint");
    }
}
