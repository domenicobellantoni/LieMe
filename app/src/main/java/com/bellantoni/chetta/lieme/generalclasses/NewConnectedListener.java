package com.bellantoni.chetta.lieme.generalclasses;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import zephyr.android.HxMBT.*;

public class NewConnectedListener extends ConnectListenerImpl
{
	private Handler OldHandler;
	private Handler aNewHandler;
	private int GP_MSG_ID = 0x20;
	private int GP_HANDLER_ID = 0x20;
	private int HR_SPD_DIST_PACKET =0x26;
	
	private final int HEART_RATE = 0x100;
	private final int INSTANT_SPEED = 0x101;
	private HRSpeedDistPacketInfo HRSpeedDistPacket = new HRSpeedDistPacketInfo();
	public NewConnectedListener(Handler handler,Handler _NewHandler) {
		super(handler, null);
		OldHandler = handler;
		aNewHandler = _NewHandler;

		// TODO Auto-generated constructor stub

	}

    public void Connected(ConnectedEvent<BTClient> eventArgs) {
		System.out.println(String.format("Connected to BioHarness %s.", eventArgs.getSource().getDevice().getName()));


		//Creates a new ZephyrProtocol object and passes it the BTComms object
		ZephyrProtocol protocol = new ZephyrProtocol(eventArgs.getSource().getComms());
		//ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms(), );
		protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
            public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
                ZephyrPacketArgs msg = eventArgs.getPacket();
                byte CRCFailStatus;
                byte RcvdBytes;


                CRCFailStatus = msg.getCRCStatus();
                RcvdBytes = msg.getNumRvcdBytes();
                if (HR_SPD_DIST_PACKET == msg.getMsgID()) {


                    byte[] DataArray = msg.getBytes();

                    //***************Displaying the Heart Rate********************************
                    int HRate = HRSpeedDistPacket.GetHeartRate(DataArray);
                    Message text1 = aNewHandler.obtainMessage(HEART_RATE);
                    Bundle b1 = new Bundle();
                    b1.putString("HeartRate", String.valueOf(HRate));
                    text1.setData(b1);
                    aNewHandler.sendMessage(text1);
                    System.out.println("Heart Rate is " + HRate);

                    //***************Displaying the Instant Speed********************************
                    double InstantSpeed = HRSpeedDistPacket.GetInstantSpeed(DataArray);

                    text1 = aNewHandler.obtainMessage(INSTANT_SPEED);
                    b1.putString("InstantSpeed", String.valueOf(InstantSpeed));
                    text1.setData(b1);
                    aNewHandler.sendMessage(text1);
                    System.out.println("Instant Speed is " + InstantSpeed);

                }
            }
        });
	}
	
}