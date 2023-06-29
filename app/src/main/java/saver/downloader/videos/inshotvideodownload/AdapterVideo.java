package saver.downloader.videos.inshotvideodownload;

import static android.widget.Toast.LENGTH_LONG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideExperiments;
import com.koushikdutta.ion.builder.Builders;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.ViewHolder> {
    Inflater inflater;
    Context context;
    ArrayList<File> pathArrList = new ArrayList<>();
    int layout;
    public AdapterVideo(Context context, ArrayList<File> pathArrList, int layout)
    {
        this.layout = layout;
        this.context = context;
        this.pathArrList = pathArrList;
        Log.e("DEBUGE", "pathArrList.size(): " + pathArrList.size());

    }
    @SuppressLint("ServiceCast")
    @NonNull
    @Override
    public AdapterVideo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new AdapterVideo.ViewHolder(view);
    }
// onBindViewHolder sẽ gọi đi gọi lại trong list, mỗi lần chạy nó sẽ trả về một
    // position, -> ko cần vòng for
    @Override
    public void onBindViewHolder(@NonNull AdapterVideo.ViewHolder holder, int position) {
        Uri uri  = Uri.fromFile(new File(pathArrList.get(position).getPath()));
        Log.e("uri", uri.toString());
        File file = pathArrList.get(position);


        ///
        Glide.with(context).load(uri).thumbnail(0.1f).into(holder.thumbnail);
        MediaPlayer mp = MediaPlayer.create(context, uri);
        int duration = mp.getDuration();
        mp.release();
        String time = String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration));
        holder.title.setText(time);
        //
        String name = file.getName();
        holder.currentTime.setText(name);
        //
        long timex = 2*1000;
        int actualPosition = holder.getAdapterPosition();
        holder.ic_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(actualPosition);
            }
        });
        //
        holder.ic_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("uri", uri.toString());
                Intent intent = new Intent(context, DetailHistoryActivity.class);
                intent.putExtra("uri", uri.toString());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return pathArrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title, currentTime;
        ImageButton ic_more_btn, ic_delete;
       // VideoView videoView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // videoView = itemView.findViewById(R.id.VideoView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
            ic_more_btn = itemView.findViewById(R.id.ic_more_btn);
            currentTime = itemView.findViewById(R.id.currentTime);
            ic_delete = itemView.findViewById(R.id.ic_delete);
        }
    }
    private void removeItem(int position)
    {

        //
        File file = pathArrList.get(position);
        Log.e("xx", file.toString());
        file.delete();
                pathArrList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pathArrList.size());
    }
}
