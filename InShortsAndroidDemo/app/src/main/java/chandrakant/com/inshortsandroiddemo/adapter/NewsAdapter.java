package chandrakant.com.inshortsandroiddemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chandrakant.com.inshortsandroiddemo.Db.DatabaseHandler;
import chandrakant.com.inshortsandroiddemo.InShortsUtils.AppUtils;
import chandrakant.com.inshortsandroiddemo.R;
import chandrakant.com.inshortsandroiddemo.model.ResponseAPI;

/**
 * Created by chandrakant on 16/9/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final NewsAdapter.OnItemClickListener listener;
    Context context1;
    List<ResponseAPI> mImgRes;


    public NewsAdapter(Context context2, List<ResponseAPI> responseAPIs, NewsAdapter.OnItemClickListener listener) {

        context1 = context2;
        mImgRes = responseAPIs;
        this.listener = listener;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(context1).inflate(R.layout.cards_layout, parent, false);

        NewsAdapter.ViewHolder viewHolder1 = new NewsAdapter.ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.ViewHolder Vholder, final int position) {


        final ResponseAPI responseAPI = mImgRes.get(position);
        if (responseAPI.getTITLE() != null && !responseAPI.getTITLE().isEmpty()) {
            Vholder.mTxtTitle.setText(responseAPI.getTITLE());
        }
        if (responseAPI.getHOSTNAME() != null && !responseAPI.getHOSTNAME().isEmpty()) {
            Vholder.mTxtHostedName.setText(responseAPI.getHOSTNAME());
        }
        if (responseAPI.getCATEGORY() != null && !responseAPI.getCATEGORY().isEmpty()) {
            Vholder.mTxtCatergory.setText("Category : " + responseAPI.getCATEGORY());
        }
        if (responseAPI.getTIMESTAMP() != null) {
            Vholder.mTxtDate.setText("Date : " + AppUtils.convertTime(Long.valueOf(responseAPI.getTIMESTAMP())));
        }
        if (!responseAPI.getPUBLISHER().isEmpty() && responseAPI.getPUBLISHER() != null) {
            Vholder.mTxtPublisher.setText(responseAPI.getPUBLISHER());
        }
        if (responseAPI.isLike() != null) {
            if (responseAPI.isLike().equals("true")) {
                Vholder.imageView.setBackgroundResource(R.drawable.favorite_like);

            }
        }

        Vholder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(context1);
                String status = db.getContact(mImgRes.get(position).getId());
                if (status == null) {

                    Vholder.imageView.setBackgroundResource(R.drawable.favorite_like);
                    ResponseAPI responseAPI1 = mImgRes.get(position);
                    responseAPI1.setLike("true");
                    db.updateContact(responseAPI1);
                } else {
                    if (status.equals("true")) {
                        Vholder.imageView.setBackgroundResource(R.drawable.favorite_select);
                        ResponseAPI responseAPI1 = mImgRes.get(position);
                        responseAPI1.setLike("false");
                        db.updateContact(responseAPI1);
                    } else {
                        Vholder.imageView.setBackgroundResource(R.drawable.favorite_like);
                        ResponseAPI responseAPI1 = mImgRes.get(position);
                        responseAPI1.setLike("true");
                        db.updateContact(responseAPI1);
                    }
                }
            }
        });

        Vholder.bind(mImgRes.get(position), listener);


    }

    @Override
    public int getItemCount() {

        return mImgRes.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ResponseAPI responseAPI);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTxtDate, mTxtCatergory, mTxtPublisher, mTxtTitle, mTxtHostedName;
        public ImageView imageView;

        public ViewHolder(View v) {

            super(v);

            imageView = (ImageView) v.findViewById(R.id.imagelike);
            mTxtCatergory = (TextView) v.findViewById(R.id.txt_category);
            mTxtDate = (TextView) v.findViewById(R.id.txt_date);
            mTxtPublisher = (TextView) v.findViewById(R.id.txt_publisher_name);
            mTxtTitle = (TextView) v.findViewById(R.id.txt_title);
            mTxtHostedName = (TextView) v.findViewById(R.id.txt_hosted_name);

        }

        public void bind(final ResponseAPI responseAPI, final NewsAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onItemClick(responseAPI);

                }

            });

        }
    }
}

