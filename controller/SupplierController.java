package controller;

import DAO.SupplierDAO;
import model.Supplier;
import java.util.ArrayList;

public class SupplierController {
    private final SupplierDAO supplierDAO;

    public SupplierController(SupplierDAO dao) {
        this.supplierDAO = dao;
    }

    public boolean addSupplier(String supplier_name, String contact_no, String alt_contact_no, int location_id) {
        Supplier supplier = new Supplier();
        supplier.setSupplier_name(supplier_name);
        supplier.setContact_no(contact_no);
        supplier.setAlt_contact_no(alt_contact_no);
        supplier.setLocation_id(location_id);

        return supplierDAO.addSupplier(supplier);
    }

    public boolean deleteSupplier(int supplier_id) {
        return supplierDAO.deleteSupplier(supplier_id);
    }

    public ArrayList<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public ArrayList<Supplier> getSuppliersByLocation(int locationId) {
        return supplierDAO.getSuppliersByLocation(locationId);
    }

    public Supplier getSupplierByID(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }
}
