package com.teabow.csv.model;

import com.teabow.csv.annotation.CSVAnnotation;

public class DataKPI {

    private String DateId;
    private String deviceAlias;
    private String regionAlias;
    private String service_availability;
    private String bandwidth_utilization_max;



    @Override
    public String toString() {
        return "DataKPI{" +
                "date_id='" + DateId + '\'' +
                ", device_alias='" + deviceAlias + '\'' +
                ", region_alias='" + regionAlias + '\'' +
                ", service_availability='" + service_availability + '\'' +
                ", bandwidth_utilization_max='" + bandwidth_utilization_max + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataKPI user = (DataKPI) o;

        if (!DateId.equals(user.DateId)) return false;
        if (!deviceAlias.equals(user.deviceAlias)) return false;
        if (!regionAlias.equals(user.regionAlias)) return false;
        if (!service_availability.equals(user.service_availability)) return false;
        if (!bandwidth_utilization_max.equals(user.bandwidth_utilization_max)) return false;

        return true;
    }



    @CSVAnnotation.CSVSetter(info = "Date_id")
    public void setDateId(String dateId) {
        DateId = dateId;
    }

    @CSVAnnotation.CSVSetter(info = "device_alias")
    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    @CSVAnnotation.CSVSetter(info = "region_alias")
    public void setRegionAlias(String regionAlias) {
        this.regionAlias = regionAlias;
    }
    @CSVAnnotation.CSVSetter(info = "service_availability")
    public void setService_availability(String service_availability) {
        this.service_availability = service_availability;
    }

    @CSVAnnotation.CSVSetter(info = "bandwidth_utilization_max")
    public void setBandwidth_utilization_max(String bandwidth_utilization_max) {
        this.bandwidth_utilization_max = bandwidth_utilization_max;
    }

    public String getRegionAlias() {
        return regionAlias;
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public String getDateId() {
        return DateId;
    }
    public String getBandwidth_utilization_max() {
        return bandwidth_utilization_max;
    }

    public String getService_availability() {
        return service_availability;
    }
}
