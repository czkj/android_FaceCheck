package com.hl.takepicture;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import tool.FileUtil;
import tool.FtpUtil;
import tool.NetUtil;
import tool.SDCardUtil;
import tool.StaticParameter;
import tool.UtilsSendStr;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TakPicActivity extends Activity {
	private Camera camera;
	FileUtil fileUtil;
	private static String commFilePath = "";
	private ProgressBar pb;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ��Ϊ����
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//����
		setContentView(R.layout.layout_takepic);
		
		pb = (ProgressBar) this.findViewById(R.id.wait_bar);
		pb.setVisibility(View.GONE);
		SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setFixedSize(176, 144);
		surfaceView.getHolder().setKeepScreenOn(true);
		surfaceView.getHolder().addCallback(new SurfaceCallback());
		fileUtil = new FileUtil(this.getBaseContext());
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		System.out.println("onStart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
//		System.out.println("onRestart");
		pb = (ProgressBar) this.findViewById(R.id.wait_bar);
		pb.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @author �ص�������
	 * 
	 */
	private final class SurfaceCallback implements Callback {
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open();// ������ͷ
				Camera.Parameters parameters = camera.getParameters();
				// System.out.println(parameters.flatten());//���г����������
				parameters.setPictureFormat(PixelFormat.JPEG);// ������Ƭ�ĸ�ʽ
				parameters.setPreviewSize(800, 480);
				parameters.setPreviewFrameRate(5);
//				parameters.setPictureSize(1024, 768);
				parameters.setPictureSize(640, 480);
				parameters.setJpegQuality(80);
//				camera.setDisplayOrientation(270);
				camera.setParameters(parameters);// ��������ͷ�Ĳ���.����ǰ���������Ч
				camera.setPreviewDisplay(holder);
				camera.startPreview();// ��ʼԤ��
				camera.autoFocus(null);// ��ʼ���Խ�
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// android:screenOrientation="sensor"
			// System.out.println("surfaceChanged turn le ");
			if (camera != null) {
				
//				System.out.println(getWindowManager().getDefaultDisplay().getRotation());
//				System.out.println(Surface.ROTATION_270);
//				int rotation = getWindowManager().getDefaultDisplay().getRotation();
//				int degrees = 0;
//				switch (rotation) {
//				case Surface.ROTATION_0:
//					degrees = 0;
//					break;
//				case Surface.ROTATION_90:
//					degrees = 90;
//					break;
//				case Surface.ROTATION_180:
//					degrees = 180;
//					break;
//				case Surface.ROTATION_270:
//					degrees = 270;
//					break;
//				}
//				
//				camera.setDisplayOrientation(degrees);
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.release();
				camera = null;
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:takepicture
	 * @Full_path:com.hl.takepicture.EntranceActivity.java
	 * @Date:@2014 2014-7-28 ����11:15:48
	 * @Return_type:void
	 * @Desc : ������صİ�ť�¼�
	 */
	public void takepicture(View view) {
		switch (view.getId()) {
		case R.id.take_pic:
			camera.takePicture(null, null, new MyPictureCallback());
			break;
		case R.id.cancle_pic:
			// 1.ɾ���ļ�
			System.out.println("delFile : " + fileUtil.delFile(commFilePath));
			commFilePath = "";
			// 2.ʹ����ɼ�������
			camera.startPreview();
			break;
		case R.id.send_pic:
			// Android 4.0 ֮���������߳�������HTTP����
			// Anexception occured:android.os.NetworkOnMainThreadException
			/*
			 * new Thread(new Runnable(){
			 * 
			 * @Override public void run() { // 1.�����ļ��������� UploadImage.upload(new File(commFilePath),"http://192.168.1.112:8088/html5/UploadFileServlet");
			 * 
			 * //2.ɾ���ļ� fileUtil.delFile(commFilePath); commFilePath=""; // 3.��ʾ������� Intent intent = new Intent(); intent.setClass(TakPicActivity.this, ResultAdapterActivity.class);
			 * startActivity(intent); } }).start();
			 */
			if (NetUtil.isNetworkAvailable(getApplicationContext())) {// ����ͨ��
				
				pb.setVisibility(View.VISIBLE);
					 
				
				// 1.�����ļ���������
				// FtpUtil.ftpUpload("125.76.161.141", "21", "weixin", "weixin", "FileFromAndroid", commFilePath);
				// FtpUtil.ftpUpload("192.168.1.112", "21", "weixin", "weixin", "FileFromAndroid", "/mnt/sdcard-ext/", commFilePath);
				FtpUtil.ftpUpload(StaticParameter.FTP_IP, StaticParameter.FTP_PORT, StaticParameter.FTP_USERName, StaticParameter.FTP_USERPWD, StaticParameter.FTP_PIC_PATH, commFilePath);
				// 2. ���������ִ������ʶ����������շ��ؽ����
				String XML_URL="";
				try {
//					XML_URL = UtilsSendStr.httpPost("http://192.168.1.112:8088/html5/servletTest","UTF-8",null,new File(commFilePath).getName());
					XML_URL = UtilsSendStr.httpPost("http://"+StaticParameter.WEB_IP+":"+StaticParameter.WEB_PORT+"/"+StaticParameter.APP_NAME+"/FaceCheck","UTF-8",null,new File(commFilePath).getName());
//					System.out.println("XML_URL : "+XML_URL);
				} catch (Exception e) {
				}
				
				// 3.ɾ���ļ�
				fileUtil.delFile(commFilePath);
				commFilePath = "";
				// 4.��ʾ�������
				if("".equals(XML_URL)){
					Toast.makeText(TakPicActivity.this, R.string.no_return_result, Toast.LENGTH_LONG).show();
					pb.setVisibility(View.GONE);
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("XML_URL", XML_URL);
				intent.putExtra("ftp_dest_path", "face_check/1/");
				
				intent.setClass(TakPicActivity.this, ResultAdapterActivity.class);
				startActivity(intent);
				//pb.setVisibility(View.GONE);
			} else {
				Toast.makeText(TakPicActivity.this, R.string.net_fail, Toast.LENGTH_LONG).show();
				return;
			}
			break;
		default:
			break;
		}
	}

	@SuppressLint("SdCardPath")
	private final class MyPictureCallback implements PictureCallback {
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				// System.out.println("path : " + Environment.getExternalStorageDirectory().toString());
				if (SDCardUtil.sdCardExist()) {// ��sd�������
					// File jpgFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
					File jpgFile = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID() + ".jpg");
					FileOutputStream outStream = new FileOutputStream(jpgFile);
					outStream.write(data);
					outStream.close();
					commFilePath = jpgFile.getPath();
				} else {
					// jpgFile = new File("/mnt/sdcard-ext", System.currentTimeMillis() + ".jpg");
					// jpgFile = new File("/mnt/sdcard-ext", UUID.randomUUID() + ".jpg");
					String fileName = UUID.randomUUID() + ".jpg";
					FileUtil.save(getApplicationContext(), data, fileName);
					commFilePath = "/data/data/com.hl.takepicture/files/" + fileName;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Author:HaoMing(����)
	 * @Project_name:takepicture
	 * @Full_path:com.hl.takepicture.EntranceActivity.java
	 * @Date:@2014 2014-7-28 ����11:15:48
	 * @Return_type:void
	 * @Desc : ������Ļ�ɶԽ�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*
			 * System.out.println("����������Ļ");
			 * 
			 * if (SDCardUtil.sdCardExist()) { System.out.println("����sd������Ŀ¼�ǣ� " + SDCardUtil.sdDir()); }else{ System.out.println("no ����sd�� "); }
			 * 
			 * if (NetUtil.isNetworkAvailable(getApplicationContext())) { System.out.println("�������� ok"); }
			 * 
			 * if (NetUtil.isWifi(getApplicationContext())) { System.out.println("�ж���wifi"); } else { System.out.println("�ж���3g"); } if (NetUtil.isWifiEnabled(getApplicationContext())) {
			 * System.out.println("WIFI had ��"); }
			 * 
			 * if (NetUtil.is3rd(getApplicationContext())) { System.out.println("has 3G����"); }else{ System.out.println("no 3G����"); } if (NetUtil.isGpsEnabled(getApplicationContext())) {
			 * System.out.println("GPS had ��"); }
			 */
			camera.autoFocus(null);
			return true;
		}
		return super.onTouchEvent(event);
	}
}
