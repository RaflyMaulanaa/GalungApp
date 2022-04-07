package id.example.galungapp.Adapter;

import android.content.Context;
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

import id.example.galungapp.Api.Model.DataKeranjang;
import id.example.galungapp.R;

public class AdapterDetailPembayaran extends RecyclerView.Adapter<AdapterDetailPembayaran.MyViewHolder> {

    public List<DataKeranjang> dataKeranjang;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama, tv_harga, tv_jumlah, tv_subtotal;
        Button btn_pesan;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_nama = (TextView) view.findViewById(R.id.tv_nama);
            tv_harga = (TextView) view.findViewById(R.id.tv_harga);
            tv_jumlah = (TextView) view.findViewById(R.id.tv_jumlah);
            tv_subtotal = (TextView) view.findViewById(R.id.tv_subtotal);
        }
    }

    public AdapterDetailPembayaran(List<DataKeranjang> list, Context context2) {
        dataKeranjang = list;
        context = context2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detailpembayaran, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_nama.setText(dataKeranjang.get(position).getNama());
        if(dataKeranjang.get(position).getJenis().equals("beras") || dataKeranjang.get(position).getJenis().equals("bibit")) {
            holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataKeranjang.get(position).getHarga_satuan())) + " / Kg");
            holder.tv_jumlah.setText(dataKeranjang.get(position).getJumlah() + " Kg");
        }
        if(dataKeranjang.get(position).getJenis().equals("alat")){
            holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataKeranjang.get(position).getHarga_satuan())) + " / Unit");
            holder.tv_jumlah.setText(dataKeranjang.get(position).getJumlah() + " Unit");
        }
        holder.tv_subtotal.setText(formatRupiah(Double.parseDouble(dataKeranjang.get(position).getSubtotal())));
    }

    public int getItemCount() {
        return dataKeranjang.size();
    }

    public void updateDataDetailPembayaran(List<DataKeranjang> list) {
        dataKeranjang.clear();
        dataKeranjang.addAll(list);
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

