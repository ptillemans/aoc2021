


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

