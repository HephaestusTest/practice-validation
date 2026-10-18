# practice-validation

A sandbox repository used to exercise Hephaestus practice reviews end to end.

Changes here are expected to be throwaway.

## User-directory retry configuration

`RetryPolicy` reads the retry limit and delay for the user-directory client.
Both inputs must be positive whole numbers. Blank, malformed, zero or negative
values must stop configuration with a descriptive `IllegalArgumentException`.
A bad configuration must never silently disable the delay.

The dependency-free smoke check covers valid inputs. From the repository root:

```sh
javac --release 21 -d /tmp/retry-policy-check \
  src/main/java/com/example/userapi/config/RetryPolicy.java \
  src/test/java/com/example/userapi/config/RetryPolicySmokeTest.java
java -cp /tmp/retry-policy-check com.example.userapi.config.RetryPolicySmokeTest
```
