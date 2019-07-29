package com.bitauto.yiche;

import android.app.Application;
import com.bitauto.heart_net.AiniLu;

/**
 * @author : sunbq
 * e-mail : subbq@yiche.com
 * date   : 2019-07-2914:39
 * desc   :
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AiniLu.INSTANCE.init(this);
    }
}
