package com.example.speedyil;

public class Ap {
	private int level;
	private String mac;
	public Ap(String m, int l)
	{
		this.mac = m;
		this.level = l;
	}
	public String getMac()
	{
		return mac;
	}
	public int getLevel()
	{
		return level;
	}
}
