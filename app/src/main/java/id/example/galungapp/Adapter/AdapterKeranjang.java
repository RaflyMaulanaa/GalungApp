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
import com.example.sweetalert.SweetAlertDialog;
import com.google.android.libraries.places.internal.c;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import id.example.galungapp.Api.Model.DataJual;
import id.example.galungapp.Api.Model.DataKeranjang;
import id.example.galungapp.DetailJualActivity;
import id.example.galungapp.KeranjangActivity;
import id.example.galungapp.MainActivity;
import id.example.galungapp.R;
import id.example.galungapp.Storage.SharedPMUser;

public class AdapterKeranjang extends RecyclerView.Adapter<AdapterKeranjang.MyViewHolder> {

    public List<DataKeranjang> dataKeranjang;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_gambar, btn_hapus;
        TextView tv_stok, tv_nama, tv_harga, tv_jumlahbeli;
        Button btn_pesan;

        public MyViewHolder(@NonNull View view) {
            super(view);
            iv_gambar = (ImageView) view.findViewById(R.id.iv_gambar);
            btn_hapus = (ImageView)  view.findViewById(R.id.btn_hapus);
            tv_stok = (TextView) view.findViewById(R.id.tv_stok);
            tv_nama = (TextView) view.findViewById(R.id.tv_nama);
            tv_harga = (TextView) view.findViewById(R.id.tv_harga);
            tv_jumlahbeli = (TextView) view.findViewById(R.id.tv_jumlahbeli);
            btn_pesan = (Button) view.findViewById(R.id.btn_pesan);
        }
    }

    public AdapterKeranjang(List<DataKeranjang> list, Context context2) {
        dataKeranjang = list;
        context = context2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_keranjang, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String imgUrl = "https://galung-app.asdar12.my.id/storage/"+dataKeranjang.get(position).getGambar();
        Glide.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.dua)
                .centerCrop()
                .into(holder.iv_gambar);
        holder.tv_nama.setText(dataKeranjang.get(position).getNama());
        holder.tv_jumlahbeli.setText(dataKeranjang.get(position).getJumlah());
        if(dataKeranjang.get(position).getJenis().equals("beras") || dataKeranjang.get(position).getJenis().equals("bibit")) {
//            holder.tv_stok.setText("Tersisa " + dataKeranjang.get(position).getStok() + " Karung");
            holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataKeranjang.get(position).getHarga_satuan())) + " / Kg");
        }
        if(dataKeranjang.get(position).getJenis().equals("alat")){
//            holder.tv_stok.setText("Tersisa " + dataBeras.get(position).getStok() + " Unit");
            holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataKeranjang.get(position).getHarga_satuan())) + " / Unit");
//            holder.tv_minpembelian.setVisibility(View.GONE);
        }
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Hapus Keranjang");
                sweetAlertDialog.setContentText("Apakah anda yakin ingin menghapus "+dataKeranjang.get(position).getNama()+" dari keranjang ?");
                sweetAlertDialog.setConfirmText("Ya, Hapus!");
                sweetAlertDialog.setCancelText("Tidak");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if(context instanceof KeranjangActivity){
                                    ((KeranjangActivity)context).hapusKeranjang(dataKeranjang.get(position).getId_item());
                                }
                                sweetAlertDialog.dismiss();
                            }
                        });
                sweetAlertDialog.show();
            }
        });
    }

    public int getItemCount() {
        return dataKeranjang.size();
    }

    public void updateDataKeranjang(List<DataKeranjang> list) {
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

