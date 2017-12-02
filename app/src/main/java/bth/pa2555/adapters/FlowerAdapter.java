package bth.pa2555.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import bth.pa2555.agilegardeningapp.R;
import bth.pa2555.models.Flower;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.FlowerViewHolder> {

    private List<Flower> mFlowers;
    private Context mContext;

    public FlowerAdapter(List<Flower> flowers, Context context) {
        mFlowers = flowers;
        mContext = context;
    }

    @Override
    public FlowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_flower, parent, false);
        return new FlowerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FlowerViewHolder viewHolder, int position) {
        Flower flower = mFlowers.get(position);
        viewHolder.textViewFlowerName.setText(flower.getName());
        Glide.with(mContext)
                .load(flower.getImageUrl())
                .transition(withCrossFade())
                .into(viewHolder.imageViewFlowerImage);
    }

    public int getItemCount() {
        return mFlowers.size();
    }

    static class FlowerViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFlowerName;
        ImageView imageViewFlowerImage;

        FlowerViewHolder(View itemView) {
            super(itemView);
            textViewFlowerName = itemView.findViewById(R.id.textView_flower_name);
            imageViewFlowerImage = itemView.findViewById(R.id.imageView_item_image);
        }
    }
}

