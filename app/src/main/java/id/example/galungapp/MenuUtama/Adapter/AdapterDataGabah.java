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
import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.R;

public class AdapterDataGabah extends RecyclerView.Adapter<AdapterDataGabah.MyViewHolder> {

    public List<DataGabah> dataGabah;
    public Context context;

    private String status;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_jenisgabah, tv_status, tv_alamat, tv_jumlah, tv_harga, tv_total, tv_tanggal;
        CardView cv_datagabah;
        ImageView iv_status;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_jenisgabah = (TextView) view.findViewById(R.id.tv_jenisgabah);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_alamat = (TextView) view.findViewById(R.id.tv_alamat);
            tv_jumlah = (TextView) view.findViewById(R.id.tv_jumlah);
            tv_harga = (TextView) view.findViewById(R.id.tv_harga);
            tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_tanggal = (TextView) view.findViewById(R.id.tv_tanggal);
            cv_datagabah = (CardView) view.findViewById(R.id.cv_datagabah);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);
        }
    }

    public AdapterDataGabah(List<DataGabah> list, Context context2, String status) {
        dataGabah = list;
        context = context2;
        this.status = status;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_datagabah, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String tanggaltransaksi = null;
        if(status.equals("Verifikasi")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terverifikasi_icon));
            tanggaltransaksi = dataGabah.get(position).getUpdated_at();
        }
        if(status.equals("Dibatalkan")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_batalkan_verifikasi_icon));
            holder.tv_status.setTextColor(Color.RED);
            tanggaltransaksi = dataGabah.get(position).getUpdated_at();
        }
        if(status.equals("Menunggu Verifikasi")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_belum_terverifikasi_icon));
            tanggaltransaksi = dataGabah.get(position).getCreated_at();
        }
        holder.tv_jenisgabah.setText(dataGabah.get(position).getNama_gabah());
        holder.tv_status.setText(status);
        holder.tv_alamat.setText(dataGabah.get(position).getAlamat());
        holder.tv_jumlah.setText(dataGabah.get(position).getJumlah()+" Kg");
        holder.tv_harga.setText(formatRupiah(Double.parseDouble(dataGabah.get(position).getHarga()))+" /Kg");
        int harga = Integer.parseInt(dataGabah.get(position).getHarga());
        int jumlah = Integer.parseInt(dataGabah.get(position).getJumlah());
        int total = harga * jumlah;
        holder.tv_total.setText(formatRupiah(Double.parseDouble(String.valueOf(total))));
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
        return dataGabah.size();
    }

    public void updateDataGabah(List<DataGabah> list) {
        dataGabah.clear();
        dataGabah.addAll(list);
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
