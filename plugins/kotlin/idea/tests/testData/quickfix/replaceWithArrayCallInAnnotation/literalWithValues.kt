// "Replace with array call" "true"
// LANGUAGE_VERSION: 1.2

annotation class Some(vararg val ints: Int)

@Some(ints = *[1, 2,<caret> 3])
class My
