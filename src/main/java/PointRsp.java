public class PointRsp {
    public double lat;
    public double lon;
    public double rsp;

    public PointRsp(double la, double lo, double rs){
        lat=la;
        lon=lo;
        rsp=rs;
    }

    @Override
    public String toString() {
        return lat+","+lon+"\r";
    }
}
