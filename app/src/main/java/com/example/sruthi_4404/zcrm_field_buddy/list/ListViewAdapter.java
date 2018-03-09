package com.example.sruthi_4404.zcrm_field_buddy.list;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zoho.crm.library.crud.ZCRMRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sruthi-4404 on 26/02/17.
 */

public class ListViewAdapter {
    public static ArrayAdapter<ZCRMRecord> adapter;
    public static ListView RecordList;
    public static List<ZCRMRecord> records = new ArrayList();
    public static List<ZCRMRecord> storeList = new ArrayList();
    public static long idClicked;
    public static String nameClicked;
    public static String moduleAPIname;
    public static String title;
    public static Long cvID = null;

    public static void setContactList(ListView contactList) {
        RecordList = contactList;
    }

    public static ListView getContactList() {
        return RecordList;
    }

    public static ArrayAdapter getAdapter() {
        return adapter;
    }
}
