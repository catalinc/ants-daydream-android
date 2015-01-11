package catalinc.daydream.ants;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;

class RenderThread extends Thread {

    private SurfaceHolder mSurfaceHolder;

    private Grid mGrid;

    private Paint mBackgroundPaint;
    private Paint mGridPaint;
    private Paint mActiveCellPaint;
    private Paint mAntPaint;

    private volatile boolean mRunning;

    RenderThread(Context context, SurfaceHolder mSurfaceHolder) {
        this.mSurfaceHolder = mSurfaceHolder;

        mGrid = new Grid(10, 10);
        mGrid.spawnAnt();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // TODO simplify preferences
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.parseColor(preferences.getString("background_color_preference", "#000000")));
        mGridPaint = new Paint();
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setColor(Color.YELLOW); // TODO set from preference
        mActiveCellPaint = new Paint();
        mActiveCellPaint.setColor(Color.parseColor(preferences.getString("active_cell_color_preference", "#FFFF00")));
        mAntPaint = new Paint();
        mAntPaint.setColor(Color.parseColor(preferences.getString("ant_color_preference", "#00FFFF")));
    }

    void go() {
        mRunning = true;
        start();
    }

    @Override
    public void run() {
        while (mRunning) {
            mGrid.moveAnts();
            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    drawGrid(canvas);
                }
            } finally {
                if (canvas != null) mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        int cellHeight = canvas.getHeight() / mGrid.getRows();
        int cellWidth = canvas.getWidth() / mGrid.getCols();

        canvas.drawPaint(mBackgroundPaint);

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mGridPaint);

        for (int r = 1; r < mGrid.getRows(); r++) {
            int y = r * cellHeight;
            canvas.drawLine(0, y, canvas.getWidth(), y, mGridPaint);
        }

        for (int c = 1; c < mGrid.getCols(); c++) {
            int x = c * cellWidth;
            canvas.drawLine(x, 0, x, canvas.getHeight(), mGridPaint);
        }

        for (int r = 0; r < mGrid.getRows(); r++) {
            for (int c = 0; c < mGrid.getCols(); c++) {
                if (mGrid.getCell(r, c)) {
                    drawCell(canvas, mActiveCellPaint, r, c, cellHeight, cellWidth);
                }
            }
        }

        for (Grid.Ant ant: mGrid.getAnts()) {
            drawCell(canvas, mAntPaint, ant.getRow(), ant.getCol(), cellHeight, cellWidth);
        }
    }

    private void drawCell(Canvas canvas, Paint paint, int row, int col, int cellHeight, int cellWidth) {
        int top = row * cellHeight;
        int left = col * cellWidth;
        RectF rect = new RectF(left + 1, top + 1, left + cellWidth - 1, top + cellHeight - 1);
        canvas.drawRoundRect(rect, 2f, 2f, paint);
    }

    void terminate() {
        mRunning = false;
        interrupt();
        try {
            join();
        } catch (InterruptedException e) {
        }
    }
}
