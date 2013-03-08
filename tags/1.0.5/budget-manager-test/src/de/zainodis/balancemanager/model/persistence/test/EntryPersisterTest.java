package de.zainodis.balancemanager.model.persistence.test;

import java.util.Date;

import android.test.ApplicationTestCase;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;

/**
 * Class under test: {@link BudgetCyclePersister}.
 * 
 * @author fzarrai
 * 
 */
public class EntryPersisterTest extends ApplicationTestCase<Application> {

   private final BudgetCyclePersister persister = new BudgetCyclePersister();

   public EntryPersisterTest() {
	 super(Application.class);
   }

   public EntryPersisterTest(Class<Application> applicationClass) {
	 super(applicationClass);
   }

   @Override
   protected void setUp() throws Exception {
	 // Required according to API
	 createApplication();
	 super.setUp();
   }

   public void testRoundtrip() throws Exception {
	 persister.clearTable();
	 // We have no active budget cycle right now
	 assertTrue(persister.getActiveCyclesId() == 0);

	 // Save a new budget cycle
	 BudgetCycle cycle = new BudgetCycle(new Date());
	 assertTrue(persister.save(cycle));

	 // Make sure this is our currently active cycle
	 assertTrue(persister.getActiveCyclesId() > 0);

	 persister.endOngoingCycles();

	 // We have no active budget cycle anymore
	 assertTrue(persister.getActiveCyclesId() == 0);
   }

}
