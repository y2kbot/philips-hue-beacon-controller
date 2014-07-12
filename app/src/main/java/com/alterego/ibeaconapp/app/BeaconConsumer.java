package com.alterego.ibeaconapp.app;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.alterego.ibeaconapp.app.data.Beacon;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import java.util.Collection;

public class BeaconConsumer implements IBeaconConsumer {

    private final SettingsManager mSettingsManager;

    public BeaconConsumer (SettingsManager mgr) {
        mSettingsManager = mgr;
    }


    @Override
    public void onIBeaconServiceConnect() {

        //monitoring
        mSettingsManager.getIBeaconManager().setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                logToDisplayMonitoring("I just saw an iBeacon named " + region.getUniqueId() + " for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                logToDisplayMonitoring("I no longer see an iBeacon named " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                logToDisplayMonitoring("I have just switched from seeing/not seeing iBeacons: " + state);
            }


        });

        //ranging

        mSettingsManager.getIBeaconManager().setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                if (iBeacons.size() > 0) {
                    logToDisplayRanging("iBeacon number = " + iBeacons.size());
                    IBeacon iBeacon = iBeacons.iterator().next();
                    Beacon immBeacon = new Beacon(iBeacon);
//                    this.major = otherIBeacon.major;
//                    this.minor = otherIBeacon.minor;
//                    this.accuracy = otherIBeacon.accuracy;
//                    this.proximity = otherIBeacon.proximity;
//                    this.rssi = otherIBeacon.rssi;
//                    this.proximityUuid = otherIBeacon.proximityUuid;
//                    this.txPower = otherIBeacon.txPower;
//                    this.bluetoothAddress = otherIBeacon.bluetoothAddress;

                    logToDisplayRanging(
                            "iMMBeacon data = " + immBeacon.toString());
                }
            }

        });

        try {
            Region myMonitoringRegion = new Region("MMMonitoringRegion", SettingsManager.MMBeaconProximityUUID.toLowerCase(), SettingsManager.MMBeaconMajor, SettingsManager.MMBeaconMinor);
            Region myRangingRegion = new Region("MMRangingRegion", SettingsManager.MMBeaconProximityUUID.toLowerCase(), SettingsManager.MMBeaconMajor, SettingsManager.MMBeaconMinor);
            mSettingsManager.getIBeaconManager().startMonitoringBeaconsInRegion(myMonitoringRegion);
            mSettingsManager.getIBeaconManager().startRangingBeaconsInRegion(myRangingRegion);

        } catch (RemoteException e) {
            mSettingsManager.getMLogger().error("BeaconConsumer remote exception = " + e.getMessage());
        }
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    private void logToDisplayMonitoring(final String line) {
        mSettingsManager.getMLogger().info("BeaconConsumer monitoring = " + line);
    }

    private void logToDisplayRanging(final String line) {
        mSettingsManager.getMLogger().info("BeaconConsumer ranging = " + line);
    }
}
