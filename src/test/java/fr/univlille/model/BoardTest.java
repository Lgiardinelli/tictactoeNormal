package fr.univlille.model;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private Board board;

    @Before
    public void init() {
        board = new Board();
    }

    @Test
    public void test_initialisation() {
        assertNull(board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        assertEquals(3, taillePlateau());
    }

    @Test
    public void test_un_coup_simple() {
        board.mark(1, 1);

        assertEquals(Player.X, lireCase(1, 1));
        assertEquals(Player.O, board.getCurrentTurn());
        assertNull(board.getWinner());
    }

    @Test
    public void test_case_hors_limites_invalide() {
        board.mark(-1, 0);
        board.mark(0, 3);

        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
    }

    @Test
    public void test_cover_branch_invalides() {
        board.mark(0, 0);
        board.mark(0, 0);

        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        board.mark(2, 2);

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, lireCase(0, 0));
        assertEquals(Player.X, lireCase(0, 1));
        assertEquals(Player.X, lireCase(0, 2));
        assertEquals(Player.O, lireCase(1, 0));
        assertEquals(Player.O, lireCase(1, 1));
    }

    @Test
    public void test_mutation_diagonale_inverse() {
        board.mark(0, 0);
        board.mark(0, 2);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(2, 1);
        board.mark(2, 0);
        board.mark(1, 2);
        board.mark(1, 1);

        assertEquals(Player.O, board.getWinner());
        assertTrue(board.isInFinishedMode());
        assertEquals(Player.O, lireCase(0, 2));
        assertEquals(Player.O, lireCase(1, 1));
        assertEquals(Player.O, lireCase(2, 0));
    }

    @Test
    public void test_victoire_horizontale() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
        assertFalse(board.isInProgressMode());
    }

    @Test
    public void test_restart() {
        board.mark(0, 0);
        board.mark(1, 0);

        board.restart();

        assertNull(board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
    }

    private int taillePlateau() {
        try {
            Field cellsField = Board.class.getDeclaredField("cells");
            cellsField.setAccessible(true);
            Cell[][] cells = (Cell[][]) cellsField.get(board);
            return cells.length;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Impossible de lire le plateau", e);
        }
    }

    private Player lireCase(int row, int col) {
        try {
            Field cellsField = Board.class.getDeclaredField("cells");
            cellsField.setAccessible(true);
            Cell[][] cells = (Cell[][]) cellsField.get(board);
            return cells[row][col].getValue();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Impossible de lire le plateau", e);
        }
    }
}
