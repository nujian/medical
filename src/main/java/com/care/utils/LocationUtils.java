package com.care.utils;

import com.care.Constants;
import com.care.domain.embeddables.Location;
import com.spatial4j.core.distance.DistanceUtils;

import java.math.BigDecimal;

/**
 * Created by nujian on 16/2/26.
 */
public class LocationUtils {

    public static double distance(Location l1, Location l2) {
        try {
            return DistanceUtils.radians2Dist(DistanceUtils.distLawOfCosinesRAD(
                    DistanceUtils.toRadians(l1.getLatitude()),
                    DistanceUtils.toRadians(l1.getLongitude()),
                    DistanceUtils.toRadians(l2.getLatitude()),
                    DistanceUtils.toRadians(l2.getLongitude())), DistanceUtils.EARTH_EQUATORIAL_RADIUS_KM);
        } catch (Exception e) {
            return 0;
        }
    }

    public static BigDecimal distance4km(Location l1,Location l2){
        return new BigDecimal(distance(l1,l2)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public static double latitudeOffset(double distance) {
        return distance / 111.045;
    }

    public static double longitudeOffset(double distance, double latitude) {
        return distance / (111.045 * Math.cos(DistanceUtils.toRadians(latitude)));
    }

    public static Location generateRandomLocation( double lng,double lat) {
        Location location = new Location();
        location.setLatitude(lat + (Math.random() * 4 - 2));
        location.setLongitude(lng + (Math.random() * 4 - 2));
        return location;
    }

    public static String getLocationPic(Location location,Integer width,Integer height,Integer zoom) {
        StringBuffer path = null;
        if(location != null){
            path = new StringBuffer();
            path.append(Constants.BAIDU_MAP_PATH).append("?")
                    .append("ak=")
                    .append(Constants.baidu_map_key)
                    .append("&center=").append(location.getLongitude()).append(",").append(location.getLatitude())
                    .append("&width=").append(width == null?500:width).append("&height=").append(height == null?500:height)
                    .append("&zoom=").append(zoom==null || zoom >19 || zoom <= 0 ?19:zoom);
        }
        return path == null?null:path.toString();
    }
}
