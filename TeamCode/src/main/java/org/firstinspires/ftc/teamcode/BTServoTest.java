package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class BTServoTest extends LinearOpMode {

    // Find a servo in the hardware map 
    Servo servo;

    public void printPosition() {
        // Show the position of the motor on telemetry
        double position = this.servo.getPosition();
        telemetry.addData("Servo Position", position);
        telemetry.update();
    }

    public void setPosition(double pos) {
        // Show the position of the motor on telemetry
        telemetry.addData("Setting Position: ", pos);
        telemetry.update();
        this.servo.setPosition(pos);
        printPosition();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        
        // Find a servo in the hardware map 
        // this.servo = hardwareMap.servo.get("droneLauncher");
//        this.servo = hardwareMap.servo.get("armDoor");
         this.servo = hardwareMap.servo.get("armWrist");

        waitForStart();

        while (opModeIsActive()) {

            // drone; start at 1 end at 0.5
            // door; rotate door 90 degrees so it can start at pos 0. It rotates "up" only, so go to 90 degrees

            this.setPosition(0.5);  // this is close to the resting position of the hand
            sleep(3*1000);
            this.setPosition(0.25); // this is the 90 degree we want when deploying
            sleep(3*1000);
//            this.setPosition(0.0);
//            sleep(3*1000);

        }
    }
}