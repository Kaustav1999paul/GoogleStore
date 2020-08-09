package Model;

public class Users {

    private String Name , Password , Phone , Address,Photo;


    public Users(String name, String password, String phone, String address,String photo) {
        Name = name;
        Password = password;
        Phone = phone;
        Address = address;
        Photo = photo;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Users(){

    }
}
