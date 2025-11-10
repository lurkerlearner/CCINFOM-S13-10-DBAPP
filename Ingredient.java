import java.sql.Date;

public class Ingredient {
    private int ingredient_id;
    private int batch_no;
    private String ingredient_name;
    private Category category;
    private Storage_type storage_type;
    private Measurement_unit measurement_unit; 
    private double stock_quantity;
    private Date expiry_date;
    private Restock_status restock_status; 

    public Ingredient(int ingredient_id, int batch_no, String ingredient_name, 
                        Category category, Storage_type storage_type, Measurement_unit measurement_unit,
                        double stock_quantity, Date expiry_date, Restock_status restock_status) 
    {
        this.ingredient_id = ingredient_id;
        this.batch_no = batch_no;
        this.ingredient_name = ingredient_name;
        this.category = category;
        this.storage_type = storage_type;
        this.measurement_unit = measurement_unit;
        this.stock_quantity = stock_quantity;
        this.expiry_date = expiry_date;
        this.restock_status = restock_status;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public int getBatch_no() {
        return batch_no;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public Category getCategory() {
        return category;
    }

    public Storage_type getStorage_type() {
        return storage_type;
    }

    public Measurement_unit getMeasurement_unit() {
        return measurement_unit;
    }

    public double getStock_quantity() {
        return stock_quantity;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }
    public Restock_status getRestock_status() {
        return restock_status;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public void setBatch_no(int batch_no) {
        this.batch_no = batch_no;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStorage_type(Storage_type storage_type) {
        this.storage_type = storage_type;
    }

    public void setMeasurement_unit(Measurement_unit measurement_unit) {
        this.measurement_unit = measurement_unit;
    }

    public void setStock_quantity(double stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }
    public void setRestock_status(Restock_status restock_status) {
        this.restock_status = restock_status;
    }
    
}   
