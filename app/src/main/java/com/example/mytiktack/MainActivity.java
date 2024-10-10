package com.example.mytiktack;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isPlayerOneTurn = true;
    private boolean isGameActive = true;
    private int[][] board = new int[3][3];
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private boolean playWithBot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Switch botSwitch = findViewById(R.id.botSwitch);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        Button resetButton = findViewById(R.id.resetButton);

        botSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> playWithBot = isChecked);

        resetButton.setOnClickListener(v -> resetGame());

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final int row = i / 3;
            final int col = i % 3;
            Button button = (Button) gridLayout.getChildAt(i);
            button.setOnClickListener(v -> {
                if (!isGameActive || !button.getText().equals("")) return;

                if (isPlayerOneTurn) {
                    button.setText("X");
                    board[row][col] = 1;
                } else {
                    button.setText("O");
                    board[row][col] = 2;
                }

                if (checkWin()) {
                    isGameActive = false;
                    if (isPlayerOneTurn) {
                        playerOneScore++;
                    } else {
                        playerTwoScore++;
                    }
                    updateScore();
                    return;
                }

                isPlayerOneTurn = !isPlayerOneTurn;

                if (playWithBot && !isPlayerOneTurn && isGameActive) {
                    botMove();
                }
            });
        }
    }

    private void botMove() {
        // Примитивная логика бота (выбираем первый доступный ход)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    Button button = (Button) ((GridLayout) findViewById(R.id.gridLayout)).getChildAt(i * 3 + j);
                    button.setText("O");
                    board[i][j] = 2;
                    if (checkWin()) {
                        isGameActive = false;
                        playerTwoScore++;
                        updateScore();
                    }
                    isPlayerOneTurn = true;
                    return;
                }
            }

        }
    }


    private boolean checkWin() {
        // Проверка горизонталей, вертикалей и диагоналей на победу
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0) {
                highlightWinningCells(i, 0, i, 1, i, 2);
                return true;
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != 0) {
                highlightWinningCells(0, i, 1, i, 2, i);
                return true;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0) {
            highlightWinningCells(0, 0, 1, 1, 2, 2);
            return true;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0) {
            highlightWinningCells(0, 2, 1, 1, 2, 0);
            return true;
        }
        return false;
    }

    private void highlightWinningCells(int r1, int c1, int r2, int c2, int r3, int c3) {
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Button b1 = (Button) gridLayout.getChildAt(r1 * 3 + c1);
        Button b2 = (Button) gridLayout.getChildAt(r2 * 3 + c2);
        Button b3 = (Button) gridLayout.getChildAt(r3 * 3 + c3);
        b1.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        b2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        b3.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
    }

    private void updateScore() {
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Player 1: " + playerOneScore + "  Player 2/Bot: " + playerTwoScore);
    }

    private void resetGame() {
        isGameActive = true;
        isPlayerOneTurn = true;
        board = new int[3][3];
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setText("");
            button.setBackgroundColor(Color.parseColor("#1F65D1"));
        }
    }

}
