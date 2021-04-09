import java.io.Serializable;

public class Figure implements Serializable {
    Integer type;
    Integer x;
    Integer y;
    Integer radius;
    public Figure() {
	this.type = 1;
	this.x = 0;
	this.y = 0;
	this.radius = 0;
    }
}
