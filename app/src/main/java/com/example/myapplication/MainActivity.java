package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonsubmit;
    EditText nik;
    String url = "https://steveandika.com/";
    TextView tanggalLahir;
    TextView provinsi;
    TextView kota;
    TextView kecamatan,textView1,textView2,textView3,textView4;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiate();


    }
    private void initiate(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        tanggalLahir = findViewById(R.id.tanggalLahir);
        kota = findViewById(R.id.Kota);
        textView1= findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3=findViewById(R.id.textview3);
        textView4=findViewById(R.id.textview4);
        kecamatan=findViewById(R.id.Kecamatan);
        provinsi=findViewById(R.id.Provinsi);
        progressBar = findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        buttonsubmit = findViewById(R.id.buttonsubmit);
        nik = findViewById(R.id.nik);
        buttonsubmit.setOnClickListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        kota.setVisibility(View.GONE);
        kecamatan.setVisibility(View.GONE);
        tanggalLahir.setVisibility(View.GONE);
        provinsi.setVisibility(View.GONE);
        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView4.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.buttonsubmit) {

            String niks = nik.getText().toString();

            if (niks.length() < 16) {
                Snackbar.make(view, "Masukan 16 Digit Nik yang benar", Snackbar.LENGTH_LONG).show();

            }
            if (niks.length() == 16 || niks.length() > 16) {
                final StringBuilder kodeProvinsi = new StringBuilder();
                final StringBuilder kodeKota = new StringBuilder();
                final StringBuilder kodeKecamatan = new StringBuilder();
                final StringBuilder kodeTanggal = new StringBuilder();
                StringBuilder kodeBulan = new StringBuilder();
                StringBuilder kodeTahun = new StringBuilder();
                for (int i = 0; i < 2; i++) {
                    kodeProvinsi.append(niks.charAt(i));
                }
                for (int i = 2; i < 4; i++) {
                    String chars = String.valueOf(niks.charAt(i));
                    if (!chars.contains("0")) {
                        kodeKota.append(niks.charAt(i));

                    }


                }
                for (int i = 4; i < 6; i++) {
                    kodeKecamatan.append(niks.charAt(i));

                }
                for (int i = 6; i < 8; i++) {
                    kodeTanggal.append(niks.charAt(i));

                }
                for (int i = 8; i < 10; i++) {
                    String chars = String.valueOf(niks.charAt(i));
                    if (!chars.contains("0")) {
                        if (i == 8 && !chars.contains("1")) {

                        } else {
                            kodeBulan.append(niks.charAt(i));
                        }
                    }


                }
                for (int i = 10; i < 12; i++) {
                    String chars = String.valueOf(niks.charAt(i));
                    if (!chars.contains("0")) {
                        kodeTahun.append(niks.charAt(i));

                    }
                    if (i == 11 && chars.contains("0"))
                        kodeTahun.append(niks.charAt(i));

                }



                Log.d("String", kodeKota.toString());
                String bulans = kodeBulan.toString();
                if (!bulans.isEmpty()) {
                    final int bulan = Integer.parseInt(bulans);
                    if (bulan < 13 && bulan > 0) {
                        kota.setVisibility(View.GONE);
                        kecamatan.setVisibility(View.GONE);
                        tanggalLahir.setVisibility(View.GONE);
                        provinsi.setVisibility(View.GONE);
                        textView1.setVisibility(View.GONE);
                        textView2.setVisibility(View.GONE);
                        textView3.setVisibility(View.GONE);
                        textView4.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        final String tahuns = kodeTahun.toString();
                        Retrofit retrofit  = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        final GetNIK getNIK = retrofit.create(GetNIK.class);
                        Call<Value> call = getNIK.sendIdProvinsi(kodeProvinsi.toString());
                        Log.d("kodeProvinsi",kodeProvinsi.toString());
                        call.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                if(response.body()!=null && response.body().value!=null ){
                                    if(response.body().value.contains("1")){

                                        final Alamat alamat =response.body().getResult();

                                        Call<Value> call2 = getNIK.sendIdKota(kodeProvinsi.toString()+"."+ kodeKota.toString());
                                        call2.enqueue(new Callback<Value>() {
                                            @Override
                                            public void onResponse(Call<Value> call, Response<Value> response) {
                                                if(response.body()!=null && response.body().value!=null ){
                                                    if(response.body().value.contains("1")){
                                                        final Alamat alamat2 = response.body().getResult();
                                                        String kodeKota2 = kodeKota.toString();
                                                        if(kodeKota2.length()<2){
                                                            kodeKota2 = "0"+ kodeKota2;
                                                        }
                                                        Call<Value> call3 = getNIK.sendIdKecamatan(kodeProvinsi.toString()+"."+kodeKota2+"."+kodeKecamatan.toString());
                                                        call3.enqueue(new Callback<Value>() {
                                                            @Override
                                                            public void onResponse(Call<Value> call, Response<Value> response) {
                                                                if(response.body()!=null && response.body().value!=null ){
                                                                    if(response.body().value.contains("1")){
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Alamat alamat3 = response.body().result;
                                                                        provinsi.setText(alamat.getProvinsi());
                                                                        kota.setText(alamat2.getKota());
                                                                        int tahun = Integer.parseInt(tahuns);

                                                                        int tahunDepan = 0;
                                                                        if (tahun < 10) {
                                                                            tahunDepan = 2000;
                                                                        } else if (tahun > 9) {
                                                                            tahunDepan = 1900;

                                                                        }
                                                                        tahun = tahunDepan + tahun;
                                                                        String[] bulanss = getResources().getStringArray(R.array.bulan);
                                                                        String tanggallahir = kodeTanggal + "/" + bulanss[bulan - 1] + "/" + tahun;
                                                                        tanggalLahir.setText(tanggallahir);
                                                                        kecamatan.setText(alamat3.getKecamatan());
                                                                        kota.setVisibility(View.VISIBLE);
                                                                        kecamatan.setVisibility(View.VISIBLE);
                                                                        tanggalLahir.setVisibility(View.VISIBLE);
                                                                        provinsi.setVisibility(View.VISIBLE);
                                                                        textView1.setVisibility(View.VISIBLE);
                                                                        textView2.setVisibility(View.VISIBLE);
                                                                        textView3.setVisibility(View.VISIBLE);
                                                                        textView4.setVisibility(View.VISIBLE);
                                                                    }
                                                                    else{
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Snackbar.make(view, "Masukan kode Nik Dengan Benar 6", Snackbar.LENGTH_LONG).show();

                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Value> call, Throwable t) {
                                                                progressBar.setVisibility(View.GONE);
                                                                Snackbar.make(view, "Jaringan anda bermasalah", Snackbar.LENGTH_LONG).show();

                                                            }
                                                        });



                                                    }else{
                                                        progressBar.setVisibility(View.GONE);
                                                        Snackbar.make(view, "Masukan kode Nik Dengan Benar 4", Snackbar.LENGTH_LONG).show();
                                                    }

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Value> call, Throwable t) {
                                                progressBar.setVisibility(View.GONE);
                                                Snackbar.make(view, "Jaringan anda bermasalah", Snackbar.LENGTH_LONG).show();

                                            }
                                        });


                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        Snackbar.make(view, "Masukan kode Nik Dengan Benar 1 ", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("String",t.toString());
                                Snackbar.make(view, "Jaringan anda bermasalah", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(view, "Masukan kode Nik Dengan Benar 2 ", Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(view, "Masukan kode Nik Dengan Benar 3 ", Snackbar.LENGTH_LONG).show();
                }
            }


        }
    }
}
