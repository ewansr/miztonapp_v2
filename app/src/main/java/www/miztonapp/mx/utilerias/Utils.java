package www.miztonapp.mx.utilerias;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import www.miztonapp.mx.R;

/**
 * Created by EwanS on 17/08/2016.
 */
public class Utils  {

    /**
     * Para el manejo de strings
     */
    public static Boolean isEquals(String string, String stringCompare){
        return new String(string).equals(stringCompare);
    }

    public static Toast crear_toast(Context context, String mensaje){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate( R.layout.custom_toast, null );
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.custom_toast_container);

        TextView text = (TextView) linearLayout.findViewById(R.id.text);
        text.setText(mensaje);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(linearLayout);
        return toast;
    }


    /**
     * para el manejo de ventanas ( Layouts )
     */



    /** Esta Función colorea la barra de estado en caso de ser versión compatible*/
    public static void setStatusColor(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void showMessage( String title, String message, Context context, AlertDialog.Builder builder ){
            builder = new AlertDialog.Builder( context );
            builder
                    .setTitle( title )
                    .setMessage( message )
                    .create()
                    .show();
    }

    public static void showMessage( String title, String message, Context context ){
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder
                .setTitle( title )
                .setMessage( message )
                .create()
                .show();
    }

    public static android.app.AlertDialog crear_alerta(Context context, String titulo, String mensaje ) {
        final android.app.AlertDialog.Builder builder;
        builder = new android.app.AlertDialog.Builder( context );
        builder.setTitle( titulo );
        builder.setMessage( mensaje );

        builder.setPositiveButton( "Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }

    public static void showLoadMessage( String message, ProgressDialog progressDialog, Context context ){
        progressDialog = new ProgressDialog( context );
        progressDialog.setMessage( message );
        progressDialog.show();
    }

    public static void showLoadMessage( String message, ProgressDialog progressDialog ){
        progressDialog.setMessage( message );
        progressDialog.show();
    }


    public static class LoadRemoteImg extends AsyncTask<String, Void, Bitmap> {
        private Exception exception;
        private ImageView img;

        public LoadRemoteImg(ImageView img){
            this.img = img;
        }

        protected Bitmap doInBackground(String... params) {
            try {

                URL url = null;
                try {
                    url = new URL(params[0].toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bmp;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(Bitmap bmp) {
            if (bmp != null && img != null) {
                img.setImageBitmap(bmp);
            }
        }
    }
}

