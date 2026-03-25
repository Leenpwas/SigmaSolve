package com.sigmasolve.app.ui.calculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.sigmasolve.app.calculator.ScientificCalculator;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {

    private Paint gridPaint;
    private Paint axisPaint;
    private Paint graphPaint;
    private ScientificCalculator calculator;
    private List<String> expressions = new ArrayList<>();
    
    private float minX = -10;
    private float maxX = 10;
    private float minY = -10;
    private float maxY = 10;

    private float lastTouchX;
    private float lastTouchY;
    private ScaleGestureDetector scaleGestureDetector;

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1);

        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(3);

        graphPaint = new Paint();
        graphPaint.setStrokeWidth(5);
        graphPaint.setStyle(Paint.Style.STROKE);
        graphPaint.setAntiAlias(true);

        calculator = new ScientificCalculator();
        
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                float centerX = (minX + maxX) / 2;
                float centerY = (minY + maxY) / 2;
                float halfWidth = (maxX - minX) / 2 / scaleFactor;
                float halfHeight = (maxY - minY) / 2 / scaleFactor;
                
                minX = centerX - halfWidth;
                maxX = centerX + halfWidth;
                minY = centerY - halfHeight;
                maxY = centerY + halfHeight;
                
                invalidate();
                return true;
            }
        });
    }

    public void setExpression(String expression) {
        this.expressions.clear();
        if (expression != null && !expression.isEmpty()) {
            this.expressions.add(expression);
        }
        invalidate();
    }
    
    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        
        if (scaleGestureDetector.isInProgress()) return true;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = x;
                lastTouchY = y;
                return true;
            case MotionEvent.ACTION_UP:
                performClick();
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastTouchX;
                float dy = y - lastTouchY;
                
                float worldDx = dx / getWidth() * (maxX - minX);
                float worldDy = dy / getHeight() * (maxY - minY);
                
                minX -= worldDx;
                maxX -= worldDx;
                minY += worldDy;
                maxY += worldDy;
                
                lastTouchX = x;
                lastTouchY = y;
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        drawGrid(canvas, width, height);
        drawAxes(canvas, width, height);

        int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN};
        for (int i = 0; i < expressions.size(); i++) {
            graphPaint.setColor(colors[i % colors.length]);
            drawFunction(canvas, width, height, expressions.get(i));
        }
    }

    private void drawGrid(Canvas canvas, int width, int height) {
        float stepX = calculateStep(maxX - minX);
        float startX = (float) (Math.floor(minX / stepX) * stepX);
        for (float x = startX; x <= maxX; x += stepX) {
            float screenX = (x - minX) / (maxX - minX) * width;
            canvas.drawLine(screenX, 0, screenX, height, gridPaint);
        }

        float stepY = calculateStep(maxY - minY);
        float startY = (float) (Math.floor(minY / stepY) * stepY);
        for (float y = startY; y <= maxY; y += stepY) {
            float screenY = height - (y - minY) / (maxY - minY) * height;
            canvas.drawLine(0, screenY, width, screenY, gridPaint);
        }
    }

    private float calculateStep(float range) {
        if (range <= 0) return 1;
        float log = (float) Math.log10(range / 5);
        float level = (float) Math.pow(10, Math.floor(log));
        return level;
    }

    private void drawAxes(Canvas canvas, int width, int height) {
        float centerX = (0 - minX) / (maxX - minX) * width;
        float centerY = height - (0 - minY) / (maxY - minY) * height;
        canvas.drawLine(centerX, 0, centerX, height, axisPaint);
        canvas.drawLine(0, centerY, width, centerY, axisPaint);
    }

    private void drawFunction(Canvas canvas, int width, int height, String expr) {
        Path path = new Path();
        boolean first = true;
        float step = (maxX - minX) / width;
        
        for (int i = 0; i < width; i++) {
            float x = minX + i * step;
            // Robust variable replacement
            String exprWithX = expr.replace("x", "(" + x + ")");
            String resultStr = calculator.evaluate(exprWithX, true);
            
            try {
                double y = Double.parseDouble(resultStr);
                float screenX = i;
                float screenY = (float) (height - (y - minY) / (maxY - minY) * height);

                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    if (first) {
                        path.moveTo(screenX, screenY);
                        first = false;
                    } else {
                        // Clamp screenY to avoid massive drawing issues with vertical asymptotes
                        float clampedY = Math.max(-height, Math.min(2 * height, screenY));
                        path.lineTo(screenX, clampedY);
                    }
                } else {
                    first = true;
                }
            } catch (Exception e) {
                first = true;
            }
        }
        canvas.drawPath(path, graphPaint);
    }
}
