package app.com.shalan.spacego.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.com.shalan.spacego.Adapters.viewPagerAdapter;
import app.com.shalan.spacego.R;

public class DetailsActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Implemeting UI by findviewById
        mViewPager =  (ViewPager) findViewById(R.id.viewPager_Details);
        mTabLayout  = (TabLayout) findViewById(R.id.tabs_Details);

        /*
        Implementing viewPager in Details screen
         */
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
            }

        });
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
