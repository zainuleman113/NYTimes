package com.example.nytimesmostpopular.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nytimesmostpopular.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ALARM_SERVICE;



public class CommonMethods {
    private static ProgressDialog pgDialog;
    private static TextView tvTitle;
    private static Toast toast;
    private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
    private static final float ACCURACY = 0.01f;
    private static Bitmap mask = null;
    private static Bitmap original = null;

//    public static LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
//        latDistanceInMeters /= 2;
//        lngDistanceInMeters /= 2;
//        LatLngBounds.Builder builder = LatLngBounds.builder();
//        float[] distance = new float[1];
//        {
//            boolean foundMax = false;
//            double foundMinLngDiff = 0;
//            double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
//            do {
//                Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
//                float distanceDiff = distance[0] - lngDistanceInMeters;
//                if (distanceDiff < 0) {
//                    if (!foundMax) {
//                        foundMinLngDiff = assumedLngDiff;
//                        assumedLngDiff *= 2;
//                    } else {
//                        double tmp = assumedLngDiff;
//                        assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
//                        foundMinLngDiff = tmp;
//                    }
//                } else {
//                    assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
//                    foundMax = true;
//                }
//            } while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
//            LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
//            builder.include(east);
//            LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
//            builder.include(west);
//        }
//        {
//            boolean foundMax = false;
//            double foundMinLatDiff = 0;
//            double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
//            do {
//                Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
//                float distanceDiff = distance[0] - latDistanceInMeters;
//                if (distanceDiff < 0) {
//                    if (!foundMax) {
//                        foundMinLatDiff = assumedLatDiffNorth;
//                        assumedLatDiffNorth *= 2;
//                    } else {
//                        double tmp = assumedLatDiffNorth;
//                        assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
//                        foundMinLatDiff = tmp;
//                    }
//                } else {
//                    assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
//                    foundMax = true;
//                }
//            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
//            LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
//            builder.include(north);
//        }
//        {
//            boolean foundMax = false;
//            double foundMinLatDiff = 0;
//            double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
//            do {
//                Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
//                float distanceDiff = distance[0] - latDistanceInMeters;
//                if (distanceDiff < 0) {
//                    if (!foundMax) {
//                        foundMinLatDiff = assumedLatDiffSouth;
//                        assumedLatDiffSouth *= 2;
//                    } else {
//                        double tmp = assumedLatDiffSouth;
//                        assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
//                        foundMinLatDiff = tmp;
//                    }
//                } else {
//                    assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
//                    foundMax = true;
//                }
//            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
//            LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
//            builder.include(south);
//        }
//        return builder.build();
//    }



//    public static Dialog showConfirmationDialog(Context nContext, String title, String message) {
//        View view = CommonMethods.createView(nContext,
//                R.layout.popup_confirmation, null);
//        TextView tvTittle = (TextView) view.findViewById(R.id.tvTitle);
//        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
//        if (!title.equals("")) {
//            tvTittle.setText(title);
//        }
//        if (!message.equals("")) {
//            tvMessage.setText(message);
//        }
//        Dialog nDialog = new Dialog(nContext, R.style.NewDialog);
//        nDialog.setContentView(view);
//        nDialog.setCancelable(true);
//        nDialog.show();
//        return nDialog;
//    }

    public static void callFragmentWithParameter(Fragment nFragment, int view, Bundle bundle, int enterAnim, int exitAnim, Context context) {
        FragmentManager fm;
        nFragment.setArguments(bundle);
        fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim);
        fragmentTransaction.replace(view, nFragment);
        fragmentTransaction.commit();
    }

    public static void callFragment(Fragment nFragment, int view, int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim, Context context, boolean isBack) {
        FragmentManager fm;
        fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        fragmentTransaction.replace(view, nFragment);
        if (isBack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public static void callFragmentWithTag(Fragment nFragment, int view, int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim, Context context, boolean isBack, String tag) {
        FragmentManager fm;
        fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        fragmentTransaction.replace(view, nFragment);
        if (isBack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }


    public static String loadJSONFromAsset(String fileName, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void showToast(String message, int toastDuration) {
        try {
            cancelToast();
//            View v = createView(CommonObjects.getContext(), R.layout.toast_layout, null);
//            TextView nTextView = (TextView) v.findViewById(R.id.tvToast);
//            nTextView.setText(message);
            toast = Toast.makeText(CommonObjects.getContext(), message, toastDuration);
            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.setView(v);
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void cancelToast() {
        try {
            if (toast != null) {
                toast.cancel();
            }
        } catch (Exception e) {
        }
    }


    public static void hideSoftKeyboard(View v, Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static void callAnActivity(Context newContext,
                                      Class<?> newActivityClass) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            newContext.startActivity(newIntent);
        } catch (Exception e) {
        }
    }

    public static void callAnActivityNew(Context newContext,
                                         Class<?> newActivityClass) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            newContext.startActivity(newIntent);
        } catch (Exception e) {
        }
    }

    public static void callAnActivityForResult(Context newContext,
                                               Class<?> newActivityClass, int requestCode) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            ((Activity) newContext).startActivityForResult(newIntent, requestCode);
        } catch (Exception e) {
        }
    }

    public static void callAnActivityForResultWithParameter(Context newContext,
                                                            Class<?> newActivityClass, int requestCode, String tag, String value) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            newIntent.putExtra(tag, value);
            ((Activity) newContext).startActivityForResult(newIntent, requestCode);
        } catch (Exception e) {

        }
    }

    public static void callAnActivityWithParameter(Context newContext,
                                                   Class<?> newActivityClass, String tag, String value) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            newIntent.putExtra(tag, value);
            newContext.startActivity(newIntent);
        } catch (Exception e) {

        }
    }

    public static void callAnActivityWithParameter(Context newContext,
                                                   Class<?> newActivityClass, String tag1, String value1, String tag2, String value2) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            newIntent.putExtra(tag1, value1);
            newIntent.putExtra(tag2, value2);
            newContext.startActivity(newIntent);
        } catch (Exception e) {

        }
    }

    public static void callAnActivityWithParameter(Context newContext,
                                                   Class<?> newActivityClass, String tag1, String value1, String tag2, String value2, String tag3, String value3) {
        try {
            Intent newIntent = new Intent(newContext, newActivityClass);
            newIntent.putExtra(tag1, value1);
            newIntent.putExtra(tag2, value2);
            newIntent.putExtra(tag3, value3);
            newContext.startActivity(newIntent);
        } catch (Exception e) {

        }
    }

    public static View createView(Context context, int layout, ViewGroup parent) {
        try {
            LayoutInflater newLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert newLayoutInflater != null;
            return newLayoutInflater.inflate(layout, parent, false);
        } catch (Exception e) {
            return null;
        }
    }



    public static int getIntPreference(Context nContext,
                                       String preferenceName, String preferenceItem, int deafaultValue) {
        try {
            SharedPreferences nPreferences;
            nPreferences = nContext.getSharedPreferences(preferenceName,
                    Context.MODE_PRIVATE);
            return nPreferences.getInt(preferenceItem, deafaultValue);
        } catch (Exception e) {
            return deafaultValue;
        }
    }

    public static Boolean getBooleanPreference(Context nContext,
                                               String preferenceName, String preferenceItem,
                                               Boolean defaultValue) {
        try {
            SharedPreferences nPreferences;
            nPreferences = nContext.getSharedPreferences(preferenceName,
                    Context.MODE_PRIVATE);
            return nPreferences.getBoolean(preferenceItem, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public static void setIntPreference(Context nContext,
                                        String preferenceName, String preferenceItem,
                                        int preferenceItemValue) {
        try {
            SharedPreferences nPreferences;
            nPreferences = nContext.getSharedPreferences(preferenceName,
                    Context.MODE_PRIVATE);
            Editor nEditor = nPreferences.edit();
            nEditor.putInt(preferenceItem, preferenceItemValue);
            nEditor.apply();
        } catch (Exception e) {
        }
    }

    public static void setBooleanPreference(Context nContext,
                                            String preferenceName, String preferenceItem,
                                            Boolean preferenceItemValue) {
        try {
            SharedPreferences nPreferences;
            nPreferences = nContext.getSharedPreferences(preferenceName,
                    Context.MODE_PRIVATE);
            Editor nEditor = nPreferences.edit();
            nEditor.putBoolean(preferenceItem, preferenceItemValue);
            nEditor.commit();
        } catch (Exception e) {
        }
    }

    public static String removeSpacing(String phoneNumber) {
        try {
            phoneNumber = phoneNumber.replace("-", "");
            phoneNumber = phoneNumber.replace(" ", "");
            removeNonDigits(phoneNumber);
            if (phoneNumber.length() >= 11) {
                phoneNumber = phoneNumber.substring(phoneNumber.length() - 11);
            }
            return phoneNumber;
        } catch (Exception e) {
            return phoneNumber;
        }
    }

    public static String removeNonDigits(String text) {
        try {
            int length = text.length();
            StringBuffer buffer = new StringBuffer(length);
            for (int i = 0; i < length; i++) {
                char ch = text.charAt(i);
                if (Character.isDigit(ch)) {
                    buffer.append(ch);
                }
            }
            return buffer.toString();
        } catch (Exception e) {
            return text;
        }
    }

    public static int getBooleanInt(boolean bol) {
        try {
            if (bol)
                return 1;
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean getBoolean(int bolInt) {
        try {
            if (bolInt == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context nContext) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) nContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isWifiConnected(Context nContext) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) nContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            }
            return networkInfo == null ? false : networkInfo.isConnected();
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static int getRandom(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    public static int getFontSize(Activity activity, float var) {

        DisplayMetrics dMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);

        // lets try to get them back a font size realtive to the pixel width of the screen
        final float WIDE = activity.getResources().getDisplayMetrics().heightPixels;
        int valueWide = (int) (WIDE / 32.0f / (dMetrics.scaledDensity));
        return (int) ((float) (valueWide * var));
    }

    public static int getDeviceWidth(Context nConext) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) nConext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getDeviceHeight(Context nConext) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) nConext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static boolean isEmailValid(String email) {
        try {
            Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static void threadSleep(int value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void fadeOut(final View view) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            AlphaAnimation alpha = new AlphaAnimation(1f, 0.5f);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            view.startAnimation(alpha);
        } else {
            view.animate()
                    .alpha(0.5f)
                    .setDuration(0)
                    .setListener(null);
        }
    }

    public static void fadeIn(View view) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation alpha = new AlphaAnimation(0.5f, 1f);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            view.startAnimation(alpha);
        } else {
            view.setAlpha(0.5f);
            view.animate()
                    .alpha(1f)
                    .setDuration(0)
                    .setListener(null);
        }
    }

    public static boolean checkForNetworkProvider(
            LocationManager nLocationManager, Context nContext) {
        if (nLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || (CommonMethods.isNetworkAvailable(nContext) || CommonMethods
                .isWifiConnected(nContext))) {
            return true;
        }
        return false;
    }


    public static Bitmap createImageFromView(View view, Point size) {
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(size.x, size.y);
        view.layout(0, 0, size.x, size.y);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    public static Bitmap createImageFromMask(Context context, int maskImage, int originalImage) {
        if (mask == null) {
            mask = BitmapFactory.decodeResource(context.getResources(), maskImage);
        }
        if (original == null) {
            original = BitmapFactory.decodeResource(context.getResources(), originalImage);
        }
        original = Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), true);
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        return result;
    }

    public static Bitmap createImageFromMask(Context context, int maskImage, Bitmap original) {
        if (mask == null) {
            mask = BitmapFactory.decodeResource(context.getResources(), maskImage);
        }
        original = Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), true);
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        return result;
    }

    public static void setImage(ImageView nImageView, byte[] nImageBytes, Point size, boolean isRoundedCorners, int pixels) {
        new LoadImageTask(nImageView, nImageBytes, size, isRoundedCorners, pixels).execute();
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Boolean> {
        private ImageView nImageView;
        private byte[] mCameraData;
        private Bitmap nBitmap;
        private Point nSize;
        private boolean isRoundedCorners;
        private boolean isRounded;
        private int pixels;
        private String nImageString;
        private int rotate;

        public LoadImageTask(ImageView nImageView, String nImageString, Point nSize, boolean isRounded, int rotate) {
            this.nImageView = nImageView;
            this.nImageString = nImageString;
            this.nSize = nSize;
            this.isRounded = isRounded;
            this.rotate = rotate;
        }

        public LoadImageTask(ImageView nImageView, byte[] mCameraData, Point nSize, boolean isRoundedCorners, int pixels) {
            this.nImageView = nImageView;
            this.mCameraData = mCameraData;
            this.nSize = nSize;
            this.isRoundedCorners = isRoundedCorners;
            this.pixels = pixels;
        }

        public LoadImageTask(ImageView nImageView, byte[] mCameraData, Point nSize, boolean isRounded) {
            this.nImageView = nImageView;
            this.mCameraData = mCameraData;
            this.nSize = nSize;
            this.isRounded = isRounded;
        }

        public LoadImageTask(ImageView nImageView, byte[] mCameraData, Point nSize) {
            this.nImageView = nImageView;
            this.mCameraData = mCameraData;
            this.nSize = nSize;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (mCameraData != null) {
                nBitmap = loadBitmap(mCameraData);
            } else {
                nBitmap = loadBitmap(nImageString);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (nBitmap != null) {
                nImageView.setImageBitmap(nBitmap);
            }
        }

        private Bitmap loadBitmap(byte[] cameraData) {
            Bitmap bitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length), nSize.y, nSize.x, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            if (isRoundedCorners) {
                bitmap = getRoundedCornerBitmap(bitmap, pixels);
            } else if (isRounded) {
                bitmap = getRoundedShapeBitmap(bitmap);
            }
            Matrix nMatrix = new Matrix();
            nMatrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), nMatrix, true);
            return bitmap;
        }

        private Bitmap loadBitmap(String imageString) {
            Bitmap bitmap = null;
            if (!imageString.contains("http")) {
//                bitmap = Bitmap.createScaledBitmap(
//                        BitmapFactory.decodeFile(imageString), nSize.x, nSize.y, true);
                bitmap = lessResolution(imageString, nSize.x, nSize.y);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                bitmap = rotateBitmap(bitmap, getCameraPhotoOrientation(imageString));
                bitmap = rotateBitmap(bitmap, rotate);
            } else {
                try {
                    InputStream in = new URL(imageString).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    bitmap = Bitmap.createScaledBitmap(bitmap, nSize.x, nSize.y, true);
//                    bitmap = rotateBitmap(bitmap, 360f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null) {
                if (isRoundedCorners) {
                    bitmap = getRoundedCornerBitmap(bitmap, pixels);
                } else if (isRounded) {
                    bitmap = getRoundedShapeBitmap(bitmap);
                }
                Matrix nMatrix = new Matrix();
//                nMatrix.postRotate(getCameraPhotoOrientation(imageString));
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), nMatrix, true);
            }
            return bitmap;
        }

        private Bitmap lessResolution(String filePath, int width, int height) {
            int reqHeight = height;
            int reqWidth = width;
            BitmapFactory.Options options = new BitmapFactory.Options();

            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(filePath, options);
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                // Calculate ratios of height and width to requested height and width
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);

                // Choose the smallest ratio as inSampleSize value, this will guarantee
                // a final image with both dimensions larger than or equal to the
                // requested height and width.
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        }

        public int getCameraPhotoOrientation(String imagePath) {
            int rotate = 0;
            try {
//                context.getContentResolver().notifyChange(imageUri, null);
//                File imageFile = new File(imagePath);

                ExifInterface exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rotate;
        }

        public Bitmap rotateBitmap(Bitmap source, float angle) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }

        private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }

        private Bitmap getRoundedShapeBitmap(Bitmap bitmap) {
            int targetWidth = nSize.x;
            int targetHeight = nSize.y;
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);
            canvas.clipPath(path);
            Bitmap sourceBitmap = bitmap;
            canvas.drawBitmap(sourceBitmap,
                    new Rect(0, 0, sourceBitmap.getWidth(),
                            sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);
            return targetBitmap;
        }
    }

    public static void setImage(ImageView nImageView, String nImageString, Point size, boolean isRounded, int rotate) {
        new LoadImageTask(nImageView, nImageString, size, isRounded, rotate).execute();
    }

    public static void setImage(ImageView nImageView, byte[] mCameraData, Point nSize, boolean isRounded) {
        new LoadImageTask(nImageView, mCameraData, nSize, isRounded).execute();
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

//    public static boolean isValidUrl(String url) {
//        UrlValidator defaultValidator = new UrlValidator(); // default schemes
//        if (defaultValidator.isValid(url)) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTodaysDate() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String hour = date.get(Calendar.HOUR) + "";
//        String minute = date.get(Calendar.MINUTE) + "";
//        if (hour.length() == 1) {
//            hour = "0" + hour;
//        }
//        if (minute.length() == 1) {
//            minute = "0" + minute;
//        }
//        String dateToday = getDay(date.get(Calendar.DAY_OF_WEEK)) + ", " + date.get(Calendar.DAY_OF_MONTH) + " " + getMonth(date.get(Calendar.MONTH)) + " " + hour + ":" + minute + ", " + date.get(Calendar.YEAR);
        return sdf.format(date.getTime());
    }

    public static String getDay(int id) {
        switch (id) {
            case 1:
                return "Sunday";

            case 2:
                return "Monday";

            case 3:
                return "Tuesday";

            case 4:
                return "Wednesday";

            case 5:
                return "Thursday";

            case 6:
                return "Friday";

            case 7:
                return "Saturday";

        }

        return "";
    }

    public static String getMonth(int id) {
        switch (id) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

//    public static  double calculationByDistance(LatLng StartP, LatLng EndP) {
////        int Radius = 6371;// radius of earth in Km
//        double lat1 = StartP.latitude;
//        double lat2 = EndP.latitude;
//        double lon1 = StartP.longitude;
//        double lon2 = EndP.longitude;
//
////        double lat1 = 31.516968;
////        double lat2 = 31.524256;
////        double lon1 = 74.344373;
////        double lon2 = 74.346178;
//
////        double dLat = Math.toRadians(lat2 - lat1);
////        double dLon = Math.toRadians(lon2 - lon1);
////        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
////                + Math.cos(Math.toRadians(lat1))
////                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
////                * Math.sin(dLon / 2);
////        double c = 2 * Math.asin(Math.sqrt(a));
////        double valueResult = Radius * c;
////        double km = valueResult / 1;
////        DecimalFormat newFormat = new DecimalFormat("####");
////        int kmInDec = Integer.valueOf(newFormat.format(km));
////        double meter = valueResult % 1000;
////        return meter;
////        int meterInDec = Integer.valueOf(newFormat.format(meter));
////        float[] results = new float[1];
////        Location.distanceBetween(lat1, lon1,
////                lat2, lon2, results);
////        float meter=0;
////        if(results.length==1)
////        {
////            meter=results[0];
////        }
////        else if(results.length==2)
////        {
////            meter=results[1];
////        }
////        else if(results.length==3)
////        {
////            meter=results[2];
////        }
////        return meter;
////        Location l1 = new Location("");
////        Location l2 = new Location("");
////        l1.setLatitude(lat1);
////        l1.setLongitude(lon1);
////        l2.setLatitude(lat2);
////        l2.setLongitude(lat2);
////        return Math.round(l1.distanceTo(l2));
//
//        double earthRadius = 6371000; //meters
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLng = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        float dist = (float) (earthRadius * c);
//
//        return dist;
////        return meters;
//    }

//    public static boolean isLocationEnabled(Context context) {
//        int locationMode = 0;
//        String locationProviders;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
//        } else {
//            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            return !TextUtils.isEmpty(locationProviders);
//        }
//    }

    public static Bitmap getImageFromText(String text, Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 55);
        tv.setLayoutParams(layoutParams);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv.setMaxLines(1);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.TRANSPARENT);

        Bitmap testB;

        testB = Bitmap.createBitmap(80, 55, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(testB);
        tv.layout(0, 0, 80, 55);
        tv.draw(c);
        return testB;
    }

//    public static void loadBitmapFromUrl(String url, FutureCallback<Bitmap> bitmapLoadCallBack) {
//        LoadBuilder<Builders.Any.B> loadBuilder = Ion.with(CommonObjects.getContext());
//        Builders.Any.B b = loadBuilder.load("GET", Constants.APP_METHODS.BASE_URL + url);
//        b.uploadProgressBar(new ProgressBar(CommonObjects.getContext()));
//        b.asBitmap().setCallback(bitmapLoadCallBack);
//    }

//    public static void loadBitmapFromUrl(final String url, final BitmapCallBackImageLoad bitmapCallBackImageLoad) {
//        LoadBuilder<Builders.Any.B> loadBuilder = Ion.with(CommonObjects.getContext());
//        Builders.Any.B b = loadBuilder.load("GET", Constants.APP_METHODS.BASE_URL + url);
//        b.uploadProgressBar(new ProgressBar(CommonObjects.getContext()));
//        b.asBitmap().setCallback(new FutureCallback<Bitmap>() {
//            @Override
//            public void onCompleted(Exception e, Bitmap result) {
//                bitmapCallBackImageLoad.onImageLoad(e, result, url);
//            }
//        });
//    }

//    public static void loadBitmapFromOther(String url, FutureCallback<Bitmap> bitmapLoadCallBack) {
//        LoadBuilder<Builders.Any.B> loadBuilder = Ion.with(CommonObjects.getContext());
//        Builders.Any.B b = loadBuilder.load("GET", url);
//        b.uploadProgressBar(new ProgressBar(CommonObjects.getContext()));
//        b.asBitmap().setCallback(bitmapLoadCallBack);
//    }

//    public static ArrayList<Contact> readPhoneContacts(Context context) {
//        ArrayList<Contact> contactArrayList = new ArrayList<>();
//        try {
//            ContentResolver cr = context.getContentResolver();
//            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//            HashMap<String,String> contactEmails=new HashMap<>();
//            Contact contact;
//            while (phones.moveToNext()) {
//                String contactId = phones
//                        .getString(phones
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//                String contactName = phones
//                        .getString(phones
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                String contactNumber = phones
//                        .getString(phones
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                contact = new Contact(contactId,contactNumber, contactName);
//                contactArrayList.add(contact);
//            }
//            phones.close();
//            return contactArrayList;
//        } catch (Exception e) {
//            return contactArrayList;
//        }
//    }

//    public static Bitmap retrieveContactPhoto(Context context, String number) {
//        ContentResolver contentResolver = context.getContentResolver();
//        String contactId = null;
//        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
//
//        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};
//
//        Cursor cursor =
//                contentResolver.query(
//                        uri,
//                        projection,
//                        null,
//                        null,
//                        null);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
//            }
//            cursor.close();
//        }
//
//        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_contact_place_holder);
//
//        try {
//            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
//                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));
//
//            if (inputStream != null) {
//                photo = BitmapFactory.decodeStream(inputStream);
//                inputStream.close();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return photo;
//    }

//    public static boolean setGridListViewHeightBasedOnItems(ListView listView, int numberChange) {
//
//        ListAdapter listAdapter = listView.getAdapter();
//
//        int heightHeader = 0;
//        if (numberChange < 0) {
//            heightHeader = (-1) * numberChange;
//            numberChange = 0;
//        }
//
//        if (listAdapter != null) {
//            int numberOfItems = 0;
//            if (numberChange == 0) {
//                numberOfItems = listAdapter.getCount();
//            } else {
//                numberOfItems = numberChange;
//            }
//
//            // Get total height of all items.
//            if (numberOfItems != 0) {
//                int totalItemsHeight = 0;
////                View item = listAdapter.getView(0, null, listView);
////                if (item != null)
////                    item.measure(0, 0);
////                totalItemsHeight = item.getMeasuredHeight() * numberOfItems;
//                for (int i = 0; i < listAdapter.getCount(); i++) {
//                    View listItem = listAdapter.getView(i, null, listView);
//                    if (listItem instanceof ViewGroup) {
//                        listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    }
//
//                    listItem.measure(0, 0);
//                    totalItemsHeight += listItem.getMeasuredHeight();
//                }
//
//
//                // Get total height of all item dividers.
//                int totalDividersHeight = listView.getDividerHeight() *
//                        (numberOfItems - 1);
//
//                // Set list height.
//                ViewGroup.LayoutParams params = listView.getLayoutParams();
//                params.height = totalItemsHeight + totalDividersHeight + heightHeader;
//                listView.setLayoutParams(params);
//                listView.requestLayout();
//            }
//            return true;
//        } else {
//            return false;
//        }
//
//    }


    public static boolean setListViewHeightBasedOnItems(ListView listView, int numberChange) {

        ListAdapter listAdapter = listView.getAdapter();

        int heightHeader = 0;
        if (numberChange < 0) {
            heightHeader = (-1) * numberChange;
            numberChange = 0;
        }

        if (listAdapter != null) {
            int numberOfItems = 0;
            if (numberChange == 0) {
                numberOfItems = listAdapter.getCount();
            } else {
                numberOfItems = numberChange;
            }

            // Get total height of all items.
            if (numberOfItems != 0) {
                int totalItemsHeight = 0;
                View item = listAdapter.getView(0, null, listView);
                if (item != null)
                    item.measure(0, 0);
                totalItemsHeight = item.getMeasuredHeight() * numberOfItems;


                // Get total height of all item dividers.
                int totalDividersHeight = listView.getDividerHeight() *
                        (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight + heightHeader;
                listView.setLayoutParams(params);
                listView.requestLayout();
            }
            return true;
        } else {
            return false;
        }

    }

    public static void setupUI(final View view, final Context context) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(view, context);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, context);
            }
        }
    }

    public static String getThumbnailPathForLocalFile(Activity context,
                                                      Uri fileUri) {
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};

        long fileId = getFileId(context, fileUri);
        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
        Cursor thumbCursor = null;
        try {
            thumbCursor = context.managedQuery(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                            + fileId, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }
        } finally {
        }
        return null;
    }

    public static long getFileId(Activity context, Uri fileUri) {
        String[] mediaColumns = {MediaStore.Video.Media._ID};

        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null,
                null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int id = cursor.getInt(columnIndex);

            return id;
        }

        return 0;
    }

    public static String getDateByCount(int count) {
        String dateByCount = "";
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, count);
        dateByCount = getMonth(date.get(Calendar.MONTH) + 1) + " " + date.get(Calendar.YEAR);
        return dateByCount;
    }


    public static String getShortDay(int id) {
        switch (id) {
            case 1:
                return "Sun";

            case 2:
                return "Mon";

            case 3:
                return "Tue";

            case 4:
                return "Wed";

            case 5:
                return "Thu";

            case 6:
                return "Fri";

            case 7:
                return "Sat";

        }

        return "";
    }

    public static ArrayList<String> getCurrentWeekDates() {
        ArrayList<String> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar date = Calendar.getInstance();
        date.setFirstDayOfWeek(Calendar.SUNDAY);

//        date.set(Calendar.DAY_OF_WEEK, date.getFirstDayOfWeek());

        for (int i = 1; i <= 7; i++) {
            if (i <= date.get(Calendar.DAY_OF_WEEK)) {
                dates.add(sdf.format(date.getTime()));
                date.add(Calendar.DAY_OF_WEEK, 1);
            }
//            dates.add(date.get(Calendar.DAY_OF_MONTH) + " " + getMonth(date.get(Calendar.MONTH)) + " " + date.get(Calendar.YEAR));
        }
        return dates;
    }

    public static String geTodayDate() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date.getTime());
    }

    public static String getTomorrowDate() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date.add(Calendar.DAY_OF_WEEK, 1);
//        return date.get(Calendar.DAY_OF_MONTH) + " " + getMonth(date.get(Calendar.MONTH)) + " " + date.get(Calendar.YEAR);
        return sdf.format(date.getTime());
    }

    //Open email intent to send email
    public static void composeEmail(Context context, String email, String subject, Spanned body, String filePath) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    //Open url in browser
    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static String getOfferLink(String name) {
        return "https://thedubaimall.com/en/offer-detail/" + name.replace(" ", "-");
//        return "http://tdm.acceptance.mall-connect.com/en/offer-detail/"+name.replace(" ","-");
    }

    public static String getEventLink(String name) {
        return "https://thedubaimall.com/en/event-detail/" + name.replace(" ", "-");
//        return "http://tdm.acceptance.mall-connect.com/en/event-detail/"+name.replace(" ","-");
    }

    public static String getStoreLink(int id) {
//        return "http://tdm.acceptance.mall-connect.com/en/shop/"+id;
        return "https://thedubaimall.com/en/shop/" + id;

    }

    //Share link on faceBook using deep linking
    public static void faceBookShareIntent(Context context, String urlToShare) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

// As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        context.startActivity(intent);
    }

    //Share link on twitter using deep linking
    public static void twitterShareIntent(Context context, String urlToShare) {
        // Create intent using ACTION_VIEW and a normal Twitter url:
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(""),
                urlEncode(urlToShare));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        context.startActivity(intent);
    }

    //Share link on whatsapp using deep linking
    public static void whatsAppShareIntent(Context context, String text) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            context.startActivity(whatsappIntent);
        } catch (ActivityNotFoundException ex) {
        }
    }

    public static void smsShareIntent(Context context, String text) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("address", "");
            sendIntent.putExtra("sms_body", text);
            sendIntent.setType("vnd.android-dir/mms-sms");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("Twitter Share", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    //Share link on pinterest using deep linking
    public static void pinterestShareIntent(Context context, String shareUrl) {
        String mediaUrl = "";
        String description = "";
        String url = String.format(
                "https://www.pinterest.com/pin/create/button/?url=%s&media=%s&description=%s",
                urlEncode(shareUrl), urlEncode(mediaUrl), description);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        filterByPackageName(context, intent, "com.pinterest");
        context.startActivity(intent);
    }

    //Open instagram url in instagram app ussing deep linking if available otherwise open it in browser
    public static void openInstagram(Context context, String url) {
        Uri uri = Uri.parse(url.replace(".com/", ".com/_u/"));
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        }
    }

    private static void filterByPackageName(Context context, Intent intent, String prefix) {
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(prefix)) {
                intent.setPackage(info.activityInfo.packageName);
                return;
            }
        }
    }

    //Check for version greater then LOLLIPOP
    public static boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    //Check if specific array of permissions are given or not
    public static boolean checkAllPermission(Context context, String[] permissons) {
        boolean isAllow = false;
        for (String perm : permissons) {
            if (hasPermission(context, perm)) {
                isAllow = true;
            } else {
                isAllow = false;
            }
        }
        return isAllow;
    }

    //Check if specific permission is given or not
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context, String permission) {
        if (canMakeSmores()) {
            return (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    //Show a permission string message on alert dialog
    public static void showPermissionMessage(final Context context, String message, final String[] perms) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                int permsRequestCode = 200;
                ((Activity) context).requestPermissions(perms, permsRequestCode);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();

            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    //Show a string message on alert dialog
    public static void showMessage(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        if (message.equals("")) {
            message = "No erro message has received from server.";
        }
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    //Return floor name by floor id
    private static String getFloorNameUsingId(int id) {
        switch (id) {
            case 1:
                return "Lower Ground";
            case 2:
                return "Ground Floor";
            case 3:
                return "First Floor";
            case 4:
                return "Second Floor";
        }
        return null;
    }

    public interface BitmapCallBackImageLoad {
        public void onImageLoad(Exception e, Bitmap bitmap, String url);
    }


    public interface OnDownloadRepoResult {
        public void onDownloaded();

        public void onCanceled();
    }

//    //Load image on a image view from url
//    public static void universalImageLoadTask(String url, int resourcePlaceHolder, ImageView imageView) {
//        DisplayImageOptions displayImageOptions = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(true)
//                .showImageOnFail(resourcePlaceHolder)
//                .showImageForEmptyUri(resourcePlaceHolder)
//                .showImageOnLoading(resourcePlaceHolder)
//                .cacheOnDisk(true).delayBeforeLoading(0)
//                .build();
//        ImageLoader.getInstance().displayImage( url, imageView, displayImageOptions);
//    }

//    //Load image on a image view from url
//    public static void universalImageLoadTask(String url, ImageView imageView,String baseUrl) {
//
//        Log.d("DEBUG_IMAGE_LOADER",baseUrl+url);
//        DisplayImageOptions displayImageOptions = new DisplayImageOptions
//                .Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true).delayBeforeLoading(0)
//                .showImageOnLoading(0) // resource or drawable
//                .showImageForEmptyUri(0) // resource or drawable
//                .showImageOnFail(0)
//                .build();
//        ImageLoader.getInstance().displayImage(baseUrl+url, imageView, displayImageOptions);
//    }

    //Returns device unique id
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    //Apply expand bottom to top animation on a View
    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    //Apply collapse top to bottom animation on a View
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    //Return date by converting from string to Date object
    public static Date getDate(String dateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    //Return date in format yyyy-MM-dd HH:mm:ss
    public static String getDateFormated(String dateInput) {
        if (dateInput != null) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = sdf1.parse(dateInput);
                return sdf2.format(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateInput;
    }

    //Return date in format yyyy-MM-dd HH:mm:ss
    public static String getDateFormatedServer(String dateInput) {
        if (dateInput != null) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = sdf2.parse(dateInput);
                return sdf1.format(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateInput;
    }

    //Return date in format yyyy-MM-dd HH:mm:ss
    public static String getTimeFormated(String dateInput) {
        if (dateInput != null) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf1.parse(dateInput);
                return sdf2.format(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateInput;
    }


    //Return time in 12 hour format hh:mm a
    public static String getTimeFormatChanged(String _24HourTime) {
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //Return number in format xxxx-x-xxx-xxxx
    public static String getNumberFormated(String numberStr) {
        if (numberStr.contains("+971") && numberStr.contains("-")) {
            return numberStr;
        } else {
            if (numberStr.contains("+971")) {
                String number = "";
                for (int i = 0; i < numberStr.length(); i++) {
                    number += numberStr.charAt(i) + "";
                    if (i == 3 || i == 4 || i == 7) {
                        number += "-";
                    }
                }
                numberStr = number;
            }

        }
        return numberStr;
    }

    //Return date in format day month year - day month year
    public static String getDateFormatedStartEnd(String startDate, String endDate) {
        if (!startDate.equals("")) {
            startDate = startDate.split(" ")[0];
            String dateParts[] = startDate.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            month = CommonMethods.getMonth(Integer.valueOf(month));
            if (year.length() > 2) {
                year = year.charAt(year.length() - 2) + "" + year.charAt(year.length() - 1) + "";
            }
            startDate = day + " " + month + " " + year;
        }
        if (!endDate.equals("")) {
            endDate = endDate.split(" ")[0];
            String dateParts[] = endDate.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];
            month = CommonMethods.getMonth(Integer.valueOf(month));
            if (year.length() > 2) {
                year = year.charAt(year.length() - 2) + "" + year.charAt(year.length() - 1) + "";
            }
            endDate = day + " " + month + " " + year;
        }
        return startDate + " - " + endDate;
    }

    //Show keyboard of specific edittext
    public static void showSoftKeyboard(EditText et, Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
        }
    }


    //Returns string without characters based on specific pattern
    public static String getStringWithoutCharacter(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z . ,']");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    //Remove a fragment from frame layout with animation
    public static void removeFragment(Fragment nFragment, int enterAnim, int exitAnim, Context context) {
        FragmentManager fm;
        fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim);
        fragmentTransaction.remove(nFragment);
        fragmentTransaction.commit();
    }

    public static String getTimeDifference(String date, int type) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
        Date dateToday = new Date();
        Date dateInput = null;
        try {
            dateInput = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = dateToday.getTime() - dateInput.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (type == 1) {
            return getTimeString1ForDayOne(diffSeconds, diffMinutes, diffHours, diffDays);
        } else {
            return getTimeString1ForDayTwo(diffSeconds, diffMinutes, diffHours, diffDays);
        }
    }

    public static String getTimeString1ForDayOne(long seconds, long minutes, long hours, long days) {
        if (days > 0) {
            if (days == 1) {
                return days + "d";
            } else {
                return days + "d";
            }
        } else if (hours > 0) {
            if (hours == 1) {
                return hours + "h";
            } else {
                return hours + "h";
            }
        } else if (minutes > 0) {
            if (minutes == 1) {
                return minutes + "m";
            } else {
                return minutes + "m";
            }
        } else if (seconds > 0) {
            if (seconds == 1) {
                return seconds + "s";
            } else {
                return seconds + "s";
            }
        }
        return "";
    }

    public static String getTimeString1ForDayTwo(long seconds, long minutes, long hours, long days) {
        if (days > 0) {
            if (days == 1) {
                return days + " day ago";
            } else {
                return days + " days ago";
            }
        } else if (hours > 0) {
            if (hours == 1) {
                return hours + " hour ago";
            } else {
                return hours + " hours ago";
            }
        } else if (minutes > 0) {
            if (minutes == 1) {
                return minutes + " mimute ago";
            } else {
                return minutes + " minutes ago";
            }
        } else if (seconds > 0) {
            if (seconds == 1) {
                return seconds + " second ago";
            } else {
                return seconds + " seconds ago";
            }
        }
        return "";
    }


    public static String getDateFromTimeStamp(String timestring) {
        Date d = new Date(Long.valueOf(timestring) * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestring = sdf.format(d);
        return timestring;
    }

    public static void rate(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static String getFromUnix(String timestring) {
        Date d = new Date(Long.valueOf(timestring) * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestring = sdf.format(d);
        return timestring;
    }

    public static String getDateToLocal(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dfTo = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = df.parse(dateStr);
            dfTo.setTimeZone(TimeZone.getDefault());
            return dfTo.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setSpanColor(TextView textview, String first, String next, int color) {
        String nextPlus = "<font color='#50b748'>" + next + "</font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textview.setText(Html.fromHtml(first + nextPlus, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textview.setText(Html.fromHtml(first + nextPlus));
        }
//        textview.setText(first + next, BufferType.SPANNABLE);
//        Spannable s = (Spannable)textview.getText();
//        int start = first.length();
//        int end = start + next.length();
//        s.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setSpanColorDouble(TextView textview, String first, String next, String second, String secondNext) {
        String nextPlus = "<font color='#50b748'>" + next + "</font>";
        String nextPlusSecond = "<font color='#50b748'>" + secondNext + "</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textview.setText(Html.fromHtml(first + nextPlus + " " + second + nextPlusSecond, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textview.setText(Html.fromHtml(first + nextPlus + " " + second + nextPlusSecond));
        }
//        textview.setText(first + next, BufferType.SPANNABLE);
//        Spannable s = (Spannable)textview.getText();
//        int start = first.length();
//        int end = start + next.length();
//        s.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    //Show call dialog for user to call a store or mall number etc
    public static void makeCall(final Context context, final String number) {
        if (number != null && !number.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            context.startActivity(intent);
        }
        else
        {
            showMessage(context,"Phone number not available");
        }

    }

}
