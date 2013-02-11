package de.zainodis.balancemanager.model.persistence.test;

import java.util.Date;

import android.test.ApplicationTestCase;
import de.zainodis.balancemanager.core.Application;
import de.zainodis.balancemanager.model.BudgetCycle;
import de.zainodis.balancemanager.model.CashflowDirection;
import de.zainodis.balancemanager.model.Entry;
import de.zainodis.balancemanager.model.persistence.BudgetCyclePersister;
import de.zainodis.balancemanager.model.persistence.EntryPersister;
import de.zainodis.commons.model.CurrencyAmount;

/**
 * Class under test: {@link BudgetCyclePersister}.
 * 
 * @author fzarrai
 * 
 */
public class BudgetCyclePersisterTest extends ApplicationTestCase<Application> {

   private final BudgetCyclePersister persister = new BudgetCyclePersister();

   public BudgetCyclePersisterTest() {
	 super(Application.class);
   }

   public BudgetCyclePersisterTest(Class<Application> applicationClass) {
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
	 assertEquals(1, persister.count());

	 persister.endOngoingCycles();

	 // We have no active budget cycle anymore
	 assertTrue(persister.getActiveCyclesId() == 0);
   }

   public void testClearOldBudgetCycles() throws Exception {
	 EntryPersister entryPersister = new EntryPersister();
	 entryPersister.clearTable();

	 long entryCount = entryPersister.count();
	 assertEquals(0, entryCount);

	 persister.clearTable();
	 // We have no active budget cycle right now
	 assertTrue(persister.getActiveCyclesId() == 0);

	 // 12.02.2010 at 14:26
	 Date one = new Date(110, 1, 12, 14, 26);
	 // 01.04.2011 at 19:50
	 Date two = new Date(111, 3, 3, 19, 50);
	 // 25.01.2013 at 10:19
	 Date three = new Date(113, 0, 25, 10, 19);

	 // First budget cycle
	 BudgetCycle cycle = new BudgetCycle(one);
	 assertTrue(persister.save(cycle));

	 // Add entries and end the cycle
	 persistSampleEntries();
	 persister.endOngoingCycles();
	 assertEquals(1, persister.count());
	 assertTrue(entryPersister.count() > entryCount);
	 entryCount = entryPersister.count();

	 // Second budget cycle
	 cycle = new BudgetCycle(two);
	 assertTrue(persister.save(cycle));

	 // Add entries and end the cycle
	 persistSampleEntries();
	 persister.endOngoingCycles();
	 assertEquals(2, persister.count());
	 assertTrue(entryPersister.count() > entryCount);
	 entryCount = entryPersister.count();

	 // Third budget cycle (ongoing)
	 cycle = new BudgetCycle(three);
	 assertTrue(persister.save(cycle));

	 // Add entries and end the cycle
	 persistSampleEntries();
	 assertEquals(3, persister.count());
	 assertTrue(entryPersister.count() > entryCount);
	 // Entry count before we start deleting
	 entryCount = entryPersister.count();

	 // Nothing should be deleted
	 persister.delete(new Date(110, 1, 11, 14, 26));
	 assertEquals(3, persister.count());
	 assertTrue(entryCount == entryPersister.count());

	 // The first entry should be deleted
	 persister.delete(new Date(111, 3, 2, 19, 50));
	 assertEquals(2, persister.count());
	 assertTrue(entryCount > entryPersister.count());
	 // Update entry count to current value
	 entryCount = entryPersister.count();

	 // All but the active cycle should be deleted
	 persister.delete(new Date());
	 assertEquals(1, persister.count());
	 assertTrue(entryCount > entryPersister.count());

	 // Now end the active cycle and delete it
	 persister.endOngoingCycles();
	 persister.delete(new Date());
	 assertEquals(0, persister.count());
	 assertEquals(0, entryPersister.count());
   }

   /**
    * Persists sample entries for the currently ongoing budget cycle.
    */
   protected void persistSampleEntries() {
	 EntryPersister entryPersister = new EntryPersister();

	 assertTrue(entryPersister.save(new Entry(CashflowDirection.INCOME, "Income", true,
		  new CurrencyAmount(1000))));
	 assertTrue(entryPersister.save(new Entry(CashflowDirection.EXPENSE, "Rent", true,
		  new CurrencyAmount(200))));
	 assertTrue(entryPersister.save(new Entry(CashflowDirection.EXPENSE, "Food", false,
		  new CurrencyAmount(50))));
	 assertTrue(entryPersister.save(new Entry(CashflowDirection.INCOME, "Money Present", false,
		  new CurrencyAmount(23))));
	 assertTrue(entryPersister.save(new Entry(CashflowDirection.EXPENSE, "Clothes", false,
		  new CurrencyAmount(45))));
	 assertTrue(entryPersister.save(new Entry(CashflowDirection.EXPENSE, "Pet Food", false,
		  new CurrencyAmount(18))));

   }

}
