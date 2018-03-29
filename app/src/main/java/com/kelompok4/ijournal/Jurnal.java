package com.kelompok4.ijournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kelompok4.ijournal.Adapter.AdapterDataSiswa;
import com.kelompok4.ijournal.Model.ModelData;
import com.kelompok4.ijournal.Util.AppController;
import com.kelompok4.ijournal.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jurnal extends AppCompatActivity {

    TextView tvid_sekolah;
    RecyclerView mRecyclerviewSiswa;
    RecyclerView.Adapter mAdapter_siswa;
    RecyclerView.LayoutManager mManager_siswa;
    List<ModelData> mItems_siswa;

    ProgressDialog pd;
    SwipeRefreshLayout sw_siswa;
    TextView tvnama_siswa;
    TextView tvnama_sekolah;
    TextView tvdisconn;
    TextView tvrata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurnal);

        tvid_sekolah = (TextView)findViewById(R.id.id_skolah_siswa);
        tvnama_siswa = (TextView)findViewById(R.id.nama_siswa);
        tvnama_sekolah = (TextView)findViewById(R.id.nama_sekolah_siswa);
        tvrata = (TextView)findViewById(R.id.rata_rata);

        Bundle bundle = getIntent().getExtras();
        tvid_sekolah.setText(bundle.getString("id"));
        tvnama_sekolah.setText(bundle.getString("nama_sekolah"));
        tvdisconn = (TextView)findViewById(R.id.tvdisconn_siswa);
        tvrata.setText("Nilai rata-rata : "+bundle.getString("min")+" | Kuota : "+bundle.getString("kuota"));

        // Inisialisasi SwipeRefreshLayout
        sw_siswa = (SwipeRefreshLayout) findViewById(R.id.swlayout_siswa);
        mRecyclerviewSiswa = (RecyclerView)findViewById(R.id.recycler_siswa);
        pd = new ProgressDialog(Jurnal.this);
        mItems_siswa = new ArrayList<>();
        mAdapter_siswa = new AdapterDataSiswa(Jurnal.this, mItems_siswa);
        mManager_siswa = new LinearLayoutManager(Jurnal.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerviewSiswa.setLayoutManager(mManager_siswa);
        mRecyclerviewSiswa.setAdapter(mAdapter_siswa);

        sw_siswa.setColorSchemeResources(R.color.colorPrimary, R.color.bg_screen2, R.color.bg_screen3, R.color.bg_screen4);
        sw_siswa.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        loadJson();

    }

    //untuk fungsi refresh
    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mItems_siswa.clear();
                loadJson();
                sw_siswa.setRefreshing(false);
            }
        },1000);
    }

    private void loadJson(){
        pd.setTitle("Sedang memuat data");
        pd.setIcon(R.drawable.refresh);
        pd.setMessage("Tunggu sebentar ya, pastikan kamu terhubung ke internet :)");
        pd.setCancelable(false);
        pd.show();
        String parameter = tvid_sekolah.getText().toString();
        String url_siswa = ServerAPI.URL_SISWA+parameter;
        JsonArrayRequest requestdata = new JsonArrayRequest(Request.Method.POST, url_siswa, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : "+ response.toString());
                        int no = 1;
                        if (response.length()<1){
                            showDisconnDialog("Sayang sekali data tidak ada :(", "Coba hubungi sekolahmu untuk memperoleh data");
                        }else {
                            for (int i=0; i<response.length();i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelData md = new ModelData();
                                    md.setNama_siswa(data.getString("nama_siswa"));
                                    md.setNo_daftar(data.getString("no_pendaftaran"));
                                    md.setNilai_siswa(data.getString("nilai_siswa"));
                                    md.setMin(data.getString("min_nilai"));
                                    String urut = String.valueOf(no++);
                                    md.setNo_urut(urut);
                                    mItems_siswa.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            customtoast("Data berhasil dimuat... :)");
                        }
                        mAdapter_siswa.notifyDataSetChanged();
                        tvdisconn.setVisibility(View.GONE);
                        //Toast.makeText(MainActivity.this, "Data berhasil dimuat... :)", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        showDisconnDialog("Oops... ada yang salah nih :(","Periksa koneksi internet kamu, kemudian tarik ke bawah untuk memuat data");
                        //Toast.makeText(MainActivity.this, "Perikssa koneksi internet kamu, kemudian tarik ke bawah untuk memperbarui", Toast.LENGTH_LONG).show();
                        Log.d("volley", "Error "+error.getMessage());
                        tvdisconn.setVisibility(View.VISIBLE);
                    }
                })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_sekolah", tvid_sekolah.getText().toString());
                //params.put("2ndParamName","valueoF2ndParam");
                return params;
            }
        }*/;

        AppController.getInstance().AddToRequestQueue(requestdata);
    }

    //search
    private void loadJson_search(final String query){
        pd.setTitle("Sedang memuat data");
        pd.setIcon(R.drawable.refresh);
        pd.setMessage("Tunggu sebentar ya, pastikan kamu terhubung ke internet :)");
        pd.setCancelable(false);
        pd.show();
        String parameter = tvid_sekolah.getText().toString();
        String url_siswa = ServerAPI.URL_SEARCH_SISWA+parameter+"/"+query.replaceAll(" ", "%20");
        JsonArrayRequest requestdata = new JsonArrayRequest(Request.Method.POST, url_siswa, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : "+ response.toString());
                        int no = 1;
                        if (response.length()<1){
                            customtoast("Nama tidak ditemukan :(");
                        }else {
                            for (int i=0; i<response.length();i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelData md = new ModelData();
                                    md.setNama_siswa(data.getString("nama_siswa"));
                                    md.setNo_daftar(data.getString("no_pendaftaran"));
                                    md.setNilai_siswa(data.getString("nilai_siswa"));
                                    md.setMin(data.getString("min_nilai"));
                                    String urut = String.valueOf(no++);
                                    md.setNo_urut(urut);
                                    mItems_siswa.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            customtoast("Hasil pencarian : "+query);
                        }
                        mAdapter_siswa.notifyDataSetChanged();
                        tvdisconn.setVisibility(View.GONE);
                        //Toast.makeText(MainActivity.this, "Data berhasil dimuat... :)", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        showDisconnDialog("Oops... ada yang salah nih :(","Periksa koneksi internet kamu, kemudian tarik ke bawah untuk memuat data");
                        //Toast.makeText(MainActivity.this, "Perikssa koneksi internet kamu, kemudian tarik ke bawah untuk memperbarui", Toast.LENGTH_LONG).show();
                        Log.d("volley", "Error "+error.getMessage());
                        tvdisconn.setVisibility(View.VISIBLE);
                    }
                })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_sekolah", tvid_sekolah.getText().toString());
                //params.put("2ndParamName","valueoF2ndParam");
                return params;
            }
        }*/;

        AppController.getInstance().AddToRequestQueue(requestdata);
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_option, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mItems_siswa.clear();
                loadJson_search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.masukan:
                showMasukan();
                return true;
            case R.id.bantuan:
                showBantuan();
                return true;
            case R.id.tentang:
                showTentang();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMasukan(){
        Intent intent = new Intent(Jurnal.this, Masukan.class);
        startActivity(intent);
    }

    private void showBantuan(){
        Intent intent = new Intent(Jurnal.this, Bantuan.class);
        startActivity(intent);
    }

    private void showTentang(){
        Intent intent = new Intent(Jurnal.this, Tentang.class);
        startActivity(intent);
    }

    private void showDisconnDialog(String judul, String pesan){
        new AlertDialog.Builder(this)
                .setTitle(judul)
                .setIcon(R.drawable.unable)
                .setMessage(pesan)
                .setCancelable(false)
                .setNegativeButton("Ok", null)
                .show();
    }

    private void customtoast(String pesan){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.texttoast);
        text.setText(pesan);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
