package app.com.shalan.spacego;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import app.com.shalan.spacego.Models.Space;

/**
 * Created by noura on 20/08/2017.
 */

public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Space> spaceList;
    private List<Double> distanceList;

    public WidgetViewsFactory(Context context, List<Space> spaceList, List<Double> distanceList){
        this.mContext = context ;
        this.spaceList = spaceList ;
        this.distanceList = distanceList ;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (spaceList.size() != 0) {
            return spaceList.size();

        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_row);
        row.setTextViewText(R.id.nearby_space_title,spaceList.get(i).getName());
        row.setTextViewText(R.id.nearby_space_address,spaceList.get(i).getAddress());
        row.setTextViewText(R.id.nearby_space_distance,Math.round(distanceList.get(i)*10.0)/10.0+"km");
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        if (spaceList.size() != 0) {
            return  spaceList.size() ;
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
