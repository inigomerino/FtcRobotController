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
import org.firstinspires.ftc.teamcode.libs.subsystems.planning.BTRoute;


@Autonomous()
public class BTAutonomousOpModeBlue2 extends BTAutonomousOpMode {
    
    // you have to define this
    @Override
    protected BT2023SeasonRobotV2 makeRobot() throws BTRobotInitializationException, BTRobotNotInitializedException, BTHardwareNotFoundException {

        // notable spots worth visiting
        BTLocation blue2Home            = new BTLocation(106, 8);
        BTLocation blue2PixelCenterPos  = new BTLocation(106, 39);
        BTLocation blue2PixelBackPos    = new BTLocation(106, 35);
        BTLocation blue2EscapePos       = new BTLocation(130, 35);
        BTLocation blue2Foward          = new BTLocation(130, 72);
        BTLocation blue2CornerPos       = new BTLocation(8, 57);

        // construct the route we want to follow
        BTRoute r = new BTRoute();
        r.addStop(blue2PixelCenterPos);     // drive to push pixel
        r.addStop(blue2PixelBackPos);       // back away from pixel
        r.addStop(blue2EscapePos);          // clear pixel
        r.addStop(blue2Foward);             // move to x coord center of map
        r.addStop(blue2CornerPos);          // drive to end position
        
        return new BT2023SeasonRobotV2(blue2Home, r);
    }

}
  


