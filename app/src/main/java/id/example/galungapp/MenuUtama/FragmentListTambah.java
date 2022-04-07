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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import id.example.galungapp.Api.Client;
import id.example.galungapp.Api.InterfaceGet;
import id.example.galungapp.Api.Model.DataGabah;
import id.example.galungapp.Api.Model.DataGabah_respon;
import id.example.galungapp.Api.Model.DataGadaiSawah;
import id.example.galungapp.Api.Model.DataGadaiSawah_respon;
import id.example.galungapp.Api.Model.DataHargaGabah_respon;
import id.example.galungapp.Api.Model.DataModalTanam;
import id.example.galungapp.Api.Model.DataModalTanam_respon;
import id.example.galungapp.Api.Model.DataUserLogin;
import id.example.galungapp.MainActivity;
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

public class FragmentListTambah extends Fragment {

    private RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private InterfaceGet interfaceGet;

    private AdapterDataGabah adapterDataGabah;
    private AdapterDataGadaiSawah adapterDataGadaiSawah;
    private AdapterDataModalTanam adapterDataModalTanam;

    private List<DataGabah> dataGabah = new ArrayList<>();
    private List<DataGadaiSawah> dataGadaiSawah = new ArrayList<>();
    private List<DataModalTanam> dataModalTanam = new ArrayList<>();

    private ProgressDialog dialog;
    private SwipeRefreshLayout swiperefresh;
    private FloatingActionButton float_tambah;

    private String jenisdata;

    private ConstraintLayout constraint_info;
    private ImageView iv_info;
    private TextView tv_title, tv_subtitle;
    private Button btn_submit;

    public FragmentListTambah(String jenisdata){
        this.jenisdata = jenisdata;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listtambah, container, false);

        final DataUserLogin dataUserLogin = SharedPMUser.getmInstance(getContext()).getTokenUser();
        interfaceGet = (InterfaceGet) Client.getClientToken(dataUserLogin.getToken()).create(InterfaceGet.class);

        listView = (RecyclerView) view.findViewById(R.id.listView);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        float_tambah = (FloatingActionButton) view.findViewById(R.id.float_tambah);

        constraint_info = (ConstraintLayout) view.findViewById(R.id.constraint_info);
        iv_info = (ImageView) view.findViewById(R.id.iv_info);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);

        dialog = new ProgressDialog(getContext(), "Please Wait..");

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        float_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jenisdata.equals("DataJualGabah")) {
                    getIntent(JualGabahActivity.class);
                }
                if(jenisdata.equals("DataGadaiSawah")){
                    getIntent(GadaiSawahActivity.class);
                }
                if(jenisdata.equals("DataModalTanam")){
                    getIntent(ModalTanamActivity.class);
                }
            }
        });

        loadData();

        return view;
    }

    private void loadData(){
        switch (jenisdata){
            case "DataJualGabah" :
                dataGabah.clear();
                dialog.show();
                getDataGabahDijual();
                break;

            case "DataGadaiSawah" :
                dataGadaiSawah.clear();
                dialog.show();
                getDataSawahDiagadai();
                break;

            case "DataModalTanam" :
                dataModalTanam.clear();
                dialog.show();
                getDataModalTanam();
                break;
        }
    }

    private void refreshData(){
        switch (jenisdata){
            case "DataJualGabah" :
                dataGabah.clear();
                getDataGabahDijual();
                break;

            case "DataGadaiSawah" :
                dataGadaiSawah.clear();
                getDataSawahDiagadai();
                break;

            case "DataModalTanam" :
                dataModalTanam.clear();
                getDataModalTanam();
                break;
        }
    }

    private void getDataGabahDijual(){
        interfaceGet.getGabahDijual().enqueue(new Callback<DataGabah_respon>() {
            @Override
            public void onResponse(Call<DataGabah_respon> call, Response<DataGabah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGabah_respon mDataGabah_respon = (DataGabah_respon) response.body();
                    if(mDataGabah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGabah = new AdapterDataGabah(((DataGabah_respon) response.body()).getData(), getContext(), "Menunggu Verifikasi");
                            listView.setAdapter(adapterDataGabah);
                        }else{
                            showInfo("Data Kosong","Data gabah dijual kosong","Refresh");
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
                showInfo("Gagal terhubung keserver","Data gabah dijual tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataSawahDiagadai(){
        interfaceGet.getSawahDigadai().enqueue(new Callback<DataGadaiSawah_respon>() {
            @Override
            public void onResponse(Call<DataGadaiSawah_respon> call, Response<DataGadaiSawah_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataGadaiSawah_respon mDataGadaiSawah_respon = (DataGadaiSawah_respon) response.body();
                    if(mDataGadaiSawah_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataGadaiSawah = new AdapterDataGadaiSawah(((DataGadaiSawah_respon) response.body()).getData(), getContext(), "Menunggu Verifikasi");
                            listView.setAdapter(adapterDataGadaiSawah);
                        }else{
                            showInfo("Data Kosong","Data sawah digadai kosong","Refresh");
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
                showInfo("Gagal terhubung keserver","Data sawah digadai tidak dapat ditampilkan","Refresh");
                dialog.dismiss();
                swiperefresh.setRefreshing(false);
            }
        });
    }

    private void getDataModalTanam(){
        interfaceGet.getModalTanam().enqueue(new Callback<DataModalTanam_respon>() {
            @Override
            public void onResponse(Call<DataModalTanam_respon> call, Response<DataModalTanam_respon> response) {
                if(response.isSuccessful()) {
                    constraint_info.setVisibility(View.GONE);
                    DataModalTanam_respon mDataModalTanam_respon = (DataModalTanam_respon) response.body();
                    if(mDataModalTanam_respon.isStatus()){
                        if(response.body().getData().size() > 0) {
                            adapterDataModalTanam = new AdapterDataModalTanam(((DataModalTanam_respon) response.body()).getData(), getContext(), "Menunggu Verifikasi");
                            listView.setAdapter(adapterDataModalTanam);
                        }else{
                            showInfo("Data Kosong","Data modal tanam kosong","Refresh");
                        }
                        dialog.dismiss();
                        swiperefresh.setRefreshing(false);
                    }else{
                        showInfo("Gagal terhubung keserver",mDataModalTanam_respon.getMessage(),"Refresh");
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
                showInfo("Gagal terhubung keserver","Data modal tanam tidak dapat ditampilkan","Refresh");
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

    private void getIntent(Class activity){
        Intent intent =  new Intent(getContext(),activity);
        startActivity(intent);
    }

    private void  SweetalertSuccess(String ContentText){
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses")
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
