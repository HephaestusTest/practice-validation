# practice-validation

A sandbox repository used to exercise Hephaestus practice reviews end to end.

Changes here are expected to be throwaway.

## Display names for new users

New user creation strips surrounding whitespace and collapses consecutive whitespace to one
space. Names must contain at least one non-whitespace character and may contain at most 80
Unicode code points after normalization. Letter case and accents are preserved. Existing users
are unchanged.

### Focused verification (JDK 21 or later)

The display-name contract has no external dependencies. From the repository root:

```sh
build_dir=$(mktemp -d)
trap 'rm -rf "$build_dir"' EXIT
javac --release 21 -encoding UTF-8 -d "$build_dir" \
  src/main/java/com/example/userapi/service/DisplayNames.java \
  src/test/java/com/example/userapi/service/DisplayNamesContractTest.java
java -cp "$build_dir" com.example.userapi.service.DisplayNamesContractTest
```

This checks valid names, whitespace normalization, missing/blank names and both sides of the
length limit. It does not start the HTTP service or exercise persistence. The existing Maven
project currently has no versions for its Spring Boot starter dependencies, so `mvn test`
fails during project-model resolution before compilation. These focused checks do not establish
that the full application builds or that its HTTP error responses are correct.
