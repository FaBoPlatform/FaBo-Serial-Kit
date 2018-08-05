package io.fabo.serialkit;

public class FaBoUsbParams {

    private int stopBit = 0;
    private int flow = 0;
    private int parityBit = 0;
    private int baudrate = 0;
    private int bitrate = 0;

    public void setStopBit(int stopBit) {
        this.stopBit = stopBit;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public void setParityBit(int parityBit) {
        this.parityBit = parityBit;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getStopBit() {
        return this.stopBit;
    }

    public int getFlow() {
        return this.flow;
    }

    public int getParityBit() {
        return this.parityBit;
    }

    public int getBaudrate() {
        return this.baudrate;
    }

    public int getBitrate() {
        return this.bitrate;
    }
}
