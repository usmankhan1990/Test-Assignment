package activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.interviewtest.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import co.sspp.library.SweetAlertDialog;
import eu.janmuller.android.simplecropimage.CropImageRectangleSample;
import helper.Utils;
import view.UiView;


public class MainActivity extends AppCompatActivity {

    EditText edtFirstName,edtLastName;
    ImageView imgProfile;
    Button btnLogout;
    SweetAlertDialog parseDialog;
    TextView txtSave, txtEmail;
    private static File mFileTemp;
    ParseUser parseUser = ParseUser.getCurrentUser();
    String firstName = "", lastName = "", email = "", imageUrl = "";
    boolean edit = false;
    private final int mREQUEST_CODE_GALLERY = 101,mREQUEST_CODE_TAKE_PICTURE = 102, mREQUEST_CODE_CROP_IMAGE = 103;
    byte[] imageByte;
    public static final String sTEMP_PHOTO_FILE_NAME = "user_profile.jpg";
    UiView uiView = UiView.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_profile_edit);
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(parseUser!=null){
            setUserDetails();
        }

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit == false){
                    edit = true;
                    txtSave.setText("Save");
                    enableDisableView(edit);
                }else{
                    edit = false;
                    txtSave.setText("Edit");
                    enableDisableView(edit);
                    saveUserDetails();
                }
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiView.showDialogConfirmationForLogout(MainActivity.this);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Dexter.withActivity(MainActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    // check if all permissions are granted
                                    if (report.areAllPermissionsGranted()) {
                                        // do you work now
                                        fileSaving();
                                        imageDialog(v);
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        // permission is denied permenantly, navigate user to app settings
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            })
                            .onSameThread()
                            .check();
                } else {
                    fileSaving();
                    imageDialog(v);
                }
            }
        });

    }



    public void init(){
        edtFirstName   = findViewById(R.id.edtFirstName);
        edtLastName    = findViewById(R.id.edtLastName);
        txtEmail       = findViewById(R.id.txtEmail);
        imgProfile     = findViewById(R.id.imgProfile);
        txtSave        = findViewById(R.id.txtSave);
        btnLogout      = findViewById(R.id.btnLogout);
    }

    public void setUserDetails(){

        try {
            parseUser.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(parseUser.has("firstName")){
            firstName = parseUser.getString("firstName");
        }else{
            firstName = "";
        }
        if(parseUser.has("lastName")){
            lastName = parseUser.getString("lastName");
        }else{
            lastName = "";
        }

        if (parseUser.has("imageUrl")) {
            imageUrl = parseUser.getString("imageUrl");
            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(1).cornerRadiusDp(70).oval(false).build();
            Picasso.with(this).load(imageUrl).transform(transformation).fit().into(imgProfile);
        } else {
            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(1).cornerRadiusDp(70).oval(false).build();
            Picasso.with(this).load(R.drawable.defaultprofile).transform(transformation).fit().into(imgProfile);
        }

        email     = parseUser.getEmail();
        edtFirstName.setText(firstName);
        edtLastName.setText(lastName);
        txtEmail.setText(email);
        txtSave.setText("Edit");
        enableDisableView(edit);
    }

    public void enableDisableView(boolean state){
        edtFirstName.setEnabled(state);
        edtLastName.setEnabled(state);
        imgProfile.setEnabled(state);
    }

    public void saveUserDetails(){

        parseUser.put("firstName",edtFirstName.getText().toString());
        parseUser.put("lastName",edtLastName.getText().toString());
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                try {
                    parseUser.fetchIfNeeded();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {


            switch (requestCode) {

                case mREQUEST_CODE_GALLERY:

                    try {

                        InputStream inputStream = getApplication().getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                        Utils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();

                        startCropImage();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case mREQUEST_CODE_TAKE_PICTURE:
                    startCropImage();
                    break;

                case mREQUEST_CODE_CROP_IMAGE:

                    String path = data.getStringExtra(CropImageRectangleSample.IMAGE_PATH);
                    if (path == null)
                        return;
                    imageByte = Utils.getByteArrayFromUri(this, Uri.fromFile(new File(path)));
                    parseDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                    parseDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    parseDialog.setTitleText("Loading");
                    parseDialog.setCancelable(false);
                    parseDialog.show();

                    final ParseUser pUser = ParseUser.getCurrentUser();
                    final ParseFile parseFile = new ParseFile("profile_image.jpg", imageByte);
                    parseFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            try {
                                pUser.put("imageUrl", parseFile.getUrl());
                                pUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject object, ParseException e) {
                                                parseDialog.dismiss();
                                                if(e==null){
                                                    setUserDetails();
                                                }
                                            }
                                        });

                                    }
                                });

                            }catch (Exception exc){
                                exc.getMessage();
                            }


                        }
                    });


                    break;
            }
        }
    }

    private void startCropImage() {

        Intent intent = new Intent(this, CropImageRectangleSample.class);
        intent.putExtra(CropImageRectangleSample.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImageRectangleSample.SCALE, true);
        intent.putExtra(CropImageRectangleSample.ASPECT_X, 3);
        intent.putExtra(CropImageRectangleSample.ASPECT_Y, 3);
        startActivityForResult(intent, mREQUEST_CODE_CROP_IMAGE);
    }

    private void imageDialog(View v) {
        final CharSequence[] items = {"Take Photo", "Choose Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 1)
                    openGallery();
                if (item == 0)
                    takePicture();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private static File getExternalFilesDir(Context context) {
        File dataDir = new File(new File(
                Environment.getExternalStorageDirectory(), "Android"), "data");
        File appFilesDir = new File(
                new File(dataDir, context.getPackageName()), "files");
        if (!appFilesDir.exists()) {
            if (!appFilesDir.mkdirs()) {
                // L.w("Unable to create external cache directory");
                return null;
            }
            try {
                new File(appFilesDir, ".nomedia").createNewFile();
            } catch (IOException exp) {
                exp.getMessage();
                // L.i("Can't create \".nomedia\" file in application external cache directory");
                return null;
            }
        }
        return appFilesDir;
    }

    private void openGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, mREQUEST_CODE_GALLERY);
    }
    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                // mImageCaptureUri =
                // InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, mREQUEST_CODE_TAKE_PICTURE);
        } catch (Exception exp) {
            exp.getMessage();
        }
    }
    private void fileSaving() {
        String stateEnvironment = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(stateEnvironment)) {
            mFileTemp = Utils.getOutputMediaFile(101);
        } else {
            mFileTemp = new File( getBaseContext().getFilesDir(), sTEMP_PHOTO_FILE_NAME);
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs Camera/Photo permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
