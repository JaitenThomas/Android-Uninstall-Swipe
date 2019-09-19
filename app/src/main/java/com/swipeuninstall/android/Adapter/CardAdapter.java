package com.swipeuninstall.android.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipeuninstall.android.Model.Model;
import com.swipeunistall.android.R;
import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.List;

public class CardAdapter extends BaseCardAdapter
{

    private List<Model> modelList;
    private Context context;


    public CardAdapter(List<Model> modelList, Context context)
    {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId()
    {
        return R.layout.card_item;
    }

    @Override
    public void onBindData(final int position, View cardview)
    {
        if (modelList == null || modelList.size() == 0) {
            return;
        }

        ImageView imageView = cardview.findViewById(R.id.imageView);
        TextView textView = cardview.findViewById(R.id.package_name);
        TextView nameView = cardview.findViewById(R.id.app_name);
        TextView versionText = cardview.findViewById(R.id.version_name);


        Model model = modelList.get(position);
        textView.setText("Package: " + model.getTitle());
        nameView.setText(model.getName());
        versionText.setText("Version: " + model.getVersionNumber());


        //imageView.setImageResource(R.drawable.ic_launcher_background);


        imageView.setBackgroundDrawable(model.getIcon());


        //imageView.setImageDrawable();
        //Picasso.get().load(model.getImage()).into(imageView);

    }

}
