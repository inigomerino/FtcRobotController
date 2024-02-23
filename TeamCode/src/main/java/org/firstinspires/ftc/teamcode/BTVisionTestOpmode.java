
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous()
public class BTVisionTestOpmode extends OpMode {

    private org.firstinspires.ftc.teamcode.processors.BTVisionProcessor visionProcessor;
    private VisionPortal visionPortal;

    @Override
    public void init() {
        visionProcessor = new org.firstinspires.ftc.teamcode.processors.BTVisionProcessor();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), visionProcessor);
        telemetry.addData("Status", "Initializing video. Please wait...");
        while ( visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING ) {
            telemetry.addData("Status", "State: " + visionPortal.getCameraState());
            try {
                Thread.sleep(250); // give stuff time to load before we say we are done
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        telemetry.addData("Status", "Initialization done.");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        visionPortal.stopStreaming();
    }

    @Override
    public void loop() {

        String sv = visionProcessor.getSaturationValues();

        telemetry.addData("Identified", visionProcessor.getSelection());
        telemetry.addData("Saturations ", sv );

    }
}
