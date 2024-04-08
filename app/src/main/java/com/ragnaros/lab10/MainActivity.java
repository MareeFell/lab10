package com.ragnaros.lab10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Bitmap image, image1;

    ImageView imageView, imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        imageView1 = findViewById(R.id.image1);

        openImage("lenna.png");

        findViewById(R.id.copy).setOnClickListener(v -> copy());
        findViewById(R.id.invert).setOnClickListener(v -> invert());
        findViewById(R.id.gray).setOnClickListener(v -> gray());
        findViewById(R.id.black_white).setOnClickListener(v -> blackWhite());
        findViewById(R.id.open).setOnClickListener(v -> {
            View view = LayoutInflater.from(this).inflate(R.layout.alert_input, null, false);

            new AlertDialog.Builder(this).setView(view).setTitle("Open").setPositiveButton("Ok", (v1, index) -> {
                String file = ((EditText) view.findViewById(R.id.input_file)).getText().toString();
                openImage(file);
            }).show();
        });
        findViewById(R.id.bright).setOnClickListener(v -> brightness());
        findViewById(R.id.horizontal).setOnClickListener(v -> horizontal());
        findViewById(R.id.vertical).setOnClickListener(v -> vertical());
    }

    public void brightness() {
        float brightness = ((SeekBar) findViewById(R.id.bright_bar)).getProgress() / 255f;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getPixel(x, y);
                image1.setPixel(x, y, Color.argb(255, (int)(Color.red(color) * brightness), (int)(Color.green(color) * brightness), (int)(Color.blue(color) * brightness)));
            }
        }
    }

    public void horizontal() {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image1.setPixel(image.getWidth() - x - 1, y, image.getPixel(x, y));
            }
        }
    }

    public void vertical() {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image1.setPixel(x, image.getHeight() - y - 1, image.getPixel(x, y));
            }
        }
    }

    public void openImage(String file) {
        AssetManager manager = getAssets();
        InputStream stream;
        try {
            stream = manager.open(file);
        } catch (Exception ignored) {
            Toast.makeText(getApplicationContext(), "Ошибка чтения файла", Toast.LENGTH_LONG).show();
            return;
        }

        image = BitmapFactory.decodeStream(stream);
        image1 = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

        imageView1.setImageBitmap(image1);
        imageView.setImageBitmap(image);
    }

    public void gray() {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getPixel(x, y);
                int resColor = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3;
                image1.setPixel(x, y, Color.argb(255, resColor, resColor, resColor));
            }
        }
    }

    public void blackWhite() {
        int threshold = ((SeekBar) findViewById(R.id.black_white_threshold)).getProgress();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getPixel(x, y);
                int resColor = ((Color.red(color) + Color.green(color) + Color.blue(color)) / 3) > threshold ? 255 : 0;
                image1.setPixel(x, y, Color.argb(255, resColor, resColor, resColor));
            }
        }
    }

    public void copy() {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image1.setPixel(x, y, image.getPixel(x, y));
            }
        }
    }

    public void invert() {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getPixel(x, y);
                image1.setPixel(x, y, Color.argb(255, 255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color)));
            }
        }
    }
}