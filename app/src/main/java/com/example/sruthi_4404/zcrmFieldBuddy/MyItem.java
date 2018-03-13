package com.example.sruthi_4404.zcrmFieldBuddy;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by sruthi-4404 on 04/10/17.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final Long id;

    public MyItem(double lat, double lng, String title, String snippet,Long recid) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        id = recid;
    }

    public MyItem(double lat, double lng) {
       this(lat,lng,null,null,null);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public Long getId() {
        return id;
    }
}