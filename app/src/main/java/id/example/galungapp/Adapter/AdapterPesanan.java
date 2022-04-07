package id.example.galungapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import id.example.galungapp.Api.Model.DataKeranjang;
import id.example.galungapp.Api.Model.DataPesanan;
import id.example.galungapp.R;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.MyViewHolder> {

    public List<DataPesanan> dataPesanan;
    public Context context;

    private String status;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notransaksi, tv_status, tv_tanggal;
        ImageView iv_status;
        CardView cv_datapesanan;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_notransaksi = (TextView) view.findViewById(R.id.tv_notransaksi);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_tanggal = (TextView) view.findViewById(R.id.tv_tanggal);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);
            cv_datapesanan = (CardView) view.findViewById(R.id.cv_datapesanan);
        }
    }

    public AdapterPesanan(List<DataPesanan> list, Context context2, String status) {
        dataPesanan = list;
        context = context2;
        this.status = status;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pesanan, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_notransaksi.setText(dataPesanan.get(position).getTransaksi_code());
        if(status.equals("Pesanan Selesai")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terverifikasi_icon));
        }
        if(status.equals("Pesanan Dibatalkan")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_batalkan_verifikasi_icon));
            holder.tv_status.setTextColor(Color.RED);
        }
        if(status.equals("Pesanan Diproses")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_belum_terverifikasi_icon));
        }
        holder.tv_status.setText(status);
    }

    public int getItemCount() {
        return dataPesanan.size();
    }

    public void updateDataPesanan(List<DataPesanan> list) {
        dataPesanan.clear();
        dataPesanan.addAll(list);
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

