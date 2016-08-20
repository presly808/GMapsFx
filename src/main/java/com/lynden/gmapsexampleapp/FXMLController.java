package com.lynden.gmapsexampleapp;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;

import java.net.URL;
import java.util.*;

import com.lynden.model.Coordinates;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class FXMLController extends Application implements Initializable, MapComponentInitializedListener {


    private static final int LAT_POSITION = 0;
    private static final int LONG_POSITION = 1;
    private static final int TITLE_POSITION = 2;
    private static double sumLat = 0;
    private static double sumLong = 0;
    private int countMarkers = 0;

    private static String[] parameters;
    /*private static String[] parameters = {"lat1:1", "long1:1","title1:Test1","lat2:2","long3:3",
            "long2:2","lat3:3","title2:Test2","title3:Test3"};*/
  /*  private static String[] parameters = {"lat1:39.6197 long1:-122.3231 title1:Test1",
                                          "lat1:42.6197 long1:-122.3231 title1:Test2",
                                          "lat1:41.6197 long1:-124.3231 title1:Test3",
                                          "lat1:41.6197 long1:-121.3231 title1:Test4"};*/

   // private List<Coordinates> coordinates = new ArrayList<>();

    private Map<Integer,Coordinates> coordinates = new HashMap<>();

    @FXML
    private Button button;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    // when load called
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
    }

    @Override
    public void mapInitialized() {

       /* // get parameters
        parse and get locations*/

        if (parameters == null) {
            System.out.println("invalid input parameters");
            return;
        }

        // Parsing String.
        parseString();

        //Set the initial properties of the map.
        setMapOptions();

        //Put marker's on the map.
        putMapMarkers();

    }

    private void setMapOptions() {

        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(sumLat/countMarkers, sumLong/countMarkers))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(7);
        map = mapView.createMap(mapOptions);
    }

    private void parseString() {

        for (String temp  : parameters) {

            // Parsing lat,long,title from incoming data.
            double latitude = 0.0;
            double longitude = 0.0;
            String title = "null";
            int place = 0;

            if (temp.contains("lat")){

                place = Integer.parseInt(temp.substring(3,4));
                latitude = Double.parseDouble(temp.split(":")[1]);
                if(coordinates.containsKey(place)){
                    coordinates.get(place).setLatitude(latitude);
                } else {
                    coordinates.put(place,new Coordinates(latitude,0,null));
                }
            }

            if (temp.contains("long")){
                place = Integer.parseInt(temp.substring(4,5));
                longitude = Double.parseDouble(temp.split(":")[1]);
                if(coordinates.containsKey(place)){
                    coordinates.get(place).setLongitude(longitude);
                } else {
                    coordinates.put(place,new Coordinates(0,longitude,null));
                }
            }

            if (temp.contains("title")){

                place = Integer.parseInt(temp.substring(5,6));
                title = temp.split(":")[1];
                if(coordinates.containsKey(place)){
                    coordinates.get(place).setTitle(title);
                } else {
                    coordinates.put(place,new Coordinates(0,0,title));
                }
            }


            // Adding into List.


            //  Coordinates point = new Coordinates(new LatLong(latitude,longitude),title);
            // coordinates.add();


            // Calculation  sum and count.

            sumLat = sumLat + latitude;
            sumLong = sumLong + longitude;
            countMarkers++;

        }
    }

    private void putMapMarkers() {

        for (Map.Entry<Integer,Coordinates> coordinate : coordinates.entrySet()) {

            // Checking is Value of map not empty.

            if (coordinate.getValue().getLatitude() != 0 && coordinate.getValue().getLongitude() != 0) {

                // Definition position of marker on the map.

                LatLong mapPoint = new LatLong(coordinate.getValue().getLatitude(),
                                               coordinate.getValue().getLongitude());
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(mapPoint);

                // Creation new marker and placing it on the map.

                Marker marker = new Marker(markerOption);
                map.addMarker(marker);

                // Creation InfoWindow and placing it on the map.

                InfoWindowOptions tempInfoWindowOptions = new InfoWindowOptions();
                tempInfoWindowOptions.content(coordinate.getValue().getTitle());
                InfoWindow tempInfoWindow = new InfoWindow(tempInfoWindowOptions);
                tempInfoWindow.open(map, marker);
            }
        }


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Map");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL resource = FXMLController.class.getResource("/FXMLController.fxml");

        fxmlLoader.setLocation(resource);

        AnchorPane anchorPane = fxmlLoader.load();
        FXMLController controller = fxmlLoader.getController();

        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        // example run
        // java -jar some.jar lat1:23.2323 log1:11.12111 title1:SomeTitle lat2...

        parameters = args;

        launch(args);
    }


}

