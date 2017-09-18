package chandrakant.com.inshortsandroiddemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog(String bodyText, final boolean isRequestCancelable) {
        try {
            if (isFinishing()) {
                return;
            }
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(BaseActivity.this);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mProgressDialog.setCancelable(isRequestCancelable);
                //mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
                            return true; //
                        } else if (keyCode == KeyEvent.KEYCODE_BACK && isRequestCancelable) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
            }
            mProgressDialog.setMessage(bodyText);

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {

        }
    }

    public void removeProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}