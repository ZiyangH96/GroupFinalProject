package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;


import algonquin.cst2355.groupfinalproject.R;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class NotificationExampleActivity extends AppCompatActivity {

    private EditText inputEditText;
    private Button showToastButton;
    private Button showSnackbarButton;
    private Button showAlertDialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        inputEditText = findViewById(R.id.inputEditText);
        showToastButton = findViewById(R.id.showToastButton);
        showSnackbarButton = findViewById(R.id.showSnackbarButton);
        showAlertDialogButton = findViewById(R.id.showAlertDialogButton);

        showToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShortToast("This is a Toast message");
            }
        });

        showSnackbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar(view, "This is a Snackbar message");
            }
        });

        showAlertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("Alert Title", "This is an important message.");
            }
        });
    }

    private void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the action click
                        // For example, navigate to another screen or perform an action
                        showShortToast("Snackbar action clicked");
                    }
                })
                .show();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the positive button click
                        // For example, dismiss the dialog or perform an action
                        showShortToast("OK button clicked");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the negative button click
                        // For example, dismiss the dialog or perform a different action
                        showShortToast("Cancel button clicked");
                    }
                })
                .show();
    }
}

