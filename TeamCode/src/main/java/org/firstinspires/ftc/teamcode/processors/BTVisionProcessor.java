package org.firstinspires.ftc.teamcode.processors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;

public class BTVisionProcessor implements VisionProcessor {
    public enum Selected {
        NONE,
        LEFT,
        MIDDLE,
        RIGHT
    }

    // Members for selection and dynamic rectangles
    private Selected selection = Selected.NONE;
    private List<Rect> dynamicRects = new ArrayList<>();
    
    // Individual members for saturation values
    private double satRectLeft = 0.0;
    private double satRectMiddle = 0.0;
    private double satRectRight = 0.0;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // Dynamically calculate box dimensions based on the width and height
        this.initBoxDimensions(width, height);
    }

    private void initBoxDimensions(int width, int height) {
        int marginPercentage = 30; // x% margin on each side
        int boxWidth = width / 3;
    
        // Calculate the margin height for one side (e.g., top or bottom)
        int singleMarginHeight = (int) ((height * (marginPercentage / 100.0)) / 2);
        int boxHeight = height - (singleMarginHeight * 2); // Adjusted box height to exclude top and bottom margins
    
        // Calculate the starting Y position to center the boxes vertically
        int startY = singleMarginHeight;
    
        this.dynamicRects.clear();
        for (int i = 0; i < 3; i++) {
            // Ensure we use the constructor correctly: Rect(int x, int y, int width, int height)
            // The width needs to be adjusted to not exceed the canvas boundaries
            int rectWidth = boxWidth;
            int rectHeight = boxHeight;
            // Calculate the left (x coordinate) of the rectangle
            int startX = i * boxWidth;
    
            this.dynamicRects.add(new Rect(startX, startY, rectWidth, rectHeight));
        }
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

        if (dynamicRects.size() == 3) {
            satRectLeft     = this.getAvgSaturation(hsvMat, dynamicRects.get(0));
            satRectMiddle   = this.getAvgSaturation(hsvMat, dynamicRects.get(1));
            satRectRight    = this.getAvgSaturation(hsvMat, dynamicRects.get(2));

            double maxSaturation = Math.max(satRectLeft, Math.max(satRectMiddle, satRectRight));

            // Update the selection logic to include the 20% higher check
            selection = Selected.NONE; // Default to NONE
            if (maxSaturation == satRectLeft && this.isAtLeast20PercentHigher(satRectLeft, satRectMiddle, satRectRight)) {
                selection = Selected.LEFT;
            } else if (maxSaturation == satRectMiddle && this.isAtLeast20PercentHigher(satRectMiddle, satRectLeft, satRectRight)) {
                selection = Selected.MIDDLE;
            } else if (maxSaturation == satRectRight && this.isAtLeast20PercentHigher(satRectRight, satRectLeft, satRectMiddle)) {
                selection = Selected.RIGHT;
            }
        }
        return selection;
    }

    /**
     * Checks if the first saturation value is at least 20% higher than the other two.
     */
    private boolean isAtLeast20PercentHigher(double sat1, double sat2, double sat3) {
        return sat1 >= sat2 * 1.20 && sat1 >= sat3 * 1.20;
    }


    protected double getAvgSaturation(Mat input, Rect rect) {
        Mat submat = input.submat(rect);
        Scalar color = Core.mean(submat);
        return color.val[1];
    }

    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);

        return new android.graphics.Rect(left, top, right, bottom);
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        
        // colors to use
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);

        Paint nonSelectedPaint = new Paint(selectedPaint);
        nonSelectedPaint.setColor(Color.GREEN);

        // paint the one
        for (int i = 0; i < dynamicRects.size(); i++) {
            android.graphics.Rect drawRect = makeGraphicsRect(dynamicRects.get(i), scaleBmpPxToCanvasPx);
            Paint paintToUse = (selection.ordinal() == i) ? selectedPaint : nonSelectedPaint;
            canvas.drawRect(drawRect, paintToUse);
        }
    }

    public Selected getSelection() { 
        return this.selection;
    }

    public String getSaturationValues() {
        return String.format("%nLeft: %5.2f%nMiddle: %5.2f%nRight: %5.2f%n", this.satRectLeft, this.satRectMiddle, this.satRectRight);
    }
}
