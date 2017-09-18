package chandrakant.com.inshortsandroiddemo.model;

import java.util.Comparator;

/**
 * Created by chandrakant on 17/9/17.
 */

public class publishersort implements Comparator<ResponseAPI> {
    @Override
    public int compare(ResponseAPI responseAPI, ResponseAPI t1) {
        return responseAPI.getPUBLISHER().compareTo(t1.getPUBLISHER());
    }
}
