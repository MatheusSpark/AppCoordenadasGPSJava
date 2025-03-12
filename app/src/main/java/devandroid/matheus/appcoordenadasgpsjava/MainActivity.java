package devandroid.matheus.appcoordenadasgpsjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] permissoes = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final int APP_PERMISSOES_ID = 2025;
    double latitude, longitude;
    boolean gpsAtivo;
    TextView tvLatitudeValue, tvLongitudeValue;
    ImageView imgMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCompoments();
        verifyGps();
    }

    private void initCompoments() {
        tvLongitudeValue = findViewById(R.id.tvLongitudeValue);
        tvLatitudeValue = findViewById(R.id.tvLatitudeValue);
        imgMap = findViewById(R.id.imgMap);
        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
    }
    private void verifyGps() {
        gpsAtivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsAtivo) {
            getCoordinates();
        } else {
            latitude = 1.0;
            longitude = 1.0;
            tvLatitudeValue.setText(String.valueOf(latitude));
            tvLongitudeValue.setText(String.valueOf(longitude));
        }
    }

    private void getCoordinates() {
        boolean permissaoAtiva = solicitarPermissao();
        if (permissaoAtiva) {
            capturarUltimaLocalizacao();
        }
    }

    private boolean solicitarPermissao() {
        List<String> permissoesNaoConcedidas = new ArrayList<>();
        for (String permissao : this.permissoes) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissao) != PackageManager.PERMISSION_GRANTED) {
                permissoesNaoConcedidas.add(permissao);
            }
        }
        if (!permissoesNaoConcedidas.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, permissoesNaoConcedidas.toArray(new String[0]), APP_PERMISSOES_ID);
            return false;
        } else {
            return true;
        }
    }

    private void capturarUltimaLocalizacao() {
        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
        tvLatitudeValue.setText(formatarGeopoint(latitude));
        tvLongitudeValue.setText(formatarGeopoint(longitude));
    }

    private String formatarGeopoint(double valor) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        return decimalFormat.format(valor);
    }
}