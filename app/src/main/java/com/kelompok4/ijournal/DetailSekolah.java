package com.kelompok4.ijournal;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kelompok4.ijournal.Model.ModelData;

public class DetailSekolah extends AppCompatActivity {

    TextView tvnama_sekolah;
    TextView tvalamat_sekolah;
    TextView tvtellp_sekolah;
    TextView tvmin;
    TextView tvemail_sekolah;
    TextView tvwebsite_sekolah;
    TextView tvkuota;
    TextView tvinfo_umum;
    TextView tvdetail_siswa;
    Button btnjurnal;
    TextView id_sekolahl;
    ModelData md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sekolah);

        //replace title to icon
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);

        tvnama_sekolah = (TextView)findViewById(R.id.nama_sekolah1);
        tvalamat_sekolah = (TextView) findViewById(R.id.alamat_sekolah1);
        tvtellp_sekolah = (TextView) findViewById(R.id.telp_sekolah);
        tvmin = (TextView) findViewById(R.id.min1);
        tvnama_sekolah = (TextView) findViewById(R.id.nama_sekolah1);
        tvemail_sekolah = (TextView) findViewById(R.id.email_sekolah);
        tvwebsite_sekolah = (TextView) findViewById(R.id.website_sekolah);
        tvinfo_umum = (TextView) findViewById(R.id.info_umum);
        btnjurnal = (Button)findViewById(R.id.btnjournal);
        id_sekolahl = (TextView)findViewById(R.id.id_sekolah);

        final Bundle bundle = getIntent().getExtras();
        tvnama_sekolah.setText(bundle.getString("nama_sekolah"));
        tvalamat_sekolah.setText(bundle.getString("alamat_sekolah"));
        tvtellp_sekolah.setText(bundle.getString("telp_sekolah"));
        tvmin.setText("Nilai rata-rata : "+bundle.getString("min")+" | Kuota : "+bundle.getString("kuota"));
        tvemail_sekolah.setText(bundle.getString("email_sekolah"));
        tvwebsite_sekolah.setText(bundle.getString("website_sekolah"));
        tvinfo_umum.setText(bundle.getString("info_umum"));
        id_sekolahl.setText(bundle.getString("id"));
        Linkify.addLinks(tvwebsite_sekolah, Linkify.WEB_URLS);
        Linkify.addLinks(tvtellp_sekolah, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(tvemail_sekolah, Linkify.EMAIL_ADDRESSES);
        //tvinfo_umum.setText(bundle.getString("nama_siswa"));
        //tvdetail_siswa.setText(bundle.getString("detail_siswa"));

        btnjurnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailSekolah.this, Jurnal.class);
                i.putExtra("id", id_sekolahl.getText());
                i.putExtra("nama_sekolah", tvnama_sekolah.getText());
                i.putExtra("min", bundle.getString("min"));
                i.putExtra("kuota", bundle.getString("kuota"));
                startActivity(i);
            }
        });
    }
}