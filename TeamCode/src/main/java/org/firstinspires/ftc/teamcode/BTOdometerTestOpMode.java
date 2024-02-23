package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;
import org.firstinspires.ftc.teamcode.libs.subsystems.location.BTOdometer;

import java.util.ArrayList;
import java.util.List;


@TeleOp(name="Odometer Class Test", group="Tests")
public class BTOdometerTestOpMode extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private long testDurationSeconds = 60;
    private ArrayList<BTOdometer> odos = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    

    private void resfreshDisplay() {
        // notify
        telemetry.addData("BTStat", telemetryLog.toString());
        telemetry.addData("Reading", currentReading);
        telemetry.update();
    }

    private void printUpdate(String message) {

        // add it
        telemetryLog.append(message).append("\n");

        // refresh
        this.resfreshDisplay();
    }

    private void updateReading(String reading) {

        // do it
        currentReading = reading;

        // refresh
        this.resfreshDisplay();
    }

    private void commitReading() {
        this.printUpdate(this.currentReading);
        this.updateReading("(none)");
    }


    private void testOdometers() {

        // Initialize and run motors one by one
        try {

            // notify
            this.printUpdate("Reading from Odometers " + String.join(", ", this.names) + " for " + this.testDurationSeconds + "sec.");
            this.printUpdate(" *** NOTE: odometers with names containing 'L' read backwards!! *** ");
            this.printUpdate("Readings in inches and inches/second");

            // Record start time
            long startTime = System.currentTimeMillis();
            long maxTime = this.testDurationSeconds * 1000;

            // Loop until the current time - start time is less than maxTime
            while (opModeIsActive() && ( System.currentTimeMillis() - startTime < maxTime )) {

                // to store what we need
                ArrayList<String> updates = new ArrayList<>();

                // get readings
                for (int i = 0; i < this.odos.size() ; i++) {

                    // get the dist and name
                    String name = this.names.get(i);
                    BTOdometer odo = this.odos.get(i);

                    // measurements
                    double dist = odo.getDistance();
                    double incr = odo.getIncrementalDistance();
                    double speed = odo.getSpeed();

                    // print
                    String msg = String.format("%s %3.2f %7.5f %7.5f", name, dist, incr, speed);
                    updates.add(msg);
                }

                // telemetry
                String header = "Odo\tDist\tIncr\tSpeed";
                String update = String.format("%s%n%s", header, String.join("\n", updates ));
                this.updateReading( update );

                // give OS time to do something else                    
                Thread.sleep(20); // give stuff time to load before we say we are done
            }
            
        } catch (Exception e) {
            this.printUpdate("Unknown error while testing odometers: " + e);
        } finally {
            // done
            this.printUpdate("Odometer test complete");
        }
    }

    public void initializeHardware() {
        // init hwMap factory -- has to be done b4 init'ing robot
        BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
        BTHardwareFactory.getInstance().initProvider( prov );
        
        // odometer
        int i = 0;
        for (String odo : new String[]{"dtFL", "dtFR", "dtBL", "dtBR" }) {
            try {
                this.odos.add( new BTOdometer(odo) );
                this.names.add( odo );
            } catch (BTHardwareNotFoundException e) {
                throw new RuntimeException("Could not initialize odometer" + e);
            }
            i++;
        }
    }


    @Override
    public void runOpMode() {

        try {

            // init the log
            telemetryLog = new StringBuilder();
            currentReading = new String();

            // hw map + odo
            this.initializeHardware();

            // done
            this.printUpdate("Initialization complete");

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run tests
            this.testOdometers();

        } catch (Exception e) {
            this.printUpdate("Unknown error in main loop: " + e);
        } finally {
            this.printUpdate("Suggested troubleshooting actions: " +
                    "TBD");
        }
    }

}
