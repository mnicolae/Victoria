This read me is informal and is only meant to assist team members in
using or maintaining the database.

Check out setupDB.sql to see the tables that exist in the database.

How to query, insert or update SelfCheckOut.db.
###################Getting started################
	Linux
		0) Open terminal.
		1) Install Sqlite3 - Eg. "sudo apt-get install sqlite3"
		2) Change directory to the same directory that contains SelfCheckOut.db.
		3) Enter interactive mode with command "sqlite3 SelfCheckOut.db".
		
	Windows
		0) Open cmd prompt.
		1) Change directory to the same directory that contains SelfCheckOut.db and sqlite3.exe.
		2) Enter interactive mode with command "sqlite3 SelfCheckOut.db".

Note: If you run the Java application do not be in interactive mode or the database will be in use.
#####################Example Cmds####################		
Now that you are in interactive mode you can easily query/insert/update the Database.

Example Query: Viewing all Bulk Product's.
cmd: "select * from BulkProduct;"

Example Query: Viewing the daily tax logs.
cmd: "select * from TaxLog;"

Example Insert: Adding an new category with tax rate 15%.
cmd: "insert into ProductCategory(category, taxRate) values ('beer', 0.15);"	

Example Update: Change tax rate for category beer to 5%.
cmd: "update ProductCategory SET taxRate = 0.05  WHERE category = "beer";

#####################Using Scripts#####################
Make sure you are in the same directory but not in interactive mode.

To run any sql script on the db: "sqlite3 SelfCheckOut.db < file.sql"

Examples
Create all tables in the database.
cmd: sqlite3 SelfCheckOut.db < setupDB.sql

Insert all starting data into the tables.
cmd: sqlite3 SelfCheckOut.db < insertDB.sql

Drops/deletes all tables in the database.
cmd: sqlite3 SelfCheckOut.db < dropDB.sql
