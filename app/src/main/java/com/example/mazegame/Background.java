package com.example.mazegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Background extends View {
    private enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
    private Cell[][] cells;
    private Cell player, end;
    private static final int COLS = 5;
    private static final int ROWS = 14;
    private static final int WALL_DEPTH = 7;
    private float cSize, h, v;
    private Random random;
    private Paint wPaint, playerPaint, endPaint;
    private static int count = 0;

    public Background(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        wPaint = new Paint();
        wPaint.setColor(Color.BLACK);
        wPaint.setStrokeWidth(WALL_DEPTH);
        random = new Random();

        playerPaint = new Paint();
        playerPaint.setColor(Color.BLUE);

        endPaint = new Paint();
        endPaint.setColor(Color.RED);

        createGame();
    }

    private Cell getNeighbor(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        if (cell.c > 0) {
            if (!cells[cell.c - 1][cell.r].visited) {
                neighbors.add(cells[cell.c - 1][cell.r]);
            }
        }
        if (cell.c < (COLS - 1)) {
            if (!cells[cell.c + 1][cell.r].visited) {
                neighbors.add(cells[cell.c + 1][cell.r]);
            }
        }
        if (cell.r > 0) {
            if (!cells[cell.c][cell.r - 1].visited) {
                neighbors.add(cells[cell.c][cell.r - 1]);
            }
        }
        if (cell.r < (ROWS - 1)) {
            if (!cells[cell.c][cell.r + 1].visited) {
                neighbors.add(cells[cell.c][cell.r + 1]);
            }
        }

        if (neighbors.size() > 0) {
            int i = random.nextInt(neighbors.size());
            return neighbors.get(i);
        }
        return null;
    }

    private void remove(Cell curr, Cell next) {
        if ((curr.c == next.c) && (curr.r == next.r + 1)) {
            curr.top = false;
            next.bottom = false;
        }
        if ((curr.c == next.c) && (curr.r == next.r - 1)) {
            curr.bottom = false;
            next.top = false;
        }
        if ((curr.c == next.c + 1) && (curr.r == next.r)) {
            curr.left = false;
            next.right = false;
        }
        if ((curr.c == next.c - 1) && (curr.r == next.r)) {
            curr.right = false;
            next.left = false;
        }
    }

    public void createGame() {
        Stack<Cell> stack = new Stack<>();
        Cell curr, next;

        cells = new Cell[COLS][ROWS];

        for (int a = 0; a < COLS; a++) {
            for (int b = 0; b < ROWS; b++) {
                cells[a][b] = new Cell(a, b);
            }
        }

        player = cells[0][0];
        end = cells[COLS - 1][ROWS - 1];

        curr = cells[0][0];
        curr.visited = true;

        do {
            next = getNeighbor(curr);
            if (next != null) {
                remove(curr, next);
                stack.push(curr);
                curr = next;
                curr.visited = true;
            } else {
                curr = stack.pop();
            }
        } while (!stack.empty());

    }


    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GREEN);

        int width = getWidth();
        int height = getHeight();
        if ((width / height) < (COLS / ROWS)) {
            cSize = width / (COLS + 1);
        } else {
            cSize = height / (ROWS + 1);
        }
        h = (width - (COLS * cSize)) / 2;
        v = (height - (ROWS * cSize)) / 2;
        canvas.translate(h, v);
        for (int a = 0; a < COLS; a++) {
            for (int b = 0; b < ROWS; b++) {
                if (cells[a][b].top) {
                    canvas.drawLine(
                            a * cSize,
                            b * cSize,
                            (a + 1) * cSize,
                             b * cSize,
                            wPaint);
                }
                if (cells[a][b].left) {
                    canvas.drawLine(
                            a * cSize,
                            b * cSize,
                            a * cSize,
                            (b + 1) * cSize,
                            wPaint);
                }
                if (cells[a][b].bottom) {
                    canvas.drawLine(
                            a * cSize,
                            (b + 1) * cSize,
                            (a + 1) * cSize,
                            (b + 1) * cSize,
                            wPaint);
                }
                if (cells[a][b].right) {
                    canvas.drawLine(
                            (a + 1) * cSize,
                            b * cSize,
                            (a + 1) * cSize,
                            (b + 1) * cSize,
                            wPaint);
                }
            }
            float marg = cSize / 10;
            canvas.drawRect(
                    player.c * cSize + marg,
                    player.r * cSize + marg,
                    (player.c + 1) * cSize - marg,
                    (player.r + 1) * cSize - marg,
                    playerPaint);
            canvas.drawRect(
                    end.c * cSize + marg,
                    end.r * cSize + marg,
                    (end.c + 1) * cSize - marg,
                    (end.r + 1) * cSize - marg,
                    endPaint);
        }
    }
    private void move(Direction direction) {
        switch (direction) {
            case UP:
                if (!player.top) {
                    player = cells[player.c][player.r - 1];
                }
                break;
            case DOWN:
                if (!player.bottom) {
                    player = cells[player.c][player.r + 1];
                }
                break;
            case LEFT:
                if (!player.left) {
                    player = cells[player.c - 1][player.r];
                }
                break;
            case RIGHT:
                if (!player.right) {
                    player = cells[player.c + 1][player.r];
                }
        }
        reachEnd();
        invalidate();
    }

    private void reachEnd() {
        if (player == end) {

            createGame();
            count++;
            TextView levelCounter = (TextView) findViewById(R.id.levelCount);
            System.out.println(findViewById(R.id.levelCount));
            String level = "Level: " + count;
            //levelCounter.setText(level);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            float pCenterX = h + (player.c + 0.5f) * cSize;
            float pCenterY = v + (player.r + 0.5f) * cSize;
            float xDelta = x - pCenterX;
            float yDelta = y - pCenterY;
            float absXDelta = Math.abs(xDelta);
            float absYDelta = Math.abs(yDelta);

            if (absXDelta > cSize || absYDelta > cSize) {
                if (absXDelta > absYDelta) {
                    if (xDelta > 0) {
                        move(Direction.RIGHT);
                    } else {
                        move(Direction.LEFT);
                    }
                } else {
                    if (yDelta > 0) {
                        move(Direction.DOWN);
                    } else {
                        move(Direction.UP);
                    }
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class Cell {
        boolean top = true;
        boolean bottom = true;
        boolean left = true;
        boolean right = true;
        boolean visited = false;

        int c, r;

        public Cell(int c, int r) {
            this.c = c;
            this.r = r;
        }
    }
}
