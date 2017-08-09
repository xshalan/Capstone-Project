package app.com.shalan.spacego.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.shalan.spacego.Handler.onSpaceClickListener;
import app.com.shalan.spacego.R;

/**
 * Created by noura on 06/08/2017.
 */

public class spaceItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
     public TextView spaceName, spaceRate;
    public ImageView spaceImage;
    public View mView;
    public onSpaceClickListener mOnSpaceClickListener ;
    public spaceItemViewHolder(View itemView) {
        super(itemView);
        spaceName = (TextView) itemView.findViewById(R.id.item_space_name);
        spaceRate = (TextView) itemView.findViewById(R.id.item_space_rate);
        spaceImage = (ImageView) itemView.findViewById(R.id.item_space_image);
        itemView.setOnClickListener(this);
        mView = itemView;
    }


    @Override
    public void onClick(View view) {
        mOnSpaceClickListener.onSpaceClick(view,getAdapterPosition());
    }

    public void setOnItemClickListener(onSpaceClickListener listener){
        mOnSpaceClickListener = listener;
    }
}
