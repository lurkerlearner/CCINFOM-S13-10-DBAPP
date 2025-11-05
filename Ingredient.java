public class Ingredient {
    private int ingredient_id;
    private int batch_no;
    private String ingredient_name;
    private String category;
    private String storage_type;
    private String measurement_unit; 
    // private String[] category = {"Protein", "Dairy", "Grains", "Fat","Condiments"};
    // private String[] storage_type = {"Dry", "Refrigerated", "Frozen"};
    // private String[] measurement_unit = {"grams", "litres"};
    private double stock_quantity;
    private String expiry_date;
    private String restock_status; 
    // private String[] restock_status = {"Available", "Low Stock", "Out of Stock"};

    public Ingredient(int ingredient_id, int batch_no, String ingredient_name, 
                        String category, String storage_type, String measurement_unit,
                        double stock_quantity, String expiry_date, String restock_status) 
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

    public String getCategory() {
        return category;
    }

    public String getStorage_type() {
        return storage_type;
    }

    public String getMeasurement_unit() {
        return measurement_unit;
    }

    public double getStock_quantity() {
        return stock_quantity;
    }

    public String getExpiry_date() {
        return expiry_date;
    }
    public String getRestock_status() {
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStorage_type(String storage_type) {
        this.storage_type = storage_type;
    }

    public void setMeasurement_unit(String measurement_unit) {
        this.measurement_unit = measurement_unit;
    }

    public void setStock_quantity(double stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }
    public void setRestock_status(String restock_status) {
        this.restock_status = restock_status;
    }
    
}   
