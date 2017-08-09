package app.com.shalan.spacego.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import app.com.shalan.spacego.Adapters.viewPagerAdapter;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    private String TAG = DetailsActivity.class.getSimpleName();
    @BindView(R.id.details_activity_toolbar)
    Toolbar detailsToolbar ;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private static Space spaceModel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar(detailsToolbar);
        ButterKnife.bind(this);


        // Implemeting UI by findviewById
        mViewPager =  (ViewPager) findViewById(R.id.viewPager_Details);
        mTabLayout  = (TabLayout) findViewById(R.id.tabs_Details);

        //Implementing viewPager in Details screen
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
            }

        });
        mTabLayout.setupWithViewPager(mViewPager);

        //get spaceModel data from mainActivity
        spaceModel = getIntent().getExtras().getParcelable("spaceModel");
        Log.v(TAG,spaceModel.getName());

    }
}
