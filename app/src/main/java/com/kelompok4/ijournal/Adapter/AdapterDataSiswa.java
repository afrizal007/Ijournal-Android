package com.kelompok4.ijournal.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelompok4.ijournal.Model.ModelData;
import com.kelompok4.ijournal.Jurnal;
import com.kelompok4.ijournal.R;

import java.util.Collections;
import java.util.List;

public class AdapterDataSiswa extends RecyclerView.Adapter<AdapterDataSiswa.MyHolder> {

    private List<ModelData> mItems;
    private Context context;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterDataSiswa(Context context, List<ModelData> items){
        this.mItems = items;
        this.context = context;
    }

    // Inflate the layout when viewholder created
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_siswa,parent,false);
        MyHolder holderData = new MyHolder(layout);
        return holderData;
    }

    // Bind data
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        ModelData md = mItems.get(position);
        holder.tvnama_siswa.setText(md.getNama_siswa());
        holder.tvno_daftar.setText(md.getNo_daftar());
        holder.tvnilai_siswa.setText(md.getNilai_siswa());
        holder.tvno_urut.setText(md.getNo_urut());
        holder.md = md;
        /*if (Integer.parseInt(holder.md.getNilai_siswa()) <= Integer.parseInt(holder.md.getMin())){
            holder.tvnama_siswa.setTextColor(R.color.bg_screen1);
            holder.tvno_daftar.setTextColor(R.color.bg_screen1);
        }*/

        /*holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("detail", "Detail Sekolah");
                Intent intent = new Intent(context, DetailSekolah.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });*/

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class MyHolder extends ViewHolder{
        TextView tvnama_siswa, tvno_daftar, tvnilai_siswa, tvno_urut;
        //CardView card;
        ModelData md;

        public MyHolder(View view){
            super(view);

            tvnama_siswa = (TextView) view.findViewById(R.id.nama_siswa);
            tvno_daftar = (TextView) view.findViewById(R.id.no_daftar);
            tvnilai_siswa = (TextView) view.findViewById(R.id.nilai_siswa);
            tvno_urut = (TextView) view.findViewById(R.id.no_urut);

            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(context, DetailSekolah.class);
                    detail.putExtra("id", md.getId());
                    detail.putExtra("nama_sekolah", md.getNamaSekolah());
                    detail.putExtra("alamat_sekolah", md.getAlamatSekkolah());
                    detail.putExtra("telp_sekolah", md.getNo_sekolah());
                    detail.putExtra("min", md.getMin());
                    detail.putExtra("email_sekolah", md.getEmail_sekolah());
                    detail.putExtra("website_sekolah", md.getWebsite_sekolah());
                    detail.putExtra("kuota", md.getKuota());
                    detail.putExtra("info_umum", md.getInfo_umum());
                    //detail.putExtra("detail_siswa", md.getDetail_siswa());

                    context.startActivity(detail);
                }
            });*/
        }
    }

}