
package eduardobachiega.hackindebt.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import eduardobachiega.hackindebt.R;
import eduardobachiega.hackindebt.view.pf.MapFragment;
import eduardobachiega.hackindebt.view.pf.QRCodeReader;
import eduardobachiega.hackindebt.view.pj.GenerateQRCodeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        boolean isPj = preferences.getBoolean("IS_PJ", false);
        if (isPj) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.pj_drawer);
			try {
            	FragmentManager fragmentManager = getSupportFragmentManager();
            	Fragment fragment = (Fragment) GenerateQRCodeFragment.class.newInstance();
            	fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        	}catch (Exception e){

        	}
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.pf_drawer);
            try {
            	FragmentManager fragmentManager = getSupportFragmentManager();
            	Fragment fragment = (Fragment) MapFragment.class.newInstance();
            	fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        	}catch (Exception e){

        	}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id != R.id.finances && id != R.id.moneySaved && id != R.id.history) {
            Fragment fragment = null;
            Class fragmentClass = null;
            if (id == R.id.stores) {
                fragmentClass = MapFragment.class;
            } else if (id == R.id.pay) {
                fragmentClass = QRCodeReader.class;
            } else if (id == R.id.receive) {
                fragmentClass = GenerateQRCodeFragment.class;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
