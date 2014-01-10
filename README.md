Pwinty-Java-SDK
===============

A java library for communicating with the [Pwinty API](http://www.pwinty.com/api.html).

v2 of the API is supported. v1 is also available, but marked as deprecated and should no longer be used.



Usage
-----

Everything is done with an Order object:

``` java
// Choose Environment.LIVE for real orders
Pwinty pwinty = new Pwinty(Environment.SANDBOX, "merchant-id", "api-key");

// Contruct a new Order
Order order = new Order(pwinty, CountryCode.GB, CountryCode.GB, QualityLevel.Standard);
order.setRecipientName("Matt Burns");
order.setAddress1("123 Some Street");
order.setAddress2("Some Village");
order.setAddressTownOrCity("Bristol");
order.setStateOrCounty("Bristol");
order.setPostalOrZipCode("BS1 123");

// Upload a photo
File file = new File("kittens.jpg");
order.addPhoto(file, Type._4x6, 1, Sizing.Crop);

// Check the Order is valid for submission
if (order.getSubmissionStatus().isValid()) {
    // Submit it for printing and posting
    order.submit();
}

```


Download
--------

Just add the jar to your project. You can find the jar in the [dist directory]
(https://github.com/mattburns/pwinty-java-sdk/tree/master/pwinty-java-sdk/dist)


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



Dependencies
------------

I have bundled up the Jars you need into pwinty-sdk-with-dependencies-*.zip which you can find in the [dist directory]
(https://github.com/mattburns/pwinty-java-sdk/tree/master/pwinty-java-sdk/dist)
. If you prefer to manage them yourself, you can see which jars you need in the [lib directory]
(https://github.com/mattburns/pwinty-java-sdk/tree/master/pwinty-java-sdk/lib)



Contribute/Extend
-----------------

The project files are present so you can just import the project into [eclipse](http://www.eclipse.org/).
If you want to play, you can build the jars from source using [ant](http://ant.apache.org/). 



More
----

See the [test code](https://github.com/mattburns/pwinty-java-sdk/tree/master/pwinty-java-sdk/src/test/uk/co/mattburns/pwinty/v2/) for more usage examples.
