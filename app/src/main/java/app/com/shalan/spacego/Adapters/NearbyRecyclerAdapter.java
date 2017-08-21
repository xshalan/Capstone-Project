package app.com.shalan.spacego.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.com.shalan.spacego.Handler.OnSpaceClickListener;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;

/**
 * Created by noura on 12/08/2017.
 */

public class NearbyRecyclerAdapter extends RecyclerView.Adapter<NearbyRecyclerAdapter.ViewHolder> {

    private List<Space> spaceList ;
    private List<Double> spaceDistance ;
    private int itemLayout;

    private OnSpaceClickListener mOnSpaceClickListener ;

    public NearbyRecyclerAdapter(int itemLayout){
        this.itemLayout = itemLayout ;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(spaceList.size()>0){
            holder.spaceName.setText(spaceList.get(position).getName());
            holder.spaceDistance.setText(Math.round(spaceDistance.get(position)*10.0)/10.0 + "km");
            holder.spaceAddress.setText(spaceList.get(position).getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return spaceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView spaceName;
        private TextView spaceAddress;
        private TextView spaceDistance;
        public ViewHolder(View itemView) {
            super(itemView);
            spaceAddress = (TextView) itemView.findViewById(R.id.nearby_space_address);
            spaceName = (TextView) itemView.findViewById(R.id.nearby_space_title);
            spaceDistance = (TextView) itemView.findViewById(R.id.nearby_space_distance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnSpaceClickListener.onSpaceClick(view,getAdapterPosition());

        }
    }


    public void setOnItemClickListener(OnSpaceClickListener listener){
        mOnSpaceClickListener = listener;
    }
    public void setSpaceDistance(List<Double> spaceDistancee){
        this.spaceDistance = spaceDistancee ;
    }
    public void setSpaceModel(List<Space> spaceList){
        this.spaceList = spaceList ;
    }
}
