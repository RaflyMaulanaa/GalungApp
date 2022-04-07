package id.example.galungapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Adapter.AdapterPesanan;
import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.Model.DataGabah;
import id.example.galungapp.Api.Model.DataGabah_respon;
import id.example.galungapp.Api.Model.DataGadaiSawah;
import id.example.galungapp.Api.Model.DataGadaiSawah_respon;
import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.Api.Model.DataHargaGabah_respon;
import id.example.galungapp.Api.Model.DataItemPesanan;
import id.example.galungapp.Api.Model.DataModalTanam;
import id.example.galungapp.Api.Model.DataModalTanam_respon;
import id.example.galungapp.Api.Model.DataPesanan;
import id.example.galungapp.Api.Model.DataPesanan_respon;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.MenuUtama.Adapter.AdapterDataGabah;
import id.example.galungapp.MenuUtama.Adapter.AdapterDataGadaiSawah;
import id.example.galungapp.MenuUtama.Adapter.AdapterDataModalTanam;
import id.example.galungapp.MenuUtama.Adapter.AdapterInfoGabah;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListPesanan extends Fragment {

    private RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private InterfaceGet interfaceGet;

    private AdapterPesanan adapterPesanan;


    private ProgressDialog dialog;
    private SwipeRefreshLayout swiperefresh;

    private List<DataPesanan> dataPesanan = new ArrayList();
    private List<DataItemPesanan> dataItemPesanan = new ArrayList<>();

    private String jenisdata;

    private ConstraintLayout constraint_info;
    private ImageView iv_info;
    private TextView tv_title, tv_subtitle;
    private Button btn_submit;

    public FragmentListPesanan(String jenisdata){
        this.jenisdata = jenisdata;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = (RecyclerView) view.findViewById(R.id.listView);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);

        dialog = new ProgressDialog(getContext(), "Please Wait..");

        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(getContext()).getTokenUser();
        interfaceGet = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);

        constraint_info = (ConstraintLayout) view.findViewById(R.id.constraint_info);
        iv_info = (ImageView) view.findViewById(R.id.iv_info);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        loadData();

        return view;
    }

    private void loadData(){
        switch (jenisdata){
            case "PesananDiproses" :
                dataPesanan.clear();
                dialog.show();
                getPesananDiproses();
                break;

            case "PesananSelesai" :
                dataPesanan.clear();
                dialog.show();
                getPesananSelesai();
                break;

            case "PesananDibatalkan" :
                dataPesanan.clear();
                dialog.show();
                getPesananDibatalkan();
                break;

        }
    }

    private void refreshData(){
        switch (jenisdata){
            case "PesananDiproses" :
                dataPesanan.clear();
                getPesananDiproses();
                break;

            case "PesananSelesai" :
                dataPesanan.clear();
                getPesananSelesai();
                break;

            case "PesananDibatalkan" :
                dataPesanan.clear();
                getPesananDibatalkan();
                break;
        }
    }

    private void getPesananDiproses(){
       interfaceGet.getPesananProses().enqueue(new Callback<DataPesanan_respon>() {
           @Override
           public void onResponse(Call<DataPesanan_respon> call, Response<DataPesanan_respon> response) {
               if(response.isSuccessful()) {
                   constraint_info.setVisibility(View.GONE);
                   DataPesanan_respon mDataPesanan_respon = (DataPesanan_respon) response.body();
                   if(mDataPesanan_respon.isStatus()){
                       if(response.body().getData().size() > 0) {
                           adapterPesanan = new AdapterPesanan(((DataPesanan_respon) response.body()).getData(), getContext(), "Pesanan Diproses");
                           listView.setAdapter(adapterPesanan);
                       }else{
                           showInfo("Data Kosong","Data pesanan diproses kosong","Refresh");
                       }
                       dialog.dismiss();
                       swiperefresh.setRefreshing(false);
                   }else{
                       showInfo("Gagal terhubung keserver",mDataPesanan_respon.getMessage(),"Refresh");
                       dialog.dismiss();
                       swiperefresh.setRefreshing(false);
                   }
               }else{
                   notValidAksesToken();
                   dialog.dismiss();
                   swiperefresh.setRefreshing(false);
               }
           }

           @Override
           public void onFailure(Call<DataPesanan_respon> call, Throwable throwable) {
               showInfo("Gagal terhubung keserver","Data pesanan diproses tidak dapat ditampilkan","Refresh");
               dialog.dismiss();
               swiperefresh.setRefreshing(false);
           }
       });
    }

    private void getPesananSelesai(){
        interfaceGet.getPesananSelesai().enqueue(new Callback<DataPesanan_respon>() {
            @Override
            public void onResponse(Call<DataPesanan_respon> call, Response<DataPesanan_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataPesanan_respon mDataPesanan_respon = (DataPesanan_respon) response.body();
                    if(mDataPesanan_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterPesanan = new AdapterPesanan(((DataPesanan_respon) response.body()).getData(), getContext(), "Pesanan Selesai");
                            listView.setAdapter(adapterPesanan);
                        }else{
                            showInfo("Data Kosong","Data pesanan selesai kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataPesanan_respon.getMessage(),"Refresh");
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    notValidAksesToken();
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataPesanan_respon> call, Throwable throwable) {
                showInfo("Gagal terhubung keserver","Data pesanan selesai tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getPesananDibatalkan(){
        interfaceGet.getPesananDibatalkan().enqueue(new Callback<DataPesanan_respon>() {
            @Override
            public void onResponse(Call<DataPesanan_respon> call, Response<DataPesanan_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataPesanan_respon mDataPesanan_respon = (DataPesanan_respon) response.body();
                    if(mDataPesanan_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterPesanan = new AdapterPesanan(((DataPesanan_respon) response.body()).getData(), getContext(), "Pesanan Dibatalkan");
                            listView.setAdapter(adapterPesanan);
                        }else{
                            showInfo("Data Kosong","Data pesanan dibatalkan kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataPesanan_respon.getMessage(),"Refresh");
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }
                }else{
                    notValidAksesToken();
                    dialog.dismiss();
                    swiperefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<DataPesanan_respon> call, Throwable throwable) {
                showInfo("Gagal terhubung keserver","Data pesanan dibatalkan tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void notValidAksesToken(){
        if(SharedPMUser.getmInstance(getContext()).isUserReady()) {
            SharedPMUser.getmInstance(getContext()).clear();
        }
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("statusLogin",0);
        intent.putExtra("statusfailed","accessToken");
        startActivity(intent);
    }

    private void  SweetalertFailed(String ContentText){
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops")
                .setContentText(ContentText)
                .show();
    }

    private void showInfo(String title, String subtitle, String button){
        constraint_info.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        tv_subtitle.setText(subtitle);
        btn_submit.setText(button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }
}
