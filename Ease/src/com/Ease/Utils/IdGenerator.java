package com.Ease.Utils;

public class IdGenerator {
	int max_id;
	public IdGenerator() {
		max_id = 0;
	}
	public int getNextId() {
		max_id++;
		return max_id;
	}
}
