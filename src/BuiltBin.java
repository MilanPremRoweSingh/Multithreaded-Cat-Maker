

public class BuiltBin
{
	public volatile int totalItems;
	boolean isPrimitive;
	
	BuiltBin( boolean _isPrimitive )
	{
		isPrimitive = _isPrimitive;
		
		if ( isPrimitive )
			totalItems = Integer.MAX_VALUE;
		else 
			totalItems = 0;
	}
	
	public synchronized void getItem()
	{
		if ( isPrimitive )
			return; 
		else
		{
			while( totalItems <= 0 ) //Note this is not spinning because 
			{
				try
				{
					this.wait();
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
			}
			totalItems--;
		}
	}
	
	public synchronized void addItem()
	{
		if ( isPrimitive ) //Shouldnt be called on primitive, but escape just to be sure
			return; 
		else
		{
			totalItems++;
			this.notify();
		}
	}
	
	public synchronized int getTotalItems()
	{
		return totalItems;
	}
}
