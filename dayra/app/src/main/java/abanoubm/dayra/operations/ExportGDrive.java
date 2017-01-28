package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.FileInputStream;
import java.io.OutputStream;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class ExportGDrive extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "BaseDriveActivity";
    private ProgressDialog pBar;

    protected static final int REQUEST_CODE_RESOLUTION = 1;
    protected static final int REQUEST_CODE_UPLOAD = 5;

    private GoogleApiClient mGoogleApiClient;
    private boolean connectionTried=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gdrive);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_gdrive);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        pBar = new ProgressDialog(ExportGDrive.this);
        pBar.setMessage(getResources().getString(R.string.label_loading));
        pBar.setCancelable(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pBar.show();
        if (connectionTried)
            finish();

        mGoogleApiClient.connect();
        connectionTried=true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            connectionTried=false;
            mGoogleApiClient.connect();
        } else if (requestCode == REQUEST_CODE_UPLOAD && resultCode == RESULT_OK) {
            showMessage(R.string.msg_dayra_exported);
            finish();
        }
    }


    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            showMessage(R.string.err_msg_export);
            finish();

        }
    }

    public void showMessage(int resource) {
        Toast.makeText(this, resource, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Creating new contents.");
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveContentsResult>() {

                    @Override
                    public void onResult(final DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            showMessage(R.string.err_msg_export);
                            finish();

                            return;
                        }
                        Log.i(TAG, "New contents created.");
                        new Thread() {
                            @Override
                            public void run() {
                                OutputStream outputStream = result.getDriveContents().getOutputStream();
                                try {
                                    FileInputStream inStream = new FileInputStream(
                                            DB.getInstant(getApplicationContext()).getDBFile(getApplicationContext()));
                                    if (inStream != null) {
                                        byte[] data = new byte[1024];
                                        while (inStream.read(data) != -1) {
                                            outputStream.write(data);
                                        }
                                        inStream.close();
                                    }

                                    outputStream.close();
                                } catch (Exception e) {
                                    showMessage(R.string.err_msg_export);
                                    finish();
                                    Log.i(TAG, "Unable to write file contents.");

                                    return;
                                }
                                // Create the initial metadata - MIME type and title.
                                // Note that the user will be able to change the title later.
                                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                        .setMimeType("application/octet-stream").setTitle(Utility.getDayraName(getApplicationContext())).build();

                                // Create an intent for the file chooser, and start it.
                                IntentSender intentSender = Drive.DriveApi
                                        .newCreateFileActivityBuilder()
                                        .setInitialMetadata(metadataChangeSet)
                                        .setInitialDriveContents(result.getDriveContents())
                                        .build(mGoogleApiClient);
                                try {
                                    startIntentSenderForResult(
                                            intentSender, REQUEST_CODE_UPLOAD, null, 0, 0, 0);
                                    pBar.dismiss();
                                } catch (SendIntentException e) {
                                    showMessage(R.string.err_msg_export);
                                    finish();
                                    Log.i(TAG, "Failed to launch file chooser.");
                                }
                            }
                        }.start();

                    }

                });


    }


}