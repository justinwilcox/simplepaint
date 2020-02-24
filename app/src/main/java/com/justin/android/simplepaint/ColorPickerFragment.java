package com.justin.android.simplepaint;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ColorPickerFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private int red, green, blue;

    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;

    private View previewView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_picker, container, false);

        previewView = view.findViewById(R.id.colorPreview);

        redSeekBar = view.findViewById(R.id.seekBarRed);
        blueSeekBar = view.findViewById(R.id.seekBarBlue);
        greenSeekBar = view.findViewById(R.id.seekBarGreen);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        Button okButton = view.findViewById(R.id.buttonColorSelected);
        okButton.setOnClickListener(this);

        updateSeekBars();

        return view;
    }


    public void setColor(int color) {

        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);

        updateSeekBars();
    }

    private void updateSeekBars() {
        if(redSeekBar != null) {
            redSeekBar.setProgress(red);
        }
        if(greenSeekBar != null) {
            greenSeekBar.setProgress(green);
        }
        if(blueSeekBar != null) {
            blueSeekBar.setProgress(blue);
        }

        //it's possible seek bars won't change, but preview won't match, so explicitly update this here too
        updatePreview();
    }


    private void updatePreview() {
        if(this.previewView != null) {
            previewView.setBackgroundColor(Color.rgb(red,green,blue));
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()) {
            case R.id.seekBarRed:
                red = progress;
                break;
            case R.id.seekBarGreen:
                green = progress;
                break;
            case R.id.seekBarBlue:
                blue = progress;
                break;
            default:
                break;
        }

        this.updatePreview();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {

        //note: using some sort of color selected delegate interface would be better here in a real app
        MainActivity activity = (MainActivity) this.getActivity();
        activity.setPaintColor(Color.rgb(red,green,blue));

        this.dismiss();
    }
}
