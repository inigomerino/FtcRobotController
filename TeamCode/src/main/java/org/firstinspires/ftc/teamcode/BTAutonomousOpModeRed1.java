package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV2;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;    
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;    
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigation;
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;


@Autonomous()
public class BTAutonomousOpModeRed1 extends BTAutonomousOpMode {
    
    // you have to define this
    @Override
    protected BT2023SeasonRobotV2 makeRobot() throws BTRobotInitializationException, BTRobotNotInitializedException, BTHardwareNotFoundException {

        // notable spots worth visiting
        BTLocation blue1Home            = new BTLocation(88.75,  8.0, 90.0);
        BTLocation blue1PixelCenterPos  = new BTLocation(88.75, 39.0, 90.0);
        BTLocation blue1CornerPos       = new BTLocation(128.5, 10.0, 90.0);

        // construct the route we want to follow
        BTRoute r = new BTRoute();
        r.addStop(blue1PixelCenterPos);     // drive to push pixel
        r.addStop(blue1Home);               // back away from pixel (back home)
        r.addStop(blue1CornerPos);          // go left all the way to wall
        
        BT2023SeasonRobotV2 robot = new BT2023SeasonRobotV2(blue1Home, r);
        BTNavigation nav = (BTNavigation)robot.getNavigation();
        nav.setDriveSpeed(0.5);
        nav.setRotationSpeed(0.5);
        return robot;
    }

}
  


