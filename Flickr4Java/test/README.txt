Test cases for flickrj.

Before building and running the test cases you will need to rename the file setup.properties.example in the /src directory to setup.properties and replace the following fields with appropriate values:

email (deprecated)
password (deprecated)
username
apikey
imagefile
secret

Currently tests will only pass if certain values are found. In the future these test values will be externalized in a
properties file so you will be able to configure the test cases based on your account settings.