package com.company.earthquake.service;

import com.company.earthquake.model.Earthquake;
import com.company.earthquake.model.Feature;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mahmutcandurak
 */

@Service
public class EarthquakeService {

    private static final String ADDRESS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";

    public Earthquake getEarthquake(String starttime, String endtime, Double minmagnitude, String country) {
        RestTemplate restTemplate = new RestTemplate();

        Earthquake earthquake  = restTemplate.exchange(createURL(starttime, endtime, minmagnitude), HttpMethod.GET,null, Earthquake.class).getBody();

        List<Feature> earthquakeList = earthquake.getFeatures().stream()
                .filter(f -> f.getProperties().getPlace() != null && f.getProperties().getPlace().contains(country.trim()))
                .collect(Collectors.toList());

        earthquake.getFeatures().clear();
        earthquake.getFeatures().addAll(earthquakeList);

        return earthquake;
    }

    private String createURL(String starttime, String endtime, Double minmagnitude) {

        StringBuilder sb = new StringBuilder();
        sb.append(ADDRESS_URL);

        if (!starttime.isEmpty()) {
            sb.append("&starttime=");
            sb.append(starttime);
        }

        if (!endtime.isEmpty()) {
            sb.append("&endtime=");
            sb.append(endtime);
        }

        if (minmagnitude != null ) {
            sb.append("&minmagnitude=");
            sb.append(minmagnitude);
        }

        sb.append("&limit=20000");

        return sb.toString();
    }


}
