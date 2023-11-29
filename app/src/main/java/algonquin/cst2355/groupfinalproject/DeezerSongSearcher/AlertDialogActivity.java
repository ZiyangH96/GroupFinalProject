package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;


import algonquin.cst2355.groupfinalproject.R;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        Button showAlertDialogButton = findViewById(R.id.showAlertDialogButton);

        showAlertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Title")
                .setMessage("This is an important message.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the positive button click
                        // For example, dismiss the dialog or perform an action
                        // You can add your logic here
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the negative button click
                        // For example, dismiss the dialog or perform a different action
                        // You can add your logic here
                    }
                })
                .show();
    }
}

