package com.nifty.cloud.mb.androidformapp2.mbaas;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;

public class Mbaas {
    public static final String EMAIL = "email";
    public static final String PREFECTURE = "prefecture";

    /***** demo1：保存 *****/
    public static void saveData(String name, String emailAddress, Integer age, String prefecture, String title, String contents, final DoneCallback callback) {
        try {
            // 保存先クラスの作成
            NCMBObject inquiry = new NCMBObject("Inquiry");
            // データの設定と保存
            inquiry.put("name", name);
            inquiry.put("emailAddress", emailAddress);
            inquiry.put("age", age);
            inquiry.put("prefecture", prefecture);
            inquiry.put("title", title);
            inquiry.put("contents", contents);
            inquiry.saveInBackground(new DoneCallback() {
                @Override
                public void done(NCMBException e) {
                    if (e != null) {
                        //保存失敗
                        callback.done(e);
                    } else {
                        callback.done(null);
                    }
                }
            });
        } catch (NCMBException e) {
            callback.done(e);
        }
    }

    /***** demo2：全件検索 *****/
    public static void getAllData(final FindCallback<NCMBObject> callback) {
        // インスタンスの生成
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("Inquiry");
        // 保存日時降順
        query.addOrderByDescending("createDate");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    // 検索失敗
                    callback.done(null, e);
                } else {
                    // 検索成功
                    callback.done(results, null);
                }
            }
        });

    }

    /***** demo3-1：条件検索 *****/
    public static void getSearchData(String searchBy, String q, final FindCallback<NCMBObject> callback) {
        // インスタンスの生成
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("Inquiry");
        // 保存日時降順
        query.addOrderByDescending("createDate");
        // データの条件検索取得（完全一致）
        if (EMAIL.equals(searchBy)) {
            query.whereEqualTo("emailAddress", q);
        } else {
            query.whereEqualTo("prefecture", q);
        }
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    //検索失敗時の処理
                    callback.done(null, e);
                } else {
                    //検索成功時の処理
                    callback.done(results, null);
                }
            }
        });
    }

    /***** demo3-2：条件検索（範囲指定） *****/
    public static void getRangeSearchData(Integer ageGreaterThan, Integer ageLessThan, final FindCallback<NCMBObject> callback) {
        // インスタンスの生成
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("Inquiry");
        // 保存日時降順
        query.addOrderByDescending("createDate");
        // データのの条件検索取得（範囲指定）
        query.whereGreaterThanOrEqualTo("age", ageGreaterThan);
        query.whereLessThan("age", ageLessThan);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    // 検索失敗
                    callback.done(null, e);
                } else {
                    // 検索成功
                    callback.done(results, null);
                }
            }
        });
    }


}
