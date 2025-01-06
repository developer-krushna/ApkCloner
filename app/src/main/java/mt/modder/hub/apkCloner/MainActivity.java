/*
 * ApkCloner - based on Android Manifest and resource.arsc package modification 
 * Copyright 2025, developer-krushna
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of developer-krushna nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


 *     Please contact Krushna by email mt.modder.hub@gmail.com if you need
 *     additional information or have any questions
 */

package mt.modder.hub.apkCloner;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.*;
import mt.modder.hub.apkCloner.util.*;
import mt.modder.hub.apkCloner.view.*;

/*
 * Author @developer-krushna
 * Similar concept used in MT Manager Clone App function
 * This project is already used in Modder Hub app
*/
public class MainActivity extends Activity {
	private EditText edit_path; // Input field for the file path
	private ImageView paste; // Button to paste content from the clipboard
	private Button Process; // Button to start processing the APK
	private LinearLayout main_linear; // Main layout for UI components
	private LinearLayout pkg_linear;
	private EditText edit_pkg;
	public String packageName;
	

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main); // Set the layout file for the activity
		initialize(_savedInstanceState); // Initialize the UI components

		// Check and request storage permissions
		if (Build.VERSION.SDK_INT >= 23) { 
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
				|| checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {
									   Manifest.permission.READ_EXTERNAL_STORAGE, 
									   Manifest.permission.WRITE_EXTERNAL_STORAGE
								   }, 1000);
			} else {
				initializeLogic(); // Initialize additional logic if permissions are already granted
			}
		} else {
			initializeLogic(); // Initialize additional logic for older API versions
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) { 
			initializeLogic(); // Proceed with initialization after permissions are granted
		}
	}

	private void initialize(Bundle _savedInstanceState) {
		// Find and link UI components by their IDs
		edit_path = findViewById(R.id.edit_path); 
		paste = findViewById(R.id.paste);
		Process = findViewById(R.id.Process);
		main_linear = findViewById(R.id.main_linear);
		pkg_linear = findViewById(R.id.pkg_linear);
		edit_pkg = findViewById(R.id.pkg_name);

		// Set the click listener for the paste button
		paste.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					try{
					runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String str = getClipboard(MainActivity.this);
								edit_path.setText(str); // Get text from clipboard and set it in edit_path
								packageName = getPackageName(str);
								edit_pkg.setText(changeEndCharacter(packageName));
								edit_pkg.setEnabled(true);
							}
						});
					} catch(Exception e){
						edit_pkg.setEnabled(false);
					}
				}
			});

		// Set the click listener for the Process button
		Process.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					if(!edit_pkg.getText().toString().equals(packageName) && !edit_pkg.getText().toString().isEmpty()){
						start(MainActivity.this, edit_path.getText().toString()); // Start processing the APK
					}
				}
			});
	}

	private void initializeLogic() {
		// Set ripple effect on the paste button
		ripple(paste, "#b2dfdb");
		// Apply rounded corners and a border to the main layout
		RoundAndBorder(main_linear, "#FFFFFF", 3, "#F4386D", 8);
		RoundAndBorder(pkg_linear, "#FFFFFF", 3, "#F4386D", 8);
	}
	
	// Change end character
	public String changeEndCharacter(final String text) {
		if (text.length() > 0) {
			char lastChar = text.charAt(text.length() - 1);
			if (Character.isLowerCase(lastChar)) {
				String alphabet = "abcdefghijklmnopqrstuvwxyz";
				int replacementIndex = alphabet.indexOf(Character.toLowerCase(lastChar)) + 1;
				char replacementLetter = alphabet.charAt(replacementIndex % 26);
				return (text.substring(0, (text.length() - 1)) + replacementLetter);
			}
			if (Character.isDigit(lastChar)) {
				int lastDigit = Character.getNumericValue(lastChar);
				int nextDigit = (lastDigit + 1) % 10;
				return (text.substring((0), (text.length() - 1)) + nextDigit);
			}
			if (Character.isUpperCase(lastChar)) {
				String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				int replacementIndex = alphabet.indexOf(Character.toUpperCase(lastChar)) + 1;
				char replacementLetter = alphabet.charAt(replacementIndex % 26);
				return (text.substring((0), (text.length() - 1)) + replacementLetter);
			}
		}
		return (text);
	}

	public void RoundAndBorder(final View view, final String color1, final double border, final String color2, final double round) {
		// Create a GradientDrawable to set background with rounded corners and a border
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		gd.setColor(Color.parseColor(color1));
		gd.setCornerRadius((int) round);
		gd.setStroke((int) border, Color.parseColor(color2));
		view.setBackground(gd);
	}

	public void ripple(View _view, String _c) {
		// Apply ripple effect on a view
		ColorStateList clr = new ColorStateList(
			new int[][]{new int[]{}},
			new int[]{Color.parseColor(_c)}
		); 
		RippleDrawable ripdr = new RippleDrawable(clr, null, null); 
		_view.setBackground(ripdr);
	}

    public static String getClipboard(Context context) {
        try {
            // Retrieve text from the clipboard
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData primaryClip = clipboard.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                return primaryClip.getItemAt(0).coerceToText(context).toString();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle clipboard exceptions
        }
        return "";
    }
	
	public void start(final Activity activity, final String path) {
		// Create and show a progress dialog
		final AlertProgress progressDialog = new AlertProgress(activity);
		progressDialog.setTitle(getString(R.string.app_name));
		progressDialog.setMessage("Processing...");
		progressDialog.setIndeterminate(false);
		progressDialog.show();

		// Handler to update UI after background task completes
		final Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Reset screen-on flag
							progressDialog.dismiss(); // Dismiss the progress dialog
						}
					});
			}
		};

		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Prevent screen from sleeping
		new Thread() {
			public void run() {
				Looper.prepare();
				String srcApk = path; // Get the APK path from the input field
				String newPkg = edit_pkg.getText().toString();
				try {
					// Process the APK file using DocumentInjector
					ApkCloner apkCloner = new ApkCloner(MainActivity.this, new ApkCloner.ApkClonerCallBack() {
							@Override
							public void onMessage(final String msg) {
								activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											progressDialog.setMessage(msg); // Update progress message
										}
									});
							}

							@Override
							public void onProgress(final int progress, final int total) {
								activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											progressDialog.setProgress(progress, total); // Update progress percentage
										}
									});
							}
						});
					apkCloner.setPath(srcApk, packageName, newPkg); // Set the APK file path
					apkCloner.ProcessApk(); // Start APK processing
					Toast.makeText(activity, "Success, Sign it yourself", Toast.LENGTH_SHORT).show(); // Show success message
				} catch (Exception e) {
					showError(activity, e); // Handle errors
				}
				mHandler.sendEmptyMessage(0); // Notify the handler to dismiss the progress dialog
				Looper.loop();
			} 
		}.start();
	}


	public void showError(Context context, Exception e){
		try {
			final AlertDialog.Builder dlg = new AlertDialog.Builder(context);
			dlg.setTitle("Error");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String exceptionDetails = sw.toString();
			dlg.setMessage(exceptionDetails);
			dlg.setPositiveButton("Cancel", null);

			// Setting custom background
			int cornerRadius = 20;
			android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
			gd.setColor(Color.parseColor("#FFFFFF"));
			gd.setCornerRadius(cornerRadius);

			final AlertDialog alert = dlg.create();

			// Set dialog width based on screen width percentage
			WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
			layoutParams.copyFrom(alert.getWindow().getAttributes());
			layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8); // 80% of screen width

			alert.getWindow().setBackgroundDrawable(gd);

			alert.show();

			final TextView message = alert.findViewById(android.R.id.message);
			message.setTextIsSelectable(true);
		} catch (WindowManager.BadTokenException e2) {
			e2.printStackTrace();
		}
	}
	
	public  String getPackageName(String apkPath) {
		try{
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_META_DATA);
			if (packageInfo != null) {
				return packageInfo.packageName;
			} else {
				return null;
			}
		} catch(Exception e){
			return null;
		}
	}
	
}
