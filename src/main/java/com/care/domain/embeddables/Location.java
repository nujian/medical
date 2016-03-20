package com.care.domain.embeddables;

import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by nujian on 2015/5/28.
 */
@Embeddable
public class Location {
    @Index(name = "longitude")
    @Column(nullable = true)
    private Double longitude;

    @Index(name = "latitude")
    @Column(nullable = true)
    private Double latitude;


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static Location fromCoordinate(double latitude, double longitude) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}