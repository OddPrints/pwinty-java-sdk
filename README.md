Pwinty-Java-SDK
===============

A java library for communicating with the [Pwinty API](http://www.pwinty.com/api.html).

v2.2 of the API is supported. If you want an older version, use an older build ;)


Releases
--------
* v2.2.6 - Added ability to add a photo via an InputStream as well as a File or URL.

Usage
-----

Everything is done with an Order object:

``` java
// Choose Environment.LIVE for real orders
Pwinty pwinty = new Pwinty(Environment.SANDBOX, "merchant-id", "api-key");

// Construct a new Order
Order order = new Order(pwinty, CountryCode.GB, CountryCode.GB, QualityLevel.Standard, false);
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

Best to checkout, then run `./gradlew install`. Then you can just add this to your gradle deps: `compile 'uk.co.mattburns.pwinty:pwinty-java-sdk:2.2.6' `

Alternatively, add the jar to your project. You can find the jar in the [build directory]
(https://github.com/mattburns/pwinty-java-sdk/tree/master/build/libs)


Error Handling
--------------

Any call to the API could throw a PwintyError which is an unchecked exception. It's up to you if you want to catch and handle it. See the [Pwinty API documentation](http://www.pwinty.com/api) for potential errors.

``` java
try {
	order.submit();
} catch (PwintyError e) {
	System.out.println(e.getCode() + ": " + e.getError());
}
```



Dependencies
------------

Dependencies are managed (and listed) in build.gradle

Contribute/Extend
-----------------

Everything is built using standard [gradle](https://gradle.org/docs/current/userguide/tutorial_java_projects.html).


Running the tests
-----------------

This library has a comprehensive test suite which is helpful as the Pwinty API evolves. To run the tests yourself, run the following commands (replacing "12345" with your API ID and KEY appropriately):

```
git clone https://github.com/mattburns/pwinty-java-sdk.git
cd pwinty-java-sdk
echo PWINTY_MERCHANT_ID_SANDBOX=12345 > test-settings.properties
echo PWINTY_MERCHANT_KEY_SANDBOX=12345 >> test-settings.properties
cp test-settings.properties src/test/resources/uk/co/mattburns/pwinty/v2_2/

./gradlew test
```

Save time and run an individual test like this:

```
./gradlew test --tests "uk.co.mattburns.pwinty.v2_2.PwintyTest.all_available_print_types_are_in_enum"
```


More
----

See the [test code](https://github.com/mattburns/pwinty-java-sdk/tree/master/src/test/java/uk/co/mattburns/pwinty/v2_2/) for more usage examples.
