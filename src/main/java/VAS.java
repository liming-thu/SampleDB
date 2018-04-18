import java.util.ArrayList;
import java.util.List;

public class VAS {
    private List<PointRsp> sample=new ArrayList<>();
    public List<PointRsp> getSample(Float[][] pointList,int sampleSize) {
        for (Float[] p : pointList) {
            if(p==null)
                continue;
            PointRsp point = new PointRsp(p[0], p[1], 0);
            if (sample.size() < sampleSize) {
                Expand(point);
            } else {
                Expand(point);
                Shrink();
            }
        }
        return sample;
    }
    public void Expand(PointRsp pr) {
        double rsp = 0;
        for (int i = 0; i < sample.size(); i++) {
            double l = kappa(pr, sample.get(i));
            sample.get(i).rsp += l;
            rsp += l;
        }
        pr.rsp = rsp;
        sample.add(pr);
    }

    public PointRsp Shrink() {
        double maxRsp = 0;
        int index = 0;
        for (int i = 0; i < sample.size(); i++) {
            if (sample.get(i).rsp > maxRsp) {
                maxRsp = sample.get(i).rsp;
                index = i;
            }
        }//
        for (int i = 0; i < sample.size(); i++) {
            if (i != index) {
                sample.get(i).rsp -= kappa(sample.get(index), sample.get(i));
            }
        }
        PointRsp retPR = sample.get(index);
        sample.remove(index);
        return retPR;
    }

    public double kappa(PointRsp x, PointRsp y) {
        double distSquare = (x.lat - y.lat) * (x.lat - y.lat) + (x.lon - y.lon) * (x.lon - y.lon);
        return Math.exp(-distSquare);
    }
}
