package rewards.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.internal.account.AccountRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.RewardRepository;

import common.money.MonetaryAmount;


public class RewardNetworkImplTests {


	private RewardNetworkImpl rewardNetwork;

	@Before
	public void setUp() throws Exception {
		AccountRepository accountRepo = new StubAccountRepository();
		RestaurantRepository restaurantRepo = new StubRestaurantRepository();
		RewardRepository rewardRepo = new StubRewardRepository();

		
		rewardNetwork = new RewardNetworkImpl(accountRepo, restaurantRepo, rewardRepo);
	}

	@Test
	public void testRewardForDining() {
		Dining dining = Dining.createDining("100.00", "1234123412341234", "1234567890");

		// call the 'rewardNetwork' to test its rewardAccountFor(Dining) method
		RewardConfirmation confirmation = rewardNetwork.rewardAccountFor(dining);

		// assert the expected reward confirmation results
		assertNotNull(confirmation);
		assertNotNull(confirmation.getConfirmationNumber());

		// assert an account contribution was made
		AccountContribution contribution = confirmation.getAccountContribution();
		assertNotNull(contribution);

		// the account number should be '123456789'
		assertEquals("123456789", contribution.getAccountNumber());

		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals(MonetaryAmount.valueOf("8.00"), contribution.getAmount());

		// the total contribution amount should have been split into 2 distributions
		assertEquals(2, contribution.getDistributions().size());

		// each distribution should be 4.00 (as both have a 50% allocation)
		assertEquals(MonetaryAmount.valueOf("4.00"), contribution.getDistribution("Annabelle").getAmount());
		assertEquals(MonetaryAmount.valueOf("4.00"), contribution.getDistribution("Corgan").getAmount());
	}
}