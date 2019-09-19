package com.swipeuninstall.android;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.swipeuninstall.android.Adapter.CardAdapter;
import com.swipeuninstall.android.Model.Model;
import com.swipeunistall.android.R;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private SwipeCardsView swipeCardsView;
    private List<Model> modelList = new ArrayList<>();
    private TextView mNoneText;

    private PackageManager packageManager = null;
    private List<ApplicationInfo> appList = null;

    //Buttons
    private ImageButton mRedoButton, mSwipeRightButton, mSwipeLeftButton;


    private int currentIndex;

    private String currentPackage;

    private boolean hasReachedEnd;

    private String filterType = "az";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mNoneText = findViewById(R.id.package_name);

        swipeCardsView = findViewById(R.id.swipeCardsView);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);



        // Buttons
        mRedoButton = findViewById(R.id.redo_button);
        mSwipeLeftButton = findViewById(R.id.swipe_left_button);
        mSwipeRightButton = findViewById(R.id.swipe_right_button);

        packageManager = getPackageManager();

        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener()
        {
            @Override
            public void onShow(int index) {

                currentIndex = index;
            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type)
            {

                currentPackage = modelList.get(index).getTitle();

                if(index == modelList.size() - 1)
                {
                    hasReachedEnd = true;

                }
                else
                {
                    hasReachedEnd = false;
                }

                if(hasReachedEnd)
                {
                    mNoneText.setVisibility(View.VISIBLE);
                }

                switch(type)
                {
                    case LEFT:
                        LeftSwipe(index, false);
                        break;
                    case RIGHT:
                        UninstallApplication(modelList.get(index).getTitle());
                        RightSwipe(index,false);
                        break;
                }

            }

            @Override
            public void onItemClick(View cardImageView, int index)
            {
                LaunchApplication(modelList.get(index).getTitle());
            }
        });

        getData();


        mRedoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(hasReachedEnd == true)
                {
                   // Log.d("Swipe", Integer.toString(modelList.size() - 1));
                    mSwipeLeftButton.setClickable(true);
                    mSwipeRightButton.setClickable(true);
                    mNoneText.setVisibility(View.INVISIBLE);
                    swipeCardsView.notifyDatasetChanged(currentIndex);
                }

                // After the end is reached and the redo button is pressed turn has end reached false
                if(hasReachedEnd == true)
                {
                    hasReachedEnd = false;
                }

                else if(currentIndex > 0)
                {
                    swipeCardsView.notifyDatasetChanged(currentIndex - 1);
                }



            }
        });

        mSwipeLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeftSwipe(0, true);
            }
        });

        mSwipeRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RightSwipe(0,true);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch(item.getItemId())
        {
            case R.id.sort_by_az:
                filterType = "az";
                break;
            case R.id.sort_by_za:
                filterType = "za";
                break;
            case R.id.sort_by_newest:
                filterType = "newest";
                break;
            case R.id.sort_by_oldest:
                filterType = "oldest";
                break;

        }

        getData();
        mNoneText.setVisibility(View.INVISIBLE);

        return super.onOptionsItemSelected(item);
    }

    private void RightSwipe(int index, boolean fromButton)
    {

        if(hasReachedEnd)
        {
            mSwipeLeftButton.setClickable(false);
            mSwipeRightButton.setClickable(false);
            return;
        }

        if (fromButton == true)
        {
            swipeCardsView.slideCardOut(SwipeCardsView.SlideType.RIGHT);
        }

    }

    private void LeftSwipe(int index, boolean fromButton)
    {

        if(hasReachedEnd)
        {
            mSwipeLeftButton.setClickable(false);
            mSwipeRightButton.setClickable(false);
            return;
        }

        if(fromButton == true)
        {
            swipeCardsView.slideCardOut(SwipeCardsView.SlideType.LEFT);
        }
    }

    private void UninstallApplication(String packageName)
    {

        Uri packageUri = Uri.parse("package:" + packageName);
        Intent uninstallIntent =
                new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
        startActivity(uninstallIntent);

    }

    private void LaunchApplication(String packageName)
    {

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null)
        {
            startActivity(launchIntent);
        }

    }


    private void getData()
    {

        modelList.clear();

        final PackageManager pm = getPackageManager();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

        for (PackageInfo packageInfo : packages)
        {
            try
            {
                if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM ) == 0)
                {
                    Drawable icon = getPackageManager().getApplicationIcon(packageInfo.packageName);
                    String version = packageInfo.versionName;

                    String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA));
                    Long time = packageInfo.firstInstallTime;



                    modelList.add(new Model(packageInfo.packageName, icon, version, appName, time));

                }
            }
            catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        FormatData(filterType);


        CardAdapter cardAdapter = new CardAdapter(modelList, this);
        swipeCardsView.setAdapter(cardAdapter);



    }

    private void FormatData(String type)
    {
        switch (type)
        {
            case "az":
                Collections.sort(modelList, new Comparator<Model>() {
                    @Override
                    public int compare(Model arg0, Model arg1) {
                        return arg0.getName().compareToIgnoreCase(arg1.getName());
                    }
                });
                break;
            case "za":
                Collections.sort(modelList, new Comparator<Model>() {
                    @Override
                    public int compare(Model arg0, Model arg1) {
                        return arg1.getName().compareToIgnoreCase(arg0.getName());
                    }
                });

                break;
            case "oldest":
                Collections.sort(modelList, new Comparator<Model>() {
                    @Override
                    public int compare(Model arg0, Model arg1) {
                        return Long.valueOf(arg0.getTime()).compareTo(Long.valueOf(arg1.getTime()));
                    }
                });
                break;
            case "newest":
                Collections.sort(modelList, new Comparator<Model>() {
                    @Override
                    public int compare(Model arg0, Model arg1) {
                        return Long.valueOf(arg1.getTime()).compareTo(Long.valueOf(arg0.getTime()));
                    }
                });
                break;
        }
    }
}
