package chandrakant.com.inshortsandroiddemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import chandrakant.com.inshortsandroiddemo.Db.DatabaseHandler;
import chandrakant.com.inshortsandroiddemo.adapter.NewsAdapter;
import chandrakant.com.inshortsandroiddemo.model.FilterByDate;
import chandrakant.com.inshortsandroiddemo.model.ResponseAPI;
import chandrakant.com.inshortsandroiddemo.model.ReverseOrderDate;
import chandrakant.com.inshortsandroiddemo.model.SortByCategory;
import chandrakant.com.inshortsandroiddemo.model.publishersort;


public class MainActivity extends BaseActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    final String[] items = {" Publisher", " Category",};
    final String[] item2 = {" old-to-new", " New-to-old",};
    Dialog dialog;
    DatabaseHandler db;
    // json array response url
    private String urlJsonArry = "http://starlord.hackerearth.com/newsjson";
    private RecyclerView mRecyclerView;
    private TextView txtResponse;
    private List<ResponseAPI> mResponseAPIList;
    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);
        initLayout();
        List<ResponseAPI> responseAPIs1 = db.getAllResponse();
        if (responseAPIs1.size() > 0) {
            updateData(responseAPIs1);
        } else {
            callApi();
        }

    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
    }


    private void callApi() {
        showProgressDialog("Please wait", false);

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        removeProgressDialog();

                        try {
                            ResponseAPI[] responseAPIs = new Gson().fromJson(response.toString(), ResponseAPI[].class);

                            List<ResponseAPI> listdata = Arrays.asList(responseAPIs);
                            db.deleteContact();
                            for (int i = 0; i < listdata.size(); i++) {
                                db.addResponse(listdata.get(i));

                            }
                            List<ResponseAPI> responseAPIs1 = db.getAllResponse();
                            if (responseAPIs1.size() > 0) {
                                updateData(responseAPIs1);
                            }

                            Log.e(TAG, "" + responseAPIs.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                removeProgressDialog();

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fav) {

            List<ResponseAPI> responseAPIfav = new ArrayList<>();

            List<ResponseAPI> responseAPIs = db.getAllResponse();
            for (int i = 0; i < responseAPIs.size(); i++) {
                ResponseAPI responseAPI = responseAPIs.get(i);
                if (responseAPI.getIsLike() != null) {
                    if (responseAPI.getIsLike().equals("true")) {
                        responseAPIfav.add(responseAPI);
                    }
                }
            }
            updateData(responseAPIfav);
        } else if (id == R.id.refresh) {
            callApi();
        } else if (id == R.id.sort) {
            checkBox(items, true);

        } else if (id == R.id.filter) {
            checkBox(item2, false);

        }else if(id==R.id.home){
            showProgressDialog("Db fetching data", false);

            List<ResponseAPI> responseAPIs1 = db.getAllResponse();
            updateData(responseAPIs1);
            removeProgressDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    private void updateData(List<ResponseAPI> responseAPIList) {
        mResponseAPIList = responseAPIList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, responseAPIList, new NewsAdapter.OnItemClickListener(

        ) {
            @Override
            public void onItemClick(ResponseAPI responseAPI) {
                Intent intent = new Intent(MainActivity.this, ShowNews.class);
                intent.putExtra("Parse", responseAPI);
                startActivity(intent);

            }
        });

        mRecyclerView.setAdapter(newsAdapter);
    }


    private void checkBox(final String[] day_check, final boolean value) {
        final boolean checked_state[] = {false, false}; //The array that holds the checked state of the checkbox items
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this)

                .setMultiChoiceItems(day_check, null, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
// TODO Auto-generated method stub

//storing the checked state of the items in an array
                        checked_state[which] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        ArrayList<String> stringArrayList = new ArrayList<String>();
                        for (int i = 0; i < 2; i++) {
                            if (checked_state[i] == true) {
                                stringArrayList.add(day_check[i]);
                            }
                        }

                        if (value) {
                            if (stringArrayList.size() > 1) {
                                if (mResponseAPIList != null) {
                                    Collections.sort(mResponseAPIList, new SortByCategory());
                                    Collections.sort(mResponseAPIList, new publishersort());
                                }
                                updateData(mResponseAPIList);


                            } else if (stringArrayList.size() == 1) {

                                if (stringArrayList.get(0).equals(day_check[0])) {
                                    if (mResponseAPIList != null) {

                                        Collections.sort(mResponseAPIList, new publishersort());
                                    }
                                    updateData(mResponseAPIList);
                                } else {
                                    if (mResponseAPIList != null) {

                                        Collections.sort(mResponseAPIList, new SortByCategory());
                                    }
                                    updateData(mResponseAPIList);
                                }
                            }
                        } else {
                            if (stringArrayList.size() > 1) {


                            } else if (stringArrayList.size() == 1) {
                                if (stringArrayList.get(0).equals(day_check[0])) {
                                    if (mResponseAPIList != null) {

                                        Collections.sort(mResponseAPIList, new ReverseOrderDate());
                                        updateData(mResponseAPIList);

                                    }
                                } else {
                                    if (mResponseAPIList != null) {

                                        Collections.sort(mResponseAPIList, new FilterByDate());
                                        updateData(mResponseAPIList);

                                    }
                                }
                            }
                        }

//used to dismiss the dialog upon user selection.
                        dialog.dismiss();
                    }
                });
        AlertDialog alertdialog1 = builder1.create();
        alertdialog1.show();

    }
}
