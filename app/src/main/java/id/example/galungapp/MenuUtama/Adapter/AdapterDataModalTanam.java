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

import id.example.galungapp.Api.Model.DataGadaiSawah;
import id.example.galungapp.Api.Model.DataModalTanam;
import id.example.galungapp.R;

public class AdapterDataModalTanam extends RecyclerView.Adapter<AdapterDataModalTanam.MyViewHolder> {

    public List<DataModalTanam> dataModalTanam;
    public Context context;

    private String status;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sawah, tv_tanggal, tv_status, tv_alamat, tv_jenisbibit, tv_jenispupuk, tv_periodetanam;
        CardView cv_datamodaltanam;
        ImageView iv_status;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_sawah = (TextView) view.findViewById(R.id.tv_sawah);
            tv_tanggal = (TextView) view.findViewById(R.id.tv_tanggal);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_alamat = (TextView) view.findViewById(R.id.tv_alamat);
            tv_jenisbibit = (TextView) view.findViewById(R.id.tv_jenisbibit);
            tv_jenispupuk = (TextView) view.findViewById(R.id.tv_jenispupuk);
            tv_periodetanam = (TextView) view.findViewById(R.id.tv_periodetanam);
            cv_datamodaltanam = (CardView) view.findViewById(R.id.cv_modaltanam);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);
        }
    }

    public AdapterDataModalTanam(List<DataModalTanam> list, Context context2, String status) {
        dataModalTanam = list;
        context = context2;
        this.status = status;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_datamodaltanam, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String tanggaltransaksi = null;
        if(status.equals("Verifikasi")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terverifikasi_icon));
            tanggaltransaksi = dataModalTanam.get(position).getUpdated_at();
        }
        if(status.equals("Selesai")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_terverifikasi_icon));
            tanggaltransaksi = dataModalTanam.get(position).getUpdated_at();
        }
        if(status.equals("Dibatalkan")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_batalkan_verifikasi_icon));
            holder.tv_status.setTextColor(Color.RED);
            tanggaltransaksi = dataModalTanam.get(position).getUpdated_at();
        }
        if(status.equals("Menunggu Verifikasi")){
            holder.iv_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_belum_terverifikasi_icon));
            tanggaltransaksi = dataModalTanam.get(position).getCreated_at();
        }
        holder.tv_sawah.setText(dataModalTanam.get(position).getNama_sawah());
        holder.tv_status.setText(status);
        holder.tv_alamat.setText(dataModalTanam.get(position).getAlamat());
        holder.tv_jenisbibit.setText(dataModalTanam.get(position).getJenis_bibit());
        holder.tv_jenispupuk.setText(dataModalTanam.get(position).getJenis_pupuk());
        holder.tv_periodetanam.setText(dataModalTanam.get(position).getPeriode_tanam());
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
        return dataModalTanam.size();
    }

    public void updateDataModalTanam(List<DataModalTanam> list) {
        dataModalTanam.clear();
        dataModalTanam.addAll(list);
        notifyDataSetChanged();
    }
}
