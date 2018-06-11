package move;

/**
 * move.MoveType
 *
 * All move types
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public enum MoveType {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    PASS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
