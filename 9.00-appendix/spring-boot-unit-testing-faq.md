# Spring Boot Unit Testing FAQ

[<img src="../images/spring-boot-unit-testing-thumbnail.png">](http://www.luv2code.com/spring-boot-unit-testing-github)

&#8287;
## Frequently Asked Questions

* [Q: spring-boot-unit-testing-code.zip file is invalid](q:-spring-boot-unit-testing-code.zip-file-is-invalid)
* Q: difference between assertIterableEquals and assertLinesMatch

## Q: spring-boot-unit-testing-code.zip file is invalid

A: **NOTES FOR MICROSOFT WINDOWS USERS - Error "File Names Too Long"**

During download of the files, you may encounter an error "File Names Too Long" or "Invalid File" ... something along those lines. This is because by default some of the MS Windows utilities can not support long file names.

Here is the solution:

If you are using the .zip file, then use the 7Zip Utility. The 7Zip Utility is a free download available at this link.

https://www.7-zip.org/download.html



## Q: difference between assertIterableEquals and assertLinesMatch

 Good questions!

> 1. Does it have to be `List<String>` in order to use `assertLinesMatch()`?

Yes. The method signature specifies `List<String>`. Here's a info from the JavaDoc for [Assertions.assertLinesMatch()](https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html#assertLinesMatch(java.util.List,java.util.List))

```
public static void assertLinesMatch(List<String> expectedLines,
 List<String> actualLines)

```

However, note that you can read in the test data from a file and store the lines in a List<String>. Code examples [available here](https://www.tabnine.com/code/java/methods/java.nio.file.Files/readAllLines).


> 2. Does assertLinesMatch() convert List<String> into string ("Java11Junit " and "Java\\d+Junit") and compare each character?

Based on the docs for [Assertions.assertLinesMatch()](https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html#assertLinesMatch(java.util.List,java.util.List)), this is the algorithm for assertLinesMatch

1. check if `expected.equals(actual)` – if yes, continue with next pair

2. otherwise, treat expected as a regular expression and check via

    `String.matches(String)` – if yes, continue with next pair

3. otherwise, check if expected line is a fast-forward marker, if yes apply

    fast-forward actual lines accordingly and goto 1.


Let's look at some coding examples. This will help with the discussion.

For testing the **case of: 1**. check if `expected.equals(actual)` – if yes, continue with next pair

```
@DisplayName("Lines Match")
@Test
void testLinesMatchExpectLineEquals() {
    List<String> expectedList = List.of("luv", "2", "code");
    List<String> actualList = List.of("luv", "2", "code");
 
    assertLinesMatch(expectedList, actualList, "Expected list should be same as actual list");
}
```

The above test is the simplest example. It attempts to check if each line is equal. In this case, this is true hence the test passes.


For testing the **case of: 2**. otherwise, treat expected as a regular expression
Essentially here, we will attempt using a regular expression

```
@DisplayName("Regex for any number")
@Test
void testLinesMatchRegularExpressionMatchAnyNumber() {
    List<String> expectedList = List.of("luv", "\\d+", "code");
    List<String> actualList = List.of("luv", "9", "code");
 
    assertLinesMatch(expectedList, actualList, "Expected list should be same as actual list");
}
```

The above test uses the regular expression `\\d+`. This will match on any number. In this case, this matches on number 9 hence the test passes.

Here's another regex examples that uses a regex to check for numbers in the range of 2 through 7.

```
@DisplayName("Regex for numbers 2 through 7 ")
@Test
void testLinesMatchRegularExpressionMatchNumbers2Through7() {
    List<String> expectedList = List.of("luv", "[2-7]+", "code");
    List<String> actualList = List.of("luv", "5", "code");
 
    assertLinesMatch(expectedList, actualList, "Expected list should be same as actual list");
}
```

The above test uses the regular expression `[2-7]+`. This will match on numbers in the range of 2 through 7. In this case, this matches on number 5 hence the test passes.


> 3. I understand because expected line is treated as a regular expression so below code does not fail but I couldn't got what fast-forward marker means.

Based on the docs, a fast-forward marker means.

A valid fast-forward marker is an expected line that starts and ends with the literal >> and contains at least 4 characters. Examples:

   
```
>>>>
>> stacktrace >>
>> single line, non Integer.parse()-able comment >>
```
Skip arbitrary number of actual lines, until first matching subsequent expected line is found. Any character between the fast-forward literals are discarded.
   
```
">> 21 >>"
```

Skip strictly 21 lines. If they can't be skipped for any reason, an assertion error is raised.



Here's an example that will use fast-forward markers in the expected value.


```
@DisplayName("Fast-forward marker, skip expected lines with markers")
@Test
void testLinesMatchFastForward() {
    List<String> expectedList = List.of("luv", "2", ">> just ignore this line >>", "code");
    List<String> actualList = List.of("luv", "2", "junk", "code");
 
    assertLinesMatch(expectedList, actualList, "Expected list should be same as actual list");
}
```

It sees the line `>> just ignore this line >>` ... this is determined to be a fast-forward line. As a result it will skip this line in the expectedList and actualList. For the actualList, we can give any string.

Finally, let's look at an example of fast-forward marker that skips X number of lines.

```
@DisplayName("Fast-forward marker, skip 4 lines")
@Test
void testLinesMatchFastForwardSkipLines() {
    List<String> expectedList = List.of("luv", "my", ">> 4 >>", "code");
    List<String> actualList = List.of("luv", "my", "one", "two", "three", "four", "code");
 
    assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
}
```

It sees the line `>> 4 >>` ... this is determined to be a fast-forward marker which means skips 4 lines. As a result it will skip four lines in the expectedList and actualList. For the actualList, we can give any 4 strings to skip.

----

If you'd like to experiment with these test cases, here is my test class.


```
package com.luv2code.junitdemo;
 
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
 
public class LinesTest {
 
    @DisplayName("Iterable equals")
    @Test
    void testIterableEquals() {
        List<String> expectedList = List.of("luv", "2", "code");
        List<String> actualList = List.of("luv", "2", "code");
 
        assertIterableEquals(expectedList, actualList, "Expected list should be same as actual list");
    }
 
    @DisplayName("Lines Match")
    @Test
    void testLinesMatchExpectLineEquals() {
        List<String> expectedList = List.of("luv", "2", "code");
        List<String> actualList = List.of("luv", "2", "code");
 
        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
 
    @DisplayName("Regex for any number")
    @Test
    void testLinesMatchRegularExpressionMatchAnyNumber() {
        List<String> expectedList = List.of("luv", "\\d+", "code");
        List<String> actualList = List.of("luv", "9", "code");
 
        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
 
    @DisplayName("Regex for numbers 2 through 5")
    @Test
    void testLinesMatchRegularExpressionMatchNumbers2Through7() {
        List<String> expectedList = List.of("luv", "[2-7]+", "code");
        List<String> actualList = List.of("luv", "5", "code");
 
        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
 
    @DisplayName("Fast-forward marker, skip expected lines with markers")
    @Test
    void testLinesMatchFastForward() {
        List<String> expectedList = List.of("luv", "2", ">> just ignore this line >>", "code");
        List<String> actualList = List.of("luv", "2", "", "code");
 
        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
 
    @DisplayName("Fast-forward marker, skip 4 lines")
    @Test
    void testLinesMatchFastForwardSkipLines() {
        List<String> expectedList = List.of("luv", "my", ">> 4 >>", "code");
        List<String> actualList = List.of("luv", "my", "one", "two", "three", "four", "code");
 
        assertLinesMatch(expectedList, actualList, "Expected lines should match actual lines");
    }
 
}
```

---

Also note that when using `assertLinesMatch`, you can also pass in [`Stream<String>`](https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html#assertLinesMatch(java.util.stream.Stream,java.util.stream.Stream)) parameters. This provides another option for reading data from files. Code examples [available here](https://www.tabnine.com/code/java/methods/java.nio.file.Files/lines).


