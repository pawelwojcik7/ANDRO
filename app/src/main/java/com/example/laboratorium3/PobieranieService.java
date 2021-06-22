package com.example.laboratorium3;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;



import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PobieranieService extends IntentService {
    public PobieranieService() {
        super("usługa pobierania plików");
    }
    public final static String ADRES = "adres pliku";
    public final static int ROZMIAR_BLOKU = 32 * 1024; // 32kB
    private PostepInfo mPostepInfo = new PostepInfo();
    private String mTypPliku;
    String mAdres,typ;
    private int rozmiar;

    @Override
    protected void onHandleIntent(Intent intent) {
        typ = intent.getStringExtra("typ");
        rozmiar = Integer.parseInt(intent.getStringExtra("rozmiar"));
        System.out.println("Typ w asdasd "+ typ);
        System.out.println("ROZMIAR W SADKAJSD ASDASDAS:D!!!!!!!!!!!!!!!!!!!!!!!!!! TO  "+ rozmiar);
        mAdres = intent.getStringExtra(ADRES);
        System.out.println(mAdres);
        Log.d("PS", "adres: " + mAdres);
        HttpURLConnection polaczenie = null;
        InputStream strumienZSieci = null;
        FileOutputStream strumienDoPliku = null;
        try {
            URL url = new URL(mAdres);
            File plikRoboczy = new File(url.getFile());
            File plikWyjsciowy = new File(
                    Environment.getExternalStorageDirectory() +
                            File.separator+ plikRoboczy.getName());
            Log.d("PS", "plik: " + plikWyjsciowy);
            if (plikWyjsciowy.exists())
                plikWyjsciowy.delete();
            //... - tworzenie połączenia
            mPostepInfo.mPobranychBajtow = 0;
            mPostepInfo.mRozmiar = rozmiar;
                    //pliku
                    mPostepInfo.mWynik = PostepInfo.POBIERANIE_TRWA;
            mTypPliku = typ; //... - pobieranie typu pliku
                    DataInputStream czytnik = new DataInputStream(
                    polaczenie.getInputStream());
            strumienDoPliku =
                    new FileOutputStream(plikWyjsciowy.getPath());
            byte bufor[] = new byte[ROZMIAR_BLOKU];
            int pobrano = czytnik.read(bufor, 0, ROZMIAR_BLOKU);
            while (pobrano != -1) {
                strumienDoPliku.write(bufor, 0, pobrano);
                mPostepInfo.mPobranychBajtow += pobrano;
                pobrano = czytnik.read(bufor, 0, ROZMIAR_BLOKU);
            }
            mPostepInfo.mWynik = PostepInfo.POBIERANIE_OK;
        } catch (Exception e) {
            e.printStackTrace();
            mPostepInfo.mWynik = PostepInfo.POBIERANIE_BLAD;
        } finally {
            if (strumienZSieci != null) {
                try {
                    strumienZSieci.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if (strumienDoPliku != null) {
                try {
                    strumienDoPliku.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (polaczenie != null)
                polaczenie.disconnect();
        }
    }
}
