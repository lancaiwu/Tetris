package com.example.tetris;

/**
 * 方块类
 * 
 * @author lancaiwu
 * 
 */
public class Block {
	private int x = 0;// 坐标x
	private int y = 0;// 坐标y
	private int type = 0;//方块类型 0--正方形 1-- 7型 2--T型 3--Z型 4--1长条型  5--反7型  6--反Z型
	public int imageW = 40;// 图片宽
	public int imageh = 40;// 图片高
	private boolean is = true;// 判断是否到最底部
	private int direction = 0;// 方块方向 0--上 1--右 2--下 3--左
	public int blockPos[][];// row*col来保存方块
	public int row = 0;// 保存方块的行,方块的行根据type及direction来决定
	public int col = 0;// 保存方块的列,方块的列根据type及direction来决定

	public Block(int type, int x, int y,int direction) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.direction=direction;
		init();
	}

	private void init() {
		switch (type) {
		case 0:// 正方形   
			/**
			 *   **
			 *   **
			 */
			this.row = 2;
			this.col = 2;
			blockPos = new int[row][col];
			blockPos[0][0] = 1;
			blockPos[0][1] = 1;
			blockPos[1][0] = 1;
			blockPos[1][1] = 1;
			break;
		case 1:// 7型
			switch (direction) {
			case 0:// 上
				/**
				 *   **
				 *    *
				 *    *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[1][1] = 1;
				blockPos[2][1] = 1;
				break;
			case 1:// 右
				/**
				 *     *   
				 *   *** 
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[1][2] = 1;
				blockPos[0][2] = 1;
				break;
			case 2:// 下
				/**
				 *   *   
				 *   *
				 *   **
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[2][0] = 1;
				blockPos[2][1] = 1;
				break;
			case 3:// 左
				/**
				 *   ***
				 *   *
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[1][0] = 1;
				break;
			}
			break;
		case 2:// T型
			switch (direction) {
			case 0:// 上
				/**
				 *   ***
				 *    *
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[1][1] = 1;
				break;
			case 1:// 右
				/**
				 *    *   
				 *   ** 
				 *    *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][1] = 1;
				blockPos[1][1] = 1;
				blockPos[2][1] = 1;
				blockPos[1][0] = 1;
				break;
			case 2:// 下
				/**
				 *   *
				 *  ***
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[1][2] = 1;
				blockPos[0][1] = 1;
				break;
			case 3:// 左
				/**
				 *   *
				 *   **
				 *   *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[2][0] = 1;
				break;
			}
			break;
		case 3:// Z型
			switch (direction) {
			case 0:// 上
				/**
				 *   **
				 *    **
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[1][1] = 1;
				blockPos[1][2] = 1;
				break;
			case 1:// 右
				/**
				 *    *   
				 *   ** 
				 *   *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][1] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[2][0] = 1;
				break;
			case 2:// 下
				/**
				 *   **
				 *    **
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[1][1] = 1;
				blockPos[1][2] = 1;
				break;
			case 3:// 左
				/**
				 *    *   
				 *   ** 
				 *   *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][1] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[2][0] = 1;
				break;
			}
			break;
		case 4:// 1型
			switch (direction) {
			case 0:// 上
				/**
				 *   *
				 *   *
				 *   *
				 *   *
				 */
				this.row = 4;
				this.col = 1;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[2][0] = 1;
				blockPos[3][0] = 1;
				break;
			case 1:// 右
				/**
				 *   ****
				 */
				this.row = 1;
				this.col = 4;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[0][3] = 1;
				break;
			case 2:// 下
				/**
				 *   *
				 *   *
				 *   *
				 *   *
				 */
				this.row = 4;
				this.col = 1;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[2][0] = 1;
				blockPos[3][0] = 1;
				break;
			case 3:// 左
				/**
				 *   ****
				 */
				this.row = 1;
				this.col = 4;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[0][3] = 1;
				break;
			}
			break;
		case 5:// 反7型
			switch (direction) {
			case 0:// 上
				/**
				 *    **
				 *    *
				 *    *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[2][0] = 1;
				blockPos[0][1] = 1;
				break;
			case 1:// 右
				/**
				 *   *** 
				 *     *
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[1][2] = 1;
				break;
			case 2:// 下
				/**
				 *   *   
				 *   *
				 *  **
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][1] = 1;
				blockPos[1][1] = 1;
				blockPos[2][1] = 1;
				blockPos[2][0] = 1;
				break;
			case 3:// 左
				/**
				 *   *
				 *   ***
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[1][2] = 1;
				break;
			}
			break;
		case 6:// 反Z型
			switch (direction) {
			case 0:// 上
				/**
				 *     **
				 *    **
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				break;
			case 1:// 右
				/**
				 *   *   
				 *   ** 
				 *    *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[2][1] = 1;
				break;
			case 2:// 下
				/**
				 *     **
				 *    **
				 */
				this.row = 2;
				this.col = 3;
				blockPos = new int[row][col];
				blockPos[0][1] = 1;
				blockPos[0][2] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				break;
			case 3:// 左
				/**
				 *   *   
				 *   ** 
				 *    *
				 */
				this.row = 3;
				this.col = 2;
				blockPos = new int[row][col];
				blockPos[0][0] = 1;
				blockPos[1][0] = 1;
				blockPos[1][1] = 1;
				blockPos[2][1] = 1;
				break;
			}
			break;
		}
	}

	public void move() {
		y += 40;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isIs() {
		return is;
	}

	public void setIs(boolean is) {
		this.is = is;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
		init();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
