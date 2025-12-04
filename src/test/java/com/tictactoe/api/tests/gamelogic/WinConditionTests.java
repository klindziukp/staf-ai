package com.tictactoe.api.tests.gamelogic;

import com.tictactoe.api.base.BaseTest;
import com.tictactoe.api.models.Status;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for game win condition scenarios.
 * Tests various winning patterns in Tic Tac Toe.
 */
@Epic("Tic Tac Toe API")
@Feature("Game Logic")
@Story("Win Conditions")
public class WinConditionTests extends BaseTest {

    @Test(description = "Verify horizontal win in row 1")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that three X marks in row 1 results in X winning")
    public void testHorizontalWinRow1() {
        // Place X marks in row 1
        squareApiClient.placeMark(1, 1, "X");
        squareApiClient.placeMark(1, 2, "X");
        Status status = squareApiClient.placeMarkAndGetStatus(1, 3, "X");

        assertThat(status.hasWinner())
                .as("Game should have a winner")
                .isTrue();
        assertThat(status.getWinner())
                .as("Winner should be X")
                .isEqualTo("X");
    }

    @Test(description = "Verify vertical win in column 1")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates that three O marks in column 1 results in O winning")
    public void testVerticalWinColumn1() {
        // Place O marks in column 1
        squareApiClient.placeMark(1, 1, "O");
        squareApiClient.placeMark(2, 1, "O");
        Status status = squareApiClient.placeMarkAndGetStatus(3, 1, "O");

        assertThat(status.hasWinner())
                .as("Game should have a winner")
                .isTrue();
        assertThat(status.getWinner())
                .as("Winner should be O")
                .isEqualTo("O");
    }

    @Test(description = "Verify diagonal win (top-left to bottom-right)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates diagonal win from position (1,1) to (3,3)")
    public void testDiagonalWinTopLeftToBottomRight() {
        // Place X marks diagonally
        squareApiClient.placeMark(1, 1, "X");
        squareApiClient.placeMark(2, 2, "X");
        Status status = squareApiClient.placeMarkAndGetStatus(3, 3, "X");

        assertThat(status.hasWinner())
                .as("Game should have a winner")
                .isTrue();
        assertThat(status.getWinner())
                .as("Winner should be X")
                .isEqualTo("X");
    }

    @Test(description = "Verify diagonal win (top-right to bottom-left)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test validates diagonal win from position (1,3) to (3,1)")
    public void testDiagonalWinTopRightToBottomLeft() {
        // Place O marks diagonally
        squareApiClient.placeMark(1, 3, "O");
        squareApiClient.placeMark(2, 2, "O");
        Status status = squareApiClient.placeMarkAndGetStatus(3, 1, "O");

        assertThat(status.hasWinner())
                .as("Game should have a winner")
                .isTrue();
        assertThat(status.getWinner())
                .as("Winner should be O")
                .isEqualTo("O");
    }

    @Test(description = "Verify no winner with incomplete game")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test validates that incomplete game has no winner")
    public void testNoWinnerIncompleteGame() {
        // Place some marks without winning
        squareApiClient.placeMark(1, 1, "X");
        squareApiClient.placeMark(1, 2, "O");
        Status status = squareApiClient.placeMarkAndGetStatus(2, 1, "X");

        assertThat(status.isInProgress())
                .as("Game should still be in progress")
                .isTrue();
    }
}
