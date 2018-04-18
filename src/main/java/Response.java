import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Response {
    private Float[][] results;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String requestID;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String signature;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String metrics;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String status;

    public Float[][] getResults() {
        return results;
    }

    public void setResults(Float[][] results) {
        this.results = results;
    }
}
