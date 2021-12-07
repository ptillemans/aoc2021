# Advent of Code using Kotlin

# Project Organization

The root folder contains 

- gradle build script with boilerplate generator
- this README
- a user provided *cookies.txt* file
- gradlew stuff 

The *gradle fetch* task creates 3 files :
- src/main/kotlin/aoc<YYYY>/Day<d>.kt  _for the solution_ 
- src/test/kotlin/aoc<YYYY>/Day<d>Test.kt  _for the tests_
- src/main/resources/aoc<YYYY>/day<d>/input.txt  _for the input_

I use IntelliJ and use the shortcuts and margin icons to run individual tests
and then run the `main` method in the solution file to get the outputs for 
part1 and part2:

    Solutions:
    {"part1":"342534","part2":"94004208"}

I can then double-click on the answer to select the number in the quotes, copy it 
and paste it. It is in handy JSON format as that would make it easy to do something 
more automated later on.

## Cookies to access your account

If you want to use the automatic input fetcher you need
a *cookies.txt* file in curl cookie format. e.g.

    # Netscape HTTP Cookie File
    # https://curl.haxx.se/rfc/cookie_spec.html
    # This is a generated file! Do not edit.

    .adventofcode.com	TRUE	/	TRUE	1953735132	session	<session_id>

You can _borrow_ the session id from your browser. I used a Firefox Extension which 
exports the cookies in the right format but you can also copy the template above and
fill in the session id.

## Gradle Usage

To fetch today's challenge input and generate the solution boilerplate

    $ gradle fetch

When you want to do a challenge for another day use

    $ gradle fetch --date=2020-12-1

To start over and overwrite existing files use the *--force* option

    $ gradle fetch --date=2020-12-1 --force

To test all challenges run

    $ gradle test


## Automatic reporting of results

the HTML of the answer submit form looks like 

    <form method="post" action="1/answer">
        <input type="hidden" name="level" value="2">
        <p>Answer: 
            <input type="text" name="answer" autocomplete="off"> 
            <input type="submit" value="[Submit]">
        </p>
    </form>

It is placed after the 2nd article and looking at the hidden level
field it is reused for both parts.

