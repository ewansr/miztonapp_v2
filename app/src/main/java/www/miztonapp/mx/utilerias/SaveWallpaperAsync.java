package www.miztonapp.mx.utilerias;

/**
 * Created by Saul on 29/10/2016.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public abstract class SaveWallpaperAsync extends AsyncTask<String, String, String> {
    private Context context;
    private ProgressDialog pDialog;
    String image_url;
    URL ImageUrl;
    String myFileUrl1;
    Bitmap bmImg = null;

    public SaveWallpaperAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub

        super.onPreExecute();

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Descargando...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected String doInBackground(String... args) {
        // TODO Auto-generated method stub

        InputStream is = null;
        String file_dir_storage= null;

        try {

            ImageUrl = new URL(args[0]);
            // myFileUrl1 = args[0];

            HttpURLConnection conn = (HttpURLConnection) ImageUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            bmImg = BitmapFactory.decodeStream(is, null, options);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            String path = ImageUrl.getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath()
                    + "/Wallpapers/");
            dir.mkdirs();
            String fileName = idStr;
            File file = new File(dir, fileName);

            file_dir_storage = dir.getPath() + "/" + file.getName();
            FileOutputStream fos = new FileOutputStream(file);
            bmImg.compress(CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            File imageFile = file;
            MediaScannerConnection.scanFile(context,
                    new String[] { imageFile.getPath() },
                    new String[] { "image/jpeg" }, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }

        return file_dir_storage;
    }

    @Override
    protected void onPostExecute(String args) {
        // TODO Auto-generated method stub

        if (bmImg == null) {

            Toast.makeText(context, "La imagen continua descargándose...",
                    Toast.LENGTH_SHORT).show();

            pDialog.dismiss();

        }

        else {

            if (pDialog!=null) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            Toast.makeText(context, "Imagen descargada",
                    Toast.LENGTH_SHORT).show();

        }

        onPostExec(args);
    }


    public abstract void onPostExec(String uri);


}