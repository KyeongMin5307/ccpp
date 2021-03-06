package com.gachon.ccpp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.gachon.ccpp.alarm.AlarmFragment;
import com.gachon.ccpp.chat.ChatFragment;
import com.gachon.ccpp.dialog.LoginDialog;
import com.gachon.ccpp.lecture.LectureFragment;
import com.gachon.ccpp.schedule.ScheduleFragment;
import com.gachon.ccpp.setting.SettingFragment;
import com.gachon.ccpp.network.RetrofitAPI;
import com.gachon.ccpp.network.RetrofitClient;

import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gachon.ccpp.api.UserManager;
import com.gachon.ccpp.parser.ContentCollector;
import com.gachon.ccpp.parser.ContentCollector.collectionListener;
import com.gachon.ccpp.parser.HtmlParser;
import com.gachon.ccpp.parser.ListForm;

public class MainActivity extends AppCompatActivity {
    public static RetrofitClient retrofitClient;
    public static RetrofitAPI api;

    HtmlParser parser;

    LoginDialog privateDialog;

    Bundle bundle = new Bundle();

    private UserManager userManager;

    private FragmentManager fragManager;
    private FragmentTransaction transaction;

    private LectureFragment lecture;
    private ScheduleFragment schedule;
    private AlarmFragment alarm;
    private ChatFragment chat;
    private SettingFragment setting;

    private String sourceId;

    private static class chatListener extends collectionListener {
        public Boolean lecture = null;
        public Boolean activity = null;
        public Boolean chat = null;

        @Override
        public void onCompleteLectureList(boolean success) {
            super.onCompleteLectureList(success);
            lecture = success;
        }

        @Override
        public void onCompleteActivity(boolean success) {
            super.onCompleteActivity(success);
            activity = success;
        }

        @Override
        public void onCompleteChatList(boolean success) {
            super.onCompleteChatList(success);
            chat = success;
        }
    }

    private chatListener chat_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        sourceId = intent.getStringExtra("id");

        chat_listener = new chatListener();

        bundle = new Bundle();
        bundle.putSerializable("courseList", intent.getSerializableExtra("courseList"));

        getSupportActionBar().hide();

        retrofitClient = RetrofitClient.getInstance();
        api = RetrofitClient.getRetrofitInterface();

        privateDialog = new LoginDialog(this);

        fragManager = getSupportFragmentManager();

        lecture = new LectureFragment();
        schedule = new ScheduleFragment();
        alarm = new AlarmFragment();
        chat = new ChatFragment();
        setting = new SettingFragment();

        lecture.setArguments(bundle);
        chat.setArguments(bundle);

        infoRequest();

        findViewById(R.id.footer_lecture).setOnClickListener(view ->
                deployFragment(R.string.MainFragment_Lecture_Title, lecture));
        findViewById(R.id.footer_schedule).setOnClickListener(view ->
                deployFragment(R.string.MainFragment_Schedule_Title, schedule));
        findViewById(R.id.footer_alarm).setOnClickListener(view ->
                deployFragment(R.string.MainFragment_Alarm_Title, alarm));
        findViewById(R.id.footer_chat).setOnClickListener(view ->
                deployFragment(R.string.MainFragment_Chat_Title, chat));
        findViewById(R.id.footer_setting).setOnClickListener(view ->
                deployFragment(R.string.MainFragment_Setting_Title, setting));
    }

    @Override
    protected void onResume() {
        super.onResume();
        privateDialog.hide();
    }

    public void infoRequest() {
        privateDialog.show("9844-loading-40-paperplane.json", getString(R.string.LoadingDialog_TextLoading));

        Call<ResponseBody> connect = api.getUri("");
        connect.enqueue(new Callback<ResponseBody>() {
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        parser.setHtml(Jsoup.parse(response.body().string()));
                        makeConnection(parser.getStudentInfo().get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        privateDialog.hide();
                        deployFragment(R.string.MainFragment_Lecture_Title, lecture);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                privateDialog.hide();
                deployFragment(R.string.MainFragment_Lecture_Title, lecture);
            }
        });
    }

    public void makeConnection(String id) {
        userManager = new UserManager(sourceId, id);
    }

    public void deployFragment(int title, Fragment fragment) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().show();

        transaction = fragManager.beginTransaction();
        transaction.replace(R.id.fragLayout, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ContentCollector.getObject("lecture") != null &&
                ContentCollector.getObject("chat") != null)
            ContentCollector.saveData(this);
    }
}