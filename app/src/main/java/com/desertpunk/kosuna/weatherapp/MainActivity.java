package com.desertpunk.kosuna.weatherapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.DecimalFormat;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import Util.Utils;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    Weather weather = new Weather();

    HttpURLConnection urlConnection = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);



        CityPreference cityPreference = new CityPreference(MainActivity.this);

        renderWeatherData(cityPreference.getCity());
    }

    public void renderWeatherData (String city){

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});

    }

    private class WeatherTask extends AsyncTask<String, Void, Weather>{
        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather.iconData = weather.currentCondition.getIcon();

            weather = JSONWeatherParser.getWeather(data);

            Log.v("Data: ", weather.place.getCity());

//            new DowndoladImageAsyncTask().execute(weather.iconData);


            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            java.text.DateFormat df = java.text.DateFormat.getTimeInstance();

            String sunriseDate = df.format( new Date(weather.place.getSunrise()));
            String sunsetDate = df.format( new Date(weather.place.getSunset()));
            String updateDate = df.format( new Date(weather.place.getLastUpdate()));

            java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#.#");

            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());


            cityName.setText(weather.place.getCity() + ", " + weather.place.getCountry());
            temp.setText("" + tempFormat + "C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
            sunrise.setText("Sunrise: " + sunriseDate);
            sunset.setText("Sunset: " + sunsetDate);
            updated.setText("Last updated: " + updateDate);
            description.setText("Condition" + weather.currentCondition.getCondition() + "("
                    + weather.currentCondition.getDescription() + ")");
        }
    }

//    private class DowndoladImageAsyncTask extends AsyncTask<String, Void, Bitmap>{
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            return downloadImage(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//
//            iconView.setImageBitmap(bitmap);
//        }
//
//        private Bitmap downloadImage(String code) throws IOException {
//            final DefaultHttpClient client = new DefaultHttpClient();
//
//            final HttpGet getRequest = new HttpGet(Utils.ICON_URL + code + ".png");
//
//            try {
//                HttpResponse response = client.execute(getRequest);
//
//                final int statusCode = response.getStatusLine().getStatusCode();
//
//                if ( statusCode != HttpStatus.SC_OK){
//                    Log.e("DownloadImage", "Error:" + statusCode);
//                    return null;
//                }
//                final HttpEntity entity = response.getEntitiy();
//
//                if (entity != null){
//
//                    InputStream inputStream = null;
//
//                    inputStream = entity.getContent;
//                    //decode contents from stream
//                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    return bitmap;
//                }
//
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//            return null;






//        }
//    }
//
//    private void showInputDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Change City");
//
//        final EditText cityInput = new EditText(MainActivity.this);
//        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
//        cityInput.setHint("Moscow,RU");
//        builder.setView(cityInput);
//        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                CityPreference cityPreference = new CityPreference(MainActivity.this);
//                cityPreference.setCity(cityInput.getText().toString());
//
//                String newCity = cityPreference.getCity();
//
//                renderWeatherData(newCity);
//            }
//        });
//        builder.show();
//
//
//    }

//    @Override
//    public boolean onCreateOptionMenu(Menu menu){
//
//
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        return true;

//        int id = item.getItemId();
//
//        if (id == R.id.change_cityId){
//            showInputDialog();
//        }
//        return super.onOptionsItemSelected(item);
    }







}
