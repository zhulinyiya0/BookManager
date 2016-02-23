package com.example.zane.bookmanager.presenters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.zane.bookmanager.R;
import com.example.zane.bookmanager.app.MyApplication;
import com.example.zane.bookmanager.inject.component.DaggerActivityComponent;
import com.example.zane.bookmanager.inject.module.ActivityModule;
import com.example.zane.bookmanager.model.bean.Book;
import com.example.zane.bookmanager.model.bean.Book_DB;
import com.example.zane.bookmanager.model.data.DataManager;
import com.example.zane.bookmanager.presenters.MainActivity;
import com.example.zane.bookmanager.presenters.fragment.MyBookInfoFragment;
import com.example.zane.bookmanager.presenters.fragment.RecommendedBookFragment;
import com.example.zane.bookmanager.view.MyBookDetailInfoView;
import com.example.zane.easymvp.presenter.BaseActivityPresenter;
import com.kermit.exutils.utils.ExUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Zane on 16/2/18.
 */
public class MyBookDetailInfoActivity extends BaseActivityPresenter<MyBookDetailInfoView>{

    private Toolbar toolbar;
    public static final String BOOKDB = "BOOKDB";
    private String whereToComeFrom;

    @Inject
    DataManager dataManager;

    @Override
    public Class<MyBookDetailInfoView> getRootViewClass() {
        return MyBookDetailInfoView.class;
    }

    @Override
    public void inCreat(Bundle bundle) {
        initInject();
        whereToComeFrom = getIntent().getStringExtra(MainActivity.TAG);
        toolbar = v.get(R.id.toolbar_mybookdetailinfo_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //判断是从哪一个activity跳转而来，因为我需要代码复用。！
        if (whereToComeFrom.equals(MainActivity.TAG)) {
            final Book_DB book = (Book_DB) getIntent().getSerializableExtra(MyBookInfoFragment.BOOK_DB);
            v.setTextviewBookIntroMybookdetail(book.getSummary());
            v.setTextviewAuthorIntroMybookdetail(book.getAuthor_intro());
            v.setupToolbar(book.getImage(), book.getTitle());
            v.setTextviewAuthornameMybookdetail(book.getAuthor());
            v.setTextviewBooknameMybookdetail(book.getTitle());
            v.setTextviewPagesMybookdetail(book.getPages());
            v.setTextviewPriceMybookdetail(book.getPrice());
            v.setTextviewPublishdateMybookdetail(book.getPubdate());
            v.setTextviewPublishnameMybookdetail(book.getPublisher());

            //fab监听，跳转到推荐书籍页面去
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyBookDetailInfoActivity.this, RecommendedBookActivity.class);
                    intent.putExtra(BOOKDB, book);
                    startActivity(intent);
                }
            }, R.id.fab_mydetialbookinfo_activity);
        } else if(whereToComeFrom.equals(RecommendedBookFragment.TAG)){
            String isbn = getIntent().getStringExtra(RecommendedBookFragment.ISBN);
            dataManager.getBookInfo(isbn)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Book>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ExUtils.Toast(String.valueOf(e));
                        }

                        @Override
                        public void onNext(Book book) {
                            v.setTextviewBookIntroMybookdetail(book.getSummary());
                            v.setTextviewAuthorIntroMybookdetail(book.getAuthor_intro());
                            v.setupToolbar(book.getImages().getLarge(), book.getTitle());
                            StringBuilder sb = new StringBuilder();
                            for(int i = 0; i < book.getAuthor().size(); i++){
                                sb.append(book.getAuthor().get(i)).append(". ");
                            }
                            v.setTextviewAuthornameMybookdetail(sb.toString());
                            v.setTextviewBooknameMybookdetail(book.getTitle());
                            v.setTextviewPagesMybookdetail(book.getPages());
                            v.setTextviewPriceMybookdetail(book.getPrice());
                            v.setTextviewPublishdateMybookdetail(book.getPubdate());
                            v.setTextviewPublishnameMybookdetail(book.getPublisher());
                        }
                    });
        }
    }

    public void initInject(){
        MyApplication app = MyApplication.getApplicationContext2();
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(app.getAppComponent())
                .build()
                .inject(this);

    }

    @Override
    public void inDestory() {

    }
}
