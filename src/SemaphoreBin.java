import java.util.concurrent.Semaphore;

public class SemaphoreBin
{
	public Semaphore semItem;
	public Semaphore semMutex;
	boolean isPrimitive;
	
	SemaphoreBin( boolean _isPrimitive )
	{
		isPrimitive = _isPrimitive;
		semMutex = new Semaphore( 1 );
		
		if ( !isPrimitive )
			semItem = new Semaphore( 0 );
	}
	
	public void getItem()
	{		
		if ( !isPrimitive )
		{
			try
			{
				semItem.acquire();
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addItem()
	{
		if ( !isPrimitive )
		{
			semItem.release();
		}
	}
	
	public int getTotalItems()
	{
		if ( isPrimitive)
			return Integer.MAX_VALUE;
		
		return  semItem.availablePermits();
	}
	
	public void acquireMutex()
	{
		try
		{
			semMutex.acquire();
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseMutex() 
	{
		semMutex.release();
	}
}
