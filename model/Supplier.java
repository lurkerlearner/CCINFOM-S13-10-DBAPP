package model;

public class Supplier {
    private int supplier_id;
    private String supplier_name;
    private String contact_no;
    private String alt_contact_no;
    private int location_id;

    public Supplier(int supplier_id, String supplier_name, String contact_no, String alt_contact_no, int location_id) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.contact_no = contact_no;
        this.alt_contact_no = alt_contact_no;
        this.location_id = location_id;
    }

    public Supplier() {
        // Default constructor
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAlt_contact_no() {
        return alt_contact_no;
    }

    public void setAlt_contact_no(String alt_contact_no) {
        this.alt_contact_no = alt_contact_no;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }
    
    

}
