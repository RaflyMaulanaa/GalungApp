package id.example.galungapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import id.example.galungapp.Api.Model.DataJual;
import id.example.galungapp.DetailJualActivity;
import id.example.galungapp.R;

public class AdapterListJual extends RecyclerView.Adapter<AdapterListJual.MyViewHolder> {

    public List<DataJual> dataJual;
    public Context context;
    private String jenisData;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_gambar;
        TextView tv_stok, tv_nama, tv_harga, tv_minpembelian;
        Button btn_pesan;

        public MyViewHolder(@NonNull View view) {
            super(view);
            iv_gambar = (ImageView) view.findViewById(R.id.iv_gambar);
            tv_stok = (TextView) view.findViewById(R.id.tv_stok);
            tv_nama = (TextView) view.findViewById(R.id.tv_nama);
            tv_harga = (TextView) view.findViewById(R.id.tv_harga);
            tv_minpembelian = (TextView) view.findViewById(R.id.tv_minpembelian);
            btn_pesan = (Button) view.findViewById(R.id.btn_pesan);
        }
    }

    public AdapterListJual(List<DataJual> list, Context context2, String data) {
        dataJual = list;
        context = context2;
        jenisData = data;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_jual, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailJualActivity.class);
                intent.putExtra("id",dataJual.get(position).getId());
                intent.putExtra("nama",dataJual.get(position).getNama());
                intent.putExtra("gambar",dataJual.get(position).getGambar());
                intent.putExtra("harga",dataJual.get(position).getHarga());
                intent.putExtra("jenis_data",jenisData);
                intent.putExtra("min_beli",dataJual.get(position).getMin_beli());
                intent.putExtra("stok",dataJual.get(position).getStok());
                intent.putExtra("keterangan",dataJual.get(position).getKeterangan());
                context.startActivity(intent);
            }
        });
        String imgUrl = "https://galung-app.asdar12.my.id/storage/"+dataJual.get(position).getGambar();
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.dua)
                .centerCrop()
                .into(holder.iv_gambar);
        holder.tv_nama.setText(dataJual.get(position).getNama());
        if(jenisData.equals("Beras") || jenisData.equals("BibitPupuk")) {
            holder.tv_stok.setText("Tersisa " + dataJual.get(position).getStok() + " Karung");
            holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataJual.get(position).getHarga())) + " / Kg");
            holder.tv_minpembelian.setVisibility(View.VISIBLE);
            holder.tv_minpembelian.setText("min. " + dataJual.get(position).getMin_beli() + " Kg");
        }
        if(jenisData.equals("Alat")){
            holder.tv_stok.setText("Tersisa " + dataJual.get(position).getStok() + " Unit");
            holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataJual.get(position).getHarga())) + " / Unit");
            holder.tv_minpembelian.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return dataJual.size();
    }

    public void updateDataJual(List<DataJual> list) {
        dataJual.clear();
        dataJual.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataJual(List<DataJual> list, String data) {
        jenisData = data;
        for (DataJual add : list) {
            dataJual.add(add);
        }
        notifyDataSetChanged();
    }

    private String formatRupiah(Double number){
        DecimalFormat formatRupiah = (DecimalFormat) NumberFormat.getInstance();
        Locale localeID = new Locale("in", "ID");
        String symbol = Currency.getInstance(localeID).getSymbol(localeID);
        formatRupiah.setGroupingUsed(true);
        formatRupiah.setPositivePrefix(symbol+" ");
        formatRupiah.setNegativePrefix(symbol+" -");
        return formatRupiah.format(number);
    }
}
