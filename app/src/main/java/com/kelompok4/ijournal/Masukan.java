package com.kelompok4.ijournal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kelompok4.ijournal.Util.AppController;
import com.kelompok4.ijournal.Util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Masukan extends AppCompatActivity {

    EditText txtemail;
    EditText txtmasukan;
    Button btnMasukan;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masukan);

        //replace title to icon
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        txtemail = (EditText)findViewById(R.id.etEmail);
        txtmasukan = (EditText)findViewById(R.id.etMasukan);
        btnMasukan = (Button)findViewById(R.id.btnKirimMasukan);
        pd = new ProgressDialog(Masukan.this);

        btnMasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtemail.getText().toString().equals("")){
                    txtemail.setError("Harap masukkan email anda");
                } else if (txtmasukan.getText().toString().equals("")){
                    txtmasukan.setError("Harap masukkan saran anda");
                }
                else {
                    insertMasukan();
                }
            }
        });

    }

    private void insertMasukan(){
        pd.setTitle("Sedang mengirim :)");
        pd.setMessage("Tunggu sebentar ya...");
        pd.setIcon(R.drawable.warning);
        pd.setCancelable(false);
        pd.show();
        StringRequest insert = new StringRequest(Request.Method.POST, ServerAPI.URL_INSERT_MASUKAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        txtemail.setText("");
                        txtmasukan.setText("");
                        customtoast("Terimakasih, saran dan masukan kamu berhasil dikirim :)");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        customtoast("Sayang sekali pesan anda tidak dapat diproses :(");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email_masukan", txtemail.getText().toString());
                map.put("isi_masukan", txtmasukan.getText().toString());

                return map;
            }
        };
        AppController.getInstance().AddToRequestQueue(insert);
    }

    private void customtoast(String pesan){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.texttoast);
        text.setText(pesan);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void showSuccessDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Terimakasih :)")
                .setIcon(R.drawable.warning)
                .setMessage("Saran dan masukan kamu telah dikirim")
                .setCancelable(false)
                .setNegativeButton("Ok", null)
                .show();
    }

}
