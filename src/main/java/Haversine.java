public class Haversine implements GreatCircleDistance {
    @Override
    public double between(Place coord1, Place coord2, double radius) {
        double coord1lat = coord1.latRadians();
        double coord1lon = coord1.lonRadians();
        double coord2lat = coord2.latRadians();
        double coord2lon = coord2.lonRadians();

        double latDistance = coord2lat - coord1lat;
        double lonDistance = coord2lon - coord1lon;

        double a = Math.pow(Math.sin(latDistance / 2), 2) + 
                   Math.pow(Math.sin(lonDistance / 2), 2) *
                   Math.cos(coord1lat) * 
                   Math.cos(coord2lat);
        double c = 2 * Math.asin(Math.sqrt(a));

        double distance = radius * c;

        return distance;
    }

    @Override
    public String toString() {
        return "Haversine";
    }
}
