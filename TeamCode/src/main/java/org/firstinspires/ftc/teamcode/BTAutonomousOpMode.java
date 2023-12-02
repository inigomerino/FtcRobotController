package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV2;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;

@Autonomous()
public abstract class BTAutonomousOpMode extends OpMode {
    
    // members
    private BT2023SeasonRobotV2 robot;    
    private ElapsedTime runtime;

    // you have to define this
    protected abstract BT2023SeasonRobotV2 makeRobot() throws BTRobotInitializationException, BTRobotNotInitializedException, BTHardwareNotFoundException;

    // methods
    @Override
    public void init() {
        try {

            // notify
            telemetry.addData("Status", "Initializing");

            // init hwMap factory -- has to be done b4 init'ing robot
            BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
            BTHardwareFactory.getInstance().initProvider( prov );

            // init members
            this.runtime = new ElapsedTime();
            this.robot = this.makeRobot();

            // notify
            telemetry.addData("Status", "Robot built");

            // get the routes
            telemetry.addData("Status", "Routes");
            String routeInfo = this.robot.getRoute().toString();
            telemetry.addData("Status", routeInfo);

            // notify
            telemetry.addData("Status", "Initialized");

        } catch (BTRobotInitializationException e) {
            telemetry.addData("Status", "Failed: Robot initialization error: " + e.getStackTraceAsString());
        } catch (BTRobotNotInitializedException e) {
            telemetry.addData("Status", "Failed: Robot could not be initializated successfully: " + e.getStackTraceAsString());
        } catch (Exception e) {
            telemetry.addData("Status", "Failed: Unidentified error: " + e);
        }
    }

    @Override
    public void start() {
        runtime.reset();
    }
    
    @Override
    public void loop() {
        try {

            // do some stuff
            this.robot.loop();
      
            // // debug
            String s = robot.getRoute().toString();
            if (!s.equals("")) {
                telemetry.addData("Robot route", s);
            } else {
                telemetry.addData("Robot route", "Complete");
            }
            
      
            // notify
            telemetry.addData("Status", "Run Time: " + runtime.toString());
      
        } catch (Exception e) {
        
            // over here, should we try to re-initialize and re-start?
            // error-recovery would be nice ...
            // for now, just report the error
        
            // notify
            telemetry.addData("Status", "Unidentifier error: " + e);
    
        }

    }
}


