package com.example.chapter08;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.chapter08.adapter.PlanetBaseAdapter;
import com.example.chapter08.entity.Planet;
import com.example.chapter08.util.ToastUtil;

import java.util.List;

public class BaseAdapterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<Planet> planetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter);
        Spinner sp_planet = findViewById(R.id.sp_planet);
        // 获取默认的行星列表，即水星、金星、地球、火星、木星、土星
        planetList = Planet.getDefaultList();
        // 构建一个行星列表的适配器
        PlanetBaseAdapter adapter = new PlanetBaseAdapter(this, planetList);
        sp_planet.setAdapter(adapter);
        sp_planet.setSelection(0);
        sp_planet.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        ToastUtil.show(this,"您选择的是" + planetList.get(position).name);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}