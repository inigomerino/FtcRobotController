package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;

import java.util.List;


@TeleOp(name="Raw Drivetrain Test", group="Tests")
public class BTRawDriveTrainTestOpMode extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private long testDurationSeconds = 5;
    private int numTests = 5;

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

    private double[] runMotorStatTest(String name, double speed) {

        // vars
        double[] data = new double[this.numTests];

        // run numTest iterations
        for (int i = 0 ; i < numTests ; i++) {
            
            try {

                // notify
                this.updateReading("Starting test #" + i+1 + " of " + this.numTests);

                // give user a chance to read
                sleep(750);

                // get ticks
                double ticks = this.runMotor(name, speed, this.testDurationSeconds);

                // give motor a chance to stop
                sleep(250);

                // aggregate into accumulator
                data[i] = ticks;
            
            } catch (Exception e) {
                this.printUpdate("Unknown error while testing motor '" + name + "': " + e);
            }
    
        }

        // calc stats
        double mean = this.round( this.mean(data), 2 );
        double stdev = this.round( this.standardDeviation(data), 2 );
        double[] stats = new double[2];
        speed = this.round( speed * 100, 1 );
        double spm = this.round( stdev / mean, 3 );

        // update
        // this.updateReading( name + "\t @: " + speed + "%\t μ: " + mean + "\t 𝜎: " + stdev + "\t 𝜎/μ: " + spm);
        String output = String.format("%-7s  %7.1f  %7.2f  %7.2f  %7.3f", name, speed, mean, stdev, spm);
        this.updateReading(output);

        // return
        stats[0] = mean;
        stats[1] = stdev;
        return stats;
    }

    private int runMotor(String name, double speed, long seconds) {

        // get it
        DcMotor m = hardwareMap.get(DcMotor.class, name);
        // reset
        m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // set mode
        m.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // apparently this refers to the motor's built in encoder; not for extenral encoders. See https://gm0.org/en/latest/docs/software/tutorials/encoders.html
        // set dir
        if ( name.contains("L") ) {
            m.setDirection(DcMotor.Direction.REVERSE);
        }

        // run
        m.setPower(speed);

        // Record start time
        long startTime = System.currentTimeMillis();
        long maxTime = seconds * 1000;

        // Loop until the current time - start time is less than maxTime
        int ticks = 0;
        while (opModeIsActive() && ( System.currentTimeMillis() - startTime < maxTime )) {

            // get number of revolutions
            ticks = m.getCurrentPosition();   // NOTE: TODO: WATCH FOR INT OVERFLOWs. This has info BUT appears to be outdated: https://docs.ftclib.org/ftclib/features/hardware/motors
            this.updateReading("'" + name + "' ticks: " + ticks + "@" + speed + "% pwr");
        }

        // Stop the motor
        m.setPower(0);

        // done
        return ticks;
    }

    private void testMotors() {

        // notify
        this.printUpdate("Testing motors. Each motor will run for " + this.testDurationSeconds + " seconds. Confirm behavior and values printed are as expected.");

        // print table 
        String headerFormat = "%-7s  %7s  %7s  %7s  %7s";
        String headerSep = "----------";
        String heading = String.format(headerFormat, "motor", "% power", "μ", "𝜎", "𝜎/μ");
        String separator = String.format(headerFormat, headerSep, headerSep, headerSep, headerSep, headerSep);
        this.printUpdate( heading);
        this.printUpdate( separator);

        // run at various speeds
        for (double pwr : new double[]{0.20, 0.15, 0.10, 0.05}) { // 0.2, 0.1, 0.05}) {

            // run motors one by one
            for (String name : new String[]{"dtFL", "dtBL", "dtBR"}) { //, "dtFR", "dtBL", "dtBR"}) {
                double[] stats = this.runMotorStatTest(name, pwr);
                this.commitReading();
            }
        }

        // done
        this.printUpdate("Motor test complete - take pics now. Sleeping for 10 mins - hit Stop to end earlier");
        sleep(10 * 60 * 1000);
    }

    @Override
    public void runOpMode() {

        try {

            // init the log
            telemetryLog = new StringBuilder();
            currentReading = new String();

            // done
            this.printUpdate("Initialization complete");
            this.printUpdate("*** NOTES: 1) Connect all motor encoders to the right port");
            this.printUpdate("***        2) Place robot in a safe position");

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run tests
            this.testMotors();

        } catch (Exception e) {
            this.printUpdate("Unknown error in main loop: " + e);
            
        } finally {
            this.printUpdate("Suggested troubleshooting actions: " +
                    "Check robot configuration in the driver station. " +
                    "Drivetrain motor placement on control hub:  'dtFL' -> 0, 'dtFR' -> 1, 'dtBL' -> 2, 'dtBR -> 3" +
                    "Check that the failed elements are correctly connecter to the appropriate ports in the control station. " +
                    "Check that the opMode initialized the hardware map properly. " +
                    "Check that motors, wheels and other mechanisms are rotating freely and not rubbing. ");
        }
    }

    ////////////////// statistics -- move to a stats class //////////////////

    private double round(double n, double digits) {
        double resolution = Math.pow(10, digits);
        double rounded = Math.round(n * resolution) / resolution;
        return rounded; 
    }

    private double mean(double[] data) {
        double sum = 0.0;
        int length = data.length;

        // Calculate the mean
        for (double num : data) {
            sum += num;
        }
        double mean = sum / length;
        return mean;
    }

    private double standardDeviation(double[] data) {
        double standardDeviation = 0.0;
        int length = data.length;

        // Calculate the mean
        double mean = this.mean(data);

        // Calculate the standard deviation
        for (double num : data) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    
    }
    ////////////////////////////////////////////////////////////////////////
    

}
