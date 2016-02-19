package com.example.zane.bookmanager.presenters.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zane.bookmanager.R;
import com.example.zane.bookmanager.model.bean.Book_DB;
import com.example.zane.bookmanager.view.MyBookInfoTopViewHolder;
import com.example.zane.bookmanager.view.MyBookinfoViewHolder;
import com.example.zane.easymvp.presenter.BaseListAdapterPresenter;
import com.example.zane.easymvp.view.BaseListViewHolderImpl;
import com.kermit.exutils.utils.ExUtils;

import java.util.List;

/**
 * Created by Zane on 16/2/18.
 */
public class MyBookInfoAdapter extends BaseListAdapterPresenter<Book_DB>{

    public static final int TOP_TYPE = 1166;
    public static final int NORMAL_TYPE = 1145;
    private List<Book_DB> myBooks;
    private OnItemClickListener listener;
    private OnSortBookListener onSortBookListener;
    private TextView textView_name;
    private TextView textView_date;

    public interface OnItemClickListener{
        void onClick(int positon);
        void onLongClick(int positon);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnSortBookListener{
        void onSortByName();
        void onSortByDate();
    }
    public void setOnSortBookListener(OnSortBookListener listener){
        onSortBookListener = listener;
    }

    public MyBookInfoAdapter(Context mContext) {
        super(mContext);
    }

    public void setMyBooks(List<Book_DB> books){
        myBooks = books;
    }

    public void clear(){
        myBooks.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TOP_TYPE;
        } else{
            return NORMAL_TYPE;
        }
    }

    @Override
    public BaseListViewHolderImpl OnCreatViewHolder(ViewGroup viewGroup, int i) {
        switch (i){
            case TOP_TYPE:
                MyBookInfoTopViewHolder viewHolder = new MyBookInfoTopViewHolder(viewGroup, R.layout.mybookinfo_top_item_layout);
                textView_name = viewHolder.getTextView_name();
                textView_date = viewHolder.getTextView_date();
                return viewHolder;
            case NORMAL_TYPE:
                return new MyBookinfoViewHolder(viewGroup, R.layout.mybookinfo_item_layout);
            default:
                return new MyBookinfoViewHolder(viewGroup, R.layout.mybookinfo_item_layout);
        }
    }

    @Override
    public void onBindViewHolder(BaseListViewHolderImpl holder, final int position) {
        if (position != 0){
            holder.setData(myBooks.get(position-1));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position-1);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(position-1);
                    return false;
                }
            });
        } else {
            textView_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSortBookListener.onSortByDate();
                }
            });
            textView_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSortBookListener.onSortByName();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return myBooks.size() + 1;
    }
}
