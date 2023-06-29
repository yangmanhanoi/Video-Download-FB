package saver.downloader.videos.inshotvideodownload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class AdapterListView extends RecyclerView.Adapter<AdapterListView.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> list;
    int layout;
    public AdapterListView(Context context, ArrayList<HashMap<String,String>> list, int layout)
    {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }
    @NonNull
    @Override
    public AdapterListView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new AdapterListView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.layout_result);
        }
    }
}
