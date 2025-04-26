FINAL PROJECT: RESTAURANT

This will go over the general design of the project in a very simplified manner.

-------
Restaurant Class
  The Heart of the program, handles assigning tables, and pretty much everything related to the functionality in the backend. I has the Database class which stores information gained during runtime of the program. The only things that are not in the direct purview of the restaurant are customerGroups, which are their own seperate thing.

------
Table Class
  If the Restaurant is the heart, tables are the blood of functionality. They are immutable, so they dont actually directly hold anything. Instead the restaurant keeps track of what tables are currently occupied, and the model keeps track of which customerGroups are assigned to what tables. Tables are really just a handy way of passing information around. 

-------
Staff Classes (Staff, Waiter, Chef, and Manager)
  Simple classes that represent employees. Hold things like their first and last names, and their salary.
  StaffTypes are enumerations that represent (but do not directly reference) the different employee types.

-------
Customer and CustomerGroup
  Customer is an immutable class that represents a single customer. A customer chooses a random entree, drink, and dessert of the menu when its created.
  CustomerGroup is just a collection of Customers. It is not immutable, but only in the sense that it has a method for adding customers to a customerGroup. customerGroup is the main thing we work with when dealing with customers, as it has a bunch of convienence methods for getting info from the customers.

-------
Order and MenuItem
  An order is just a collection of MenuItems. Each customer has one order that represent what they want from the restaurant. Both order and MenuItem are immutable classes. Order has a bunch of convienence methods that make it easy to gather information about it and all of its MenuItems, and as such most of the code interacts with MenuItems through Order. ItemType just defines if a MenuItem represents an entree, drink, or dessert.
