package de.trs.javafx.model;

public class Mitglied {

    private long id;
    private String nName;
    private String vName;
    private String street;
    private String zipCode;
    private String town;
    private String telephone;
    private String email;
    private String seat;

    public Mitglied() {
    }

    public Mitglied(long id, String nName, String vName, String street, String zipCode, String town, String telephone,
            String email, String seat) {
        this.id = id;
        this.nName = nName;
        this.vName = vName;
        this.street = street;
        this.zipCode = zipCode;
        this.town = town;
        this.telephone = telephone;
        this.email = email;
        this.seat = seat;
    }

    public String getNName() {
        return nName;
    }

    public void setNName(String nName) {
        this.nName = nName;
    }

    public String getVName() {
        return vName;
    }

    public void setVName(String vName) {
        this.vName = vName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Customer [email=" + email + ", id=" + id + ", nName=" + nName + ", seat=" + seat + ", street=" + street
                + ", telephone=" + telephone + ", town=" + town + ", vName=" + vName + ", zipCode=" + zipCode + "]";
    }

}
