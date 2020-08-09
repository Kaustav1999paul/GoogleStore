package Model;

public class mCart {
    private String Product_Name , price, productimg ,pid, quantity, state;

    public mCart(){

    }

    public mCart(String product_Name, String price, String pid, String quantity, String state,String productimg) {
        Product_Name = product_Name;
        this.price = price;
        this.pid = pid;
        this.quantity = quantity;
        this.state = state;
        this.productimg = productimg;
    }

    public String getProductimg() {
        return productimg;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
