

public class catmaker
{

	public static void main( String[] args )
	{
		catmakerMonitorSolution catmakerM = new catmakerMonitorSolution();
		long monitorIdleTime = catmakerM.solve();
		
		catmakerSemaphoreSolution catmakerS = new catmakerSemaphoreSolution();
		long sempahoreIdleTime = catmakerS.solve();
		
		System.out.println( "Monitor Solution Idle Time: " + monitorIdleTime + "ms" );
		System.out.println( "Semaphore Solution Idle Time: " + sempahoreIdleTime + "ms" );
	}

}
