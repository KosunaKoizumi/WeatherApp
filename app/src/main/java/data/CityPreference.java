package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by v.karpov on 30.05.2017.
 */

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);

    }

    public String getCity(){
        return prefs.getString("city", "Moscow,Ru&appid=0254c107ac72d94ceb869f7857a97fa4");

    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
