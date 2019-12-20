package com.lwj.wants.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * author   LWJ
 * date on  2018/3/6.
 * describe 添加描述
 */
public class PhotographUtil {
    public static Uri createImageUri(Context context) {
        String name = String.valueOf(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
     //   contentValues.put(MediaStore.Images.Media.DATA,name);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

    }
    public static void delteImageUri(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);
    }
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
