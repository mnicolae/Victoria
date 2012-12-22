package ca.utoronto.csc301.SelfCheckOut.IntegrationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ca.utoronto.csc301.SelfCheckOut.App.*;
import ca.utoronto.csc301.SelfCheckOut.Devices.PaymentCollectorTest;

@RunWith(Suite.class)
@SuiteClasses({ CartWithNoItems.class, CartWithOneItem.class,
		InstantiatingSelfCheckOut.class, PurchaseImpulseProducts.class,
		PurchaseLargeNumberOfItems.class, GenerateImpulseProductsReport.class,
		BICTest.class, BulkProductTest.class, CheckOutCartTest.class,
		GroceryItemTest.class, PackagedProductTest.class, UPCTest.class,
		CartWithOneItemOnSale.class, CartWithMoreThanOneItemOnSale.class, PaymentCollectorTest.class,
		DatabaseTest.class, SelfCheckOutTest.class})
public class AllTestSuite {

}
