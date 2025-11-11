public class Supplier {
    private int supplier_id;
    private String supplier_name;
    private int contact_no;
    private int alt_contact_no;
    private int location_id;

    public Supplier(int supplier_id, String supplier_name, int contact_no, int alt_contact_no, int location_id) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.contact_no = contact_no;
        this.alt_contact_no = alt_contact_no;
        this.location_id = location_id;
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
    public int getContact_no() {
        return contact_no;
    }
    public void setContact_no(int contact_no) {
        this.contact_no = contact_no;
    }
    public int getAlt_contact_no() {
        return alt_contact_no;
    }
    public void setAlt_contact_no(int alt_contact_no) {
        this.alt_contact_no = alt_contact_no;
    }
    public int getLocation_id() {
        return location_id;
    }
    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

}
