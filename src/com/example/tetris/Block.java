package com.example.tetris;

/**
 * ������
 * 
 * @author lancaiwu
 * 
 */
public class Block {
	private int x = 0;// ����x
	private int y = 0;// ����y
	private int type = 0;//�������� 0--������ 1-- 7�� 2--T�� 3--Z�� 4--1������  5--��7��  6--��Z��
	public int imageW = 40;// ͼƬ��
	public int imageh = 40;// ͼƬ��
	private boolean is = true;// �ж��Ƿ���ײ�
	private int direction = 0;// ���鷽�� 0--�� 1--�� 2--�� 3--��
	public int blockPos[][];// row*col�����淽��
	public int row = 0;// ���淽�����,������и���type��direction������
	public int col = 0;// ���淽�����,������и���type��direction������

	public Block(int type, int x, int y,int direction) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.direction=direction;
		init();
	}

	private void init() {
		switch (type) {
		case 0:// ������   
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
		case 1:// 7��
			switch (direction) {
			case 0:// ��
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
			case 1:// ��
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
			case 2:// ��
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
			case 3:// ��
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
		case 2:// T��
			switch (direction) {
			case 0:// ��
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
			case 1:// ��
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
			case 2:// ��
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
			case 3:// ��
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
		case 3:// Z��
			switch (direction) {
			case 0:// ��
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
			case 1:// ��
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
			case 2:// ��
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
			case 3:// ��
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
		case 4:// 1��
			switch (direction) {
			case 0:// ��
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
			case 1:// ��
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
			case 2:// ��
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
			case 3:// ��
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
		case 5:// ��7��
			switch (direction) {
			case 0:// ��
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
			case 1:// ��
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
			case 2:// ��
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
			case 3:// ��
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
		case 6:// ��Z��
			switch (direction) {
			case 0:// ��
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
			case 1:// ��
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
			case 2:// ��
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
			case 3:// ��
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
