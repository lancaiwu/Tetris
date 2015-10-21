package com.example.tetris;

import java.util.HashMap;
import java.util.Random;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * ����
 * 
 * @author lancaiwu
 * 
 */
public class GameView extends View implements OnTouchListener,
		android.view.GestureDetector.OnGestureListener {
	private Context context;
	private int w;// ��Ļ���
	private int h;// ��Ļ�߶�
	private int statusHeight;// ֪ͨ���߶�
	private int gameHeight;// ��Ϸ����߶�
	private int imageWith = 40;// ͼƬ���
	private int imageHeight = 40;// ͼƬ�߶�
	private int row = 24;// ����
	private int col = 10;// ����
	private int pos[][];
	private Bitmap image;// ��Ϸ����
	private Bitmap bg;// ��������
	private Block block;// ����ķ���
	
	// Ԥ��ʾ���������ķ���
	private int x;// ��ʼ��Ϸ����ԭ��x
	private int y;// ��ʼ��Ϸ����ԭ��y
	private int blockStartX;// �����ʼλ��Y

	private GestureDetector gestureDetector;
	private int speed = 1000;// �½��ٶ�
	private int leve = 1;// �ȼ�
	private int score = 0;// ���� ��
	private boolean gameOver = false;// ��Ϸ������־
	private boolean isPause = false;// ��Ϸ��ͣ��־
	private int speedFlag = 0;// �»�ʱ�ٶȱ��ı�־����0ʱ���½��ٶ���������0���½��ٶȱ��
	private SoundPool pool;// ��Ч
	private MediaPlayer bgMusic;//������Ч
	private HashMap<String, Integer> musicMap;// �����Ч����

	public GameView(Context context, int w, int h, int statusHeight) {
		super(context);
		this.context = context;
		this.w = w;
		this.h = h;
		this.statusHeight = statusHeight;

		init();
		playGame();
	}

	/**
	 * ��ʼ��Ϸ
	 */
	private void playGame() {
		gameOver = false;
		pos = new int[row][col];// Ĭ�ϳ�ʼ��Ϊ0
		CreateBlock();// ����һ������
		new BlockMoveThread().start(); // ���鿪ʼ�ƶ�
	}

	/**
	 * ����һ���µķ���
	 */
	private void CreateBlock() {
		Random rd = new Random();
		int blockType = rd.nextInt(7);// �����������
		int blockDirection = rd.nextInt(4);// �����������ķ���
		block = new Block(blockType, blockStartX, y, blockDirection);
		try {
			moveBlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ƶ���ķ��鸴�Ƶ���Ϸ����
	 */
	private void moveBlock() throws Exception {

		int blockCol = (block.getX() - x) / imageWith;
		int blockRow = (block.getY() - y) / imageHeight;

		for (int i = 0; i < block.row; i++) {
			for (int j = 0; j < block.col; j++) {
				if (block.blockPos[i][j] == 1) {
					pos[blockRow + i][blockCol + j] = 1;
				}
			}
		}
	}

	/**
	 * ��������ƶ�ǰ�ķ���
	 */
	private void clearBlock() {

		int blockCol = (block.getX() - x) / imageWith;
		int blockRow = (block.getY() - y) / imageHeight;

		for (int i = 0; i < block.row; i++) {
			for (int j = 0; j < block.col; j++) {
				if (block.blockPos[i][j] == 1) {
					pos[blockRow + i][blockCol + j] = 0;
				}
			}
		}
	}

	/**
	 * ���ݳ�ʼ��
	 */
	private void init() {
		// ��Դ��ʼ��
		image = BitmapFactory.decodeResource(getResources(), R.drawable.block);
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);// �����Ƶ��Ŀ10������Ʒ��5

		musicMap = new HashMap<String, Integer>();
		musicMap.put("clear", pool.load(context, R.raw.clear, 4));// ������������
		musicMap.put("choose", pool.load(context, R.raw.choose, 3));// ����ѡ�񷽿�����
		musicMap.put("lose", pool.load(context, R.raw.lose, 2));// ������Ϸʧ������
		
		bgMusic=MediaPlayer.create(context, R.raw.bg);//������Դ
		bgMusic.setLooping(true);//���õ���ѭ��
		try {
			bgMusic.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					bgMusic.start();//����
				}
			});
			
			bgMusic.prepareAsync();//׼��
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		gestureDetector = new GestureDetector(context, this);
		this.setOnTouchListener(this);
		
		// �������ݳ�ʼ��
		gameHeight = h - statusHeight;
		y = gameHeight / 2 - imageHeight * (row / 2);// ��Ϸ��ʼ����
		x = w / 2 - imageWith * (col / 2);// ��Ϸ��ʼ����
		blockStartX = x + imageWith * (col / 2);// �����ʼ��λ��Y
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// ���� ��Ϸ����
		for (int i = 4; i < row; i++) {// �ӵ�5����
			for (int j = 0; j < col; j++) {
				if (pos[i][j] == 1) {
					canvas.drawBitmap(image, x + j * imageWith, y + i
							* imageHeight, null);
				} else {
					canvas.drawBitmap(bg, x + j * imageWith, y + i
							* imageHeight, null);
				}
			}
		}

		// ���� ����(����+�ȼ�)��ʾ����
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(40);
		canvas.drawText("�ȼ�:" + leve, 0, statusHeight, paint);
		canvas.drawText("����:" + score, 0, statusHeight * 3, paint);
	}

	
	/**
	 * ����������߳�
	 * 
	 * @author lancaiwu
	 * 
	 */
	class BlockMoveThread extends Thread {
		@Override
		public void run() {
			while (!gameOver) {
				// �ж��Ƿ���pos��������Χ��Ҳ������Ϸ�������ײ����ߵ����˵ײ����ܼ����½���λ����
				if (((block.getY() - y) / imageHeight + block.row) >= row
						|| isBottom()) {
					isClean();// �ж��Ƿ���������
					speedFlag = 0;// ��ԭ�½��ٶ�
					CreateBlock();// ������һ������
				} else {
					clearBlock();
					block.move();
					try {
						moveBlock();
					} catch (Exception e) {
						block.setX(block.getX() - imageWith);
						try {
							moveBlock();
						} catch (Exception e3) {
							block.setX(block.getX() - imageWith);
							try {
								moveBlock();
							} catch (Exception e4) {
								// TODO Auto-generated catch block
								e4.printStackTrace();
							}
						}
					}

					GameView.this.postInvalidate();// ˢ����Ļ
					while (isPause) {// ��Ϸ��ͣ
						if (gameOver) {
							break;
						}
					}
				}
				try {
					if (speedFlag == 0) {
						sleep(speed - leve * 100);
					} else {
						sleep(30);
						speedFlag--;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			pool.play(musicMap.get("lose"), 1, 1, 0, 0, 1);

			Message msg = handler.obtainMessage();
			msg.what = 1;
			handler.sendMessage(msg);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float minMove = 120; // ��С��������
		float minVelocity = 0; // ��С�����ٶ�
		float beginX = e1.getX();
		float endX = e2.getX();
		float beginY = e1.getY();
		float endY = e2.getY();

		// ����������ϵķ���
		clearBlock();

		if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) { // ��
			Log.i("��", "��");
			if (block.getX() > x && !isLeft()) { // ��ֻ��û�е�������ߺ����û�з����ʱ��
				block.setX(block.getX() - imageWith);
			}
		} else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) { // �һ�
			Log.i("�һ�", "�һ�");
			if (x + imageWith * col > (block.getX() + block.col * imageWith)
					&& !isRight()) {
				block.setX(block.getX() + imageWith);
			}
		} else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // �ϻ�
			Log.i("�ϻ�", "�ϻ�");
			int direction = block.getDirection() + 1 > 3 ? 0 : block
					.getDirection() + 1;
			// ���� һ����ʱ�ķ��飬���ڲ����л��󷽿��Ƿ�����Χ�ķ������ص��������л����飬�����л�����
			Block blockTest = new Block(block.getType(), block.getX(),
					block.getY(), direction);

			int blockTestCol = (blockTest.getX() - x) / imageWith;// ��÷�����pos������x
			int blockTestRow = (blockTest.getY() - y) / imageHeight;

			boolean flag = false;// �Ƿ��ص���־

			for (int i = 0; i < blockTest.row; i++) {
				for (int j = 0; j < blockTest.col; j++) {
					if (blockTest.blockPos[i][j] == 1) {
						if (blockTestRow + i < row && blockTestCol + j < col) {
							if (pos[blockTestRow + i][blockTestCol + j] == 1) {
								flag = true;
								break;// ���ص�
							}
						}
					}
				}
				if (flag) {
					break;// ���ص�
				}
			}

			if (!flag) { // û���ص�
				block.setDirection(direction);
			}

			pool.play(musicMap.get("choose"), 1, 1, 0, 0, 1);

		} else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // �»�
			Log.i("�»�", "�»�");
			// ��������½�20��
			speedFlag = 20;
		}

		// ���ƶ���ķ�����ʾ�ڽ��棬�����ϻ�֮�����ʱ���������Խ���쳣�����Զ���쳣�׳�
		try {
			moveBlock();
		} catch (Exception e) {
			block.setX(block.getX() - imageWith);
			try {
				moveBlock();
			} catch (Exception e3) {
				block.setX(block.getX() - imageWith);
				try {
					moveBlock();
				} catch (Exception e4) {
					block.setX(block.getX() - imageWith);
					try {
						moveBlock();
					} catch (Exception e5) {
						// TODO Auto-generated catch block
						e5.printStackTrace();
					}
				}
			}
		}

		GameView.this.postInvalidate();// ˢ�½���

		// �ж��Ƿ���pos��������Χ��Ҳ������Ϸ�������ײ����ߵ����˵ײ����ܼ����½���λ����
		if (((block.getY() - y) / imageHeight + block.row) >= row || isBottom()) {
			isClean();// �ж��Ƿ���������
		}

		return true;
	}

	/**
	 * �ж��Ƿ�������
	 */
	private void isClean() {
		for (int i = 4; i < row; i++) {
			boolean isClean = true;// �ж��Ƿ�������
			for (int j = 0; j < col; j++) {
				if (pos[i][j] != 1) {
					isClean = false;
					break;
				}
			}
			if (isClean) {// ��������
				try {
					// �ȴ�0.5��������
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				clean(i);
			}
		}

	}

	private void clean(int rowNum) {
		for (int j = 0; j < col; j++) {
			pos[rowNum][j] = 0;// �Ȱ����������
		}
		for (int i = rowNum; i > 4; i--) {
			for (int j = 0; j < col; j++) {
				pos[i][j] = pos[i - 1][j];
			}
		}
		score++;
		if (score >= leve * 10) { // ÿ��������������leve*10�Σ�������һ��
			if (leve <= 6) {// ���6��
				leve++;
			}
			score = 0;
		}
		pool.play(musicMap.get("clear"), 1, 1, 0, 0, 1);
		GameView.this.postInvalidate();// ˢ����Ļ
	}

	/**
	 * �ж��Ƿ��˵ײ�
	 * 
	 * @return
	 */
	private boolean isBottom() {
		int blockCol = (block.getX() - x) / imageWith;// ��÷�����pos������x
		int blockRow = (block.getY() - y) / imageHeight;

		// ��ÿ�п�ʼ��ѯ
		for (int j = block.col - 1; j >= 0; j--) {
			for (int i = block.row - 1; i >= 0; i--) {
				if (block.blockPos[i][j] == 1) {
					// �жϷ�����һ���Ƿ��з���
					if (pos[blockRow + i + 1][blockCol + j] == 1) {
						gameOver = isGameOve(blockRow);
						return true;// ����ײ�
					}
					break;// ����һ��С�������ʱ�����������һ�񷽸�û�г��ַ���ʱ��������һ��С����
				}
			}
		}
		return false;// û�е���ײ�
	}

	/**
	 * �ж��Ƿ��˿����ƶ���������ߣ�Ҳ�����ж�����Ƿ��з���
	 * 
	 * @return
	 */
	private boolean isLeft() {
		int blockCol = (block.getX() - x) / imageWith;// ��÷�����pos������x
		int blockRow = (block.getY() - y) / imageHeight;

		// ������ߵ����п�ʼ���в�ѯ,�ж�����Ƿ��з���
		for (int j = 0; j < block.col; j++) {
			for (int i = 0; i < block.row; i++) {
				if (block.blockPos[i][j] == 1) {
					if (pos[i + blockRow][j + blockCol - 1] == 1) {
						return true;// ����з���
					}
				}

			}
		}
		return false;// ���û�з���
	}

	/**
	 * �ж��Ƿ��ұ��з���
	 * 
	 * @return
	 */
	private boolean isRight() {
		int blockCol = (block.getX() - x) / imageWith;// ��÷�����pos������x
		int blockRow = (block.getY() - y) / imageHeight;

		for (int i = block.col - 1; i >= 0; i--) {// �����ұ��п�ʼ��ѯ
			for (int j = 0; j < block.row; j++) {
				if (block.blockPos[j][i] == 1) {
					if (pos[blockRow + j][blockCol + i + 1] == 1) {
						return true;// �ұ��з���
					}
				}
			}
		}

		return false;// �ұ�û����
	}

	/**
	 * �ж��Ƿ���Ϸ����
	 * 
	 * @return
	 */
	private boolean isGameOve(int blockRow) {
		if (blockRow <= 4) {
			return true;
		}
		return false;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ��Ϸ����
			if (msg.what == 1) {
				bgMusic.pause();//��ͣ��������
				
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("Game Over");
				builder.setMessage("�Ƿ����¿�ʼ��Ϸ��");
				builder.setNegativeButton("�˳���Ϸ",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								System.exit(0);// �˳�����
							}
						});
				builder.setPositiveButton("���¿�ʼ��Ϸ",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								playGame();// ���¿�ʼ��Ϸ
								bgMusic.start();
							}
						});
				builder.create().show();// ��������ʾ�Ի���
			}

			// ��Ϸ��ͣ
			if (msg.what == 2) {
				bgMusic.pause();//��ͣ��������
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("Game pause");
				builder.setMessage("�Ƿ��˳���Ϸ��");
				builder.setNegativeButton("�˳���Ϸ",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								gameOver = true;
								System.exit(0);// �˳�����
							}
						});
				builder.setPositiveButton("������Ϸ",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								isPause = false;
								bgMusic.start();
							}
						});
				builder.create().show();// ��������ʾ�Ի���
				
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isPause = true;// ��Ϸ��ͣ
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	// ������Ϸ��ͣ
	public void setPause(boolean isPause) {
		this.isPause = isPause;
		if(this.isPause){
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
		}
	};
	
}