# _Glasses_
A lens-like library for Java

```java
var lens = Lens.create(Library.class)
    .select(Library::setAddress, Address.class)
    .select(Address::setCity, String.class)
var library = new Library()
    .setAddress(new Address()
        .setCity("Des Plaines")
    );

var city = lens.focus(library);
System.out.printf(city.value());  // prints 'Des Plaines'
System.out.printf(city.path());   // prints '$.address.city'
    
library.setAddress(null);
city = lens.focus(library);
System.out.printf(city.value());  // prints 'null'

city.override("Chicago");
System.out.printf(library.getAddress().getCity()); // prints 'Chicago'
```

# Purpose
Lenses are a way of querying nested object structures in a type-safe way by relying on class's setters methods.

## Can I Use Lenses
Maybe! As long as every class in the object model:
- Has a setter for every field
  - `TODO` Possibly optional for list fields if they are lazy initialized
- Has a no-argument constructor

The reason for needing a setter for every field is this is used by the library to find the associated field for a getter/setter combo by invoking the setter with a dummy value and then finding that dummy value reflectively

The reason for the no-argument constructor is that many operations in the Lens library require a no-arg constructor on the class for reasons like
- Default constructing intermediate nodes in an object graph when overwriting a property whose parent is not yet created
- Injecting a dummy object into the field of another object and then using reflection to determine which field it got set to. Each dummy must be unique

Essentially you need basic data classes