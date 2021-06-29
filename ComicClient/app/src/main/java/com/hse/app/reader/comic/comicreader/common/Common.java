package com.hse.app.reader.comic.comicreader.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hse.app.reader.comic.comicreader.model.Category;
import com.hse.app.reader.comic.comicreader.model.Chapter;
import com.hse.app.reader.comic.comicreader.model.Comic;
import com.hse.app.reader.comic.comicreader.retrofit.IComicAPI;
import com.hse.app.reader.comic.comicreader.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static Comic selected_comic;
    public static Chapter selected_chapter;

    public static int chapter_index = 1;

    public static List<Chapter> chapterList = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    public static IComicAPI getAPI(){
        return RetrofitClient.getInstance().create(IComicAPI.class);
    }

    public static String formatString(String name) {

        // Если название главы длинное - берем подстроку
        StringBuilder finalResult = new StringBuilder(name.length() > 15 ? name.substring(0, 15)+"...":name);
        return finalResult.toString();
    }

    public static boolean isConnectToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager !=null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){

                for (int i=0; i<info.length;i++)

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
            }
        }
        return false;
    }
}
