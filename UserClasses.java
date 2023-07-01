import java.io.File;
import java.time.LocalDate;

class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userID;
    private final LocalDate birthDate;
    File messageLog;

    public User(String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        generateID();
        loadMessages(userID);
    }

    public void generateID() {
        userID = firstName + lastName.charAt(0) + birthDate.getMonthValue() + birthDate.getDayOfMonth() + birthDate.getYear();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void loadMessages(String userID) {
        String fileName = userID + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            messageLog = file;
            System.out.println("File loaded successfully.");
        }
    }

    public File getMessageLog() {return messageLog;}
}

class Patient extends User{
    public Patient(String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        super(firstName, lastName, birthDate, phoneNumber);
    }
}

class Doctor extends User{
    public Doctor(String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        super(firstName, lastName, birthDate, phoneNumber);
    }
}

class Nurse extends User{
    public Nurse(String firstName, String lastName, LocalDate birthDate, String phoneNumber) {
        super(firstName, lastName, birthDate, phoneNumber);
    }
}
