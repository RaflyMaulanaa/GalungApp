package id.example.galungapp.MenuDrawer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.example.galungapp.Api.Model.DataSawah;
import id.example.galungapp.MenuDrawer.EditSawahActivity;
import id.example.galungapp.R;

public class AdapterSawahUser extends RecyclerView.Adapter<AdapterSawahUser.MyViewHolder> {

    public List<DataSawah> dataSawah;
    public Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sawah;
        LinearLayout linear_editasawah;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_sawah = (TextView) view.findViewById(R.id.tv_sawah);
            linear_editasawah = (LinearLayout) view.findViewById(R.id.linear_editasawah);
        }
    }

    public AdapterSawahUser(List<DataSawah> list, Context context2) {
        dataSawah = list;
        context = context2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_akunsawah, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_sawah.setText(dataSawah.get(position).getNama());
        holder.linear_editasawah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditSawahActivity.class);
                intent.putExtra("id_sawah",dataSawah.get(position).getId());
                intent.putExtra("titik_koordinat",dataSawah.get(position).getTitik_koordinat());
                intent.putExtra("alamat",dataSawah.get(position).getAlamat());
                intent.putExtra("kecamatan",dataSawah.get(position).getKecamatan());
                intent.putExtra("kelurahan",dataSawah.get(position).getKelurahan());
                intent.putExtra("nama_sawah",dataSawah.get(position).getNama());
                intent.putExtra("luas_sawah",dataSawah.get(position).getLuas_sawah());
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return dataSawah.size();
    }

    public void updateDataSawah(List<DataSawah> list) {
        dataSawah.clear();
        dataSawah.addAll(list);
        notifyDataSetChanged();
    }
}
