package com.shwetank.libraryassistant.beacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BeaconActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    protected boolean mScanning;
    private Map<String, Beacon> mBeaconMap = new LinkedHashMap<>();
    private static final double CONF_1 = 0.42093;
    private static final double CONF_2 = 6.9476;
    private static final double CONF_3 = 0.54992;
    private UUID defaultUUID = UUID.fromString("0746dfe1-319c-48b7-9cc8-561b79c3f223");
    private boolean isCalled = false;

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            if(result.getScanRecord() == null){
                return;
            }

            Pair<Boolean, Integer> beaconPattern = isBeaconPattern(result);
            if (beaconPattern.first) {
                Beacon beacon = createBeaconFromScanRecord(result.getScanRecord().getBytes(), beaconPattern.second);
                if (beacon.getUuid().equals(defaultUUID)) {

                    double distance = calculateAccuracy(result.getTxPower(), result.getRssi()) * 10000;

                    if (distance < 1) {
                        beacon.setProximityRange(Proximity.NEAR);
                    } else if (distance > 1 && distance < 7) {
                        beacon.setProximityRange(Proximity.FAR);
                    } else {
                        beacon.setProximityRange(Proximity.AWAY);
                    }
                    String uniqueKey = beacon.getUuid() + ":" + beacon.getMajor() + ":" + beacon.getMinor();
                    Log.d("Beacon", uniqueKey.concat("  ").concat(String.valueOf(distance)));
                    mBeaconMap.put(uniqueKey, beacon);
                    if(!isCalled){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                beaconDetected();
                            }
                        }, 3000);
                        isCalled = true;
                    }
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("ListBeacon", String.valueOf(results));
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    protected abstract void beaconDetected();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Manager not available", Toast.LENGTH_LONG).show();
            return;
        }
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Adapter not available", Toast.LENGTH_LONG).show();
        }
    }

    protected List<Beacon> getBeaconList() {
        ArrayList<Beacon> a = new ArrayList<>(mBeaconMap.values());
        a.sort(new Comparator<Beacon>() {
            @Override
            public int compare(Beacon beacon1, Beacon beacon2) {
                return Integer.compare(beacon1.getProximityRange().ordinal(), beacon2.getProximityRange().ordinal());
            }
        });
        return a;
    }

    protected void scanBleDevice(boolean enable) {
        BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (enable) {
            mScanning = true;
            bluetoothLeScanner.startScan(mScanCallback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(mScanCallback);
        }
    }

    public static Pair<Boolean, Integer> isBeaconPattern(final ScanResult scanResult) {
        final byte[] scanRecord = scanResult.getScanRecord().getBytes();

        int startByte = 2;
        while (startByte <= 5) {
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && // identifies an iBeacon
                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {
                // first element identifies correct data length
                return new Pair<>(true, startByte);
            }
            startByte++;
        }

        return new Pair<>(false, startByte);
    }


    private Beacon createBeaconFromScanRecord(final byte[] scanRecord, int startByte) {
        // get the UUID from the hex result
        final byte[] uuidBytes = new byte[16];
        System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
        UUID uuid = bytesToUuid(uuidBytes);
        int major = byteArrayToInteger(Arrays.copyOfRange(scanRecord, startByte + 20, startByte + 22));
        int minor = byteArrayToInteger(Arrays.copyOfRange(scanRecord, startByte + 22, startByte + 24));
        return new Beacon(uuid, major, minor);
    }

    private UUID bytesToUuid(final byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        final String hex = new String(hexChars);

        return UUID.fromString(hex.substring(0, 8) + "-" +
                hex.substring(8, 12) + "-" +
                hex.substring(12, 16) + "-" +
                hex.substring(16, 20) + "-" +
                hex.substring(20, 32));
    }

    private int byteArrayToInteger(final byte[] byteArray) {
        return (byteArray[0] & 0xff) * 0x100 + (byteArray[1] & 0xff);
    }

    public static double calculateAccuracy(final int txPower, final double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        final double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (CONF_1) * Math.pow(ratio, CONF_2) + CONF_3;
        }
    }
}
