package emotivemachine.sws_control;
import java.io.IOException;
import java.net.*;

import processing.core.*;

public class SpaceWithinSpacesControl {
  private final static int ARRAY_WIDTH = 10;
  private final static int ARRAY_HEIGHT = 18;
  private final static int ARRAY_PORT = 4210;
  private InetAddress BROADCAST_ADDRESS;

  private PApplet parent;

  private DatagramSocket sock;

  public SpaceWithinSpacesControl(PApplet parent) {
    this.parent = parent;
    parent.registerMethod("dispose", this);
    parent.registerMethod("post", this);

    try {
      BROADCAST_ADDRESS = InetAddress.getByName("255.255.255.255");
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

  private byte[] imageToBytes(PImage image) {
    byte[] bytes = new byte[ARRAY_WIDTH * ARRAY_HEIGHT];

    for (int y = 0; y < image.height; y++) {
      for (int x = 0; x < image.width; x++) {
        int brightness = (int)parent.brightness(image.get(x, y));
        bytes[y * ARRAY_WIDTH + x] = (byte)brightness;
      }
    }

    return bytes;
  }

  private void updateLEDArray(PImage frame) {
    byte[] imageBytes = imageToBytes(frame);
    DatagramPacket packet = new DatagramPacket(imageBytes, imageBytes.length, BROADCAST_ADDRESS, ARRAY_PORT);

    try {
      this.sock.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void post() {
    PImage canvasImage = parent.get();
    canvasImage.resize(ARRAY_WIDTH, ARRAY_HEIGHT);
    updateLEDArray(canvasImage);
    System.out.println("SWS: Sent pixels to array");
  }

  public void dispose() {
    this.sock.close();
  }
}
