package com.kelompok4.ijournal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kelompok4.ijournal.Adapter.AdapterData;
import com.kelompok4.ijournal.Model.ModelData;
import com.kelompok4.ijournal.Util.AppController;
import com.kelompok4.ijournal.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    ProgressDialog pd;
    SwipeRefreshLayout swLayout;
    TextView tvdisconn;
    TextView tvquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi SwipeRefreshLayout
        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        mRecyclerview = (RecyclerView)findViewById(R.id.recycler);
        pd = new ProgressDialog(MainActivity.this);
        mItems = new ArrayList<>();
        mAdapter = new AdapterData(MainActivity.this, mItems);
        mManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mRecyclerview.setAdapter(mAdapter);
        swLayout.setColorSchemeResources(R.color.colorPrimary, R.color.bg_screen2, R.color.bg_screen3, R.color.bg_screen4);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        tvdisconn = (TextView)findViewById(R.id.tvdisconn);
        tvquery = (TextView)findViewById(R.id.tvquery);


        loadJson();

    }

    //untuk fungsi refresh
    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mItems.clear();
                loadJson();
                swLayout.setRefreshing(false);
                tvquery.setVisibility(View.GONE);
            }
        },1000);
    }

    private void loadJson_search(final String query){
        pd.setTitle("Sedang memuat data");
        pd.setIcon(R.drawable.refresh);
        pd.setMessage("Tunggu sebentar ya, pastikan kamu terhubung ke internet :)");
        pd.setCancelable(false);
        pd.show();
        JsonArrayRequest requestdata = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_SEARCH_SEKOLAH+query.replaceAll(" ", "%20"), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : "+ response.toString());
                        if (response.length() < 1){
                            customtoast("Sekolah tidak ditemukan :(");
                        } else {
                            for (int i=0; i<response.length();i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelData md = new ModelData();
                                    md.setAlamatSekolah(data.getString("alamat_sekolah"));
                                    md.setNamaSekolah(data.getString("nama_sekolah"));
                                    md.setId(data.getString("id_sekolah"));
                                    md.setMin(data.getString("min_nilai"));
                                    md.setNo_sekolah(data.getString("telp_sekolah"));
                                    md.setEmail_sekolah(data.getString("email_sekolah"));
                                    md.setWebsite_sekolah(data.getString("website_sekolah"));
                                    md.setKuota(data.getString("kuota"));
                                    md.setInfo_umum(data.getString("info_umum"));
                                    //md.setDetail_siswa(data.getString("nama_siswa"));
                                    mItems.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        tvdisconn.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        customtoast("Hasil pencarian : "+query);
                        //Toast.makeText(MainActivity.this, "Data berhasil dimuat... :)", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        showDisconnDialog();
                        //Toast.makeText(MainActivity.this, "Perikssa koneksi internet kamu, kemudian tarik ke bawah untuk memperbarui", Toast.LENGTH_LONG).show();
                        Log.d("volley", "Error "+error.getMessage());

                        tvdisconn.setVisibility(View.VISIBLE);
                    }
                });
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
                mItems.clear();
                loadJson_search(query);
                tvquery.setText("Hasil pencarian : "+query);
                tvquery.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //search
    private void loadJson(){
        pd.setTitle("Sedang memuat data");
        pd.setIcon(R.drawable.refresh);
        pd.setMessage("Tunggu sebentar ya, pastikan kamu terhubung ke internet :)");
        pd.setCancelable(false);
        pd.show();
        JsonArrayRequest requestdata = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_DATA, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.cancel();
                        Log.d("volley", "response : "+ response.toString());

                        for (int i=0; i<response.length();i++){
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setAlamatSekolah(data.getString("alamat_sekolah"));
                                md.setNamaSekolah(data.getString("nama_sekolah"));
                                md.setId(data.getString("id_sekolah"));
                                md.setMin(data.getString("min_nilai"));
                                md.setNo_sekolah(data.getString("telp_sekolah"));
                                md.setEmail_sekolah(data.getString("email_sekolah"));
                                md.setWebsite_sekolah(data.getString("website_sekolah"));
                                md.setKuota(data.getString("kuota"));
                                md.setInfo_umum(data.getString("info_umum"));
                                //md.setDetail_siswa(data.getString("nama_siswa"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        tvdisconn.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        customtoast("Data Berhasil dimuat :)");
                        //Toast.makeText(MainActivity.this, "Data berhasil dimuat... :)", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        showDisconnDialog();
                        //Toast.makeText(MainActivity.this, "Perikssa koneksi internet kamu, kemudian tarik ke bawah untuk memperbarui", Toast.LENGTH_LONG).show();
                        Log.d("volley", "Error "+error.getMessage());

                        tvdisconn.setVisibility(View.VISIBLE);
                    }
                });
        AppController.getInstance().AddToRequestQueue(requestdata);
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
        Intent intent = new Intent(MainActivity.this, Masukan.class);
        startActivity(intent);
    }

    private void showBantuan(){
        Intent intent = new Intent(MainActivity.this, Bantuan.class);
        startActivity(intent);
    }

    private void showTentang(){
        Intent intent = new Intent(MainActivity.this, Tentang.class);
        startActivity(intent);
    }

    private void showDisconnDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Oops... ada yang salah nih :(")
                .setIcon(R.drawable.unable)
                .setMessage("Periksa koneksi internet kamu, kemudian tarik ke bawah untuk memuat data")
                .setCancelable(false)
                .setNegativeButton("Ok", null)
                .show();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar dari iJournal")
                .setMessage("Apakah kamu yakin akan keluar?")
                .setIcon(R.drawable.ask)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
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
