Pwinty-Java-SDK
===============

A java library for communicating with the [Pwinty API](http://www.pwinty.com/api.html).


Usage
-----

Everything is done with an Order object:

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
Order order = new Order(pwinty);
order.setRecipientName("Matt Burns");
order.setAddress1("123 Some Street");
order.setAddress2("Some Village");
order.setAddressTownOrCity("Bristol");
order.setStateOrCounty("Bristol");
order.setPostalOrZipCode("BS1 123");
order.setCountry("UK");

// Upload a photo
File file = new File("kittens.jpg");
order.addPhoto(file, Type._4x6, 1, Sizing.Crop);

// Check the Order is valid for submission
if (order.getSubmissionStatus().isValid()) {
	// Submit it for printing and posting
	order.submit();
}
```


Error Handling
--------------

Any call to the API could throw a PwintyError which is an unchecked exception. It's up to you if you want to catch and handle it. See the [Pwinty API documentation](http://www.pwinty.com/api.html) for potential errors.

``` java
try {
	order.submit();
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

Here is the SDK without the dependencies:

- [pwinty-sdk-1.0.jar](https://github.com/downloads/mattburns/pwinty-java-sdk/pwinty-sdk-1.0.jar)
- [pwinty-sdk-src-1.0.jar](https://github.com/downloads/mattburns/pwinty-java-sdk/pwinty-sdk-src-1.0.jar)


Contribute/Extend
-----------------

The project files are present so you can just import the project into [eclipse](http://www.eclipse.org/).
If you want to play, you can build the jars from source using [ant](http://ant.apache.org/). 

More
----

See the [test code](https://github.com/mattburns/pwinty-java-sdk/blob/master/pwinty-java-sdk/src/test/uk/co/mattburns/pwinty/PwintyTest.java) for more usage examples.
