package chandrakant.com.inshortsandroiddemo.model;

import java.util.Comparator;

/**
 * Created by chandrakant on 16/9/17.
 */

public class SortByCategory implements Comparator<ResponseAPI> {
    @Override
    public int compare(ResponseAPI responseAPI, ResponseAPI t1) {
        return responseAPI.getCATEGORY().compareTo(t1.getCATEGORY());
    }
}
