package com.fongmi.android.tv.api;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.net.OKHttp;
import com.fongmi.android.tv.utils.FileUtil;

import java.io.File;

public class SoLoader {

    private static final int exoVer = 1;
    private static final int ijkVer = 1;

    private static final String url = "https://ghproxy.com/https://raw.githubusercontent.com/FongMi/TV/release/other/";
    private static final String exo = "libexo-" + exoVer + ".so";
    private static final String ijk = "libijk-" + ijkVer + ".so";

    private static class Loader {
        static volatile SoLoader INSTANCE = new SoLoader();
    }

    public static SoLoader get() {
        return Loader.INSTANCE;
    }

    public void load() {
        App.execute(() -> checkSo(exo));
        App.execute(() -> checkSo(ijk));
    }

    private void checkSo(String name) {
        try {
            File file = new File(FileUtil.getLibDir(), name);
            if (!file.exists()) remove(name.split("-")[0]);
            if (!file.exists()) FileUtil.write(file, OKHttp.newCall(url + name).execute().body().bytes());
            System.load(file.getAbsolutePath());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void remove(String name) {
        File directory = FileUtil.getLibDir();
        for (File file : directory.listFiles()) {
            if (file.getName().contains(name)) {
                file.delete();
            }
        }
    }
}
