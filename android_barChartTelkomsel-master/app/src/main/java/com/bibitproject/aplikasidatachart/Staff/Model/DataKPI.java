package com.bibitproject.aplikasidatachart.Staff.Model;

import java.math.BigDecimal;

public class DataKPI extends Object {

    private String DateId;
    private String deviceAlias;
    private String regionAlias;
    private String service_availability;
    private BigDecimal bandwidth_utilization_max;



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



    public void setDateId(String dateId) {
        DateId = dateId;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    public void setRegionAlias(String regionAlias) {
        this.regionAlias = regionAlias;
    }

    public void setService_availability(String service_availability) {
        this.service_availability = service_availability;
    }

    public void setBandwidth_utilization_max(BigDecimal bandwidth_utilization_max) {
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
    public BigDecimal getBandwidth_utilization_max() {
        return bandwidth_utilization_max;
    }

    public String getService_availability() {
        return service_availability;
    }
}
