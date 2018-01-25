/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab8;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TaxpayerFX_driver extends Application {

    // Prompts for user input
    private Label label1, label2, label3, label4, label6, label7, label8;

    // Mechanisms for user input
    private TextField textfield1, textfield2, textfield3, textfield4, textfield5;

    // The ComboBox for type selection
    private ComboBox<String> typeJCB;
    // The ComboBox for occupation selection
    private ComboBox<String> jobJCB;

    // Mechanism for Output display area
    private TextArea textDisplay;

    private boolean cannotAddFlag = false; // Indicates whether taxpayer can be added
    private final int MAX_TAXPAYERS = 10;  // The maximum number of taxpayers allowed
    private double grossPay = 0.0;    // Grosspay initialized
    private int countTaxpayers = 0;   // The number of taxpayers added to array

    private Taxpayer tp;
    private Taxpayer taxpayerArray[] = new Taxpayer[MAX_TAXPAYERS];
    private int index = 0;
    private char tpt;
    
    private String typeNames[] = {"Weekly", "Bi-weekly", "Monthly"};
    private String selectedType = "Weekly";

    private String jobNames[] = {"Accountant", "Contractor", "Doctor", "Electrician",
        "Food Service", "Lawyer", "Mechanic", "Plumber", "Programmer",
        "Salesman", "Teacher"};
    private String selectedJob = "Accountant";
    
    @Override
    public void start(Stage primaryStage) {

        //create border pane to hold the gridpane and the textfield
        BorderPane borderpane = new BorderPane();
        
        //create grid pane for holding the labels and corresponding texfields
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        // Create prompt and input mechanism to get taxpayer number from user
        label1 = new Label("Number of Taxpayers:");
        pane.add(label1, 0, 0);
        textfield1 = new TextField();
        textfield1.setEditable(false);
        textfield1.setText(Integer.toString(countTaxpayers));
        pane.add(textfield1, 1, 0);  // put input textfield on gridpane

        label2 = new Label("Max # of Taxpayers:");
        pane.add(label2, 0, 1);
        textfield2 = new TextField();
        textfield2.setText(Integer.toString(MAX_TAXPAYERS));
        textfield2.setEditable(false);
        pane.add(textfield2, 1, 1);  // put input textfield on gridpane

        // Create prompt and input mechanism to get taxpayer type from user
        label6 = new Label("Taxpayer Type:");
        pane.add(label6, 0, 2);

        typeJCB = new ComboBox<>();
        typeJCB.getItems().addAll("Weekly", "Bi-weekly", "Monthly");
        typeJCB.setValue("Weekly");
        pane.add(typeJCB, 1, 2);  // put input textfield on gridpane

        // Create prompt and input mechanism to get taxpayer name from user
        label3 = new Label("Name:");
        pane.add(label3, 0, 4);
        textfield3 = new TextField();
        pane.add(textfield3, 1, 4);  // put input textfield on gridpane

        // Create prompt and input mechanism to get taxpayer number from user
        label4 = new Label("Taxpayer SSN Number:");
        pane.add(label4, 0, 5);
        textfield4 = new TextField(Integer.toString(countTaxpayers));
        pane.add(textfield4, 1, 5);   // put input textfield on gridpane

        // Create prompt and input mechanism to get taxpayer occupation
        // add your code here
        label7 = new Label("Taxpayer Occupation:");
        pane.add(label7, 0, 6);

        jobJCB = new ComboBox<>();
        jobJCB.getItems().addAll("Accountant", "Contractor", "Doctor", "Electrician",
        "Food Service", "Lawyer", "Mechanic", "Plumber", "Programmer",
        "Salesman", "Teacher");
        jobJCB.setValue(selectedJob);
        pane.add(jobJCB, 1, 6);  // put input textfield on gridpane
        
        // Create prompt and input mechanism to get taxpayer gross pay
        //add your code here
        label8 = new Label("Gross Pay:");
        pane.add(label8, 0, 7);
        
         textfield5 = new TextField();
        pane.add(textfield5, 1, 7);  // put input textfield on gridpane

        // Set grid layout at the top
        borderpane.setTop(pane);

        // Set up TextArea to display information on all the taxpayers
        textDisplay = new TextArea(getDataStringAllTaxpayers());
        textDisplay.setPrefRowCount(25);    // Width of the TextArea
        textDisplay.setPrefColumnCount(18); // Height of the TextArea
        textDisplay.setWrapText(true);

        // scrollpane creation here
        // add your code
        VBox box = new VBox();
        box.getChildren().add(textDisplay);
        borderpane.setCenter(box);  // add the VBox to the BorderPane

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderpane);
        primaryStage.setTitle(" TAXPAYER LIST");
        primaryStage.setScene(scene);
        primaryStage.show(); //Display the stage

        // Listener will respond to a user hitting Enter in any JTextField
        textfield3.setOnAction(new MyActionHandler());
        textfield4.setOnAction(new MyActionHandler());
        textfield5.setOnAction(new MyActionHandler());
        
        // Listener will respond to a user selecting from JComboBox
        typeJCB.setOnAction(new MyItemHandler());
        jobJCB.setOnAction(new MyItemHandler());
        // Display data on all Taxpayers in the JTextArea
        displayTaxpayerData();

    } //end start

    private void displayTaxpayerData() {

        setDisplayFields();
        textDisplay.setText(getDataStringAllTaxpayers());
    }

    // Create and insert the new Taxpayer into array
    private void addTaxpayerToArray(String nam, String ssn, String opt, double gp,
            String typ) {

        if (countTaxpayers < MAX_TAXPAYERS) {

            tpt = typeJCB.getValue().charAt(0);
            Taxpayer tp = null;
            switch (tpt) {
                case 'W':
                case 'w':
                    tp = new WeeklyTaxpayer(nam, Integer.parseInt(ssn), opt, gp, typ );
                    break;
                case 'B':
                case 'b':
                    tp = new BiweeklyTaxpayer(nam, Integer.parseInt(ssn), opt, gp, typ );
                    break;
                case 'M':
                case 'm':
                    tp = new MonthlyTaxpayer(nam, Integer.parseInt(ssn), opt, gp, typ );
                    break;
            }
            //Taxpayer tp = new WeeklyTaxpayer(nam, Integer.parseInt(ssn), opt, gp, typ );
            
            //Taxpayer tp = new BiweeklyTaxpayer(nam, Integer.parseInt(ssn), opt, gp, typ );
            //Taxpayer tp = new MonthlyTaxpayer(nam, Integer.parseInt(ssn), opt, gp, typ );
            taxpayerArray[countTaxpayers] = tp;
            countTaxpayers++;

        }
    }

    //Return the taxpayer instance (object) correponding to the tpNumb parameter
    private Taxpayer getTaxpayerFromArray(int tpNumb) {
        tp = null;
        if (tpNumb < countTaxpayers) {
            tp = taxpayerArray[tpNumb];
        }
        return tp;
    }

    // Set the contents of the TextFields
    private void setDisplayFields() {

        textfield1.setText(Integer.toString(countTaxpayers));
        textfield2.setText(Integer.toString(MAX_TAXPAYERS));
        textfield3.setText("");
        textfield4.setText("");
        textfield5.setText("");
        //textfield8.setText(Double.toString(grossPay));

    }

    // Get the string comprised of all the data on all taxpayers
    private String getDataStringAllTaxpayers() {

        String displayStr = "";

        // Add the information on all taxpayers to the display string
        
        for (int i = 0; i < countTaxpayers; i++) {
            displayStr += (i + 1) + ". " + taxpayerArray[i].toString() + "\n ";
        }

        if (cannotAddFlag) {
            displayStr += "\nCannot add taxpayers - the array is full\n";
        }

        return displayStr;
    }

    class MyActionHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            if (countTaxpayers < MAX_TAXPAYERS) {

                String name = textfield3.getText();
                String ssnNumber = textfield4.getText();
                String gP = textfield5.getText();
                addTaxpayerToArray(name, ssnNumber, selectedJob, Double.parseDouble(gP), selectedType);
            } else {
                cannotAddFlag = true;
            }

            displayTaxpayerData();

        }//end handle

    }//end MyActionHandler class

    class MyItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            // If one of the items in the JComboBox was selected,
            // set the selectedDept variable to the user's selection
            if (e.getSource() == typeJCB) {

                String i = typeJCB.getValue();
                index = typeJCB.getItems().indexOf(i);
                selectedType = typeNames[index];

            } //end if

            // If one of the items in the JComboBox was selected,
            // set the selectedDept variable to the user's selection
            if (e.getSource() == jobJCB) {

                String i = jobJCB.getValue();
                index = jobJCB.getItems().indexOf(i);
                selectedJob = jobNames[index];

            }
        } //end of handle

    }//end MyItemHandler class

    public static void main(String[] args) {

        launch(args);
    }

}//end TaxPayerAryApplicFX class

