package com.canabrix.airports;

/**
 * POJO to represent a single airport and its runways.
 * Should map to Elastic Search airport document.
 *
 * @javadoc This domain corresponds to the data available from ourairports.com
 *
 */
public class Airport {

    private Integer ourAirportsId; // id on ourairports.com
    private String airportCode; // Should be unique.
    private AirportType airportType; // As denoted by ourairports.com
    private String name;
    private Double latitudeDeg;
    private Double longitudeDeg;
    private Integer elevationFt;
    private Continent continent;
    private String countryIso;
    private String regionIso;
    private String municipality;
    private Boolean scheduledService;
    private String gpsCode;
    private String iataCode;
    private String localCode;
    private String homeUrl;
    private String wikipediaUrl;
    private String keywords;

    Airport() {
    }

    public Integer getOurAirportsId() {
        return ourAirportsId;
    }

    public void setOurAirportsId(Integer ourAirportsId) {
        this.ourAirportsId = ourAirportsId;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public AirportType getAirportType() {
        return airportType;
    }

    public void setAirportType(AirportType airportType) {
        this.airportType = airportType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitudeDeg() {
        return latitudeDeg;
    }

    public void setLatitudeDeg(Double latitudeDeg) {
        this.latitudeDeg = latitudeDeg;
    }

    public Double getLongitudeDeg() {
        return longitudeDeg;
    }

    public void setLongitudeDeg(Double longitudeDeg) {
        this.longitudeDeg = longitudeDeg;
    }

    public Integer getElevationFt() {
        return elevationFt;
    }

    public void setElevationFt(Integer elevationFt) {
        this.elevationFt = elevationFt;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getRegionIso() {
        return regionIso;
    }

    public void setRegionIso(String regionIso) {
        this.regionIso = regionIso;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public Boolean getScheduledService() {
        return scheduledService;
    }

    public void setScheduledService(Boolean scheduledService) {
        this.scheduledService = scheduledService;
    }

    public String getGpsCode() {
        return gpsCode;
    }

    public void setGpsCode(String gpsCode) {
        this.gpsCode = gpsCode;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}