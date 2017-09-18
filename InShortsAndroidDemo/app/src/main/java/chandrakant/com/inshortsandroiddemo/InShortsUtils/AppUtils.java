package chandrakant.com.inshortsandroiddemo.InShortsUtils;

import android.content.Context;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chandrakant on 16/9/17.
 */

public class AppUtils {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
        return format.format(date);
    }
}
