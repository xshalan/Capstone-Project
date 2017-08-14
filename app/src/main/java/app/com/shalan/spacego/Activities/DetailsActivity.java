package app.com.shalan.spacego.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Adapters.viewPagerAdapter;
import app.com.shalan.spacego.Fragments.AboutFragment;
import app.com.shalan.spacego.Fragments.FeatureFragment;
import app.com.shalan.spacego.Fragments.MapFragment;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    private String TAG = DetailsActivity.class.getSimpleName();
    @BindView(R.id.details_activity_toolbar)
    Toolbar detailsToolbar;
    @BindView(R.id.backdrop_image_cover)
    ImageView spaceCoverImage ;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private Space spaceModel;
    private String spaceID ;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar(detailsToolbar);
        ButterKnife.bind(this);

        //get spaceModel data from mainActivity
        spaceModel = getIntent().getExtras().getParcelable("spaceModel");
        spaceID = getIntent().getStringExtra("spaceID");
        //set cover Image for details activity
        Glide.with(getApplicationContext()).load(spaceModel.getImageUrl()).into(spaceCoverImage);

        // Implemeting UI by findviewById
        mViewPager = (ViewPager) findViewById(R.id.viewPager_Details);
        mTabLayout = (TabLayout) findViewById(R.id.tabs_Details);

        //Implementing viewPager in Details screen
        mFragmentList.add(AboutFragment.newInstance(spaceModel,spaceID));
        mFragmentList.add(FeatureFragment.newInstance(spaceModel.getFeatures()));
        mFragmentList.add(MapFragment.newInstance(spaceModel.getName(),spaceModel.getLatitude(),spaceModel.getLongitude()));

        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        adapter.setmFragmentList(mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
            }

        });
        mTabLayout.setupWithViewPager(mViewPager);


    }
}
