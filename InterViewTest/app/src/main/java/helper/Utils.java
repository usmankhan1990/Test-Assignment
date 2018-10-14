package helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.interviewtest.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by UsmanKhan on 11/12/17.
 */

public class Utils {


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
    public static Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap loadCircleProfileImage(Context ctx, byte[] data,
                                                ImageView imageView) {
        Bitmap bitmap = null;
        try{

            if (data == null) {

                bitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.mipmap.ic_launcher);
            } else
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            imageView.setImageBitmap(Utils.getclip(bitmap));
            imageView.setImageBitmap(bitmap);
            //BitmapDrawable ob = new BitmapDrawable(ctx.getResources(), bitmap);
            //imageView.setBackgroundDrawable(ob);
            imageView.invalidate();
            return bitmap;
        }catch(NullPointerException nEx){

        }
        return bitmap;
    }

    /**
     *
     * @param ctx
     * @param data
     * @param imageView
     */
    public static Bitmap loadProfileImage(Context ctx, byte[] data,
                                          ImageView imageView) {
        Bitmap bitmap = null;
        try{

            if (data == null) {

                bitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.drawable.defaultprofile);
            } else
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // imageView.setImageBitmap(Utils.getclip(bitmap));
            //imageView.setImageBitmap(bitmap);
            BitmapDrawable ob = new BitmapDrawable(ctx.getResources(), bitmap);
            imageView.setBackgroundDrawable(ob);
            imageView.invalidate();
            return bitmap;
        }catch(NullPointerException nEx){

        }
        return bitmap;
    }

    static int dataSize=0;

    public static byte[] getByteArrayFromUri(Activity act, Uri uri) {
        InputStream iStream;
        try {
            iStream = act.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            byte[] image = byteBuffer.toByteArray();
            byteBuffer.close();

            String scheme = uri.getScheme();
            System.out.println("Scheme type " + scheme);
            if(scheme.equals(ContentResolver.SCHEME_CONTENT))
            {

                // This method is used to get total size of File
                /* try {
                    InputStream fileInputStream=act.getApplicationContext().getContentResolver().openInputStream(uri);
                    dataSize = fileInputStream.available();
                    dataSize = dataSize/1024;
                    dataSize = dataSize/1024;
                    Log.e("File size in bytes ",dataSize+ "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */


            }



            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted

        if (Environment.getExternalStorageState() != null) {
            // this works for Android 2.2 and above
            File mediaStorageDir = getFolderFile();

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MediaStorageDir", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File mediaFile;
            if (type == 101) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "user_profile.jpg");
            } else if (type == 10101) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + new Date().getTime() + ".jpg");
            } else if (type == 20202) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VOICE_" + timeStamp + ".mp3");
            } else if (type == 30303) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }

        return null;
    }

    public static File getFolderFile() {
        return new File(Environment.getExternalStorageDirectory(), "1BMAT");
    }

    public static File getPicFolderFile() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "1BMAT/Pics");
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

//    public static void  share_bitMap_to_Apps(Context mContext, View relative_me_other, String message) {
    public static void  share_bitMap_to_Apps(Context mContext, View relative_me_other, String message) {

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("image/*");

//        intent.putExtra(Intent.EXTRA_SUBJECT, "My App name and some text");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.putExtra(Intent.EXTRA_STREAM, getImageUri(mContext, getBitmapFromView(relative_me_other)));
        try {
            mContext.startActivity(Intent.createChooser(intent, "My Profile ..."));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),      view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
