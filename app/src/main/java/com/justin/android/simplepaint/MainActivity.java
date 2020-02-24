package com.justin.android.simplepaint;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, MenuItem.OnMenuItemClickListener {

    SimplePaintView paintView;


    private AppBarConfiguration appbarConfig;

    private View colorPreviewView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = this.findViewById(R.id.PaintView);

        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        paintView.setPaintCanvasSize(displaySize.x, displaySize.y);

        this.drawerLayout = this.findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(this);

        Log.i("MainActivity", "Creating main activity...");

        //we use the NavigatioinView class so that we can use the Android Menu class/xml to make a quick menu UI
        NavigationView navView = drawerLayout.findViewById(R.id.menu_view);
        Menu menu = navView.getMenu();

        //we're using a no title theme, and don't have any fragment navigation in our simple app, so
        // we have to manually setup the menu item click listeners
        MenuItem clear = menu.findItem(R.id.menu_clear);
        clear.setOnMenuItemClickListener(this);

        MenuItem selectColor = menu.findItem(R.id.menu_select_color);
        selectColor.setOnMenuItemClickListener(this);
        colorPreviewView = selectColor.getActionView().findViewById(R.id.menuC_color_preview);

        //if user has picked a color before, restore it here
        if(savedInstanceState != null) {
            setPaintColor(savedInstanceState.getInt("color", Color.BLACK));
        }
        else {
            setPaintColor(Color.BLACK);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.paintView.restoreCachedPainting();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(paintView != null) {
            this.paintView.cachePainting();
            outState.putInt("color", paintView.getPaintColor());
        }
        super.onSaveInstanceState(outState);
    }


    public void setPaintColor(int color) {
        colorPreviewView.setBackgroundColor(color);
        paintView.setPaintColor(color);
    }


    private void showColorPicker() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ColorPickerFragment colorPickerFragment = new ColorPickerFragment();
        colorPickerFragment.setColor(paintView.getPaintColor());
        colorPickerFragment.show(ft, "dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

        Log.i("Drawer", "State changed.");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        this.drawerLayout.closeDrawer(Gravity.LEFT);

        if(item.getItemId() == R.id.menu_select_color) {
            showColorPicker();
        }
        else if(item.getItemId() == R.id.menu_clear) {
            this.paintView.clearPainting();
        }

        return true;
    }
}
