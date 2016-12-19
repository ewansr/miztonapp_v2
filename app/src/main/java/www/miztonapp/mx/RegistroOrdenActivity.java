package www.miztonapp.mx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.redbooth.WelcomeCoordinatorLayout;

public class RegistroOrdenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_orden);

        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout)findViewById(R.id.coordinator);
        coordinatorLayout.addPage(R.layout.registro_tipo,
        R.layout.activity_registro_fibra);
    }


}
