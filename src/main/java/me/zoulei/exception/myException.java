package me.zoulei.exception;

public class myException extends Exception
{
	String reason;
	public myException(String reason) {
		this.reason=reason;
	}
	@Override
	public String getMessage()
	{
		return reason;
	}
	
}
