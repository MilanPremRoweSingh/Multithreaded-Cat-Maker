import java.util.Random;

public class catmakerSemaphoreSolution
{
	
	//Primitive Parts
	SemaphoreBin bodies, tails, legs, toes, heads, eyes, whiskers;
	
	//Composite Parts
	SemaphoreBin forelegs, hindlegs;
	SemaphoreBin tailedBodies, leggedBodies, completeBodies;
	SemaphoreBin eyedHeads, whiskeredHeads, completeHeads;
	SemaphoreBin cats;
	
	volatile boolean targetReached;
	volatile boolean legsFinished;
	
	catmakerSemaphoreSolution()
	{
		bodies 		= new SemaphoreBin( true );
		tails 		= new SemaphoreBin( true );
		legs 		= new SemaphoreBin( true );
		toes 		= new SemaphoreBin( true );
		heads 		= new SemaphoreBin( true );
		eyes 		= new SemaphoreBin( true );
		whiskers 	= new SemaphoreBin( true );
		
		forelegs		= new SemaphoreBin( false );
		hindlegs		= new SemaphoreBin( false );
		tailedBodies	= new SemaphoreBin( false );
		leggedBodies	= new SemaphoreBin( false );
		completeBodies	= new SemaphoreBin( false );
		eyedHeads		= new SemaphoreBin( false );
		whiskeredHeads	= new SemaphoreBin( false );
		completeHeads	= new SemaphoreBin( false );
		cats			= new SemaphoreBin( false );
		
		targetReached = false;
		legsFinished = false;
	}
	
	long solve() 
	{
		long wastedTime = 0l;
		long time = System.currentTimeMillis();
		ToeAttacher toeThread0 = new ToeAttacher();
		ToeAttacher toeThread1 = new ToeAttacher();
		
		LegAttacher legThread0 = new LegAttacher();
		LegAttacher legThread1 = new LegAttacher();
		
		TailAttacher tailThread0 = new TailAttacher();
		TailAttacher tailThread1 = new TailAttacher();
		
		EyeAttacher eyeThread0 = new EyeAttacher();
		EyeAttacher eyeThread1 = new EyeAttacher();

		WhiskerAttacher whiskerThread0 = new WhiskerAttacher();
		WhiskerAttacher whiskerThread1 = new WhiskerAttacher();
		
		toeThread0.start();
		toeThread1.start();
		legThread0.start();
		legThread1.start();
		tailThread0.start();
		tailThread1.start();
		eyeThread0.start();
		eyeThread1.start();
		whiskerThread0.start();
		whiskerThread1.start();
		
		while( cats.getTotalItems() < 250 )
		{
			Random rand = new Random();
			long workTime =  ( long )( rand.nextDouble()* 10 ) + 10l;
			
			long timer = System.currentTimeMillis();
			completeHeads.getItem();
			completeBodies.getItem();
			wastedTime += System.currentTimeMillis() - timer;
			
			try
			{
				Thread.sleep( workTime );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
			
			cats.addItem();
		}
		
		
		targetReached = true;
		
		try
		{
			legThread0.join();
			legThread1.join();
			
			legsFinished = true;
			
			toeThread0.join();
			toeThread1.join();
			tailThread0.join();
			tailThread1.join();
			eyeThread0.join();
			eyeThread1.join();
			whiskerThread0.join();
			whiskerThread1.join();
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}
		
		
		wastedTime += toeThread0.timeWasted;
		wastedTime += toeThread1.timeWasted;
		wastedTime += legThread0.timeWasted;
		wastedTime += legThread1.timeWasted;
		wastedTime += tailThread0.timeWasted;
		wastedTime += tailThread1.timeWasted;
		wastedTime += eyeThread0.timeWasted;
		wastedTime += eyeThread1.timeWasted;
		wastedTime += whiskerThread0.timeWasted;
		wastedTime += whiskerThread1.timeWasted;
		
		System.out.println( "Time taken: " + (System.currentTimeMillis() - time) + "ms" );
		return wastedTime;
	}
	
// Define Threads to be used
	class ToeAttacher extends Thread
	{
		public long timeWasted = 0l;
		
		public void run()
		{
			while( !legsFinished )
			{
				Random rand = new Random();
				boolean createHindleg = rand.nextBoolean();
				long workTime =  ( long )( rand.nextDouble()* 10 ) + 10l;
				long timer = 0;
				
				 //get a leg
				timer = System.currentTimeMillis();
				legs.getItem();
				timeWasted += System.currentTimeMillis() - timer;
				
				int toesHeld = 0;
				if ( createHindleg )
				{
					while( toesHeld < 4 )
					{
						timer = System.currentTimeMillis();
						toes.getItem();
						timeWasted += System.currentTimeMillis() - timer;
						toesHeld++;
					}
	
					try
					{
						sleep( workTime );
					}
					catch ( InterruptedException e )
					{
						e.printStackTrace();
					}
					
					timer = System.currentTimeMillis();
					hindlegs.addItem();
					timeWasted += System.currentTimeMillis() - timer;
				}
				else
				{
					while( toesHeld < 5 )
					{
						timer = System.currentTimeMillis();
						toes.getItem();
						timeWasted += System.currentTimeMillis() - timer;
						toesHeld++;
					}
					
					try
					{
						sleep( workTime );
					}
					catch ( InterruptedException e )
					{
						e.printStackTrace();
					}
					
					timer = System.currentTimeMillis();
					forelegs.addItem();
					timeWasted += System.currentTimeMillis() - timer;
				}
			}
		}
	}

	class LegAttacher extends Thread
	{
		public long timeWasted = 0l;
		
		public void run()
		{
			long timer;
			Random rand = new Random();
			while( !targetReached )
			{
				long workTime =  ( long )( rand.nextDouble()* 20 ) + 30l;
				boolean tailedBodyRetrieved = false;
				
				timer = System.currentTimeMillis();
				
				tailedBodies.acquireMutex();// This is done to avoid waiting while using the most advanced parts available 
				
				if ( tailedBodies.getTotalItems() != 0 )
				{
					tailedBodies.getItem();
					tailedBodyRetrieved = true;
				}
				
				tailedBodies.releaseMutex();
				
				if( !tailedBodyRetrieved )
				{
					bodies.getItem();
				}
				
				hindlegs.getItem();
				hindlegs.getItem();
				forelegs.getItem();
				forelegs.getItem();
				timeWasted += System.currentTimeMillis() - timer;

				try
				{
					sleep( workTime );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
				
				timer = System.currentTimeMillis();
				if( tailedBodyRetrieved )
					completeBodies.addItem();
				else
					leggedBodies.addItem();
				timeWasted += System.currentTimeMillis() - timer;
			}
		}
	}

	class TailAttacher extends Thread
	{
		public long timeWasted = 0l;
		
		public void run()
		{
			long timer;
			Random rand = new Random();
			while( !targetReached )
			{

				long workTime =  ( long )( rand.nextDouble()* 10 ) + 10l;
				boolean leggedBodyRetrieved = false;
				
				timer = System.currentTimeMillis();
				
				leggedBodies.acquireMutex();// This is done to avoid waiting while using the most advanced parts available
					if ( leggedBodies.getTotalItems() != 0 )
					{
						leggedBodies.getItem();
						leggedBodyRetrieved = true;
					}
				leggedBodies.releaseMutex();
				
				if( !leggedBodyRetrieved )
				{
					bodies.getItem();
				}
				
				tails.getItem();
				timeWasted += System.currentTimeMillis() - timer;

				try
				{
					sleep( workTime );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
				
				timer = System.currentTimeMillis();
				if( leggedBodyRetrieved )
					completeBodies.addItem();
				else
					tailedBodies.addItem();
				timeWasted += System.currentTimeMillis() - timer;
				
			}
		}
	}

	class EyeAttacher extends Thread
	{
		public long timeWasted = 0l;
		
		public void run()
		{
			long timer;
			Random rand = new Random();
			while( !targetReached )
			{
				long workTime =  ( long )( rand.nextDouble() * 20 ) + 10l;
				boolean whiskeredHeadRetrieved = false;
				
				timer = System.currentTimeMillis();
				
				whiskeredHeads.acquireMutex();// This is done to avoid waiting while using the most advanced parts available
				if ( whiskeredHeads.getTotalItems() != 0 )
				{
					whiskeredHeads.getItem();
					whiskeredHeadRetrieved = true;
				}
				whiskeredHeads.releaseMutex();
				
				if( !whiskeredHeadRetrieved )
				{
					heads.getItem();
				}
				
				eyes.getItem();
				eyes.getItem();
				timeWasted += System.currentTimeMillis() - timer;

				try
				{
					sleep( workTime );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
				
				timer = System.currentTimeMillis();
				if( whiskeredHeadRetrieved )
					completeHeads.addItem();
				else
					eyedHeads.addItem();
				timeWasted += System.currentTimeMillis() - timer;
			}
		}
	}

	class WhiskerAttacher extends Thread
	{
		public long timeWasted = 0l;
		
		public void run()
		{
			long timer;
			Random rand = new Random();
			while( !targetReached )
			{
				long workTime =  ( long )( rand.nextDouble() * 40 ) + 20l;
				boolean eyedHeadRetrieved = false;
				
				timer = System.currentTimeMillis();
				
				eyedHeads.acquireMutex();// This is done to avoid waiting while using the most advanced parts available
				if ( eyedHeads.getTotalItems() != 0 )
				{
					eyedHeads.getItem();
					eyedHeadRetrieved = true;
				}
				eyedHeads.releaseMutex();
				
				if( !eyedHeadRetrieved )
				{
					heads.getItem();
				}
				
				whiskers.getItem();
				whiskers.getItem();
				whiskers.getItem();
				whiskers.getItem();
				whiskers.getItem();
				whiskers.getItem();
				timeWasted += System.currentTimeMillis() - timer;

				try
				{
					sleep( workTime );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
				
				timer = System.currentTimeMillis();
				if( eyedHeadRetrieved )
					completeHeads.addItem();
				else
					whiskeredHeads.addItem();
				timeWasted += System.currentTimeMillis() - timer;
				
			}
		}
	}
}
