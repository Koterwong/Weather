package com.koterwong.weather.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Author：Koterwong，
 * Date：2016/5/25 21:15
 * Description:
 */
public class ShareUtils {

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    public static void share(Context context, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }
}
