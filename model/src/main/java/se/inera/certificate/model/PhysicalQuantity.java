package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public class PhysicalQuantity {

    private Double quantity;
    private String unit;

    public PhysicalQuantity() {
    }

    public PhysicalQuantity(Double quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
