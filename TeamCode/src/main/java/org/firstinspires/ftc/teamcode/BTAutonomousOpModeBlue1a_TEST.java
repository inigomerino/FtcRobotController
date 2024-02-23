package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTSubsystemInitializationException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV4;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;    
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotNotInitializedException;    
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTIAutonomousController;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigation;
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;
import org.firstinspires.ftc.teamcode.libs.subsystems.navigation.BTNavigationV2;
import org.firstinspires.ftc.teamcode.processors.BTVisionProcessor;


@Autonomous()
public class BTAutonomousOpModeBlue1a_TEST extends BTAutonomousOpModeV2 {
    
    // you have to define this
    @Override
    protected BTIRobot makeRobot(BTVisionProcessor.Selected pixLoc)
                                                throws BTSubsystemInitializationException,
                                                        BTRobotNotInitializedException, 
                                                        BTRobotInitializationException, 
                                                        BTHardwareNotFoundException {

        // pix orient mod
        double pixOrient;
        if (pixLoc == BTVisionProcessor.Selected.LEFT) {
            pixOrient = 180.0;
        } else if (pixLoc == BTVisionProcessor.Selected.MIDDLE) {
            pixOrient = 90.0;
        } else if (pixLoc == BTVisionProcessor.Selected.RIGHT) {
            pixOrient = 0.0;
        } else {
            // none detected, push center
            pixOrient = 90.0;
        }

        // notable spots worth visiting
        BTLocation blue1Home            = new BTLocation(58.0,  8.0, 90.0);
        BTLocation blue1PixelCenterPos  = new BTLocation(58.0, 20.0, 90.0);
        BTLocation blue1PixelOrientPos  = new BTLocation(58.0, 20.0, pixOrient); //90.0);
        BTLocation blue1CornerPos       = new BTLocation(48.0,  8.0, 90.0);

        // construct the route we want to follow
        BTRoute r = new BTRoute();
        r.addStop(blue1PixelCenterPos);     // drive to push pixel
        r.addStop(blue1PixelOrientPos);     // turn to pixel as needed
        r.addStop(blue1PixelCenterPos);     // turn to center pos
        r.addStop(blue1Home);               // back away from pixel (back home)
        r.addStop(blue1CornerPos);          // go left all the way to wall
        r.addStop(blue1Home);               // go home (reset)
        
        // make robot
        BT2023SeasonRobotV4 robot = new BT2023SeasonRobotV4();
        BTIAutonomousController c = robot.getNavigation();
        c.setPosition(blue1Home);
        c.setRoute(r);
        
        return robot;
    }

}
  


