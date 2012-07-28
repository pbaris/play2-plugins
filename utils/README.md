# play2-plugins-utils
Some utilities for Play Framework 2

## How to install

* At your dependencies add
<pre>
"pbaris" %% "play2-plugins-utils" % "0.0.1"
</pre>

* At your resolvers add
<pre>
resolvers += "pbaris" at "https://github.com/pbaris/m2-repo/raw/master"
</pre>

## Features

* PrintUtil
A helper class for printing in a table format any instance of a ```java.util.Map``` or ```play.mvc.Http.Request```
<pre>
+--------------------------------------------+
|                     Map                    |
+--------------------------------------------+
| aLongerKey          : one more value again |
| key1                : a value              |
| key2                : an other value       |
| longKey             : one more value       |
| theLongerstKeyOfAll : the last value       |
+--------------------------------------------+
+--------------------------------------------------+
|                Http.Request                      |
+--------------------------------------------------+
| aLongerKey          : [1]                        |
| key1                : [1, 2, 3]                  |
| key2                : [alpha, beta, gama, delta] |
| longKey             : [value]                    |
| theLongerstKeyOfAll : [yes, no, no, yes, no]     |
+--------------------------------------------------+
</pre>