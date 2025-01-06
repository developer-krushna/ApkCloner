
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

package mt.modder.hub.apkCloner.view;


import android.content.Context;
import android.view.*;
import android.app.Activity;
import android.widget.*;
import android.graphics.Color;
import android.app.AlertDialog.Builder;
import android.graphics.drawable.GradientDrawable;
import android.app.*;
import mt.modder.hub.apkCloner.*;

/* 
 Author @developer-krushna
 */

public class AlertProgress {
	
	Context context;
	Activity activity;
	AlertDialog.Builder process;
	AlertDialog alert;
	TextView textview_mesage;
	TextView textview_title;
	ProgressBar progress;

	public AlertProgress(Context mContext){
		this.context = mContext;
		this.activity = (Activity)context;

		process = new Builder(activity);
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View view = View.inflate(context, R.layout.progress, null);
		textview_mesage = view.findViewById(R.id.message);
		progress = view.findViewById(R.id.progress);
		textview_title = view.findViewById(R.id.title);

		process.setCancelable(false);
		process.setView(view);

		alert = process.create();

		int cornerRadius = 20;
		GradientDrawable aa = new GradientDrawable();
		aa.setColor(Color.parseColor("#FFFFFF"));
		aa.setCornerRadius(cornerRadius);
		alert.setCancelable(false);
		alert.getWindow().setBackgroundDrawable(aa);

	}

	public void setTitle(final String title)  {

		activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					textview_title.setText(title);
				}
			});
	}
	public void setMessage(final String message){

		activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {

					textview_mesage.setText(message);

				}
			});
	}

	public void setProgress(final int value, final int max) {

		activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {

					progress.setVisibility(View.VISIBLE);
					progress.setProgress(value);
					progress.setMax(max);
				}
			});
	}
	public void setIndeterminate(final boolean bool){

		activity.runOnUiThread(new Runnable(){
				@Override
				public void run()  {
					if(bool){
						progress.setIndeterminate(true);
					}else{
						progress.setIndeterminate(false);
					}


				}
			});

	}


	public void show() {

		activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {

					alert.show();
				}
			});
	}


	public void dismiss(){

		activity.runOnUiThread(new Runnable(){
				@Override
				public void run() {

					alert.dismiss();
				}
			});
	}
}
