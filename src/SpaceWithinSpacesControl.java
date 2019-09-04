package emotivemachine.sws_control;
import java.io.IOException;
import java.net.*;

import processing.core.*;

public class SpaceWithinSpacesControl {
  private final static int ARRAY_WIDTH = 10;
  private final static int ARRAY_HEIGHT = 18;
  private final static int ARRAY_PORT = 4210;

  private PApplet parent;
  private boolean useCanvas;

  private DatagramSocket sock;
  private InetAddress address;

  public PImage currentFrame;

  public SpaceWithinSpacesControl(PApplet parent, boolean useCanvas, String broadcastAddress) {
    this.parent = parent;
    parent.registerMethod("dispose", this);

    if (useCanvas) {
      parent.registerMethod("post", this);
    }

    this.useCanvas = useCanvas;

    try {
      address = InetAddress.getByName(broadcastAddress);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    try {
      this.sock = new DatagramSocket();
      this.sock.setBroadcast(true);
    } catch (SocketException e) {
      e.printStackTrace();
    }
  }

  public SpaceWithinSpacesControl(PApplet parent) {
    this(parent, true, "255.255.255.255");
  }

  private byte[] imageToBytes(PImage image) {
    byte[] bytes = new byte[ARRAY_WIDTH * ARRAY_HEIGHT * 2];

    for (int y = 0; y < image.height; y++) {
      for (int x = 0; x < image.width; x++) {
        int brightness = (int)parent.brightness(image.get(x, y));
        int brightnessH = (int)parent.red(image.get(x, y));
        int brightnessL = (int)parent.green(image.get(x, y));

        bytes[(y * ARRAY_WIDTH + x) * 2] = (byte)brightnessH;
        bytes[(y * ARRAY_WIDTH + x) * 2 + 1] = (byte)brightnessL;
      }
    }

    return bytes;
  }

  public void send(PImage frame) {
    if (frame.width != ARRAY_WIDTH || frame.height != ARRAY_HEIGHT) {
      throw new RuntimeException("Image wrong size for array");
    }

    byte[] imageBytes = imageToBytes(frame);
    DatagramPacket packet = new DatagramPacket(imageBytes, imageBytes.length, address, ARRAY_PORT);

    try {
      this.sock.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void post() {
    currentFrame = parent.get();
    currentFrame.resize(ARRAY_WIDTH, ARRAY_HEIGHT);
    send(currentFrame);
  }

  public void dispose() {
    this.sock.close();
  }
}
