package com.example.asus.fp_ams_mnmg.Adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.asus.fp_ams_mnmg.Interface.ILoadMore;
import com.example.asus.fp_ams_mnmg.Model.Item;
import com.example.asus.fp_ams_mnmg.R;
import android.support.v7.widget.RecyclerView.LayoutManager;
import java.util.List;

/**
 * Created by Asus on 29-04-2018.
 */


class LoadingViewHolder extends RecyclerView.ViewHolder{

    ProgressBar progressBar;
    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar6);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{

    public TextView date,ArrivalTime,DepartedTime;

    public ItemViewHolder(View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.textView21);
        ArrivalTime = itemView.findViewById(R.id.textView15);
        DepartedTime = itemView.findViewById(R.id.textView16);

    }
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Item> items;

    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public MyAdapter(RecyclerView recyclerView,Activity activity, List<Item> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                totalItemCount = linearLayoutManager.getItemCount();
                if (!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if(loadMore != null){
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.item_layout,parent,false);
            return  new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading,parent,false);
            return new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            Item item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.date.setText(items.get(position).getDate());
            viewHolder.ArrivalTime.setText(Integer.toString(items.get(position).getArrivalTime()));
            viewHolder.DepartedTime.setText(Integer.toString(items.get(position).getDepartedTime()));

        } else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
