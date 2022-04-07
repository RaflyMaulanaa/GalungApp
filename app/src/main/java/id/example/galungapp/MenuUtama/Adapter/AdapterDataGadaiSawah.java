package id.example.galungapp.MenuUtama.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.example.galungapp.Api.Model.DataGabah;
import id.example.galungapp.Api.Model.DataGadaiSawah;
import id.example.galungapp.R;

public class AdapterDataGadaiSawah extends RecyclerView.Adapter<AdapterDataGadaiSawah.MyViewHolder> {

    public List<DataGadaiSawah> dataGadaiSawah;
    public Context context;

    private String status;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sawah, tv_tanggal, tv_status, tv_alamat, tv_periodegadai, tv_nilaigadai;
        CardView cv_datagadaisawah;
        ImageView iv_status;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_sawah = (TextView) view.findViewById(R.id.tv_sawah);
            tv_tanggal = (TextView) view.findViewById(R.id.tv_tanggal);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_alamat = (TextView) view.findViewById(R.id.tv_alamat);
            tv_periodegadai = (TextView) view.findViewById(R.id.tv_periodegadai);
            tv_nilaigadai = (TextView) view.findViewById(R.id.tv_nilaigadai);
            cv_datagadaisawah = (CardView) view.findViewById(R.id.cv_datagadaisawah);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);
        }
    }

    public AdapterDataGadaiSawah(List<DataGadaiSawah> list, Context context2, String status) {
        dataGadaiSawah = list;
        context = context2;
        this.status = status;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_datagadaisawah, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String tanggaltransaksi = null;
        if(status.equals("Verifikasi")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terverifikasi_icon));
            tanggaltransaksi = dataGadaiSawah.get(position).getUpdated_at();
        }
        if(status.equals("Selesai")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terverifikasi_icon));
            tanggaltransaksi = dataGadaiSawah.get(position).getUpdated_at();
        }
        if(status.equals("Dibatalkan")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_batalkan_verifikasi_icon));
            holder.tv_status.setTextColor(Color.RED);
            tanggaltransaksi = dataGadaiSawah.get(position).getUpdated_at();
        }
        if(status.equals("Menunggu Verifikasi")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_belum_terverifikasi_icon));
            tanggaltransaksi = dataGadaiSawah.get(position).getCreated_at();
        }
        holder.tv_sawah.setText(dataGadaiSawah.get(position).getNama_sawah());
        holder.tv_status.setText(status);
        holder.tv_periodegadai.setText(dataGadaiSawah.get(position).getPeriode());
        holder.tv_nilaigadai.setText(formatRupiah(Double.parseDouble(dataGadaiSawah.get(position).getHarga())));
        holder.tv_alamat.setText(dataGadaiSawah.get(position).getAlamat());
        if(tanggaltransaksi != null) {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat view = new SimpleDateFormat("dd MMMM yyyy");
            Date newDate = null;
            try {
                newDate = input.parse(tanggaltransaksi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String hari = (String) DateFormat.format("EEEE", newDate);
            holder.tv_tanggal.setText(hari+", "+view.format(newDate));
        }
    }

    public int getItemCount() {
        return dataGadaiSawah.size();
    }

    public void updateDataGadaiSawah(List<DataGadaiSawah> list) {
        dataGadaiSawah.clear();
        dataGadaiSawah.addAll(list);
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
