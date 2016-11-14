package cl.duoc.android.camara;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamaraFullActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_full);
    }

    public void tomarFotografia(View v) {
        dispatchTakePictureIntent();
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timestamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( takePictureIntent.resolveActivity(getPackageManager()) != null ) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if(photoFile != null) {
                // recomendable para android 7 y superior
                //Uri photoURI = FileProvider.getUriForFile(this, "cl.duoc.android.camara.fileprovider", photoFile);

                Uri photoURI = Uri.fromFile(photoFile);
                Log.v("CAMARADUOC", photoURI.toString());
                Log.v("CAMARADUOC", photoFile.getAbsolutePath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File file = new File(mCurrentPhotoPath);
            ImageView ivf = (ImageView)findViewById(R.id.imageViewFull);
            ivf.setImageURI(Uri.fromFile(file));
        }
    }
}
