package com.hl.takepicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class EntranceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.layout_entrance);
		
		
	}
	
	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:takepicture
	 * @Full_path:com.hl.takepicture.EntranceActivity.java
	 * @Date:@2014 2014-7-28 ����11:15:48
	 * @Return_type:void
	 * @Desc : �������ս���
	 */
	public void entrance(View view){
		switch (view.getId()) {
		case R.id.btn_entrance:
			// ҳ���л�
			Intent intent = new Intent();
			intent.setClass(EntranceActivity.this, TakPicActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
