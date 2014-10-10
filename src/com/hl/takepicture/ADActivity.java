package com.hl.takepicture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tool.XMLParser;
import tool.listView.LazyAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ADActivity extends Activity {
	
	Timer timer = null;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ad);
		
		// ͣ��һ��ʱ��
		timer = new Timer();
//		timer.schedule(new TaskEntranceActivity(), 3000, 1000);
		timer.schedule(new TaskEntranceActivity(), 3000);
	}
	
	class TaskEntranceActivity extends TimerTask {
		@Override
		public void run() {
			// ҳ���л�
			Intent intent = new Intent();
			intent.setClass(ADActivity.this, EntranceActivity.class);
			// �����л�����������̬Ч��
			overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
			startActivity(intent);
//			timer.cancel();// ֹͣ��ʱ��
		}
	}
}
