package social.tosch.com.social;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWorker {
    private File proFile;
    File sdPath;

    private static FileWorker instance;

    public static synchronized FileWorker getInstance() {
        if (instance == null) {
            instance = new FileWorker();
        }
        return instance;
    }

    private FileWorker(){
        createFile();
    }

    public void createFile(){
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            return;
        }

        sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + "social");
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        proFile = new File(sdPath, "profile");

    }


    /**
     * Positions for this function
     * 1 - e-mail
     * 2 - password
     */
    public void writeProfile(ArrayList<String> l){
        proFile.delete();

        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(proFile, true));
            for (String s : l) {
                bw.write(s  + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<String> getFileArray(){
        String sCurrentLine = "";
        ArrayList<String> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(proFile));
            sCurrentLine = "";
            while ((sCurrentLine = br.readLine()) != null)
            {
                list.add(sCurrentLine);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
