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
 * 界面
 * 
 * @author lancaiwu
 * 
 */
public class GameView extends View implements OnTouchListener,
		android.view.GestureDetector.OnGestureListener {
	private Context context;
	private int w;// 屏幕宽度
	private int h;// 屏幕高度
	private int statusHeight;// 通知栏高度
	private int gameHeight;// 游戏界面高度
	private int imageWith = 40;// 图片宽度
	private int imageHeight = 40;// 图片高度
	private int row = 24;// 行数
	private int col = 10;// 列数
	private int pos[][];
	private Bitmap image;// 游戏方块
	private Bitmap bg;// 背景方块
	private Block block;// 下落的方块
	
	// 预显示即将到来的方块
	private int x;// 初始游戏界面原点x
	private int y;// 初始游戏界面原点y
	private int blockStartX;// 方块初始位置Y

	private GestureDetector gestureDetector;
	private int speed = 1000;// 下降速度
	private int leve = 1;// 等级
	private int score = 0;// 分数 ，
	private boolean gameOver = false;// 游戏结束标志
	private boolean isPause = false;// 游戏暂停标志
	private int speedFlag = 0;// 下滑时速度变快的标志，当0时则下降速度正常，非0则下降速度变快
	private SoundPool pool;// 音效
	private MediaPlayer bgMusic;//背景音效
	private HashMap<String, Integer> musicMap;// 存放音效数组

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
	 * 开始游戏
	 */
	private void playGame() {
		gameOver = false;
		pos = new int[row][col];// 默认初始化为0
		CreateBlock();// 创建一个方块
		new BlockMoveThread().start(); // 方块开始移动
	}

	/**
	 * 创建一个新的方块
	 */
	private void CreateBlock() {
		Random rd = new Random();
		int blockType = rd.nextInt(7);// 随机产生方块
		int blockDirection = rd.nextInt(4);// 随机产生方块的方向
		block = new Block(blockType, blockStartX, y, blockDirection);
		try {
			moveBlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动后的方块复制到游戏界面
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
	 * 清除方块移动前的方块
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
	 * 数据初始化
	 */
	private void init() {
		// 资源初始化
		image = BitmapFactory.decodeResource(getResources(), R.drawable.block);
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);// 最大音频数目10，声音品质5

		musicMap = new HashMap<String, Integer>();
		musicMap.put("clear", pool.load(context, R.raw.clear, 4));// 加载消除声音
		musicMap.put("choose", pool.load(context, R.raw.choose, 3));// 加载选择方块声音
		musicMap.put("lose", pool.load(context, R.raw.lose, 2));// 加载游戏失败声音
		
		bgMusic=MediaPlayer.create(context, R.raw.bg);//加载资源
		bgMusic.setLooping(true);//设置单曲循环
		try {
			bgMusic.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					bgMusic.start();//播放
				}
			});
			
			bgMusic.prepareAsync();//准备
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		gestureDetector = new GestureDetector(context, this);
		this.setOnTouchListener(this);
		
		// 坐标数据初始化
		gameHeight = h - statusHeight;
		y = gameHeight / 2 - imageHeight * (row / 2);// 游戏初始坐标
		x = w / 2 - imageWith * (col / 2);// 游戏初始坐标
		blockStartX = x + imageWith * (col / 2);// 方块初始化位置Y
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 绘制 游戏界面
		for (int i = 4; i < row; i++) {// 从第5行起
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

		// 绘制 数据(分数+等级)显示界面
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(40);
		canvas.drawText("等级:" + leve, 0, statusHeight, paint);
		canvas.drawText("分数:" + score, 0, statusHeight * 3, paint);
	}

	
	/**
	 * 方块下落的线程
	 * 
	 * @author lancaiwu
	 * 
	 */
	class BlockMoveThread extends Thread {
		@Override
		public void run() {
			while (!gameOver) {
				// 判断是否到了pos数组的最大范围，也就是游戏界面的最底部或者到达了底部不能继续下降的位置了
				if (((block.getY() - y) / imageHeight + block.row) >= row
						|| isBottom()) {
					isClean();// 判断是否有消除行
					speedFlag = 0;// 还原下降速度
					CreateBlock();// 创建下一个方块
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

					GameView.this.postInvalidate();// 刷新屏幕
					while (isPause) {// 游戏暂停
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
		float minMove = 120; // 最小滑动距离
		float minVelocity = 0; // 最小滑动速度
		float beginX = e1.getX();
		float endX = e2.getX();
		float beginY = e1.getY();
		float endY = e2.getY();

		// 先清除界面上的方块
		clearBlock();

		if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) { // 左滑
			Log.i("左滑", "左滑");
			if (block.getX() > x && !isLeft()) { // 当只有没有到达最左边和左边没有方块的时候
				block.setX(block.getX() - imageWith);
			}
		} else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) { // 右滑
			Log.i("右滑", "右滑");
			if (x + imageWith * col > (block.getX() + block.col * imageWith)
					&& !isRight()) {
				block.setX(block.getX() + imageWith);
			}
		} else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // 上滑
			Log.i("上滑", "上滑");
			int direction = block.getDirection() + 1 > 3 ? 0 : block
					.getDirection() + 1;
			// 创建 一个临时的方块，用于测试切换后方块是否与周围的方块有重叠，有则不切换方块，无则切换方块
			Block blockTest = new Block(block.getType(), block.getX(),
					block.getY(), direction);

			int blockTestCol = (blockTest.getX() - x) / imageWith;// 求得方块在pos的坐标x
			int blockTestRow = (blockTest.getY() - y) / imageHeight;

			boolean flag = false;// 是否重叠标志

			for (int i = 0; i < blockTest.row; i++) {
				for (int j = 0; j < blockTest.col; j++) {
					if (blockTest.blockPos[i][j] == 1) {
						if (blockTestRow + i < row && blockTestCol + j < col) {
							if (pos[blockTestRow + i][blockTestCol + j] == 1) {
								flag = true;
								break;// 有重叠
							}
						}
					}
				}
				if (flag) {
					break;// 有重叠
				}
			}

			if (!flag) { // 没有重叠
				block.setDirection(direction);
			}

			pool.play(musicMap.get("choose"), 1, 1, 0, 0, 1);

		} else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // 下滑
			Log.i("下滑", "下滑");
			// 方块快速下降20格
			speedFlag = 20;
		}

		// 将移动后的方块显示在界面，由于上划之后会有时会出现数组越界异常，所以多层异常抛出
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

		GameView.this.postInvalidate();// 刷新界面

		// 判断是否到了pos数组的最大范围，也就是游戏界面的最底部或者到达了底部不能继续下降的位置了
		if (((block.getY() - y) / imageHeight + block.row) >= row || isBottom()) {
			isClean();// 判断是否有消除行
		}

		return true;
	}

	/**
	 * 判断是否有消除
	 */
	private void isClean() {
		for (int i = 4; i < row; i++) {
			boolean isClean = true;// 判断是否有消除
			for (int j = 0; j < col; j++) {
				if (pos[i][j] != 1) {
					isClean = false;
					break;
				}
			}
			if (isClean) {// 有消除行
				try {
					// 等待0.5秒再消除
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
			pos[rowNum][j] = 0;// 先把消除行清除
		}
		for (int i = rowNum; i > 4; i--) {
			for (int j = 0; j < col; j++) {
				pos[i][j] = pos[i - 1][j];
			}
		}
		score++;
		if (score >= leve * 10) { // 每当消除次数到达leve*10次，就升级一次
			if (leve <= 6) {// 最高6级
				leve++;
			}
			score = 0;
		}
		pool.play(musicMap.get("clear"), 1, 1, 0, 0, 1);
		GameView.this.postInvalidate();// 刷新屏幕
	}

	/**
	 * 判断是否到了底部
	 * 
	 * @return
	 */
	private boolean isBottom() {
		int blockCol = (block.getX() - x) / imageWith;// 求得方块在pos的坐标x
		int blockRow = (block.getY() - y) / imageHeight;

		// 从每列开始查询
		for (int j = block.col - 1; j >= 0; j--) {
			for (int i = block.row - 1; i >= 0; i--) {
				if (block.blockPos[i][j] == 1) {
					// 判断方块下一格是否有方块
					if (pos[blockRow + i + 1][blockCol + j] == 1) {
						gameOver = isGameOve(blockRow);
						return true;// 到达底部
					}
					break;// 当第一个小方块出现时，如果他的下一格方格没有出现方块时，则检查下一列小方格
				}
			}
		}
		return false;// 没有到达底部
	}

	/**
	 * 判断是否到了可以移动到的最左边，也就是判断左边是否有方块
	 * 
	 * @return
	 */
	private boolean isLeft() {
		int blockCol = (block.getX() - x) / imageWith;// 求得方块在pos的坐标x
		int blockRow = (block.getY() - y) / imageHeight;

		// 从最左边的那列开始按列查询,判断左边是否有方块
		for (int j = 0; j < block.col; j++) {
			for (int i = 0; i < block.row; i++) {
				if (block.blockPos[i][j] == 1) {
					if (pos[i + blockRow][j + blockCol - 1] == 1) {
						return true;// 左边有方块
					}
				}

			}
		}
		return false;// 左边没有方块
	}

	/**
	 * 判断是否右边有方块
	 * 
	 * @return
	 */
	private boolean isRight() {
		int blockCol = (block.getX() - x) / imageWith;// 求得方块在pos的坐标x
		int blockRow = (block.getY() - y) / imageHeight;

		for (int i = block.col - 1; i >= 0; i--) {// 从最右边列开始查询
			for (int j = 0; j < block.row; j++) {
				if (block.blockPos[j][i] == 1) {
					if (pos[blockRow + j][blockCol + i + 1] == 1) {
						return true;// 右边有方块
					}
				}
			}
		}

		return false;// 右边没方块
	}

	/**
	 * 判断是否游戏结束
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
			// 游戏结束
			if (msg.what == 1) {
				bgMusic.pause();//暂停背景音乐
				
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("Game Over");
				builder.setMessage("是否重新开始游戏？");
				builder.setNegativeButton("退出游戏",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								System.exit(0);// 退出程序
							}
						});
				builder.setPositiveButton("重新开始游戏",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								playGame();// 重新开始游戏
								bgMusic.start();
							}
						});
				builder.create().show();// 创建并显示对话框
			}

			// 游戏暂停
			if (msg.what == 2) {
				bgMusic.pause();//暂停背景音乐
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("Game pause");
				builder.setMessage("是否退出游戏？");
				builder.setNegativeButton("退出游戏",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								gameOver = true;
								System.exit(0);// 退出程序
							}
						});
				builder.setPositiveButton("返回游戏",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								isPause = false;
								bgMusic.start();
							}
						});
				builder.create().show();// 创建并显示对话框
				
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isPause = true;// 游戏暂停
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	// 设置游戏暂停
	public void setPause(boolean isPause) {
		this.isPause = isPause;
		if(this.isPause){
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
		}
	};
	
}