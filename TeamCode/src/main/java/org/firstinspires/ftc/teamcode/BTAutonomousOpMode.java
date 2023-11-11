package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.teamcode.libs.subsystems.BTRobotDTOnly;
// import org.firstinspires.ftc.teamcode.libs.subsystems.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.subsystems.BTRobotAutoPilot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;

@Autonomous()
public abstract class BTAutonomousOpMode extends OpMode {
    
    // members
    private BTRobotAutoPilot robot;    
    private ElapsedTime runtime;

    // you have to define this
    protected abstract BTRobotAutoPilot makeRobot() throws BTRobotInitializationException;

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
            String routeInfo = this.robot.getRoute();
            telemetry.addData("Status", routeInfo);

            // notify
            telemetry.addData("Status", "Initialized");

        } catch (BTRobotInitializationException e) {
            telemetry.addData("Status", "Initialization failed: " + e.getStackTraceAsString());
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
            String s = robot.getRoute();
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


