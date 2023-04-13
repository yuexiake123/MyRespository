package com.example.chapter08.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.chapter08.R;
import com.example.chapter08.adapter.BillListAdapter;
import com.example.chapter08.database.BillDBHelper;
import com.example.chapter08.entity.BillInfo;

import java.util.List;


public class BillFragment extends Fragment {


    public static BillFragment newInstance(String yearMonth) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putString("yearMonth",yearMonth);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // "2023-04"
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        ListView lv_bill = view.findViewById(R.id.lv_bill);
        BillDBHelper mDBHelper = BillDBHelper.getInstance(getContext());
        Bundle arguments = getArguments();
        String yearMonth = arguments.getString("yearMonth");
        List<BillInfo> billInfoList = mDBHelper.queryByMonth(yearMonth);
        BillListAdapter adapter = new BillListAdapter(getContext(), billInfoList);
        lv_bill.setAdapter(adapter);
        Log.d("Jun","ViewOk" + yearMonth);
        return view;
    }
}