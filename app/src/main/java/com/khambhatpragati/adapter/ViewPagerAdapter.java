package com.khambhatpragati.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.khambhatpragati.R;
import com.khambhatpragati.activity.WebviewActivity;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.model.response.AdvertismentBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.logging.Handler;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<AdvertismentBean> imageUrls;

    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    public ViewPagerAdapter(Context context, ArrayList<AdvertismentBean> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }
    @Override
    public int getCount() {
        return imageUrls.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
       /* ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(imageUrls.get(position))
                .fit()
                .into(imageView);
        container.addView(imageView);
        return imageView;*/

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        String utils = imageUrls.get(position).getFull_path();

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        if(!utils.equalsIgnoreCase("NULL")
                && utils.startsWith("http"))
        imageLoader.get(utils, ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!imageUrls.get(position).getWebsite_url().equalsIgnoreCase("")
                    && !imageUrls.get(position).getWebsite_url().equalsIgnoreCase("null")) {
                    Intent intent = new Intent(context, WebviewActivity.class);
                    intent.putExtra("strValue", imageUrls.get(position).getWebsite_url());
                    context.startActivity(intent);
                }
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrls.get(position).getWebsite_url()));
                v.getContext().startActivity(browserIntent);*/

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}

