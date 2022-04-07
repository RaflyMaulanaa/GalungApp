package id.example.galungapp.MenuUtama;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.Model.DataGabah;
import id.example.galungapp.Api.Model.DataGabah_respon;
import id.example.galungapp.Api.Model.DataGadaiSawah;
import id.example.galungapp.Api.Model.DataGadaiSawah_respon;
import id.example.galungapp.Api.Model.DataHargaGabah;
import id.example.galungapp.Api.Model.DataHargaGabah_respon;
import id.example.galungapp.Api.Model.DataModalTanam;
import id.example.galungapp.Api.Model.DataModalTanam_respon;
import id.example.galungapp.Api.Model.DataSawah_respon;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.MainActivity;
import id.example.galungapp.MenuDrawer.Adapter.AdapterSawahUser;
import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.MenuUtama.Adapter.AdapterDataGabah;
import id.example.galungapp.MenuUtama.Adapter.AdapterDataGadaiSawah;
import id.example.galungapp.MenuUtama.Adapter.AdapterDataModalTanam;
import id.example.galungapp.MenuUtama.Adapter.AdapterInfoGabah;
import id.example.galungapp.ProgresDialog.ProgressDialog;
import id.example.galungapp.R;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.Storage.SharedPMUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentList extends Fragment {

    private RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private InterfaceGet interfaceGet;
    private InterfaceGet interfaceGetToken;

    private AdapterInfoGabah adapterInfoGabah;
    private AdapterDataGabah adapterDataGabah;
    private AdapterDataGadaiSawah adapterDataGadaiSawah;
    private AdapterDataModalTanam adapterDataModalTanam;

    private ProgressDialog dialog;
    private SwipeRefreshLayout swiperefresh;

    private List<DataHargaGabah> dataHargaGabah = new ArrayList();
    private List<DataGabah> dataGabah = new ArrayList<>();
    private List<DataGadaiSawah> dataGadaiSawah = new ArrayList<>();
    private List<DataModalTanam> dataModalTanam = new ArrayList<>();

    private String jenisdata;

    private ConstraintLayout constraint_info;
    private ImageView iv_info;
    private TextView tv_title, tv_subtitle;
    private Button btn_submit;

    public FragmentList(String jenisdata){
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

        interfaceGet = (InterfaceGet) Client.getClient().create(InterfaceGet.class);
        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(getContext()).getTokenUser();
        interfaceGetToken = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);

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
            case "GabahInfoHarga" :
                dataHargaGabah.clear();
                dialog.show();
                getDataInfoHargaGabah();
                break;

            case "DataGabahTerjual" :
                dataGabah.clear();
                dialog.show();
                getDataGabahTerjual();
                break;

            case "DataGabahDibatalkan" :
                dataGabah.clear();
                dialog.show();
                getDataGabahDibatalkan();
                break;

            case "DataGadaiSawahTergadai" :
                dataGadaiSawah.clear();
                dialog.show();
                getDataSawahTergadai();
                break;

            case "DataGadaiSawahDibatalkan" :
                dataGadaiSawah.clear();
                dialog.show();
                getDataGadaiSawahDibatalkan();
                break;

            case "DataGadaiSawahSelesai" :
                dataGadaiSawah.clear();
                dialog.show();
                getDataSawahSelesai();
                break;

            case "DataDimodalTanam" :
                dataModalTanam.clear();
                dialog.show();
                getDataDimodalTanam();
                break;

            case "DataModalTanamDibatalkan" :
                dataModalTanam.clear();
                dialog.show();
                getDataModalTanamDibatalkan();
                break;

            case "DataModalTanamSelesai" :
                dataModalTanam.clear();
                dialog.show();
                getDataModalTanamSelesai();
                break;
        }
    }

    private void refreshData(){
        switch (jenisdata){
            case "GabahInfoHarga" :
                dataHargaGabah.clear();
                getDataInfoHargaGabah();
                break;

            case "DataGabahTerjual" :
                dataGabah.clear();
                getDataGabahTerjual();
                break;

            case "DataGabahDibatalkan" :
                dataGabah.clear();
                getDataGabahDibatalkan();
                break;

            case "DataGadaiSawahTergadai" :
                dataGadaiSawah.clear();
                getDataSawahTergadai();
                break;

            case "DataGadaiSawahDibatalkan" :
                dataGadaiSawah.clear();
                getDataGadaiSawahDibatalkan();
                break;

            case "DataGadaiSawahSelesai" :
                dataGadaiSawah.clear();
                getDataSawahSelesai();
                break;

            case "DataDimodalTanam" :
                dataModalTanam.clear();
                getDataDimodalTanam();
                break;

            case "DataModalTanamDibatalkan" :
                dataModalTanam.clear();
                getDataModalTanamDibatalkan();
                break;

            case "DataModalTanamSelesai" :
                dataModalTanam.clear();
                getDataModalTanamSelesai();
                break;
        }
    }

    private void getDataInfoHargaGabah(){
        interfaceGet.getHargaGabah().enqueue(new Callback<DataHargaGabah_respon>() {
            @Override
            public void onResponse(Call<DataHargaGabah_respon> call, Response<DataHargaGabah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataHargaGabah_respon mDataSawah_respon = (DataHargaGabah_respon) response.body();
                    if(mDataSawah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterInfoGabah = new AdapterInfoGabah(((DataHargaGabah_respon) response.body()).getData(), getContext());
                            listView.setAdapter(adapterInfoGabah);
                        }else{
                            showInfo("Data Kosong","Data info harga gabah kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataSawah_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataHargaGabah_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data info harga gabah tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataGabahTerjual(){
        interfaceGetToken.getGabahTerjual().enqueue(new Callback<DataGabah_respon>() {
            @Override
            public void onResponse(Call<DataGabah_respon> call, Response<DataGabah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGabah_respon mDataGabah_respon = (DataGabah_respon) response.body();
                    if(mDataGabah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGabah = new AdapterDataGabah(((DataGabah_respon) response.body()).getData(), getContext(), "Verifikasi");
                            listView.setAdapter(adapterDataGabah);
                        }else{
                            showInfo("Data Kosong","Data gabah terjual kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataGabah_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataGabah_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data gabah terjual tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataGabahDibatalkan(){
        interfaceGetToken.getGabahDibatalkan().enqueue(new Callback<DataGabah_respon>() {
            @Override
            public void onResponse(Call<DataGabah_respon> call, Response<DataGabah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGabah_respon mDataGabah_respon = (DataGabah_respon) response.body();
                    if(mDataGabah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGabah = new AdapterDataGabah(((DataGabah_respon) response.body()).getData(), getContext(), "Dibatalkan");
                            listView.setAdapter(adapterDataGabah);
                        }else{
                            showInfo("Data Kosong","Data gabah dibatalkan kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataGabah_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataGabah_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data gabah dibatalkan tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }


    private void getDataSawahTergadai(){
        interfaceGetToken.getSawahTergadai().enqueue(new Callback<DataGadaiSawah_respon>() {
            @Override
            public void onResponse(Call<DataGadaiSawah_respon> call, Response<DataGadaiSawah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGadaiSawah_respon mDataGadaiSawah_respon = (DataGadaiSawah_respon) response.body();
                    if(mDataGadaiSawah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGadaiSawah = new AdapterDataGadaiSawah(((DataGadaiSawah_respon) response.body()).getData(), getContext(), "Verifikasi");
                            listView.setAdapter(adapterDataGadaiSawah);
                        }else{
                            showInfo("Data Kosong","Data sawah tergadai kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataGadaiSawah_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataGadaiSawah_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data sawah tergadai tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataGadaiSawahDibatalkan(){
        interfaceGetToken.getGadaiSawahDibatalkan().enqueue(new Callback<DataGadaiSawah_respon>() {
            @Override
            public void onResponse(Call<DataGadaiSawah_respon> call, Response<DataGadaiSawah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGadaiSawah_respon dataGadaiSawah_respon = (DataGadaiSawah_respon) response.body();
                    if(dataGadaiSawah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGadaiSawah = new AdapterDataGadaiSawah(((DataGadaiSawah_respon) response.body()).getData(), getContext(), "Dibatalkan");
                            listView.setAdapter(adapterDataGadaiSawah);
                        }else{
                            showInfo("Data Kosong","Data gadai sawah dibatalkan kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",dataGadaiSawah_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataGadaiSawah_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data gadai sawah dibatalkan tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }


    private void getDataSawahSelesai(){
        interfaceGetToken.getGadaiSawahSelesai().enqueue(new Callback<DataGadaiSawah_respon>() {
            @Override
            public void onResponse(Call<DataGadaiSawah_respon> call, Response<DataGadaiSawah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGadaiSawah_respon mDataGadaiSawah_respon = (DataGadaiSawah_respon) response.body();
                    if(mDataGadaiSawah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGadaiSawah = new AdapterDataGadaiSawah(((DataGadaiSawah_respon) response.body()).getData(), getContext(), "Selesai");
                            listView.setAdapter(adapterDataGadaiSawah);
                        }else{
                            showInfo("Data Kosong","Data sawah selesai digadai kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataGadaiSawah_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataGadaiSawah_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data sawah selesai digadai tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }





    private void getDataDimodalTanam(){
        interfaceGetToken.getDiModali().enqueue(new Callback<DataModalTanam_respon>() {
            @Override
            public void onResponse(Call<DataModalTanam_respon> call, Response<DataModalTanam_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataModalTanam_respon mModalTanam_respon = (DataModalTanam_respon) response.body();
                    if(mModalTanam_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataModalTanam = new AdapterDataModalTanam(((DataModalTanam_respon) response.body()).getData(), getContext(), "Verifikasi");
                            listView.setAdapter(adapterDataModalTanam);
                        }else{
                            showInfo("Data Kosong","Data sawah dimodali kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mModalTanam_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataModalTanam_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data sawah dimodali tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }



    private void getDataModalTanamDibatalkan(){
        interfaceGetToken.getDiModali().enqueue(new Callback<DataModalTanam_respon>() {
            @Override
            public void onResponse(Call<DataModalTanam_respon> call, Response<DataModalTanam_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataModalTanam_respon mModalTanam_respon = (DataModalTanam_respon) response.body();
                    if(mModalTanam_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataModalTanam = new AdapterDataModalTanam(((DataModalTanam_respon) response.body()).getData(), getContext(), "Dibatalkan");
                            listView.setAdapter(adapterDataModalTanam);
                        }else{
                            showInfo("Data Kosong","Data modal tanam dibatalkan kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mModalTanam_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataModalTanam_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data modal tanam dibatalkan tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }


    private void getDataModalTanamSelesai(){
        interfaceGetToken.getDiModali().enqueue(new Callback<DataModalTanam_respon>() {
            @Override
            public void onResponse(Call<DataModalTanam_respon> call, Response<DataModalTanam_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataModalTanam_respon mModalTanam_respon = (DataModalTanam_respon) response.body();
                    if(mModalTanam_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataModalTanam = new AdapterDataModalTanam(((DataModalTanam_respon) response.body()).getData(), getContext(), "Selesai");
                            listView.setAdapter(adapterDataModalTanam);
                        }else{
                            showInfo("Data Kosong","Data modal tanam selesai kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mModalTanam_respon.getMessage(),"Refresh");
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
            public void onFailure(Call<DataModalTanam_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Data modal tanam selesai tidak dapat ditampilkan","Refresh");
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
