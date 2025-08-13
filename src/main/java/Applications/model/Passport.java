package Applications.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "passport")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "serial")
    @NotEmpty(message = "Serial can't be empty")
    @Size(min = 4, max = 4, message = "Serial must be 4 digits")
    private String serial;

    @Column(name = "number")
    @NotEmpty(message = "Number can't be empty")
    @Size(min = 6, max = 6, message = "Number must be 6 digits")
    private String number;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "controlDigit")
    private int controlDigit;


    public static boolean isValid(String serial, String number) {
        if (serial == null || number == null || serial.length() != 4 || number.length() != 6) {
            return false;
        }

        String coefficients = "731731731";
        String first9Digits = serial + number.substring(0, 5);

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(first9Digits.charAt(i));
                int coefficient = Character.getNumericValue(coefficients.charAt(i));
                sum += digit * coefficient;
            }

            int calculatedControl = sum % 10;
            int actualControl = Character.getNumericValue(number.charAt(5));
            return calculatedControl == actualControl;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Passport() {
    }

    public Passport(String serial, String number, User owner) {
        if (!isValid(serial, number)) {
            throw new IllegalArgumentException("Invalid passport data");
        }
        this.serial = serial;
        this.number = number;
        this.owner = owner;
        this.controlDigit = calculateControlDigit(serial, number);
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        if (serial == null || serial.length() != 4) {
            throw new IllegalArgumentException("Serial must be 4 digits");
        }
        this.serial = serial;
        this.controlDigit = calculateControlDigit(this.serial, this.number);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (number == null || number.length() != 6) {
            throw new IllegalArgumentException("Number must be 6 digits");
        }
        this.number = number;
        this.controlDigit = calculateControlDigit(this.serial, this.number);
    }

    public int getControlDigit() {
        return controlDigit;
    }

    @PrePersist
    @PreUpdate
    private void updateControlDigit() {
        this.controlDigit = calculateControlDigit(this.serial, this.number);
    }

    public void setControlDigit(int controlDigit){
        this.controlDigit = controlDigit;
    }

    private int calculateControlDigit(String serial, String number) {
        if (serial == null || number == null || serial.length() != 4 || number.length() != 6) {
            throw new IllegalStateException("Cannot calculate control digit - invalid serial or number");
        }

        String coefficients = "731731731";
        String fullNumber = serial + number.substring(0, 5);
        int result = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(fullNumber.charAt(i));
            int coefficient = Character.getNumericValue(coefficients.charAt(i));
            result += digit * coefficient;
        }

        return result % 10;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "id=" + id +
                ", serial='" + serial + '\'' +
                ", number='" + number + '\'' +
                ", controlDigit=" + controlDigit +
                '}';
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}