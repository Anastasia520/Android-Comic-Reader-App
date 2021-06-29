package com.hse.app.reader.comic.comicreader;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.GridLayoutManager;

import com.hse.app.reader.comic.comicreader.Adapter.MultipleChooseAdapter;
import com.hse.app.reader.comic.comicreader.Adapter.MyComicAdapter;
import com.hse.app.reader.comic.comicreader.common.Common;
import com.hse.app.reader.comic.comicreader.model.Category;
import com.hse.app.reader.comic.comicreader.model.Comic;
import com.hse.app.reader.comic.comicreader.retrofit.IComicAPI;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CategoryFilter extends AppCompatActivity {

    Button btn_filter, btn_search;
    IComicAPI iComicAPI;

    String searchQuery;
    TextView searchTxt;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_filter;


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);

        iComicAPI = Common.getAPI();

        new Handler().post(new Runnable(){


            @Override
            public void run() {

                fetchCategory();
            }
        });
        //view
        searchTxt = (TextView) findViewById(R.id.search_txt);
        btn_filter =(Button)findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });

        btn_search =(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        recycler_filter = (RecyclerView)findViewById(R.id.recycler_filter);
        recycler_filter.setHasFixedSize(true);
        recycler_filter.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void showSearchDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryFilter.this);
        alertDialog.setTitle("Поиск");

        LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search, null);

        final EditText edit_search = (EditText) search_layout.findViewById(R.id.edit_search);
        alertDialog.setView(search_layout);

        alertDialog.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("ПОИСК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fetchSearchComic(edit_search.getText().toString());
                if (edit_search.getText().toString() != ""){
                    searchTxt.setText("Поиск: "+edit_search.getText().toString());
                    searchTxt.setVisibility(View.VISIBLE);
                }else searchTxt.setVisibility(View.INVISIBLE);

            }
        });
        alertDialog.show();


    }

    private void fetchSearchComic(final String search) {


        compositeDisposable.add(iComicAPI.getSearchedComic(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {


                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new MyComicAdapter(getBaseContext(),comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(CategoryFilter.this, "Комикс не найден", Toast.LENGTH_LONG).show();
                    }
                }));



    }

    private void showOptionDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryFilter.this);
        alertDialog.setTitle("Выберите категорию");

        LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_option, null);

        RecyclerView recycler_options = (RecyclerView) filter_layout.findViewById(R.id.recycler_options);
        recycler_options.setHasFixedSize(true);
        recycler_options.setLayoutManager(new LinearLayoutManager(this));
        final MultipleChooseAdapter adapter = new MultipleChooseAdapter(getBaseContext(),Common.categories);
        recycler_options.setAdapter(adapter);

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("Фильтр", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fetchFilterCategory(adapter.getFilterArray());
            }
        });
        alertDialog.show();
    }

    private void fetchFilterCategory(String data) {


        compositeDisposable.add(iComicAPI.getFilteredcomic(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {

                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new MyComicAdapter(getBaseContext(),comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        Toast.makeText(CategoryFilter.this, "Комикс не найден", Toast.LENGTH_LONG).show();
                    }
                }));

    }

    private void fetchCategory() {

        compositeDisposable.add(iComicAPI.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {

                        Common.categories = categories;

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CategoryFilter.this, "Ошибка загрузки категорий", Toast.LENGTH_LONG).show();
                    }
                }));
    }
}
