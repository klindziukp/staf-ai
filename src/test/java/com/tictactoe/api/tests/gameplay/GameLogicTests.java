package com.tictactoe.api.tests.gameplay;

import com.tictactoe.api.models.Mark;
import com.tictactoe.api.models.Status;
import com.tictactoe.api.tests.base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for game logic scenarios including winning conditions and draws.
 */
@Epic("Tic Tac Toe API")
@Feature("Game Logic")
@Story("Winning Conditions and Game States")
public class GameLogicTests extends BaseTest {

    @Test(description = "Verify X wins with horizontal line in first row")
    @Description("Test that X wins when completing first row")
    @Severity(SeverityLevel.CRITICAL)
    public void testXWinsHorizontalFirstRow() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(2, 1, Mark.O);
        apiClient.putSquare(1, 2, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        Status status = apiClient.putSquareAndGetStatus(1, 3, Mark.X);

        assertThat(status.hasWinner()).isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.X);
    }

    @Test(description = "Verify O wins with horizontal line in second row")
    @Description("Test that O wins when completing second row")
    @Severity(SeverityLevel.CRITICAL)
    public void testOWinsHorizontalSecondRow() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(2, 1, Mark.O);
        apiClient.putSquare(1, 2, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        apiClient.putSquare(3, 3, Mark.X);
        Status status = apiClient.putSquareAndGetStatus(2, 3, Mark.O);

        assertThat(status.hasWinner()).isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.O);
    }

    @Test(description = "Verify X wins with vertical line in first column")
    @Description("Test that X wins when completing first column")
    @Severity(SeverityLevel.CRITICAL)
    public void testXWinsVerticalFirstColumn() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 2, Mark.O);
        apiClient.putSquare(2, 1, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        Status status = apiClient.putSquareAndGetStatus(3, 1, Mark.X);

        assertThat(status.hasWinner()).isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.X);
    }

    @Test(description = "Verify O wins with vertical line in third column")
    @Description("Test that O wins when completing third column")
    @Severity(SeverityLevel.CRITICAL)
    public void testOWinsVerticalThirdColumn() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 3, Mark.O);
        apiClient.putSquare(2, 1, Mark.X);
        apiClient.putSquare(2, 3, Mark.O);
        apiClient.putSquare(2, 2, Mark.X);
        Status status = apiClient.putSquareAndGetStatus(3, 3, Mark.O);

        assertThat(status.hasWinner()).isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.O);
    }

    @Test(description = "Verify X wins with diagonal from top-left to bottom-right")
    @Description("Test that X wins when completing main diagonal")
    @Severity(SeverityLevel.CRITICAL)
    public void testXWinsDiagonalTopLeftToBottomRight() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 2, Mark.O);
        apiClient.putSquare(2, 2, Mark.X);
        apiClient.putSquare(2, 1, Mark.O);
        Status status = apiClient.putSquareAndGetStatus(3, 3, Mark.X);

        assertThat(status.hasWinner()).isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.X);
    }

    @Test(description = "Verify O wins with diagonal from top-right to bottom-left")
    @Description("Test that O wins when completing anti-diagonal")
    @Severity(SeverityLevel.CRITICAL)
    public void testOWinsDiagonalTopRightToBottomLeft() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 3, Mark.O);
        apiClient.putSquare(2, 1, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        apiClient.putSquare(3, 2, Mark.X);
        Status status = apiClient.putSquareAndGetStatus(3, 1, Mark.O);

        assertThat(status.hasWinner()).isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.O);
    }

    @Test(description = "Verify draw scenario when board is full with no winner")
    @Description("Test that game ends in draw when all squares filled without winner")
    @Severity(SeverityLevel.CRITICAL)
    public void testDrawScenario() {
        // Create a draw scenario
        // X O X
        // X O O
        // O X X
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(1, 2, Mark.O);
        apiClient.putSquare(1, 3, Mark.X);
        apiClient.putSquare(2, 1, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        apiClient.putSquare(2, 3, Mark.O);
        apiClient.putSquare(3, 1, Mark.O);
        apiClient.putSquare(3, 2, Mark.X);
        Status status = apiClient.putSquareAndGetStatus(3, 3, Mark.X);

        assertThat(status.hasWinner()).isFalse();
        assertThat(status.getWinner()).isEqualTo(Mark.EMPTY);
        assertThat(status.isDraw()).isTrue();
    }

    @Test(description = "Verify game in progress state")
    @Description("Test that game correctly identifies in-progress state")
    @Severity(SeverityLevel.NORMAL)
    public void testGameInProgress() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);

        Status status = apiClient.getBoardAsStatus();

        assertThat(status.isInProgress()).isTrue();
        assertThat(status.hasWinner()).isFalse();
        assertThat(status.isDraw()).isFalse();
    }

    @Test(description = "Verify winner is detected immediately after winning move")
    @Description("Test that winner is set in response to the winning move")
    @Severity(SeverityLevel.CRITICAL)
    public void testWinnerDetectedImmediately() {
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(2, 1, Mark.O);
        apiClient.putSquare(1, 2, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        
        // This move should win for X
        Status status = apiClient.putSquareAndGetStatus(1, 3, Mark.X);

        assertThat(status.hasWinner())
                .as("Winner should be detected immediately")
                .isTrue();
        assertThat(status.getWinner()).isEqualTo(Mark.X);
    }

    @Test(description = "Verify complete game playthrough")
    @Description("Test a complete game from start to finish")
    @Severity(SeverityLevel.NORMAL)
    public void testCompleteGamePlaythrough() {
        // Initial state
        Status initialStatus = apiClient.getBoardAsStatus();
        assertThat(initialStatus.isInProgress()).isTrue();

        // Play game
        apiClient.putSquare(1, 1, Mark.X);
        apiClient.putSquare(2, 1, Mark.O);
        apiClient.putSquare(1, 2, Mark.X);
        apiClient.putSquare(2, 2, Mark.O);
        
        // Check in-progress
        Status midGameStatus = apiClient.getBoardAsStatus();
        assertThat(midGameStatus.isInProgress()).isTrue();
        
        // Winning move
        Status finalStatus = apiClient.putSquareAndGetStatus(1, 3, Mark.X);
        assertThat(finalStatus.hasWinner()).isTrue();
        assertThat(finalStatus.isInProgress()).isFalse();
    }
}
