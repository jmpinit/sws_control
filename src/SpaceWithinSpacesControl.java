package emotivemachine.sws_control;
import processing.core.*;

public class SpaceWithinSpacesControl {
  PApplet parent;

  public SpaceWithinSpacesControl(PApplet parent) {
    this.parent = parent;
    parent.registerMethod("post", this);
  }

  public void post() {
    System.out.println("Hello world");
  }
}
