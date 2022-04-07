package id.example.galungapp.MenuUtama.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.Api.Model.DataSawah;
import id.example.galungapp.MenuDrawer.EditSawahActivity;
import id.example.galungapp.R;

public class AdapterInfoGabah extends RecyclerView.Adapter<AdapterInfoGabah.MyViewHolder> {

    public List<DataHargaGabah> dataHargaGabah;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_jenisgabah, tv_hargagabah;
        CardView cv_jenisgabah;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_jenisgabah = (TextView) view.findViewById(R.id.tv_jenisgabah);
            tv_hargagabah = (TextView) view.findViewById(R.id.tv_hargagabah);
            cv_jenisgabah = (CardView) view.findViewById(R.id.cv_jenisgabah);
        }
    }

    public AdapterInfoGabah(List<DataHargaGabah> list, Context context2) {
        dataHargaGabah = list;
        context = context2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_infogabah, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_jenisgabah.setText(dataHargaGabah.get(position).getNama());
        holder.tv_hargagabah.setText(formatRupiah(Double.parseDouble(dataHargaGabah.get(position).getHarga()))+" /kg");
    }

    public int getItemCount() {
        return dataHargaGabah.size();
    }

    public void updateDataGabah(List<DataHargaGabah> list) {
        dataHargaGabah.clear();
        dataHargaGabah.addAll(list);
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
