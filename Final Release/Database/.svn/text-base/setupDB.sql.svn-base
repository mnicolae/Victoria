/**
 * TaxLog keeps a running total for the tax received from purchases for a particular date.
 * This makes it convenient for the store owner when it comes time to pay the government those taxes.
 */
create table if not exists TaxLog(

	date Date,
	totalTax double default 0.00,
	primary key(date)
);

/**
 * Sale keeps records of which items are receiving a discount. 
 * It determines which items to apply the sale to, when this sale starts, when this sale ends
 * and the type of sale.
 */
create table if not exists Sale(
	saleID integer primary key, --A unique id for the sale.
	code VARCHAR(32), -- This code refers to the code of the item the sale is to be applied to.
	startDate Date, -- This is when the sale starts.
	endDate Date,	-- This is when the sale ends.
	percentReduction double default 0.00, -- This determines the percent discount.
	flatReduction double default 0.00 -- This determines the flat rate discount.
);


/**
 * ProductCategory contains all the different category's a product could be in 
 * and the tax rate for that category.
 */
create table if not exists ProductCategory(

	category varchar(64),
	taxRate double default 0.00,
	primary key(category)
);

/**
 * BulkProduct contains all the bulk product information along with their category.
 * Category is a foreign key referencing ProductCategory; this makes it easy to natural join with 
 * ProductCategory to obtain the current tax rate for any product.
 */
create table if not exists BulkProduct(

	BIC char(5),
	descrip varchar(64),
	unitPrice double,
	category varchar(64),
	wholesalePrice double, 
	primary key(BIC),
	foreign key(category) REFERENCES ProductCategory(category)
);

/**
 * PackagedProduct contains all the packaged product information along with their category.
 * Category is a foreign key referencing ProductCategory; this makes it easy to natural join with 
 * ProductCategory to obtain the current tax rate for any product.
 */
create table if not exists PackagedProduct(

	UPC char(12),
	descrip varchar(64),
	price double,
	weight double,
	category varchar(64),
	wholesalePrice double,
	primary key(UPC)
	foreign key(category) REFERENCES ProductCategory(category)
);

/**
* ImpulseProducts contain any impulse buy items and the amount that have been purchased since the started date. 
* note: totalBought for bulk items represent total weight bought
*/
create table if not exists ImpulseProducts(

	SelfCheckoutID int,
	descrip varchar(64),
	code varchar(12),
	totalBought double,
	profitMargin double,
	started Date,
	primary key(SelfCheckoutID, code)
	
);

/**
* UserAccounts contain all username, password, full name, and privileges of employees in the store
* that can access the administritive view.
* privilege is a int, where 0 = search only access, 1 = all access.
*/
	
create table if not exists UserAccounts(

	username varchar(64),
	password varchar(512),
	name varchar(64),
	privilege int,
	primary key(username)

);

