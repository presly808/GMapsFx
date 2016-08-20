package com.lynden.gmapsexampleapp;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    private static String[] parameters = {"lat1:39.6197 long1:-122.3231 title1:Test1",
                                          "lat1:42.6197 long1:-122.3231 title1:Test2",
                                          "lat1:41.6197 long1:-124.3231 title1:Test3",
                                          "lat1:41.6197 long1:-121.3231 title1:Test4"};

    private List<Coordinates> coordinates = new ArrayList<>();

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

            String[] markerInfo = temp.split(" ");
            double latitude = Double.parseDouble(markerInfo[LAT_POSITION].split(":")[1]);
            double longitude = Double.parseDouble(markerInfo[LONG_POSITION].split(":")[1]);
            String title = markerInfo[TITLE_POSITION].split(":")[1];

            // Adding into List.

            Coordinates point = new Coordinates(new LatLong(latitude,longitude),title);
            coordinates.add(point);


            // Calculation  sum and count.

            sumLat = sumLat + latitude;
            sumLong = sumLong + longitude;
            countMarkers++;

        }
    }

    private void putMapMarkers() {

        for (Coordinates coordinate : coordinates) {

            // Definition position of marker on the map.

            LatLong mapPoint = coordinate.getCoordinates();
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(mapPoint);

            // Creation new marker and placing it on the map.

            Marker marker = new Marker(markerOption);
            map.addMarker(marker);

            // Creation InfoWindow and placing it on the map.

            InfoWindowOptions tempInfoWindowOptions= new InfoWindowOptions();
            tempInfoWindowOptions.content(coordinate.getTitle());
            InfoWindow tempInfoWindow = new InfoWindow(tempInfoWindowOptions);
            tempInfoWindow.open(map, marker);
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

     //   parameters = args;

        launch(args);
    }


}

