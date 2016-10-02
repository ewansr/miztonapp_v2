package www.miztonapp.mx;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import www.miztonapp.mx.api.mException;
import www.miztonapp.mx.models.LoginModel;
import www.miztonapp.mx.requests.RequestLogin;
import www.miztonapp.mx.utilerias.Utils;

/**
 * Created by EwanS on 01/10/2016.
 */

public class LoginActivity extends RequestLogin implements View.OnClickListener,View.OnFocusChangeListener,View.OnTouchListener {
    private TextView etiqueta_usuario;
    private TextView etiqueta_contrasena;
    private EditText usuario;
    private EditText contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_button = (Button) findViewById(R.id.btnLogin);
        etiqueta_usuario = (TextView)  findViewById(R.id.tvusuario);
        etiqueta_contrasena = (TextView)  findViewById(R.id.tvContrasena);
        usuario = (EditText) findViewById(R.id.edtUsuario);
        contrasena = (EditText) findViewById(R.id.edtContrasena);

        login_button.setOnClickListener(this);
        Utils.setStatusColor(this);
        usuario.setOnTouchListener(this);
        contrasena.setOnTouchListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin){

            String str_usuario     = usuario.getText().toString();
            String str_contraseana = contrasena.getText().toString();
            try {
                iniciar_sesion(str_usuario, str_contraseana);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Intent i = new Intent(this, MainActivity.class);
//                startActivity(i);

        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.edtUsuario && b){
            etiqueta_usuario.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.edtContrasena && b){
            etiqueta_contrasena.setVisibility(View.VISIBLE);
        } else {
            etiqueta_usuario.setVisibility(View.GONE);
            etiqueta_contrasena.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof EditText) {
            view.setOnFocusChangeListener(this);
        }
        return false;
    }

    @Override
    public void loginAutenticacionBeforeLoad() {

    }

    @Override
    public void loginAutenticacionExitosa(LoginModel usuario) {

    }

    @Override
    public void loginAutenticacionErronea(mException error) {
        android.app.AlertDialog alertDialog = Utils.crear_alerta(this, "Error al iniciar sesión", error.getMessage());
        alertDialog.show();
    }
}
