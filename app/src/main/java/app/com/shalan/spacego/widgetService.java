package app.com.shalan.spacego;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import app.com.shalan.spacego.Models.Space;

import static app.com.shalan.spacego.Activities.NearbyActivity.DISTANCE_LIST_KEY;
import static app.com.shalan.spacego.Activities.NearbyActivity.MyPREFERENCES;
import static app.com.shalan.spacego.Activities.NearbyActivity.SPACE_LIST_KEY;

/**
 * Created by noura on 20/08/2017.
 */

public class widgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences(MyPREFERENCES,MODE_PRIVATE) ;
        Type spaceListType = new TypeToken<List<Space>>(){}.getType();
        Type distanceListType = new TypeToken<List<Double>>(){}.getType();
        Gson gson = new Gson();
        String spaceJSON = preferences.getString(SPACE_LIST_KEY, "");
        String disatnceJSON = preferences.getString(DISTANCE_LIST_KEY,"") ;
        List<Space> spaceList = gson.fromJson(spaceJSON,spaceListType);
        List<Double> distanceList = gson.fromJson(disatnceJSON,distanceListType);
        Log.v("widgetService", spaceList.toString());



        return new widgetViewsFactory(this,spaceList,distanceList);
    }
}
