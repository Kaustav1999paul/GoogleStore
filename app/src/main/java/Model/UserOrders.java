package Model;

public class UserOrders {

    public UserOrders(){}

    private String Date, Product_Name, price, quantity, state, pid, productimg ;


    public UserOrders(String date, String product_Name, String price, String quantity, String state, String pid,String productimg) {
        Date = date;
        Product_Name = product_Name;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
        this.pid = pid;
        this.productimg = productimg;
    }

    public String getProductimg() {
        return productimg;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
