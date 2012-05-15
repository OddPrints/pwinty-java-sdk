Pwinty-Java-SDK
===============

A java library for communicating with the [Pwinty API](http://www.pwinty.com/api.html).


Usage
-----

Everything is done with a Pwinty object:

``` java
import uk.co.mattburns.pwinty.Pwinty;
import uk.co.mattburns.pwinty.Pwinty.Environment;
import uk.co.mattburns.pwinty.model.Order;
import uk.co.mattburns.pwinty.model.Photo.Sizing;
import uk.co.mattburns.pwinty.model.Photo.Type;

...

// Choose Environment.LIVE for real orders
Pwinty pwinty = new Pwinty(Environment.SANDBOX, "merchant-id", "api-key");

// Contruct a new Order
Order newOrder = new Order();
newOrder.setRecipientName("Matt Burns");
newOrder.setAddress1("123 Some Street");
newOrder.setAddress2("Some Village");
newOrder.setAddressTownOrCity("Bristol");
newOrder.setStateOrCounty("Bristol");
newOrder.setPostalOrZipCode("BS1 123");
newOrder.setCountry("UK");

// Create the new Order object on Pwinty
Order createdOrder = pwinty.createOrder(newOrder);
int id = createdOrder.getId();

// Upload a photo
File file = new File("kittens.jpg");
pwinty.addPhotoToOrder(id, file, Type._4x6, 1, Sizing.Crop);

// Check the Order is valid for submission
if (pwinty.getSubmissionStatus(id).isValid()) {
	// Submit it for printing and posting
	pwinty.submitOrder(id);
}
```


Error Handling
--------------

Any call to the Pwinty object could throw a PwintyError which is an unchecked exception. It's up to you if you want to catch and handle it. See the [Pwinty API documentation](http://www.pwinty.com/api.html) for potential errors.

``` java
try {
	pwinty.submitOrder(id);
} catch (PwintyError e) {
	System.out.println(e.getCode() + ": " + e.getError());
}
```

Download
--------

- [pwinty-sdk-with-dependencies-1.0.zip](https://github.com/downloads/mattburns/pwinty-java-sdk/pwinty-sdk-with-dependencies-1.0.zip) (includes required dependencies)


Dependencies
------------

I have bundled up the Jars you need into pwinty-sdk-with-dependencies-1.0.zip. If you prefer to manage them yourself, they are:

- [Gson](https://code.google.com/p/google-gson/) - gson-2.2.jar
- [Jersey](http://jersey.java.net/) - jersey-core-1.12.jar, jersey-client-1.12.jar, jersey-multipart-1.12.jar, jsr311-api-1.1.1.jar

Alternatively:

- [pwinty-sdk-1.0.jar](https://github.com/downloads/mattburns/pwinty-java-sdk/pwinty-sdk-1.0.jar)
- [pwinty-sdk-src-1.0.jar](https://github.com/downloads/mattburns/pwinty-java-sdk/pwinty-sdk-src-1.0.jar)


Building
--------

ant

More
----

Browse the test code for more usage examples.
