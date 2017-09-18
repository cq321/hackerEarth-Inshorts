package chandrakant.com.inshortsandroiddemo.model;

import java.util.Comparator;

/**
 * Created by chandrakant on 17/9/17.
 */

public class ReverseOrderDate implements Comparator<ResponseAPI> {
    @Override
    public int compare(ResponseAPI responseAPI, ResponseAPI t1) {
        return Long.valueOf(t1.getTIMESTAMP()).compareTo(Long.valueOf(responseAPI.getTIMESTAMP()));
    }
}
