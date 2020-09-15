package top.abeille.basic.hypervisor.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "address_info")
public class AddressInfo {

    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 国家
     */
    @Field(value = "country")
    private String country;
    /**
     * 省/直辖市
     */
    @Field(value = "province")
    private String province;
    /**
     * 市
     */
    @Field(value = "city")
    private String city;
    /**
     * 县/区
     */
    @Field(value = "region")
    private String region;
    /**
     * 街道
     */
    @Field(value = "street")
    private String street;
    /**
     * 地址
     */
    @Field(value = "address")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
