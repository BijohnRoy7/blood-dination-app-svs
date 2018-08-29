package invenz.roy.blooddonationapp1.models;

public class SearchDonor {

    private String id, name, token, mblNo, mblNo2, email, divition, district, bloodGroup, available;

    public SearchDonor(String id, String name, String token, String mblNo, String mblNo2, String email, String divition, String district, String bloodGroup, String available ) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.mblNo = mblNo;
        this.mblNo2 = mblNo2;
        this.email = email;
        this.divition = divition;
        this.district = district;
        this.bloodGroup = bloodGroup;
        this.available = available;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMblNo() {
        return mblNo;
    }

    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }

    public String getMblNo2() {
        return mblNo2;
    }

    public void setMblNo2(String mblNo2) {
        this.mblNo2 = mblNo2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDivition() {
        return divition;
    }

    public void setDivition(String divition) {
        this.divition = divition;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
