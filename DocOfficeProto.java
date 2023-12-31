import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class DocOfficeProto extends Application {
    private Stage primaryStage;
    private GridPane signInPane;
    private GridPane createAccountPane;
    private VBox accountCreatedPane;
    private VBox patientHomePane;
    private VBox staffHomePane;
    private GridPane viewPatientPane;
    private GridPane healthQuestionPane;
    private VBox messagesPane;
    private VBox updateContactPane;
    private Scene signInScene;
    //LinkedLists used for tracking user data and holding memory
    LinkedList<Patient> patientList = new LinkedList<>();
    LinkedList<Doctor> doctorList = new LinkedList<>();
    LinkedList<Nurse> nurseList = new LinkedList<>();

    @Override
    public void start(Stage primaryStage) {
        //instantiating initial sign-in screen for the user to first see when starting up the system
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Doctor's Office");
        createSignInPane();

        signInScene = new Scene(signInPane);
        showSignInScreen();
    }

    //used for sign-in usage by the user whether they are a a patient, doctor, or nurse
    private void createSignInPane() {
        signInPane = new GridPane();
        signInPane.setPadding(new Insets(10));
        signInPane.setHgap(10);
        signInPane.setVgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        DatePicker dobPicker = new DatePicker();

        Button signInButton = new Button("Sign In");
        signInButton.setOnAction(event -> {
            LocalDate selectedDate = dobPicker.getValue();

            //confirms that all of the textboxes are identical to the specific user before allowing them in
            for (Patient p : patientList) {
                if (p.getFirstName().equals(firstNameField.getText()) &&
                        p.getLastName().equals(lastNameField.getText()) &&
                        p.getBirthDate().isEqual(selectedDate)) {
                    showHomeScreenPatient(p);
                }
            }
            //confirms that all of the textboxes are identical to the specific user before allowing them in
            for (Nurse n : nurseList) {
                if (n.getFirstName().equals(firstNameField.getText()) &&
                        n.getLastName().equals(lastNameField.getText()) &&
                        n.getBirthDate().isEqual(selectedDate)) {
                    showHomeScreen(n);
                }
            }
            //confirms that all of the textboxes are identical to the specific user before allowing them in
            for (Doctor d : doctorList) {
                if (d.getFirstName().equals(firstNameField.getText()) &&
                        d.getLastName().equals(lastNameField.getText()) &&
                        d.getBirthDate().isEqual(selectedDate)) {
                    showHomeScreen(d);
                }
            }
        });

        Button createAccountButton = new Button("Create New Account");
        createAccountButton.setOnAction(event -> showCreateAccountScreen());

        //generic sign in screen for all users with specific data to fill in to log in
        signInPane.add(new Label("First Name:"), 0, 0);
        signInPane.add(firstNameField, 1, 0);
        signInPane.add(new Label("Last Name:"), 0, 1);
        signInPane.add(lastNameField, 1, 1);
        signInPane.add(new Label("Date of Birth:"), 0, 2);
        signInPane.add(dobPicker, 1, 2);
        signInPane.add(signInButton, 1, 3);
        signInPane.add(createAccountButton, 0, 3);
    }

    //used to allow the users to create accounts by selecing specific options and entering the necessary data
    private void createCreateAccountPane() {
        createAccountPane = new GridPane();
        createAccountPane.setPadding(new Insets(10));
        createAccountPane.setHgap(10);
        createAccountPane.setVgap(10);

        //buttons used for the user to determine their role in our system
        ToggleGroup roleToggleGroup = new ToggleGroup();
        RadioButton patientRadioButton = new RadioButton("Patient");
        RadioButton nurseRadioButton = new RadioButton("Nurse");
        RadioButton doctorRadioButton = new RadioButton("Doctor");
        patientRadioButton.setToggleGroup(roleToggleGroup);
        nurseRadioButton.setToggleGroup(roleToggleGroup);
        doctorRadioButton.setToggleGroup(roleToggleGroup);

        TextField enterFirstNameField = new TextField();
        TextField enterLastNameField = new TextField();
        DatePicker enterDobPicker = new DatePicker();
        TextField enterPhoneNumberField = new TextField();

        Button createAccountButton = new Button("Create Account");
        /* functionality for all users after selecting a specific option
        their account is created and they are notified with an account created screen */
        createAccountButton.setOnAction(event -> {
            //checks if the user checked the patient button and adds them to the patient linked list
            if (patientRadioButton.isSelected()) {
                Patient newPatient = new Patient(enterFirstNameField.getText(),
                        enterLastNameField.getText(),
                        enterDobPicker.getValue(),
                        enterPhoneNumberField.getText());
                patientList.add(newPatient);
                showAccountCreatedScreen();
            }
            //checks if the user checked the nurse button and adds them to the nurse linked list
            else if (nurseRadioButton.isSelected()) {
                Nurse newNurse = new Nurse(enterFirstNameField.getText(),
                        enterLastNameField.getText(),
                        enterDobPicker.getValue(),
                        enterPhoneNumberField.getText());
                nurseList.add(newNurse);
                showAccountCreatedScreen();
            }
            //checks if the user checked the doctor button and adds them to the doctor linked list
            else if (doctorRadioButton.isSelected()) {
                Doctor newDoctor = new Doctor(enterFirstNameField.getText(),
                        enterLastNameField.getText(),
                        enterDobPicker.getValue(),
                        enterPhoneNumberField.getText());
                doctorList.add(newDoctor);
                showAccountCreatedScreen();
            }
        });

        //specific buttons and labels added to the pane to organize the data the user will input to make an account
        createAccountPane.add(new Label("Select Title:"), 0, 0);
        createAccountPane.add(patientRadioButton, 1, 0);
        createAccountPane.add(nurseRadioButton, 2, 0);
        createAccountPane.add(doctorRadioButton, 3, 0);
        createAccountPane.add(new Label("Enter First Name:"), 0, 1);
        createAccountPane.add(enterFirstNameField, 1, 1, 3, 1);
        createAccountPane.add(new Label("Enter Last Name:"), 0, 2);
        createAccountPane.add(enterLastNameField, 1, 2, 3, 1);
        createAccountPane.add(new Label("Enter Birth Date:"), 0, 3);
        createAccountPane.add(enterDobPicker, 1, 3, 3, 1);
        createAccountPane.add(new Label("Enter Phone Number:"), 0, 4);
        createAccountPane.add(enterPhoneNumberField, 1, 4, 3, 1);
        createAccountPane.add(createAccountButton, 0, 5, 4, 1);
    }

    //used to let the user know their account was created and has them return back to the sign in screen to log in
    private void createAccountCreatedPane() {
        accountCreatedPane = new VBox();
        accountCreatedPane.setPadding(new Insets(10));
        accountCreatedPane.setSpacing(10);

        Label accountCreatedLabel = new Label("Account Created!");

        Button returnToSignInButton = new Button("Return to Sign In Page");
        returnToSignInButton.setOnAction(event -> showSignInScreen());

        accountCreatedPane.getChildren().addAll(accountCreatedLabel, returnToSignInButton);
    }

    //patient home screen to allow them to view their history, messages, and update any contact information
    private void createPatientHomePane(Patient p) {
        patientHomePane = new VBox();
        patientHomePane.setSpacing(10);
        patientHomePane.setPadding(new Insets(10));

        Label contactInfoLabel = new Label("Contact Information for " + p.getUserID() + ":");
        Label nameLabel = new Label("Name - " + p.getFirstName() + " " + p.getLastName());
        Label phoneLabel = new Label("Phone - " + p.getPhoneNumber());

        TextArea historyTextArea = new TextArea();
        historyTextArea.setVisible(false);
        historyTextArea.setEditable(false);

        if (!(p.getPatientHistory().isBlank())) {
            historyTextArea.setText(p.getPatientHistory());
            historyTextArea.setVisible(true);
        }

        Button updateContactButton = new Button("Update Contact Information");
        updateContactButton.setOnAction(event -> showUpdateContactScreen(p));

        Button messagesButton = new Button("Messages");
        messagesButton.setOnAction(event -> showMessagesScreen(p));

        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> showSignInScreen());

        patientHomePane.getChildren().addAll(
                contactInfoLabel,
                nameLabel,
                phoneLabel,
                updateContactButton,
                historyTextArea,
                messagesButton,
                signOutButton
        );
    }

    //home page used for both doctors and nurses to access given information about a patient and messages
    private void createStaffHomePane(User u) {
        staffHomePane = new VBox();
        staffHomePane.setSpacing(10);
        staffHomePane.setPadding(new Insets(10));

        Label signInId = new Label("Signed in as: " + u.getUserID());

        Button viewPatientButton = new Button("View Patient");

        viewPatientButton.setOnAction(event -> {
            if (u.getClass().equals(Nurse.class)) {
                showViewPatientScreen((Nurse) u);
            }
            else {
                showViewPatientScreen((Doctor) u);
            }
        });

        Button messagesButton = new Button("Messages");
        messagesButton.setOnAction(event -> showMessagesScreen(u));

        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> showSignInScreen());

        staffHomePane.getChildren().addAll(
                signInId,
                viewPatientButton,
                messagesButton,
                signOutButton
        );
    }

    //interface specific to the nurse that allows them to input the given information from a patient while doing their intake
    private void createViewPatientNursePane(Nurse n) {
        viewPatientPane = new GridPane();
        viewPatientPane.setPadding(new Insets(10));
        viewPatientPane.setHgap(10);
        viewPatientPane.setVgap(10);

        TextField enterPatientIDField = new TextField();
        DatePicker enterDobPicker = new DatePicker();
        TextField enterWeightField = new TextField();
        TextField enterHeightField = new TextField();
        TextField enterBodyTempField = new TextField();
        CheckBox overTwelveCheckbox = new CheckBox();
        TextField enterBloodPressure = new TextField();

        //enters the patients information into their account
        Button enterVitalsButton = new Button("Enter Vitals");
        enterVitalsButton.setOnAction(event -> {
            for (Patient p: patientList) {
                if (p.getUserID().equalsIgnoreCase(enterPatientIDField.getText().trim())) {
                    if (overTwelveCheckbox.isSelected()) {
                        p.updateHistory("\n\nDate of Birth: " + enterDobPicker.getValue() +
                                "\nWeight: " + enterWeightField.getText() + " lbs" +
                                "\nHeight: " + enterHeightField.getText() + " inches" +
                                "\nBody Temperature: " + enterBodyTempField.getText() + " Fahrenheit" +
                                "\nBlood Pressure: " + enterBloodPressure.getText() + " SYS(mmHg)/DIA(mmHg)");
                    }
                    else {
                        p.updateHistory("\n\nDate of Birth: " + enterDobPicker.getValue() +
                                "\nWeight: " + enterWeightField.getText() + " lbs" +
                                "\nHeight: " + enterHeightField.getText() + " inches" + "" +
                                "\nBody Temperature: " + enterBodyTempField.getText() + " Fahrenheit");
                    }
                    break;
                }
            }
            showHealthQuestionsScreen(n, enterPatientIDField.getText());
        });

        //information to be taken from the patient by the nurse
        viewPatientPane.add(new Label("Patient ID:"), 0, 0);
        viewPatientPane.add(enterPatientIDField, 1, 0);
        viewPatientPane.add(new Label("Date of Birth:"), 0, 1);
        viewPatientPane.add(enterDobPicker, 1, 1);
        viewPatientPane.add(new Label("Weight:"), 0, 2);
        viewPatientPane.add(enterWeightField, 1, 2);
        viewPatientPane.add(new Label("Height:"), 0, 3);
        viewPatientPane.add(enterHeightField, 1, 3);
        viewPatientPane.add(new Label("Body Temperature:"), 0, 4);
        viewPatientPane.add(enterBodyTempField, 1, 4);
        viewPatientPane.add(new Label("Over Twelve:"), 0, 5);
        viewPatientPane.add(overTwelveCheckbox, 1, 5);
        viewPatientPane.add(new Label("Blood Pressure:"), 0, 6);
        viewPatientPane.add(enterBloodPressure, 1, 6);
        viewPatientPane.add(enterVitalsButton, 0, 7, 2, 1);
    }

    //health questions screen for the nurse to use to get more information from the patient about their health
    private void createHealthQuestionsPane(Nurse n, String id) {
        healthQuestionPane = new GridPane();
        healthQuestionPane.setPadding(new Insets(10));
        healthQuestionPane.setHgap(10);
        healthQuestionPane.setVgap(10);

        //health questions with places for the nurse to enter data
        healthQuestionPane.add(new Label("Known Allergies:"), 0, 0);
        TextField enterAllergiesField = new TextField();
        healthQuestionPane.add(enterAllergiesField, 0, 1, 2, 1);

        healthQuestionPane.add(new Label("Health Concerns:"), 0, 2);
        TextField enterHealthConcernsField = new TextField();
        healthQuestionPane.add(enterHealthConcernsField, 0, 3, 2, 1);

        healthQuestionPane.add(new Label("Previous Issues:"), 0, 4);
        TextField enterIssuesField = new TextField();
        healthQuestionPane.add(enterIssuesField, 0, 5, 2, 1);

        healthQuestionPane.add(new Label("Previous Medication:"), 0, 6);
        TextField  enterPrevMedicaitonField = new TextField();
        healthQuestionPane.add(enterPrevMedicaitonField, 0, 7, 2, 1);

        healthQuestionPane.add(new Label("Immunizations:"), 0, 8);
        TextField enterImmunizationsField = new TextField();
        healthQuestionPane.add(enterImmunizationsField, 0, 9, 2, 1);

        //lets the nurse know the information was entered and added to the patient's account
        Label responsesEnteredLabel = new Label("Responses Entered!");
        responsesEnteredLabel.setVisible(false);
        healthQuestionPane.add(responsesEnteredLabel, 1, 10);

        Button enterHealthQuestionsButton = new Button("Enter Responses");
        healthQuestionPane.add(enterHealthQuestionsButton, 1, 11);

        //allows the nurse to go back to the home page after entering patient information
        Button homeButton = new Button("Home");
        healthQuestionPane.add(homeButton, 0, 11);

        homeButton.setOnAction(actionEvent -> showHomeScreen(n));

        enterHealthQuestionsButton.setOnAction(actionEvent -> {
            for (Patient p: patientList) {
                if (p.getUserID().equalsIgnoreCase(id)) {

                    p.updateHistory("\nKnown Allergies: " + enterAllergiesField.getText() +
                            "\nHealth Concerns: " + enterHealthConcernsField.getText() +
                            "\nPrevious Issues: " + enterIssuesField.getText() +
                            "\nPrevious Medication: " + enterPrevMedicaitonField.getText() +
                            "\nImmunizations: " + enterImmunizationsField.getText() + "\n");
                    break;
                }
            }
            responsesEnteredLabel.setVisible(true);
        });
    }

    //interface specific to the doctor to allow them to access patient information and add new information when necessary
    private void createViewPatientDoctorPane(Doctor d) {
        viewPatientPane = new GridPane();
        viewPatientPane.setPadding(new Insets(10));
        viewPatientPane.setHgap(10);
        viewPatientPane.setVgap(10);

        //screen for entering and viewing patient history and findings
        TextField enterPatientIDField = new TextField();
        Button loadButton = new Button("Load Patient ID");
        TextArea patientHistory = new TextArea("Patient History:");
        patientHistory.setEditable(false);
        patientHistory.setVisible(false);
        Label testFindingsLabel = new Label("Test Findings:");
        testFindingsLabel.setVisible(false);
        TextField testFindingsField = new TextField();
        testFindingsField.setVisible(false);
        Button saveFindingsButton = new Button("Save");
        saveFindingsButton.setVisible(false);
        Button homeButton = new Button("Home");

        homeButton.setOnAction(actionEvent -> showHomeScreen(d));

        saveFindingsButton.setOnAction(actionEvent -> {
            for (Patient p: patientList) {
                if (p.getUserID().equalsIgnoreCase(enterPatientIDField.getText().trim())) {
                    p.updateHistory("\nTest Findings: " + testFindingsField.getText().trim() + "\n");
                    patientHistory.setText(p.getPatientHistory());
                    testFindingsField.clear();
                    break;
                }
            }
        });

        //gives the doctor the option to prescribe medication to the patient given their health concerns
        Label prescribeLabel = new Label("Medication Name:");
        prescribeLabel.setVisible(false);
        TextField medicationNameField = new TextField();
        medicationNameField.setVisible(false);
        Label reasonLabel = new Label("Prescription Reason:");
        reasonLabel.setVisible(false);
        TextField prescriptionReasonField = new TextField();
        prescriptionReasonField.setVisible(false);
        Button savePrescriptionButton = new Button("Save Prescription");
        savePrescriptionButton.setVisible(false);

        savePrescriptionButton.setOnAction(actionEvent -> {
            for (Patient p: patientList) {
                if (p.getUserID().equalsIgnoreCase(enterPatientIDField.getText().trim())) {
                    p.updateHistory("\nMedication Prescription: " + medicationNameField.getText().trim() + "\nReason: " + prescriptionReasonField.getText().trim() + "\n");
                    patientHistory.setText(p.getPatientHistory());
                    medicationNameField.clear();
                    prescriptionReasonField.clear();
                    break;
                }
            }
        });

        loadButton.setOnAction(actionEvent -> {
            boolean validPatient = false;
            for (Patient p: patientList) {
                if (p.getUserID().equalsIgnoreCase(enterPatientIDField.getText().trim())) {
                    patientHistory.setText(p.getPatientHistory());
                    validPatient = true;
                    break;
                }
            }
            if (validPatient) {
                patientHistory.setVisible(true);
                testFindingsLabel.setVisible(true);
                testFindingsField.setVisible(true);
                saveFindingsButton.setVisible(true);
                prescribeLabel.setVisible(true);
                medicationNameField.setVisible(true);
                reasonLabel.setVisible(true);
                prescriptionReasonField.setVisible(true);
                savePrescriptionButton.setVisible(true);
            }
        });

        viewPatientPane.add(enterPatientIDField, 0, 0);
        viewPatientPane.add(loadButton, 1, 0, 2, 1);
        viewPatientPane.add(patientHistory, 0, 1, 2, 1);
        viewPatientPane.add(testFindingsLabel, 0, 2);
        viewPatientPane.add(testFindingsField, 0, 3, 2, 1);
        viewPatientPane.add(saveFindingsButton, 1, 4);
        viewPatientPane.add(homeButton, 0, 6);

        viewPatientPane.add(prescribeLabel, 2, 2);
        viewPatientPane.add(medicationNameField, 2, 3);
        viewPatientPane.add(reasonLabel, 2, 4, 1, 2);
        viewPatientPane.add(prescriptionReasonField, 2, 5);
        viewPatientPane.add(savePrescriptionButton, 2, 6);
    }

    //allows users to send and receive/view messages from other people in the clinic using a buffer reader and file IO
    public void createMessagesPane(User u) {
        messagesPane = new VBox();
        messagesPane.setSpacing(10);
        messagesPane.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane();
        VBox messagesContainer = new VBox();
        scrollPane.setContent(messagesContainer);
        scrollPane.setFitToWidth(true);

        messagesContainer.setPadding(new Insets(10));
        messagesContainer.setSpacing(1);

        scrollPane.setPadding(new Insets(0, 0, 10, 0));
        scrollPane.setPrefHeight(100);
        scrollPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        Label currentPersonLabel = new Label("Staff ID:");

        Button loadIDMessagesButton = new Button("Load Messages");

        TextField personIdTextField = new TextField();

        loadIDMessagesButton.setOnAction(actionEvent -> {
            messagesContainer.getChildren().clear();

            String filePath1 = System.getProperty("user.dir") + File.separator + u.getUserID() + ".txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath1))) {
                String line;

                boolean foundPhraseFrom = false;
                boolean foundPhraseTo = false;

                while ((line = reader.readLine()) != null) {
                    if (foundPhraseFrom) {
                        messagesContainer.getChildren().add(createMessageEntry(line, false));
                        foundPhraseFrom = false;
                        continue;
                    }
                    if (foundPhraseTo) {
                        messagesContainer.getChildren().add(createMessageEntry(line, true));
                        foundPhraseTo = false;
                        continue;
                    }

                    foundPhraseFrom = false;
                    foundPhraseTo = false;

                    if (line.contains("From: " + personIdTextField.getText()) && !(personIdTextField.getText().isEmpty())) {
                        foundPhraseFrom = true;
                    } else if (line.contains("To: " + personIdTextField.getText()) && !(personIdTextField.getText().isEmpty())) {
                        foundPhraseTo = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //gives the user the option to send a new message
        TextArea messageTextArea = new TextArea();
        messageTextArea.setMinHeight(20);
        messageTextArea.setPromptText("Type a new message");
        messageTextArea.setPrefRowCount(5);
        messageTextArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        Button sendButton = new Button("Send");
        //allows the user to send messages with a file by either creating a new file or appending to the previous messages
        sendButton.setOnAction(event -> {
            String message = messageTextArea.getText().trim();
            if (!message.isEmpty()) {
                messagesContainer.getChildren().add(createMessageEntry(message, true));

                String currentDirectory = System.getProperty("user.dir");
                String filePath = currentDirectory + File.separator + u.getUserID() + ".txt";

                //option to make a new file to write in
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                    writer.write("To: " + personIdTextField.getText());
                    writer.newLine();
                    writer.write(messageTextArea.getText());
                    writer.newLine();
                    System.out.println("Data appended to the file successfully.");
                } catch (IOException e) {
                    System.out.println("An error occurred while appending the file.");
                }

                //option to append to previous messages
                filePath = currentDirectory + File.separator + personIdTextField.getText() + ".txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

                    writer.write("From: " + u.getUserID());
                    writer.newLine();
                    writer.write(messageTextArea.getText());
                    writer.newLine();
                    System.out.println("Data appended to the file successfully.");
                } catch (IOException e) {
                    System.out.println("An error occurred while appending the file.");
                }

                messageTextArea.clear();
                Platform.runLater(() -> scrollPane.setVvalue(1.0));
            }
        });

        //creates home button to return to home screen
        Button homeButton = new Button("Home");
        homeButton.setOnAction(event -> {

            if (u.getClass().equals(Patient.class)) {
                showHomeScreenPatient((Patient) u);
            } else if (u.getClass().equals(Nurse.class)) {
                showHomeScreen(u);
            } else {
                showHomeScreen(u);
            }
        });

        //creates messages screen
        messagesPane.getChildren().addAll(
                currentPersonLabel,
                personIdTextField,
                loadIDMessagesButton,
                scrollPane,
                messageTextArea,
                sendButton,
                homeButton
        );
    }

    //HBox for creating messages screen, positions messages based on which user sent them (current user or other user)
    private HBox createMessageEntry(String message, boolean isCurrentUser) {
        HBox messageEntry = new HBox();
        messageEntry.setSpacing(10);
        messageEntry.setAlignment(isCurrentUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        Text messageText = new Text("   " + message);

        double textWidth = messageText.getLayoutBounds().getWidth();
        double textHeight = messageText.getLayoutBounds().getHeight();

        Rectangle backgroundRect = new Rectangle(textWidth + 10, textHeight + 10);
        backgroundRect.setFill(isCurrentUser ? Color.LIGHTBLUE : Color.LIGHTGREEN);
        backgroundRect.setArcWidth(10);
        backgroundRect.setArcHeight(10);

        StackPane messageContainer = new StackPane(backgroundRect, messageText);
        messageContainer.setAlignment(Pos.CENTER_LEFT);

        messageEntry.getChildren().add(messageContainer);

        return messageEntry;
    }

    //VBox for the Update Contact Info screen
    private void createUpdateContactPane(Patient p) {
        updateContactPane = new VBox();
        updateContactPane.setSpacing(10);
        updateContactPane.setPadding(new Insets(10));

        TextField firstNameTextField = new TextField();
        firstNameTextField.setPromptText("Enter new First Name");

        TextField lastNameTextField = new TextField();
        lastNameTextField.setPromptText("Enter new Last Name");

        TextField phoneTextField = new TextField();
        phoneTextField.setPromptText("Enter new Phone Number");

        Label contactInfoUpdatedLabel = new Label("Contact Information Updated!");
        contactInfoUpdatedLabel.setVisible(false);

        Button saveChangesButton = new Button("Save Changes");
        saveChangesButton.setOnAction(event -> {
            p.setFirstName(firstNameTextField.getText());
            p.setLastName(lastNameTextField.getText());
            p.setPhoneNumber(phoneTextField.getText());
            contactInfoUpdatedLabel.setVisible(true);
        });

        Button homeButton = new Button("Home");
        homeButton.setOnAction(event -> showHomeScreenPatient(p));

        updateContactPane.getChildren().addAll(
                firstNameTextField,
                lastNameTextField,
                phoneTextField,
                contactInfoUpdatedLabel,
                saveChangesButton,
                homeButton
        );
    }

    //Method for showing the sign-in screen
    private void showSignInScreen() {
        primaryStage.setScene(signInScene);
        primaryStage.show();
    }

    //Method for showing the create account screen
    private void showCreateAccountScreen() {
        createCreateAccountPane();
        Scene scene = new Scene(createAccountPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the "Account Created" screen
    private void showAccountCreatedScreen() {
        createAccountCreatedPane();
        Scene scene = new Scene(accountCreatedPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the patient home screen
    private void showHomeScreenPatient(Patient p) {
        createPatientHomePane(p);
        Scene scene = new Scene(patientHomePane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the doctor/nurse home screen
    private void showHomeScreen(User u) {
        createStaffHomePane(u);
        Scene scene = new Scene(staffHomePane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the messages screen
    private void showMessagesScreen(User u) {
        createMessagesPane(u);
        Scene scene = new Scene(messagesPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the update contact info screen
    private void showUpdateContactScreen(Patient p) {
        createUpdateContactPane(p);
        Scene scene = new Scene(updateContactPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the nurse's view patient screen
    private void showViewPatientScreen(Nurse n) {
        createViewPatientNursePane(n);
        Scene scene = new Scene(viewPatientPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the doctor's view patient screen
    private void showViewPatientScreen(Doctor d) {
        createViewPatientDoctorPane(d);
        Scene scene = new Scene(viewPatientPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for showing the nurse's health questions screen
    private void showHealthQuestionsScreen(Nurse n, String id) {
        createHealthQuestionsPane(n, id);
        Scene scene = new Scene(healthQuestionPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Obviously the main function lol
    public static void main(String[] args) {
        launch(args);
    }
}
