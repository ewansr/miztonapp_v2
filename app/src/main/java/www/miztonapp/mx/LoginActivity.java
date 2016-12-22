package www.miztonapp.mx;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import mehdi.sakout.fancybuttons.FancyButton;
import www.miztonapp.mx.SQL.SQLiteDBDataSource;
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

        SQLiteDBDataSource dataSource = new SQLiteDBDataSource(this);
        if (Utils.usuario_iniciado(this)){
            iniciar_actividad_principal(Utils.obtener_usuario(this));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FancyButton login_button = (FancyButton) findViewById(R.id.btn_material);
        usuario = (EditText) findViewById(R.id.edtUsuario);
        contrasena = (EditText) findViewById(R.id.edtContrasena);

        login_button.setOnClickListener(this);
        Utils.setStatusColor(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_material){

            String str_usuario     = usuario.getText().toString();
            String str_contraseana = contrasena.getText().toString();

            try {
                iniciar_sesion(str_usuario, str_contraseana);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        Utils.guardar_usuario(this, usuario );
        iniciar_actividad_principal(usuario);
    }

    @Override
    public void loginAutenticacionErronea(mException error) {
        android.app.AlertDialog alertDialog = Utils.crear_alerta(this, "Error al iniciar sesión", error.getMessage());
        alertDialog.show();
    }

    public void iniciar_actividad_principal(LoginModel usuario){
        Utils.crear_toast(this, "Iniciando sesión...").show();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtras(Utils.usuario_to_bundle(usuario) );
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}
