package me.xsenny_.basecreator.bases;


import com.google.gson.Gson;
import me.xsenny_.basecreator.BaseCreator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class BaseMethods {

    public static void saveBase(BaseCollection baseCollection){
        Gson gson = new Gson();
        String json = gson.toJson(baseCollection);
        try (FileWriter writer = new FileWriter(BaseCreator.plugin.getDataFolder().getAbsolutePath() + "/bases.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBases(){
        try(Reader reader = new FileReader(BaseCreator.plugin.getDataFolder().getAbsolutePath() + "/bases.json")){
            Gson gson = new Gson();
            BaseCreator.baseCollection = gson.fromJson(reader, BaseCollection.class);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
