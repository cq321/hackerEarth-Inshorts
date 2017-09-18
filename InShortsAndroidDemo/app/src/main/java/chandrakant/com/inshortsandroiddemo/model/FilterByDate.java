package chandrakant.com.inshortsandroiddemo.model;

import java.util.Comparator;

/**
 * Created by chandrakant on 16/9/17.
 */

public class FilterByDate implements Comparator<ResponseAPI> {
    @Override
    public int compare(ResponseAPI responseAPI, ResponseAPI t1) {
        return Long.valueOf(responseAPI.getTIMESTAMP()).compareTo(Long.valueOf(t1.getTIMESTAMP()));
    }
}
