package com.example.laboratorium3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;




public class MainActivity extends AppCompatActivity {


    private Button przyciskPobierz;
    public TextView typPliku;
    public TextView rozmiarPliku;
    public String tempRozmiar;
    public String tempTyp;
    private Button przyciskPobierzPlik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        przyciskPobierz=findViewById(R.id.przyciskPobierzInfo);
        rozmiarPliku= findViewById(R.id.rozmiarPliku);
        typPliku=findViewById(R.id.typPliku);

        przyciskPobierz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView adresEdycja = (TextView) findViewById(R.id.adresEdycja);
                MainActivity.PobierzInfoTask pobierzInfoTask = new MainActivity.PobierzInfoTask();
                pobierzInfoTask.execute(new String[]{adresEdycja.getText().toString()});

            }
        });


        przyciskPobierzPlik =findViewById(R.id.przyciskPobierzPlik);
        przyciskPobierzPlik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pobierzPlik();
            }
        });



    }

    protected void pobierzPlik(){
        pobierzInfo();
        EditText adresEdycja =
                (EditText) findViewById(R.id.adresEdycja);
       // String temp =Integer.toString(this.mRozmiar);


        System.out.println(typPliku.getText().toString());



        Intent zamiarPobierania =
                new Intent(this, PobieranieService.class);
        zamiarPobierania.putExtra(PobieranieService.ADRES,
                adresEdycja.getText().toString());

        zamiarPobierania.putExtra("typ",
                typPliku.getText().toString());


        zamiarPobierania.putExtra("rozmiar",
                rozmiarPliku.getText().toString());
        startService(zamiarPobierania);
    }

    private class InfoOPliku {
        public int mRozmiar;
        public String mTyp;
    }

    private class PobierzInfoTask extends
            AsyncTask<String, Void, MainActivity.InfoOPliku> {
        @Override
        protected MainActivity.InfoOPliku doInBackground(String... params) {
            HttpURLConnection polaczenie = null;
            MainActivity.InfoOPliku infoOPliku = null;
            try {
                URL url = new URL(params[0]);
                polaczenie =
                        (HttpURLConnection) url.openConnection();
                polaczenie.setRequestMethod("GET");
                polaczenie.setDoOutput(true);
                infoOPliku = new MainActivity.InfoOPliku();
                infoOPliku.mRozmiar = polaczenie.getContentLength();
                infoOPliku.mTyp = polaczenie.getContentType();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (polaczenie != null)
                    polaczenie.disconnect();
            }

            return infoOPliku;
        }

        @Override
        protected void onPostExecute(MainActivity.InfoOPliku result) {
            ustawInfoOPliku(result);

            super.onPostExecute(result);
        }
    }

    private void ustawInfoOPliku(MainActivity.InfoOPliku infoOPliku) {
        this.tempTyp=infoOPliku.mTyp;
        String temp =Integer.toString(infoOPliku.mRozmiar);
        this.tempRozmiar = temp;
        typPliku.setText(infoOPliku.mTyp);
        rozmiarPliku.setText(temp);

    }

    protected void pobierzInfo() {
        TextView adresEdycja = (TextView) findViewById(R.id.adresEdycja);
        MainActivity.PobierzInfoTask pobierzInfoTask = new MainActivity.PobierzInfoTask();
        pobierzInfoTask.execute(new String[]{adresEdycja.getText().toString()});
    }



}