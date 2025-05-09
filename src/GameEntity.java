public abstract class GameEntity {
    Position position;
    double speed;
    static int HP;

    public abstract void movement();
    public abstract void takeDamage();
    public abstract void die();


    public GameEntity(Position position, double speed, int HP) {
        this.position = position;
        this.speed = speed;
        GameEntity.HP = HP;
    }
}
