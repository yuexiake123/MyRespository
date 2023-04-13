package com.example.chapter08;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.chapter08.adapter.PlanetGridAdapter;
import com.example.chapter08.entity.Planet;
import com.example.chapter08.util.ToastUtil;

import java.util.List;

public class GridViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gv_planet;
    private List<Planet> planetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        gv_planet = findViewById(R.id.gv_planet);
        planetList = Planet.getDefaultList();
        PlanetGridAdapter adapter = new PlanetGridAdapter(this,planetList);
        gv_planet.setAdapter(adapter);
        gv_planet.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ToastUtil.show(this, "您选择了：" + planetList.get(position).name);
    }
}