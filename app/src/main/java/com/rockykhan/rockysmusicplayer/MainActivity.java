package com.rockykhan.rockysmusicplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        String[] allPath = StorageUtil.getStorageDirectories(MainActivity.this);

                        ArrayList<File> mySongs;

                        if (allPath.length == 2){

                            File sdCard = new File(allPath[0]);
                            File phoneStorage = new File(allPath[1]);

                            ArrayList<File> phoneSongs = fetchSongs(phoneStorage);
                            ArrayList<File> sdCardSongs = fetchSongs(sdCard);

                            mySongs = new ArrayList<>();
                            mySongs.addAll(phoneSongs);
                            mySongs.addAll(sdCardSongs);

                        }else{

                            File phoneStorage = new File(allPath[0]);
                            mySongs = fetchSongs(phoneStorage);

                        }

//                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                        String[] items = new String[mySongs.size()];

                        if (items.length == 0){

                            Intent emptyActivityIntent = new Intent(MainActivity.this, EmptyListActivity.class);
                            startActivity(emptyActivityIntent);
                            finish();

                        }else {

                            for (int i = 0; i < mySongs.size(); i++) {
                                items[i] = mySongs.get(i).getName().replace(".mp3", "");
                            }
                            RockyAdapter Radapter = new RockyAdapter(MainActivity.this, R.layout.rocky_layout, items);
                            listView.setAdapter(Radapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String currentSong = listView.getItemAtPosition(i).toString();
                                    Intent intent = new Intent(MainActivity.this, PlayMusic.class);
                                    intent.putExtra("songsList", mySongs);
                                    intent.putExtra("currentSong", currentSong);
                                    intent.putExtra("position", i);
                                    startActivity(intent);
                                }
                            });

                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }

    public ArrayList<File> fetchSongs(File file){
        ArrayList arrayList = new ArrayList();
        File[] songs = file.listFiles();
        if (songs != null){
            for (File myFile: songs){
                if (myFile.isDirectory() && !myFile.isHidden()){
                    arrayList.addAll(fetchSongs(myFile));
                }else{
                    if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.warning)
                .show();

    }
}