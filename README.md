# e4 (Eclipse) notifications library

For a description and screenshots please see
[here](http://www.beanaire.fr/e4-notification/e4-notification.html).

## Build the library and the demo application

1. Change working directory: `$ cd org.beanaire.ui.e4.notification.parent/`
1. Execute Maven: `$ mvn clean install`
1. After the build has finished the **library** can be found at e.g.
   `org.beanaire.ui.e4.notification.lib/target/org.beanaire.ui.e4.notification.lib-1.0.1-SNAPSHOT.jar`.
1. After the build has finished the **demo application** can be found at
   `org.beanaire.ui.e4.notification.demo.product/target/products/org.beanaire.ui.e4.notification.demo.product`.
   By default 64bit builds for Linux, Mac OS X and Windows are created.

## Troubleshooting the demo application ##

* If you encouner any errors when running the demo application try
  to start the executable with the option `-consoleLog`.
* When using Windows make sure that your path to the demo application
  does not get too long. Winows is not able to cope with file path
  longer than 256 characters. If you encounter this error you will
  likely get an error message like *The <...> executable launcher was 
  unable to locate its companion shared library*.
* Do not forget that running the 64bit SWT libraries on an 32bit JVM
  is not supported.
