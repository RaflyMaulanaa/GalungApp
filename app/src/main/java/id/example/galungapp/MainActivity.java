package id.example.galungapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sweetalert.SweetAlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import id.example.galungapp.Api.Model.DataUser;
import id.example.galungapp.LihatSemua.LihatSemuaActivity;
import id.example.galungapp.MenuDrawer.AkunActivity;
import id.example.galungapp.MenuDrawer.KontakKamiActivity;
import id.example.galungapp.RegistrasiLogin.LoginRegisterActivity;
import id.example.galungapp.Storage.SharedPMUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView nav_view;
    private FrameLayout frame_tampilanutama;
    private TextView tv_nama,tv_verifikasi;
    private Button btn_login,btn_registrasi;
    private LinearLayout linearRegLog;

    private int statusLogin = 0;
    private String statusfailed;

    private BerandaFragment mBerandaFragment;
    private BottomNavigationView mMainNav;

    boolean suksesCheckout = false, lihatPesanan = false;
    private String NoTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        statusLogin = extras.getInt("statusLogin");


        if(extras.getBoolean("suksesCheckout") != suksesCheckout){
            if(extras.getString("NoTransaksi") != null){
                NoTransaksi = extras.getString("NoTransaksi");
            }
            Intent intent = new Intent(MainActivity.this, SuksesCheckoutActivity.class);
            intent.putExtra("NoTranskasi",NoTransaksi);
            startActivity(intent);
        }

        if(extras.getBoolean("lihatPesanan") != lihatPesanan){
            Intent intent = new Intent(MainActivity.this, PesananAcitivty.class);
            startActivity(intent);
        }

        if(extras.getString("statusfailed") != null){
            statusfailed = extras.getString("statusfailed");
        }

        toolbar = findViewById(R.id.toolbar);
        frame_tampilanutama = findViewById(R.id.frame_tampilanutama);
        mMainNav = findViewById(R.id.main_nav);

        mBerandaFragment = new BerandaFragment();

        tv_nama = (TextView) findViewById(R.id.tv_nama);
        tv_verifikasi = (TextView) findViewById(R.id.tv_verifikasi);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //menghilangkan titlebar bawaan
            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setTitle(R.string.app_name);
        }

        nav_view = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        setFragment(mBerandaFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home :
                        setFragment(mBerandaFragment);
                        return true;
                    case R.id.nav_search :
                        Intent search = new Intent(MainActivity.this, LihatSemuaActivity.class);
                        search.putExtra("menuSearch","aktif");
                        search.putExtra("data","Beras");
                        startActivity(search);
                        return true;
                    case R.id.nav_pesanan :
                        Intent pesanan = new Intent(MainActivity.this, PesananAcitivty.class);
                        startActivity(pesanan);
                        return true;
                    case R.id.nav_keranjang :
                        Intent keranjang = new Intent(MainActivity.this, KeranjangActivity.class);
                        startActivity(keranjang);
                        return true;
                    default:
                        return false;
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        toggle.syncState();
        nav_view.setItemIconTintList(null);
        nav_view.getMenu().setGroupVisible(R.id.group_akun, true);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.nav_Kontak){
                    getActivityMenu(KontakKamiActivity.class);
                }else if (id == R.id.nav_Bantuan){
                    getActivityMenu(KontakKamiActivity.class);
                }else if (id == R.id.nav_Akun && statusLogin == 1) {
                    getActivityMenu(AkunActivity.class);
                }else if(id == R.id.nav_Keluar && statusLogin == 1){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Keluar")
                            .setContentText("Apakah anda yakin ingin keluar atau logout ?")
                            .setConfirmText("Ya, Keluar!")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if(SharedPMUser.getmInstance(MainActivity.this).isUserReady()) {
                                        SharedPMUser.getmInstance(MainActivity.this).clear();
                                    }
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("statusLogin",0);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                drawer.closeDrawers();
                return true;
            }
        });

        View header = nav_view.getHeaderView(0);
        tv_nama = (TextView) header.findViewById(R.id.tv_nama);
        tv_verifikasi = (TextView) header.findViewById(R.id.tv_verifikasi);
        linearRegLog = (LinearLayout) header.findViewById(R.id.linearRegLog);

        btn_login = (Button) header.findViewById(R.id.btn_login);
        btn_registrasi = (Button) header.findViewById(R.id.btn_register);

        getHeaderandMenu(statusLogin);

        if(statusfailed != null){
            if(statusfailed.equals("accessToken")) {
                getActivity(LoginRegisterActivity.class, 1);
            }
        }
    }

    private void getHeaderandMenu(int statusLogin){
        switch (statusLogin){
            case 0:
                linearRegLog.setVisibility(View.VISIBLE);
                btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity(LoginRegisterActivity.class,1);
                    }
                });

                btn_registrasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity(LoginRegisterActivity.class,0);
                    }
                });
                nav_view.getMenu().setGroupVisible(R.id.group_akun, false);
                break;
            case 1 :
                linearRegLog.setVisibility(View.GONE);
                DataUser dataUser = SharedPMUser.getmInstance(this).getUser();
                tv_nama.setText(dataUser.getName());
                if(dataUser.getRole().equals("petani")){
                    tv_verifikasi.setVisibility(View.VISIBLE);
                    String statusverifikasi;
                    if(dataUser.getPetani_verified().equals("1")){
                        statusverifikasi = "Terverifikasi";
                    }else{
                        statusverifikasi = "Belum Terverifikasi";
                    }
                    tv_verifikasi.setText(statusverifikasi);
                }else{
                    tv_verifikasi.setVisibility(View.GONE);
                }
                nav_view.getMenu().setGroupVisible(R.id.group_akun, true);
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame_tampilanutama, fragment);
        beginTransaction.commit();
    }

    private void getActivity(Class activity, int position){
        Intent intent = new Intent(this,activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void getActivityMenu(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }


}
