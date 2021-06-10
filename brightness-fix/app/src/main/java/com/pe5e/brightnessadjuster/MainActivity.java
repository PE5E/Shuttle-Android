package com.pe5e.brightnessadjuster;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.InputStreamReader;

import java.io.BufferedReader;

import static java.lang.Runtime.getRuntime;


public class MainActivity extends AppCompatActivity {
    String myTag = "PE5E:brightness";
    int maxBrightness = 4;
    int actualBrightness = 2;

    TextView textLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLabel = findViewById(R.id.textView1);

        String command = "cat /sys/class/backlight/acpi_video0/max_brightness";
        int returnedValue = getCommand(command);
        if(returnedValue >= 0) {
            maxBrightness = returnedValue;
        }

        command = "cat /sys/class/backlight/acpi_video0/actual_brightness";
        returnedValue = getCommand(command);
        if(returnedValue >= 0) {
            actualBrightness = returnedValue;
        }

        // set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar.setMax(maxBrightness);
        seekBar.setProgress(actualBrightness);

        int mainBrightness = seekBar.getProgress();

        // Log.d(myTag, textLabel.getText().toString());
        String text = "Brightness: " + mainBrightness;
        textLabel.setText(text);
    }



    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            String text = "Brightness: " + progress;
            textLabel.setText(text);
            setBrightness(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    void setBrightness(int brightness) {
        String command = "echo \"" + brightness + "\" >> /sys/class/backlight/acpi_video0/brightness";
        Log.d(myTag, "This is the command: " + command);
        String returnedError = setCommand(command);
        if(returnedError.compareTo("done") != 0) {
            Log.e(myTag, "setBrightness: " + returnedError);
        }
    }

    int getCommand(String command) {
        int result = 0;
        java.lang.Process process;
        try {
            process = getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                     result = Integer.parseInt(line);
                }
            }
            Log.e(myTag, "Here is the standard error of the command (if any):\n");
            while ((line = stdError.readLine()) != null) {
                Log.e(myTag, line);
                return -1;
            }
        } catch (Exception ex) {
            Log.e(myTag, "getCommand" + ex.toString());
            return -1;
        }
        return result;
    }

    String setCommand(String command) {
        String result = "";
        java.lang.Process process;
        try {
            process = getRuntime().exec(new String[] { "su", "-c", command });
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    return line;
                }
                else {
                    return "done";
                }
            }
            Log.e(myTag, "Here is the standard error of the command (if any):\n");
            while ((line = stdError.readLine()) != null) {
                Log.e(myTag, line);
                return line;
            }
        } catch (Exception ex) {
            Log.e(myTag, "setCommand: " + ex.toString());
            return "error";
        }
        return result;
    }

}