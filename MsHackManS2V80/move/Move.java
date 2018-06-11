package move;

/**
 * move.Move
 *
 * Used to output a move to the engine
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class Move {

    private MoveType moveType = MoveType.PASS;
    private Integer bombTicks;

    public Move() {}

    public Move(MoveType moveType) {
        this.moveType = moveType;
        this.bombTicks = null;
    }

    public Move(MoveType moveType, int bombTicks) {
        this.moveType = moveType;
        this.bombTicks = bombTicks;
    }

    public String toString() {
        if (this.moveType == MoveType.PASS || this.bombTicks == null) {
            return this.moveType.toString();
        }

        return String.format("%s;drop_bomb %d", this.moveType, this.bombTicks);
    }
}
