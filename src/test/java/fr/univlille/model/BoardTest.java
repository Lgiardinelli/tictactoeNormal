package fr.univlille.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    private Board board;

    @BeforeEach
    void init() {
        board = new Board();
    }

    @Test
    void test_initialisation() {
        assertNull(board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        verifierPlateauVide();
    }

    @Test
    void test_joue_case() {
        board.mark(1, 1);

        assertEquals(Player.X, lireCase(1, 1));
        assertEquals(Player.O, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
    }

    @Test
    void test_case_hors_limites() {
        board.mark(-1, 0);
        board.mark(0, 3);

        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
        verifierPlateauVide();
    }

    @Test
    void test_case_deja_jouee() {
        board.mark(0, 0);
        board.mark(0, 0);

        assertEquals(Player.X, lireCase(0, 0));
        assertEquals(Player.O, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
    }

    @Test
    void test_joueur_o_commence() {
        board.setCurrentTurn(Player.O);
        board.mark(0, 0);

        assertEquals(Player.O, lireCase(0, 0));
        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
    }

    @Test
    void test_victoire_horizontale() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        assertEquals(Player.X, board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        assertFalse(board.isInProgressMode());
        verifierLigneX();
    }

    @Test
    void test_victoire_verticale() {
        board.mark(0, 0);
        board.mark(0, 1);
        board.mark(1, 0);
        board.mark(1, 1);
        board.mark(2, 2);
        board.mark(2, 1);

        assertEquals(Player.O, board.getWinner());
        assertEquals(Player.O, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        verifierColonneO();
    }

    @Test
    void test_victoire_verticale_de_x() {
        board.mark(0, 0);
        board.mark(0, 1);
        board.mark(1, 0);
        board.mark(1, 1);
        board.mark(2, 0);

        assertEquals(Player.X, board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, lireCase(0, 0));
        assertEquals(Player.X, lireCase(1, 0));
        assertEquals(Player.X, lireCase(2, 0));
    }

    @Test
    void test_victoire_horizontale_de_o() {
        board.setCurrentTurn(Player.O);
        board.mark(1, 0);
        board.mark(0, 0);
        board.mark(1, 1);
        board.mark(0, 1);
        board.mark(1, 2);

        assertEquals(Player.O, board.getWinner());
        assertEquals(Player.O, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        assertEquals(Player.O, lireCase(1, 0));
        assertEquals(Player.O, lireCase(1, 1));
        assertEquals(Player.O, lireCase(1, 2));
    }

    @Test
    void test_victoire_diagonale() {
        board.mark(0, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);
        board.mark(2, 2);

        assertEquals(Player.X, board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        verifierDiagonaleX();
    }

    @Test
    void test_victoire_diagonale_inverse() {
        board.mark(0, 0);
        board.mark(0, 2);
        board.mark(1, 0);
        board.mark(1, 1);
        board.mark(2, 2);
        board.mark(2, 0);

        assertEquals(Player.O, board.getWinner());
        assertEquals(Player.O, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        verifierDiagonaleInverseO();
    }

    @Test
    void test_partie_nulle_sans_gagnant() {
        board.mark(0, 0);
        board.mark(0, 1);
        board.mark(0, 2);
        board.mark(1, 1);
        board.mark(1, 0);
        board.mark(1, 2);
        board.mark(2, 1);
        board.mark(2, 0);
        board.mark(2, 2);

        assertNull(board.getWinner());
        assertEquals(Player.O, board.getCurrentTurn());
        assertTrue(board.isInProgressMode());
        assertEquals(Player.X, lireCase(0, 0));
        assertEquals(Player.O, lireCase(0, 1));
        assertEquals(Player.X, lireCase(0, 2));
        assertEquals(Player.X, lireCase(1, 0));
        assertEquals(Player.O, lireCase(1, 1));
        assertEquals(Player.O, lireCase(1, 2));
        assertEquals(Player.O, lireCase(2, 0));
        assertEquals(Player.X, lireCase(2, 1));
        assertEquals(Player.X, lireCase(2, 2));
    }

    @Test
    void test_plus_de_coup_apres_fin() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        Player[][] avant = copierPlateau();

        board.mark(2, 2);

        assertEquals(Player.X, board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
        verifierPlateau(avant);
    }

    @Test
    void test_restart() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        board.restart();

        assertNull(board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        verifierPlateauVide();
    }

    private void verifierPlateauVide() {
        verifierPlateau(new Player[][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
        });
    }

    private void verifierLigneX() {
        assertEquals(Player.X, lireCase(0, 0));
        assertEquals(Player.X, lireCase(0, 1));
        assertEquals(Player.X, lireCase(0, 2));
    }

    private void verifierColonneO() {
        assertEquals(Player.O, lireCase(0, 1));
        assertEquals(Player.O, lireCase(1, 1));
        assertEquals(Player.O, lireCase(2, 1));
    }

    private void verifierDiagonaleX() {
        assertEquals(Player.X, lireCase(0, 0));
        assertEquals(Player.X, lireCase(1, 1));
        assertEquals(Player.X, lireCase(2, 2));
    }

    private void verifierDiagonaleInverseO() {
        assertEquals(Player.O, lireCase(0, 2));
        assertEquals(Player.O, lireCase(1, 1));
        assertEquals(Player.O, lireCase(2, 0));
    }

    private void verifierPlateau(Player[][] expected) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(expected[row][col], lireCase(row, col),
                        "Unexpected value at [" + row + "][" + col + "]");
            }
        }
    }

    private Player[][] copierPlateau() {
        Player[][] result = new Player[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row][col] = lireCase(row, col);
            }
        }
        return result;
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



