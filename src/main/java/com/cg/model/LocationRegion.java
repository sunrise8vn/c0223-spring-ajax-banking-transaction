package com.cg.model;

import com.cg.model.dto.LocationRegionCreResDTO;
import com.cg.model.dto.LocationRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "location_region")
@Accessors(chain = true)
public class LocationRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provinceId;

    private String provinceName;

    private String districtId;

    private String districtName;

    private String wardId;

    private String wardName;

    private String address;

    public LocationRegionDTO toLocationRegionDTO() {
        return new LocationRegionDTO()
                .setId(id)
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address)
                ;
    }


    public LocationRegionCreResDTO toLocationRegionCreResDTO() {
        return new LocationRegionCreResDTO()
                .setId(id)
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address)
                ;
    }

}
